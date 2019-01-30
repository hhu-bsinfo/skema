package de.hhu.bsinfo.autochunk.demo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import de.hhu.bsinfo.autochunk.demo.schema.ObjectSchema;
import de.hhu.bsinfo.autochunk.demo.schema.Schema;
import de.hhu.bsinfo.autochunk.demo.util.FieldUtil;
import de.hhu.bsinfo.autochunk.demo.util.Operation;
import de.hhu.bsinfo.autochunk.demo.util.SizeUtil;
import de.hhu.bsinfo.autochunk.demo.util.UnsafeProvider;

@SuppressWarnings("WeakerAccess")
public final class SchemaSerializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private static final long BYTE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
    private static final long CHAR_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(char[].class);
    private static final long SHORT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(long[].class);
    private static final long INT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(int[].class);
    private static final long LONG_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(long[].class);
    private static final long FLOAT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(float[].class);
    private static final long DOUBLE_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(double[].class);
    private static final long BOOLEAN_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(boolean[].class);
    private static final long OBJECT_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(Object[].class);

    private static final int REFERENCE_SIZE = UNSAFE.arrayIndexScale(Object[].class);

    private static final byte TRUE = 1;
    private static final byte FALSE = 0;

    private static final Map<Class<?>, Schema> SCHEMAS = new HashMap<>();

    static {
        register(
                new ObjectSchema(Byte.class, Collections.singletonList("value")),
                new ObjectSchema(Character.class, Collections.singletonList("value")),
                new ObjectSchema(Short.class, Collections.singletonList("value")),
                new ObjectSchema(Integer.class, Collections.singletonList("value")),
                new ObjectSchema(Long.class, Collections.singletonList("value")),
                new ObjectSchema(Float.class, Collections.singletonList("value")),
                new ObjectSchema(Double.class, Collections.singletonList("value")),
                new ObjectSchema(Boolean.class, Collections.singletonList("value")),
                new ObjectSchema(String.class, Collections.singletonList("value")),
                new ObjectSchema(BigInteger.class, Arrays.asList("signum", "mag")),
                new ObjectSchema(BigDecimal.class, Arrays.asList("intVal", "scale")),
                new ObjectSchema(LocalDate.class, Arrays.asList("day", "month", "year")),
                new ObjectSchema(LocalTime.class, Arrays.asList("hour", "minute", "second", "nano")),
                new ObjectSchema(LocalDateTime.class, Arrays.asList("date", "time")),
                new ObjectSchema(UUID.class, Arrays.asList("mostSigBits", "leastSigBits"))
        );
    }

    public static synchronized void register(Class<?> p_class) {
        SCHEMAS.put(p_class, SchemaGenerator.generate(p_class));
    }

    private static synchronized void register(Schema... p_schema) {
        for (Schema schema : p_schema) {
            SCHEMAS.put(schema.getTarget(), schema);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static synchronized void register(Class<?> p_class, Schema p_schema) {
        SCHEMAS.put(p_class, p_schema);
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

    public static byte[] serialize(final Object p_object) {
        Schema schema = getSchema(p_object.getClass());
        byte[] buffer = new byte[schema.getSize(p_object)];
        serialize(p_object, buffer);
        return buffer;
    }

    public static int serialize(final Object p_object, final byte[] p_buffer) {
        return serialize(p_object, p_buffer, 0);
    }

    public static int serialize(final Object p_object, final byte[] p_buffer, final int p_offset) {
        Schema schema = getSchema(p_object.getClass());
        int position = p_offset;
        int arraySize;
        int arrayLength;
        int i, j;
        Object object;
        Object[] array;
        Schema.FieldSpec[] fields = schema.getFields();
        Schema.FieldSpec fieldSpec = null;
        for (i = 0; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case BYTE:
                    UNSAFE.putByte(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(p_object, fieldSpec.getOffset()));
                    position += Byte.BYTES;
                    break;

                case CHAR:
                    UNSAFE.putChar(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getChar(p_object, fieldSpec.getOffset()));
                    position += Character.BYTES;
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

                case FLOAT:
                    UNSAFE.putFloat(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getFloat(p_object, fieldSpec.getOffset()));
                    position += Float.BYTES;
                    break;

                case DOUBLE:
                    UNSAFE.putDouble(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getDouble(p_object, fieldSpec.getOffset()));
                    position += Double.BYTES;
                    break;

                case BOOLEAN:
                    UNSAFE.putByte(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getBoolean(p_object, fieldSpec.getOffset()) ? TRUE : FALSE);
                    position += Byte.BYTES;
                    break;

                //  Arrays are handled specially since their size is not constant.
                //
                //      1. Get a reference on the array
                //      2. Read the array's length field
                //      3. Write the array's length field to the buffer
                //      4. Write the array's content to the buffer

                case BYTE_ARRAY:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(object);
                    arraySize = arrayLength * Byte.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(object, BYTE_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case CHAR_ARRAY:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(object);
                    arraySize = arrayLength * Character.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(object, CHAR_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case SHORT_ARRAY:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(object);
                    arraySize = arrayLength * Short.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(object, SHORT_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case INT_ARRAY:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(object);
                    arraySize = arrayLength * Integer.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(object, INT_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case LONG_ARRAY:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(object);
                    arraySize = arrayLength * Long.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(object, LONG_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case FLOAT_ARRAY:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(object);
                    arraySize = arrayLength * Float.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(object, FLOAT_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case DOUBLE_ARRAY:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(object);
                    arraySize = arrayLength * Double.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(object, DOUBLE_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case BOOLEAN_ARRAY:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(object);
                    arraySize = arrayLength * Byte.BYTES;
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, arrayLength);
                    position += Integer.BYTES;
                    UNSAFE.copyMemory(object, BOOLEAN_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                //  Primitive types are handled above. All other types must be objects.
                //  In this case we retrieve the instance from our object and serialize it.
                //  (This will fail if the reference is null).

                case ENUM:
                case OBJECT:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    position += serialize(object, p_buffer, position);
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getArray(p_object, fieldSpec);
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, array.length);
                    position += Integer.BYTES;
                    for (j = 0; j < array.length; j++) {
                        position += serialize(array[j], p_buffer, position);
                    }
                    break;
            }
        }

        return position - p_offset;
    }

    public static void deserialize(final Operation p_object, final byte[] p_buffer, final int p_offset) {
        if (p_object.isFinished()) {
            return;
        }

        int bytesProcessed = deserialize(p_object.getObject(), p_buffer, p_offset);
        p_object.addCurrentBytes(bytesProcessed);
    }

    public static void deserialize(final Object p_object, final byte[] p_buffer) {
        deserialize(p_object, p_buffer, 0);
    }

    public static <T> T deserialize(final Class<T> p_class, final byte[] p_buffer) {
        return deserialize(p_class, p_buffer, 0);
    }

    public static <T> T deserialize(final Class<T> p_class, final byte[] p_buffer, final int p_offset) {
        if (p_class.isEnum()) {
            return deserializeEnum(p_class, p_buffer, p_offset);
        }

        try {
            Object object = UNSAFE.allocateInstance(p_class);
            deserialize(object, p_buffer, p_offset);
            return p_class.cast(object);
        } catch (InstantiationException e) {
            return null;
        }
    }

    private static <T> T deserializeEnum(final Class<T> p_class, final byte[] p_buffer) {
        return deserializeEnum(p_class, p_buffer, 0);
    }

    private static <T> T deserializeEnum(final Class<T> p_class, final byte[] p_buffer, final int p_offset) {
        Schema schema = getSchema(p_class);
        final int ordinal = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + p_offset);
        return p_class.cast(schema.getEnumConstant(ordinal));
    }

    private static Object deserializeEnum(final Schema.FieldSpec p_fieldSpec, final byte[] p_buffer, final int p_offset) {
        Schema schema = getSchema(p_fieldSpec.getType());
        final int ordinal = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + p_offset);
        return schema.getEnumConstant(ordinal);
    }

    public static int deserialize(final Object p_object, final byte[] p_buffer, final int p_offset) {
        Schema schema = getSchema(p_object.getClass());
        int position = p_offset;
        int arrayLength;
        int i, j;
        Object object;
        Object[] array;
        Schema.FieldSpec[] fields = schema.getFields();
        Schema.FieldSpec fieldSpec = null;
        for (i = 0; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case BYTE:
                    UNSAFE.putByte(p_object, fieldSpec.getOffset(), UNSAFE.getByte(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Byte.BYTES;
                    break;

                case CHAR:
                    UNSAFE.putChar(p_object, fieldSpec.getOffset(), UNSAFE.getChar(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Character.BYTES;
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

                case FLOAT:
                    UNSAFE.putFloat(p_object, fieldSpec.getOffset(), UNSAFE.getFloat(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Float.BYTES;
                    break;

                case DOUBLE:
                    UNSAFE.putDouble(p_object, fieldSpec.getOffset(), UNSAFE.getDouble(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Double.BYTES;
                    break;

                case BOOLEAN:
                    UNSAFE.putBoolean(p_object, fieldSpec.getOffset(), UNSAFE.getByte(p_buffer, BYTE_ARRAY_OFFSET + position) == TRUE);
                    position += Byte.BYTES;
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

                case CHAR_ARRAY:
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    char[] chars = new char[arrayLength];
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, chars, CHAR_ARRAY_OFFSET, arrayLength * Character.BYTES);
                    position += arrayLength * Character.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), chars);
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

                case FLOAT_ARRAY:
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    float[] floats = new float[arrayLength];
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, floats, FLOAT_ARRAY_OFFSET, arrayLength * Float.BYTES);
                    position += arrayLength * Float.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), floats);
                    break;

                case DOUBLE_ARRAY:
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    double[] doubles = new double[arrayLength];
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, doubles, DOUBLE_ARRAY_OFFSET, arrayLength * Double.BYTES);
                    position += arrayLength * Double.BYTES;
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), doubles);
                    break;

                case BOOLEAN_ARRAY:
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    boolean[] booleans = new boolean[arrayLength];
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, booleans, BOOLEAN_ARRAY_OFFSET, arrayLength * Byte.BYTES);
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
                    arrayLength = UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position);
                    position += Integer.BYTES;
                    array = FieldUtil.allocateArray(fieldSpec, arrayLength);
                    for (j = 0; j < arrayLength; j++) {
                        object = FieldUtil.allocateComponent(fieldSpec);
                        position += deserialize(object, p_buffer, position);
                        UNSAFE.putObject(array, OBJECT_ARRAY_OFFSET + j * REFERENCE_SIZE, object);
                    }
                    UNSAFE.putObject(p_object, fieldSpec.getOffset(), array);
                    break;
            }
        }

        return position - p_offset;
    }




    // -------------------------- WORK IN PROGRESS ---------------------------- //

    @Deprecated
    private static int serializeNormal(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        Object object = p_operation.getObject();
        Schema schema = getSchema(object.getClass());
        int position = p_offset;
        int bytesLeft = p_length;

        Schema.FieldSpec fieldSpec;
        Schema.FieldSpec[] fields = schema.getFields();

        int i = p_operation.popIndex();
        for (; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case LONG:
                    if (bytesLeft < Long.BYTES) {
                        p_operation.pushIndex(i);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putLong(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getLong(object, fieldSpec.getOffset()));
                    position += Long.BYTES;
                    bytesLeft -= Long.BYTES;
                    break;

                case DOUBLE:
                    if (bytesLeft < Double.BYTES) {
                        p_operation.pushIndex(i);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putDouble(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getDouble(object, fieldSpec.getOffset()));
                    position += Double.BYTES;
                    bytesLeft -= Double.BYTES;
                    break;

                default:
                    break;
            }
        }

        return position - p_offset;
    }

    private static int serializeInterrupted(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        Object target = p_operation.getTarget();
        Schema.FieldSpec fieldSpec = p_operation.getFieldSpec();
        int fieldProcessed = p_operation.getFieldProcessed();
        int fieldLeft = p_operation.getFieldLeft();
        int position = p_offset;
        int byteCap;

        // Remove interruption status if enough bytes can be read
        if (p_length >= fieldLeft) {
            byteCap = fieldProcessed + fieldLeft;
            p_operation.setStatus(Operation.Status.NONE);
        } else {
            byteCap = fieldProcessed + p_length;
        }

        switch (fieldSpec.getFieldType()) {

            case LONG:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++) {
                    UNSAFE.putByte(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(target, fieldSpec.getOffset() + fieldProcessed));
                }
                break;

            case DOUBLE:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++) {
                    UNSAFE.putByte(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(target, fieldSpec.getOffset() + fieldProcessed));
                }
                break;

            default:
                break;
        }

        return position - p_offset;
    }

    @Deprecated
    public static int serialize(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        // Try to serialize an interrupted field first
        int totalBytes = 0;
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += serializeInterrupted(p_operation, p_buffer, p_offset, p_length);
        }

        // Perform normal serialization if the interruption status has been cleared
        if (p_operation.getStatus() == Operation.Status.NONE) {
            totalBytes += serializeNormal(p_operation, p_buffer, p_offset, p_length - totalBytes);
        }

        // Write interrupted field if normal serialization did not work
        if (p_operation.getStatus() == Operation.Status.INTERRUPTED) {
            totalBytes += serializeInterrupted(p_operation, p_buffer, p_offset, p_length - totalBytes);
        }

        return totalBytes;
    }
}
