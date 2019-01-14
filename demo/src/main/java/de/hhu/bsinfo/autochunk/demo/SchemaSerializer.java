package de.hhu.bsinfo.autochunk.demo;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class SchemaSerializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private static final Map<Class<?>, Schema> SCHEMAS = new HashMap<>();

    public static void register(Class<?> p_class) {
        SCHEMAS.put(p_class, SchemaGenerator.generate(p_class));
    }

    private SchemaSerializer() {}

    private static Schema getSchema(Class<?> p_class) {
        Schema schema = SCHEMAS.get(p_class);

        if (schema == null) {
            throw new IllegalStateException(String.format("No schema for class %s was registered",
                    p_class.getCanonicalName()));
        }

        return schema;
    }

    public static int getSize(Class<?> p_class) {
        return getSchema(p_class).getSize();
    }

    public static void serialize(final Object p_object, final ByteBuffer p_buffer) {
        Schema schema = getSchema(p_object.getClass());
        for (Schema.FieldSpec fieldSpec : schema.getFields()) {
            switch (fieldSpec.getType()) {
                case BYTE:
                    p_buffer.put(UNSAFE.getByte(p_object, fieldSpec.getOffset()));
                    break;
                case SHORT:
                    p_buffer.putShort(UNSAFE.getShort(p_object, fieldSpec.getOffset()));
                    break;
                case INT:
                    p_buffer.putInt(UNSAFE.getInt(p_object, fieldSpec.getOffset()));
                    break;
                case LONG:
                    p_buffer.putLong(UNSAFE.getLong(p_object, fieldSpec.getOffset()));
                    break;
                default:
                    break;
            }
        }
    }

    public static void deserialize(final Object p_object, final ByteBuffer p_buffer) {
        Schema schema = getSchema(p_object.getClass());
        for (Schema.FieldSpec fieldSpec : schema.getFields()) {
            switch (fieldSpec.getType()) {
                case BYTE:
                    UNSAFE.putByte(p_object, fieldSpec.getOffset(), p_buffer.get());
                    break;
                case SHORT:
                    UNSAFE.putShort(p_object, fieldSpec.getOffset(), p_buffer.getShort());
                    break;
                case INT:
                    UNSAFE.putInt(p_object, fieldSpec.getOffset(), p_buffer.getInt());
                    break;
                case LONG:
                    UNSAFE.putLong(p_object, fieldSpec.getOffset(), p_buffer.getLong());
                    break;
                default:
                    break;
            }
        }
    }

}
