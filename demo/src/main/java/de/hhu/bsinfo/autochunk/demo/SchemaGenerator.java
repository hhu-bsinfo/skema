package de.hhu.bsinfo.autochunk.demo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hhu.bsinfo.autochunk.demo.schema.Schema;
import jdk.net.SocketFlow;

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
     * @param p_class The class.
     * @return A schema for the specified class.
     */
    static Schema generate(Class<?> p_class) {
        if (p_class.isEnum()) {
            return generateEnumSchema(p_class);
        }

        Schema schema = new Schema(p_class);
        Field[] fields = getAllFields(p_class);
        for (Field field : fields) {
            if ((field.getModifiers() & EXCLUDED_MODIFIERS) == 0) {
                schema.addField(field);
            }
        }

        return schema;
    }

    static Schema generateEnumSchema(Class<?> p_class) {
        Schema schema = new Schema(p_class);

        Enum[] enumConstants = getEnumConstants(p_class);
        for (Enum constant : enumConstants) {
            schema.addEnumConstant(constant);
        }

        Field[] fields = getAllFields(p_class);
        for (Field field : fields) {
            if (field.getName().equals(ENUM_ORDINAL_FIELD)) {
                schema.addField(field);
            }
        }

        return schema;
    }

    static Field[] getAllFields(Class<?> p_class) {
        List<Field> fieldList = new ArrayList<>();
        while(p_class != null) {
            Field[] fields = p_class.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            p_class = p_class.getSuperclass();
        }
        return fieldList.toArray(new Field[0]);
    }

    static Enum[] getEnumConstants(Class<?> p_class) {
        if (!p_class.isEnum()) {
            return null;
        }

        List<Enum> enumList = new ArrayList<>();
        Field[] fields = p_class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers()) && !field.getName().equals(ENUM_VALUES_FIELD)) {
                enumList.add(getEnumField(field));
            }
        }

        return enumList.toArray(new Enum[0]);
    }

    static Enum getEnumField(final Field p_field) {
        try {
            return (Enum) p_field.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
