package de.hhu.bsinfo.autochunk.demo;

public class ArrayUtil {

    public static Object getArray(final Object p_object, final Schema.FieldSpec p_fieldSpec) {
        try {
            return p_fieldSpec.getField().get(p_object);
        } catch (IllegalAccessException e) {
            // This should never happen since all fields have been made accessible
            throw new IllegalStateException(String.format("Field %s is not accessible", p_fieldSpec.getName()));
        }
    }

}
