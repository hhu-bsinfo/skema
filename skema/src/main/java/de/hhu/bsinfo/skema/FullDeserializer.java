package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.Constants;
import de.hhu.bsinfo.skema.util.FieldUtil;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

@SuppressWarnings("sunapi")
final class FullDeserializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private FullDeserializer() {}

    static int deserialize(final Object p_object, final byte[] p_buffer, final int p_offset) {
        Schema schema = SchemaRegistry.getSchema(p_object.getClass());
        int position = p_offset;
        int arrayLength = 0;
        int i, j;
        Object object = null;
        Object[] array = null;
        Schema.FieldSpec[] fields = schema.getFields();
        Schema.FieldSpec fieldSpec = null;
        for (i = 0; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case BYTE:
                    UNSAFE.putByte(p_object, fieldSpec.getOffset(), UNSAFE.getByte(p_buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Byte.BYTES;
                    break;

                case CHAR:
                    UNSAFE.putChar(p_object, fieldSpec.getOffset(), UNSAFE.getChar(p_buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Character.BYTES;
                    break;

                case SHORT:
                    UNSAFE.putShort(p_object, fieldSpec.getOffset(), UNSAFE.getShort(p_buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Short.BYTES;
                    break;

                case INT:
                    UNSAFE.putInt(p_object, fieldSpec.getOffset(), UNSAFE.getInt(p_buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Integer.BYTES;
                    break;

                case LONG:
                    UNSAFE.putLong(p_object, fieldSpec.getOffset(), UNSAFE.getLong(p_buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Long.BYTES;
                    break;

                case FLOAT:
                    UNSAFE.putFloat(p_object, fieldSpec.getOffset(), UNSAFE.getFloat(p_buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Float.BYTES;
                    break;

                case DOUBLE:
                    UNSAFE.putDouble(p_object, fieldSpec.getOffset(), UNSAFE.getDouble(p_buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Double.BYTES;
                    break;

                case BOOLEAN:
                    UNSAFE.putBoolean(p_object, fieldSpec.getOffset(), UNSAFE.getByte(p_buffer, Constants.BYTE_ARRAY_OFFSET + position) == Constants.TRUE);
                    position += Byte.BYTES;
                    break;

                case LENGTH:
                    arrayLength = UNSAFE.getInt(p_buffer, Constants.BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    break;

                //  Arrays are handled specially since their size is not constant.
                //
                //      1. Read length field from buffer
                //      2. Allocate an array with enough space to store the data
                //      3. Set the array reference within the object to the allocated array

                case BYTE_ARRAY:
                    byte[] bytes = new byte[arrayLength];
                    UNSAFE.copyMemory(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, bytes, Constants.BYTE_ARRAY_OFFSET, arrayLength * Byte.BYTES);
                    position += arrayLength * Byte.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), bytes);
                    break;

                case CHAR_ARRAY:
                    char[] chars = new char[arrayLength];
                    UNSAFE.copyMemory(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, chars, Constants.CHAR_ARRAY_OFFSET, arrayLength * Character.BYTES);
                    position += arrayLength * Character.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), chars);
                    break;

                case SHORT_ARRAY:
                    short[] shorts = new short[arrayLength];
                    UNSAFE.copyMemory(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, shorts, Constants.SHORT_ARRAY_OFFSET, arrayLength * Short.BYTES);
                    position += arrayLength * Short.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), shorts);
                    break;

                case INT_ARRAY:
                    int[] ints = new int[arrayLength];
                    UNSAFE.copyMemory(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, ints, Constants.INT_ARRAY_OFFSET, arrayLength * Integer.BYTES);
                    position += arrayLength * Integer.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), ints);
                    break;

                case LONG_ARRAY:
                    long[] longs = new long[arrayLength];
                    UNSAFE.copyMemory(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, longs, Constants.LONG_ARRAY_OFFSET, arrayLength * Long.BYTES);
                    position += arrayLength * Long.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), longs);
                    break;

                case FLOAT_ARRAY:
                    float[] floats = new float[arrayLength];
                    UNSAFE.copyMemory(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, floats, Constants.FLOAT_ARRAY_OFFSET, arrayLength * Float.BYTES);
                    position += arrayLength * Float.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), floats);
                    break;

                case DOUBLE_ARRAY:
                    double[] doubles = new double[arrayLength];
                    UNSAFE.copyMemory(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, doubles, Constants.DOUBLE_ARRAY_OFFSET, arrayLength * Double.BYTES);
                    position += arrayLength * Double.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), doubles);
                    break;

                case BOOLEAN_ARRAY:
                    boolean[] booleans = new boolean[arrayLength];
                    UNSAFE.copyMemory(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, booleans, Constants.BOOLEAN_ARRAY_OFFSET, arrayLength * Byte.BYTES);
                    position += arrayLength * Byte.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), booleans);
                    break;

                //  Primitive types are handled above. All other types must be objects.
                //  In this case we create an instance of the class and deserialize our data into it.

                case ENUM:
                    object = deserializeEnum(fieldSpec, p_buffer, p_offset);
                    position += Integer.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), object);
                    break;

                case OBJECT:
                    object = FieldUtil.allocateInstance(fieldSpec);
                    position += deserialize(object, p_buffer, position);
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), object);
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.allocateArray(fieldSpec, arrayLength);
                    for (j = 0; j < arrayLength; j++) {
                        object = FieldUtil.allocateComponent(fieldSpec);
                        position += deserialize(object, p_buffer, position);
                        UNSAFE.putObject(array, Constants.OBJECT_ARRAY_OFFSET + j * Constants.REFERENCE_SIZE, object);
                    }
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), array);
                    break;
            }
        }

        return position - p_offset;
    }

    static int deserialize(final Object p_object, final long p_address) {
        Schema schema = SchemaRegistry.getSchema(p_object.getClass());
        long position = p_address;
        int arrayLength = 0;
        int i, j;
        Object object = null;
        Object[] array = null;
        Schema.FieldSpec[] fields = schema.getFields();
        Schema.FieldSpec fieldSpec = null;
        for (i = 0; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case BYTE:
                    UNSAFE.putByte(p_object, fieldSpec.getOffset(), UNSAFE.getByte(position));
                    position += Byte.BYTES;
                    break;

                case CHAR:
                    UNSAFE.putChar(p_object, fieldSpec.getOffset(), UNSAFE.getChar(position));
                    position += Character.BYTES;
                    break;

                case SHORT:
                    UNSAFE.putShort(p_object, fieldSpec.getOffset(), UNSAFE.getShort(position));
                    position += Short.BYTES;
                    break;

                case INT:
                    UNSAFE.putInt(p_object, fieldSpec.getOffset(), UNSAFE.getInt(position));
                    position += Integer.BYTES;
                    break;

                case LONG:
                    UNSAFE.putLong(p_object, fieldSpec.getOffset(), UNSAFE.getLong(position));
                    position += Long.BYTES;
                    break;

                case FLOAT:
                    UNSAFE.putFloat(p_object, fieldSpec.getOffset(), UNSAFE.getFloat(position));
                    position += Float.BYTES;
                    break;

                case DOUBLE:
                    UNSAFE.putDouble(p_object, fieldSpec.getOffset(), UNSAFE.getDouble(position));
                    position += Double.BYTES;
                    break;

                case BOOLEAN:
                    UNSAFE.putBoolean(p_object, fieldSpec.getOffset(), UNSAFE.getByte(position) == Constants.TRUE);
                    position += Byte.BYTES;
                    break;

                case LENGTH:
                    arrayLength = UNSAFE.getInt(position);
                    position += Integer.BYTES;
                    break;

                //  Arrays are handled specially since their size is not constant.
                //
                //      1. Read length field from buffer
                //      2. Allocate an array with enough space to store the data
                //      3. Set the array reference within the object to the allocated array

                case BYTE_ARRAY:
                    byte[] bytes = new byte[arrayLength];
                    UNSAFE.copyMemory(null, position, bytes, Constants.BYTE_ARRAY_OFFSET, arrayLength * Byte.BYTES);
                    position += arrayLength * Byte.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), bytes);
                    break;

                case CHAR_ARRAY:
                    char[] chars = new char[arrayLength];
                    UNSAFE.copyMemory(null, position, chars, Constants.CHAR_ARRAY_OFFSET, arrayLength * Character.BYTES);
                    position += arrayLength * Character.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), chars);
                    break;

                case SHORT_ARRAY:
                    short[] shorts = new short[arrayLength];
                    UNSAFE.copyMemory(null, position, shorts, Constants.SHORT_ARRAY_OFFSET, arrayLength * Short.BYTES);
                    position += arrayLength * Short.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), shorts);
                    break;

                case INT_ARRAY:
                    int[] ints = new int[arrayLength];
                    UNSAFE.copyMemory(null, position, ints, Constants.INT_ARRAY_OFFSET, arrayLength * Integer.BYTES);
                    position += arrayLength * Integer.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), ints);
                    break;

                case LONG_ARRAY:
                    long[] longs = new long[arrayLength];
                    UNSAFE.copyMemory(null, position, longs, Constants.LONG_ARRAY_OFFSET, arrayLength * Long.BYTES);
                    position += arrayLength * Long.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), longs);
                    break;

                case FLOAT_ARRAY:
                    float[] floats = new float[arrayLength];
                    UNSAFE.copyMemory(null, position, floats, Constants.FLOAT_ARRAY_OFFSET, arrayLength * Float.BYTES);
                    position += arrayLength * Float.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), floats);
                    break;

                case DOUBLE_ARRAY:
                    double[] doubles = new double[arrayLength];
                    UNSAFE.copyMemory(null, position, doubles, Constants.DOUBLE_ARRAY_OFFSET, arrayLength * Double.BYTES);
                    position += arrayLength * Double.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), doubles);
                    break;

                case BOOLEAN_ARRAY:
                    boolean[] booleans = new boolean[arrayLength];
                    UNSAFE.copyMemory(null, position, booleans, Constants.BOOLEAN_ARRAY_OFFSET, arrayLength * Byte.BYTES);
                    position += arrayLength * Byte.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), booleans);
                    break;

                //  Primitive types are handled above. All other types must be objects.
                //  In this case we create an instance of the class and deserialize our data into it.

                case ENUM:
                    object = deserializeEnum(fieldSpec, position);
                    position += Integer.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), object);
                    break;

                case OBJECT:
                    object = FieldUtil.allocateInstance(fieldSpec);
                    position += deserialize(object, position);
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), object);
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.allocateArray(fieldSpec, arrayLength);
                    for (j = 0; j < arrayLength; j++) {
                        object = FieldUtil.allocateComponent(fieldSpec);
                        position += deserialize(object, position);
                        UNSAFE.putObject(array, Constants.OBJECT_ARRAY_OFFSET + j * Constants.REFERENCE_SIZE, object);
                    }
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), array);
                    break;
            }
        }

        return (int) (position - p_address);
    }

    static <T> T deserializeEnum(final Class<T> p_class, final byte[] p_buffer) {
        return deserializeEnum(p_class, p_buffer, 0);
    }

    static <T> T deserializeEnum(final Class<T> p_class, final byte[] p_buffer, final int p_offset) {
        Schema schema = SchemaRegistry.getSchema(p_class);
        final int ordinal = UNSAFE.getInt(p_buffer, Constants.BYTE_ARRAY_OFFSET + p_offset);
        return p_class.cast(schema.getEnumConstant(ordinal));
    }

    static <T> T deserializeEnum(final Class<T> p_class, final long p_address) {
        Schema schema = SchemaRegistry.getSchema(p_class);
        final int ordinal = UNSAFE.getInt(p_address);
        return p_class.cast(schema.getEnumConstant(ordinal));
    }

    static Object deserializeEnum(final Schema.FieldSpec p_fieldSpec, final byte[] p_buffer, final int p_offset) {
        Schema schema = SchemaRegistry.getSchema(p_fieldSpec.getType());
        final int ordinal = UNSAFE.getInt(p_buffer, Constants.BYTE_ARRAY_OFFSET + p_offset);
        return schema.getEnumConstant(ordinal);
    }

    static Object deserializeEnum(final Schema.FieldSpec p_fieldSpec, final long p_address) {
        Schema schema = SchemaRegistry.getSchema(p_fieldSpec.getType());
        final int ordinal = UNSAFE.getInt(p_address);
        return schema.getEnumConstant(ordinal);
    }
}
