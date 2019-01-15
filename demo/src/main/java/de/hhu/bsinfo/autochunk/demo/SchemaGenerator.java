package de.hhu.bsinfo.autochunk.demo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class SchemaGenerator {

    public static Schema generate(Class<?> p_class) {
        Field[] fields = p_class.getDeclaredFields();
        Schema schema = new Schema(p_class);
        for (Field field : fields) {
            if (!Modifier.isTransient(field.getModifiers())) {
                schema.addField(field);
            }
        }

        return schema;
    }
}
