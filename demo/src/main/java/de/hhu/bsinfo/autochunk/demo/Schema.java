package de.hhu.bsinfo.autochunk.demo;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Schema {

    /**
     * The class this schema describes.
     */
    private final Class<?> m_parent;

    /**
     * A set containing all field specifications within this schema.
     */
    private final Set<FieldSpec> m_fields = new TreeSet<>(Comparator.comparing(FieldSpec::getName));

    /**
     * The unsafe instance for schema generation.
     */
    private static final sun.misc.Unsafe m_unsafe = UnsafeProvider.getUnsafe();

    public Schema(final Class<?> p_parent) {
        m_parent = p_parent;
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
                m_unsafe.objectFieldOffset(p_field),
                p_field.getName(),
                p_field);

        if (fieldSpec.getType() == FieldType.UNKNOWN) {
            throw new IllegalArgumentException(String.format("%s is not yet supported", p_field.getType().getCanonicalName()));
        }

        m_fields.add(fieldSpec);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Add header
        builder.append(m_parent.getCanonicalName()).append('\n');

        // Add rule below header
        for (int i = 0; i < m_parent.getCanonicalName().length(); i++) {
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

    public Set<FieldSpec> getFields() {
        return m_fields;
    }



    public int getSize(final Object p_object) {
        return m_fields.stream()
                .mapToInt(p_fieldSpec -> SizeUtil.sizeOf(p_object, p_fieldSpec))
                .sum();
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

        @Override
        public String toString() {
            return m_name;
        }
    }

}
