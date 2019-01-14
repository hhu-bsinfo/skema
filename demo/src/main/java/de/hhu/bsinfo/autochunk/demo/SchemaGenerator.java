package de.hhu.bsinfo.autochunk.demo;

import java.lang.reflect.Field;

public class SchemaGenerator {

    public static Schema generate(Class<?> p_class) {
        Field[] fields = p_class.getDeclaredFields();
        Schema schema = new Schema(p_class);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Schemable.class)) {
                schema.addField(field);
            }
        }

        return schema;
    }
}
