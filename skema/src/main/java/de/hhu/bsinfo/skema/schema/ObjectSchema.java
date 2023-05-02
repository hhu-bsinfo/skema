package de.hhu.bsinfo.skema.schema;

import java.lang.reflect.Field;
import java.util.List;

public class ObjectSchema extends Schema {
    public ObjectSchema(Class<?> clazz, final List<String> fields) {
        super(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            if (fields.contains(field.getName())) {
                addField(field);
            }
        }
    }
}
