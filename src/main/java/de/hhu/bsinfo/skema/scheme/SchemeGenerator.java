package de.hhu.bsinfo.skema.scheme;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used for scheme generation of specific classes.
 */
final class SchemeGenerator {

    private static final String ENUM_VALUES_FIELD = "$VALUES";

    private static final String ENUM_ORDINAL_FIELD = "ordinal";

    private static final int EXCLUDED_MODIFIERS = Modifier.STATIC | Modifier.TRANSIENT;

    private SchemeGenerator() {}

    /**
     * Generates a scheme for the specified class.
     *
     * @param p_class The class.
     * @return A scheme for the specified class.
     */
    public static Scheme generate(Class<?> p_class) {
        if (p_class.equals(Object.class)) {
            throw new IllegalArgumentException(String.format("Generating scheme for %s is not supported", p_class.getCanonicalName()));
        }

        if (p_class.isEnum()) {
            return generateEnumSchema(p_class);
        }

        Scheme scheme = new Scheme(p_class);
        Field[] fields = getAllFields(p_class);
        for (Field field : fields) {
            if ((field.getModifiers() & EXCLUDED_MODIFIERS) == 0) {
                scheme.addField(field);
            }
        }

        return scheme;
    }

    private static Scheme generateEnumSchema(Class<?> p_class) {
        Scheme scheme = new Scheme(p_class);

        Enum[] enumConstants = getEnumConstants(p_class);
        for (Enum constant : enumConstants) {
            scheme.addEnumConstant(constant);
        }

        Field[] fields = getAllFields(p_class);
        for (Field field : fields) {
            if (field.getName().equals(ENUM_ORDINAL_FIELD)) {
                scheme.addField(field);
            }
        }

        return scheme;
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
