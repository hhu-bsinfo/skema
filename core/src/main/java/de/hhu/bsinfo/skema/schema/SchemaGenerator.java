package de.hhu.bsinfo.skema.schema;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used for schema generation of specific classes.
 */
final class SchemaGenerator {

    private static final String ENUM_VALUES_FIELD = "$VALUES";

    private static final String ENUM_ORDINAL_FIELD = "ordinal";

    private static final int EXCLUDED_MODIFIERS = Modifier.STATIC | Modifier.TRANSIENT;

    private SchemaGenerator() {}

    /**
     * Generates a schema for the specified class.
     *
     * @param clazz The class.
     * @return A schema for the specified class.
     */
    public static Schema generate(Class<?> clazz) {
        if (clazz.equals(Object.class)) {
            throw new IllegalArgumentException(String.format("Generating schema for %s is not supported", clazz.getCanonicalName()));
        }

        if (clazz.isEnum()) {
            return generateEnumSchema(clazz);
        }

        Schema schema = new Schema(clazz);
        Field[] fields = getAllFields(clazz);
        for (Field field : fields) {
            if ((field.getModifiers() & EXCLUDED_MODIFIERS) == 0) {
                schema.addField(field);
            }
        }

        return schema;
    }

    private static Schema generateEnumSchema(Class<?> clazz) {
        Schema schema = new Schema(clazz);

        Enum[] enumConstants = getEnumConstants(clazz);
        for (Enum constant : enumConstants) {
            schema.addEnumConstant(constant);
        }

        Field[] fields = getAllFields(clazz);
        for (Field field : fields) {
            if (field.getName().equals(ENUM_ORDINAL_FIELD)) {
                schema.addField(field);
            }
        }

        return schema;
    }

    static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while(clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        return fieldList.toArray(new Field[0]);
    }

    static Enum[] getEnumConstants(Class<?> clazz) {
        if (!clazz.isEnum()) {
            return null;
        }

        List<Enum> enumList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers()) && !field.getName().equals(ENUM_VALUES_FIELD)) {
                enumList.add(getEnumField(field));
            }
        }

        return enumList.toArray(new Enum[0]);
    }

    static Enum getEnumField(final Field field) {
        try {
            return (Enum) field.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
