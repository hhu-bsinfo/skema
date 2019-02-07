package de.hhu.bsinfo.skema.scheme;

import java.lang.reflect.Field;
import java.util.List;

public class ObjectScheme extends Scheme {
    public ObjectScheme(Class<?> p_class, final List<String> p_fields) {
        super(p_class);
        Field[] fields = p_class.getDeclaredFields();
        for (Field field : fields) {
            if (p_fields.contains(field.getName())) {
                addField(field);
            }
        }
    }
}
