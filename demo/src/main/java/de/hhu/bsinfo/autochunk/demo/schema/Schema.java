package de.hhu.bsinfo.autochunk.demo.schema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import de.hhu.bsinfo.autochunk.demo.util.FieldType;
import de.hhu.bsinfo.autochunk.demo.util.SizeUtil;
import de.hhu.bsinfo.autochunk.demo.util.UnsafeProvider;

/**
 * Describes the structure of a specific class.
 */
public class Schema {

    /**
     * The class this schema describes.
     */
    private final Class<?> m_class;

    /**
     * A sorted set containing all field specifications within this schema.
     */
    private final Set<FieldSpec> m_fields = new TreeSet<>(Comparator.comparing(FieldSpec::getName));

    /**
     * A cached iterator instance to prevent instance creation.
     */
    private FieldSpec[] m_fieldArray = null;

    /**
     * The unsafe instance for schema generation.
     */
    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    /**
     * The constant size of the class this schema describes.
     */
    private int m_constantSize = 0;

    /**
     * Indicates if all fields contained within this schema and all children schemes have a constant size.
     */
    private boolean m_isConstant = false;

    /**
     * Creates a new Schema instance for the provided class.
     *
     * @param p_class The class described by this schema instance.
     */
    public Schema(final Class<?> p_class) {
        m_class = p_class;
    }

    /**
     * Adds a new field to this schema.
     *
     * @param p_field The field to add.
     */
    public void addField(final Field p_field) {
        p_field.setAccessible(true);
        FieldSpec fieldSpec = new FieldSpec(
                FieldType.fromClass(p_field.getType()),
                UNSAFE.objectFieldOffset(p_field),
                p_field.getName(),
                p_field);

        if (fieldSpec.hasConstantSize()) {
            m_constantSize += SizeUtil.constantSizeOf(fieldSpec);
        }

        m_fields.add(fieldSpec);

        onFieldsUpdated();
    }

    /**
     * Called whenever the fields of this schema are updated.
     */
    private void onFieldsUpdated() {
        m_isConstant = m_fields.stream().allMatch(FieldSpec::hasConstantSize);
        m_fieldArray = m_fields.toArray(new FieldSpec[0]);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Add header
        builder.append(m_class.getCanonicalName()).append('\n');

        // Add rule below header
        for (int i = 0; i < m_class.getCanonicalName().length(); i++) {
            builder.append('-');
        }

        // Add newline after rule
        builder.append('\n');

        // Add each field specification
        for (FieldSpec fieldSpec : m_fields) {
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
        return m_fieldArray;
    }

    /**
     * Calculates an object's size using this schema.
     *
     * @param p_object The object.
     * @return The object's size in bytes.
     */
    public int getSize(final Object p_object) {
        if (m_isConstant) {
            return m_constantSize;
        }

        int size = 0;
        for (FieldSpec fieldSpec : m_fields) {
            if (!fieldSpec.hasConstantSize()) {
                size += SizeUtil.sizeOf(p_object, fieldSpec);
            }
        }

        return size + m_constantSize;
    }

    public boolean isConstant() {
        return m_isConstant;
    }

    public Class<?> getTarget() {
        return m_class;
    }

    /**
     * Describes a field within a class.
     */
    public static final class FieldSpec {

        /**
         * The field's type.
         */
        private final FieldType m_type;

        /**
         * The field's offset within the class.
         */
        private final long m_offset;

        /**
         * The field's name.
         */
        private final String m_name;

        /**
         * the field.
         */
        private final Field m_field;

        public FieldSpec(final FieldType p_type, final long p_offset, final String p_name, final Field p_field) {
            m_type = p_type;
            m_offset = p_offset;
            m_name = p_name;
            m_field = p_field;
        }

        public FieldType getType() {
            return m_type;
        }

        public long getOffset() {
            return m_offset;
        }

        public String getName() {
            return m_name;
        }

        public Field getField() {
            return m_field;
        }

        public boolean hasConstantSize() {
            return m_type.hasConstantSize();
        }

        @Override
        public String toString() {
            return m_name;
        }
    }

}
