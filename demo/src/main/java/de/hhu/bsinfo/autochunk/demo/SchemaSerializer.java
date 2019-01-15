package de.hhu.bsinfo.autochunk.demo;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class SchemaSerializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private static final long BYTE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
    private static final long SHORT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(long[].class);
    private static final long INT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(int[].class);
    private static final long LONG_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(long[].class);

    private static final Map<Class<?>, Schema> SCHEMAS = new HashMap<>();

    public static synchronized void register(Class<?> p_class) {
        SCHEMAS.put(p_class, SchemaGenerator.generate(p_class));
    }

    private SchemaSerializer() {}

    public static Schema getSchema(Class<?> p_class) {
        Schema schema = SCHEMAS.get(p_class);

        if (schema == null) {
            throw new IllegalArgumentException(String.format("No schema for class %s was registered",
                    p_class.getCanonicalName()));
        }

        return schema;
    }

    public static void serialize(final Object p_object, final byte[] p_buffer) {
        Schema schema = getSchema(p_object.getClass());
        int position = 0;
        int arraySize;
        int arrayLength;
        Object arrayRef;
        for (Schema.FieldSpec fieldSpec : schema.getFields()) {
            switch (fieldSpec.getType()) {

                case BYTE:
                    UNSAFE.putByte(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(p_object, fieldSpec.getOffset()));
                    position += Byte.BYTES;
                    break;

                case SHORT:
                    UNSAFE.putShort(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getShort(p_object, fieldSpec.getOffset()));
                    position += Short.BYTES;
                    break;

                case INT:
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getInt(p_object, fieldSpec.getOffset()));
                    position += Integer.BYTES;
                    break;

                case LONG:
                    UNSAFE.putLong(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getLong(p_object, fieldSpec.getOffset()));
                    position += Long.BYTES;
                    break;

                //  Arrays are handled specially since their size is not constant.
                //
                //      1. Get a reference on the array
                //      2. Read the array's length field
                //      3. Write the array's length field to the buffer
                //      4. Write the array's content to the buffer

                case BYTE_ARRAY:
                    arrayRef = ArrayUtil.getArray(p_object, fieldSpec);
                    arrayLength = Array.getLength(arrayRef);
                    arraySize = arrayLength * Byte.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(arrayRef, BYTE_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case SHORT_ARRAY:
                    arrayRef = ArrayUtil.getArray(p_object, fieldSpec);
                    arrayLength = Array.getLength(arrayRef);
                    arraySize = arrayLength * Short.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(arrayRef, SHORT_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case INT_ARRAY:
                    arrayRef = ArrayUtil.getArray(p_object, fieldSpec);
                    arrayLength = Array.getLength(arrayRef);
                    arraySize = arrayLength * Integer.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(arrayRef, INT_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case LONG_ARRAY:
                    arrayRef = ArrayUtil.getArray(p_object, fieldSpec);
                    arrayLength = Array.getLength(arrayRef);
                    arraySize = arrayLength * Long.BYTES;
                    UNSAFE.putInt(p_buffer, LONG_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(arrayRef, LONG_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                default:
                    break;
            }
        }
    }

    public static void deserialize(final Object p_object, final byte[] p_buffer) {
        Schema schema = getSchema(p_object.getClass());
        int position = 0;
        int arrayLength;
        for (Schema.FieldSpec fieldSpec : schema.getFields()) {
            switch (fieldSpec.getType()) {

                case BYTE:
                    UNSAFE.putByte(p_object, fieldSpec.getOffset(), UNSAFE.getByte(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Byte.BYTES;
                    break;

                case SHORT:
                    UNSAFE.putShort(p_object, fieldSpec.getOffset(), UNSAFE.getShort(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Short.BYTES;
                    break;

                case INT:
                    UNSAFE.putInt(p_object, fieldSpec.getOffset(), UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Integer.BYTES;
                    break;

                case LONG:
                    UNSAFE.putLong(p_object, fieldSpec.getOffset(), UNSAFE.getLong(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Long.BYTES;
                    break;

                //  Arrays are handled specially since their size is not constant.
                //
                //      1. Read length field from buffer
                //      2. Allocate an array with enough space to store the data
                //      3. Set the array reference within the object to the allocated array

                case BYTE_ARRAY:
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    byte[] bytes = new byte[arrayLength];
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, bytes, BYTE_ARRAY_OFFSET, arrayLength * Byte.BYTES);
                    position += arrayLength * Byte.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), bytes);
                    break;

                case SHORT_ARRAY:
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    short[] shorts = new short[arrayLength];
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, shorts, SHORT_ARRAY_OFFSET, arrayLength * Short.BYTES);
                    position += arrayLength * Short.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), shorts);
                    break;

                case INT_ARRAY:
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    int[] ints = new int[arrayLength];
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, ints, INT_ARRAY_OFFSET, arrayLength * Integer.BYTES);
                    position += arrayLength * Integer.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), ints);
                    break;

                case LONG_ARRAY:
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    long[] longs = new long[arrayLength];
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, longs, LONG_ARRAY_OFFSET, arrayLength * Long.BYTES);
                    position += arrayLength * Long.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), longs);
                    break;

                default:
                    break;
            }
        }
    }

}
