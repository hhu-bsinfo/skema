package de.hhu.bsinfo.skema.schema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import de.hhu.bsinfo.skema.util.FieldType;
import de.hhu.bsinfo.skema.util.FieldUtil;
import de.hhu.bsinfo.skema.util.SizeUtil;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

/**
 * Describes the structure of a specific class.
 */
@SuppressWarnings("sunapi")
public class Schema {

    /**
     * The class this schema describes.
     */
    private final Class<?> schemaClass;

    /**
     * A sorted list containing all field specifications within this schema.
     */
    private final Set<FieldSpec> schemaFields = new TreeSet<>(Comparator.comparing(FieldSpec::getName));

    /**
     * A cached iterator instance to prevent instance creation.
     */
    private FieldSpec[] fieldCache = null;

    /**
     * The unsafe instance for schema generation.
     */
    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    /**
     * The constant size of the class this schema describes.
     */
    private int constantSize = 0;

    /**
     * Indicates if all fields contained within this schema and all children schemas have a constant size.
     */
    private boolean isConstant = false;

    /**
     * All enum constants if this schema describes an enum.
     */
    private final ArrayList<Enum> enumConstants = new ArrayList<>();

    /**
     * Creates a new Schema instance for the provided class.
     *
     * @param p_class The class described by this schema instance.
     */
    public Schema(final Class<?> p_class) {
        schemaClass = p_class;
    }

    /**
     * Adds a new field to this schema.
     *
     * @param field The field to add.
     */
    public void addField(final Field field) {
        field.setAccessible(true);
        FieldSpec fieldSpec = new FieldSpec(
                FieldType.fromClass(field.getType()),
                UNSAFE.objectFieldOffset(field),
                field.getName(),
                field);

        if (fieldSpec.hasConstantSize()) {
            constantSize += SizeUtil.constantSizeOf(fieldSpec);
        }

        schemaFields.add(fieldSpec);

        if (fieldSpec.isArray()) {
            constantSize += Integer.BYTES;
        }

        onFieldsUpdated();
    }

    public void addEnumConstant(final Enum constant) {
        enumConstants.add(constant.ordinal(), constant);
    }

    public Enum getEnumConstant(final int ordinal) {
        return enumConstants.get(ordinal);
    }

    public int getEnumCount() {
        return enumConstants.size();
    }

    /**
     * Called whenever the fields of this schema are updated.
     */
    private void onFieldsUpdated() {
        int arrayCount = (int) schemaFields.stream().filter(FieldSpec::isArray).count();
        fieldCache = new FieldSpec[schemaFields.size() + arrayCount];

        int i = 0;
        for (FieldSpec spec : schemaFields) {
            if (spec.isArray()) {
                fieldCache[i++] = new FieldSpec(
                        FieldType.LENGTH,
                        spec.getOffset(),
                        FieldUtil.ARRAY_LENGTH_NAME,
                        spec.getField());
            }

            fieldCache[i++] = spec;
        }

        isConstant = schemaFields.stream().allMatch(FieldSpec::hasConstantSize);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Add header
        builder.append(schemaClass.getCanonicalName()).append('\n');

        // Add rule below header
        builder.append("-".repeat(schemaClass.getCanonicalName().length()));

        // Add newline after rule
        builder.append('\n');

        // Add each field specification
        for (FieldSpec fieldSpec : schemaFields) {
            builder.append(fieldSpec).append('\n');
        }

        return builder.toString();
    }

    /**
     * Returns a set containing all field specifications within this schema.
     *
     * @return A set containing all field specifications within this schema.
     */
    public FieldSpec[] getFields() {
        return fieldCache;
    }

    /**
     * Calculates an object's size using this schema.
     *
     * @param instance The object.
     * @return The object's size in bytes.
     */
    public int getSize(final Object instance) {
        if (isConstant) {
            return constantSize;
        }

        int size = 0;
        for (FieldSpec fieldSpec : schemaFields) {
            if (!fieldSpec.hasConstantSize()) {
                size += SizeUtil.sizeOf(instance, fieldSpec);
            }
        }

        return size + constantSize;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public int getConstantSize() {
        return constantSize;
    }

    public Class<?> getTarget() {
        return schemaClass;
    }

    /**
     * Describes a field within a class.
     */
    public static final class FieldSpec {

        /**
         * The field's type.
         */
        private final FieldType fieldType;

        /**
         * The field's offset within the class.
         */
        private final long offset;

        /**
         * The field's name.
         */
        private final String name;

        /**
         * The field.
         */
        private final Field field;

        /**
         * The field's type.
         */
        private final Class<?> type;

        /**
         * Indicates if this field is an enum.
         */
        private final boolean isEnum;

        public FieldSpec(final FieldType fieldType, final long offset, final String name, final Field field) {
            this.fieldType = fieldType;
            this.offset = offset;
            this.name = name;
            this.field = field;

            type = this.field.getType();
            isEnum = type.isEnum();
        }

        public FieldType getFieldType() {
            return fieldType;
        }

        public Class<?> getType() {
            return type;
        }

        public long getOffset() {
            return offset;
        }

        public String getName() {
            return name;
        }

        public Field getField() {
            return field;
        }

        public boolean isEnum() {
            return isEnum;
        }

        public boolean hasConstantSize() {
            return fieldType.hasConstantSize();
        }

        @Override
        public String toString() {
            return name;
        }

        public boolean isArray() {
            return fieldType.getId() >= FieldType.BYTE_ARRAY.getId() && fieldType.getId() <= FieldType.OBJECT_ARRAY.getId();
        }
    }

}
