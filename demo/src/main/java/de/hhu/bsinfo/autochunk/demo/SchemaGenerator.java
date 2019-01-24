package de.hhu.bsinfo.autochunk.demo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import de.hhu.bsinfo.autochunk.demo.schema.Schema;

/**
 * Used for schema generation of specific classes.
 */
final class SchemaGenerator {

    private SchemaGenerator() {}

    /**
     * Generates a schema for the specified class.
     *
     * @param p_class The class.
     * @return A schema for the specified class.
     */
    static Schema generate(Class<?> p_class) {
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
