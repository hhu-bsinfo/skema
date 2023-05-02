package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.Constants;
import de.hhu.bsinfo.skema.util.FieldUtil;
import de.hhu.bsinfo.skema.util.SizeUtil;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

@SuppressWarnings("sunapi")
final class FullSerializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private FullSerializer() {}

    static int serialize(final Object instance, final byte[] buffer, final int offset) {
        Schema schema = SchemaRegistry.getSchema(instance.getClass());
        int position = offset;
        int arraySize = 0;
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
                    UNSAFE.putByte(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(instance, fieldSpec.getOffset()));
                    position += Byte.BYTES;
                    break;

                case CHAR:
                    UNSAFE.putChar(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getChar(instance, fieldSpec.getOffset()));
                    position += Character.BYTES;
                    break;

                case SHORT:
                    UNSAFE.putShort(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getShort(instance, fieldSpec.getOffset()));
                    position += Short.BYTES;
                    break;

                case INT:
                    UNSAFE.putInt(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getInt(instance, fieldSpec.getOffset()));
                    position += Integer.BYTES;
                    break;

                case LONG:
                    UNSAFE.putLong(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getLong(instance, fieldSpec.getOffset()));
                    position += Long.BYTES;
                    break;

                case FLOAT:
                    UNSAFE.putFloat(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getFloat(instance, fieldSpec.getOffset()));
                    position += Float.BYTES;
                    break;

                case DOUBLE:
                    UNSAFE.putDouble(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getDouble(instance, fieldSpec.getOffset()));
                    position += Double.BYTES;
                    break;

                case BOOLEAN:
                    UNSAFE.putByte(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getBoolean(instance, fieldSpec.getOffset()) ? Constants.TRUE : Constants.FALSE);
                    position += Byte.BYTES;
                    break;

                case LENGTH:
                    object = FieldUtil.getObject(instance, fieldSpec);
                    UNSAFE.putInt(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getInt(object, SizeUtil.ARRAY_LENGTH_OFFSET));
                    position += Integer.BYTES;
                    arrayLength = SizeUtil.getArrayLength(object);
                    break;

                //  Arrays are handled specially since their size is not constant.
                //
                //      1. Get a reference on the array
                //      2. Read the array's length field
                //      3. Write the array's length field to the buffer
                //      4. Write the array's content to the buffer

                case BYTE_ARRAY:
                    arraySize = arrayLength * Byte.BYTES;
                    UNSAFE.copyMemory(object, Constants.BYTE_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case CHAR_ARRAY:
                    arraySize = arrayLength * Character.BYTES;
                    UNSAFE.copyMemory(object, Constants.CHAR_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case SHORT_ARRAY:
                    arraySize = arrayLength * Short.BYTES;
                    UNSAFE.copyMemory(object, Constants.SHORT_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case INT_ARRAY:
                    arraySize = arrayLength * Integer.BYTES;
                    UNSAFE.copyMemory(object, Constants.INT_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case LONG_ARRAY:
                    arraySize = arrayLength * Long.BYTES;
                    UNSAFE.copyMemory(object, Constants.LONG_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case FLOAT_ARRAY:
                    arraySize = arrayLength * Float.BYTES;
                    UNSAFE.copyMemory(object, Constants.FLOAT_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case DOUBLE_ARRAY:
                    arraySize = arrayLength * Double.BYTES;
                    UNSAFE.copyMemory(object, Constants.DOUBLE_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case BOOLEAN_ARRAY:
                    arraySize = arrayLength * Byte.BYTES;
                    UNSAFE.copyMemory(object, Constants.BOOLEAN_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                //  Primitive types are handled above. All other types must be objects.
                //  In this case we retrieve the instance from our object and serialize it.
                //  (This will fail if the reference is null).

                case ENUM:
                case OBJECT:
                    object = FieldUtil.getObject(instance, fieldSpec);
                    position += serialize(object, buffer, position);
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getArray(instance, fieldSpec);
                    for (j = 0; j < array.length; j++) {
                        position += serialize(array[j], buffer, position);
                    }
                    break;
            }
        }

        return position - offset;
    }

    static int serialize(final Object instance, final long address) {
        Schema schema = SchemaRegistry.getSchema(instance.getClass());
        long position = address;
        int arraySize = 0;
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
                    UNSAFE.putByte(position, UNSAFE.getByte(instance, fieldSpec.getOffset()));
                    position += Byte.BYTES;
                    break;

                case CHAR:
                    UNSAFE.putChar(position, UNSAFE.getChar(instance, fieldSpec.getOffset()));
                    position += Character.BYTES;
                    break;

                case SHORT:
                    UNSAFE.putShort(position, UNSAFE.getShort(instance, fieldSpec.getOffset()));
                    position += Short.BYTES;
                    break;

                case INT:
                    UNSAFE.putInt(position, UNSAFE.getInt(instance, fieldSpec.getOffset()));
                    position += Integer.BYTES;
                    break;

                case LONG:
                    UNSAFE.putLong(position, UNSAFE.getLong(instance, fieldSpec.getOffset()));
                    position += Long.BYTES;
                    break;

                case FLOAT:
                    UNSAFE.putFloat(position, UNSAFE.getFloat(instance, fieldSpec.getOffset()));
                    position += Float.BYTES;
                    break;

                case DOUBLE:
                    UNSAFE.putDouble(position, UNSAFE.getDouble(instance, fieldSpec.getOffset()));
                    position += Double.BYTES;
                    break;

                case BOOLEAN:
                    UNSAFE.putByte(position, UNSAFE.getBoolean(instance, fieldSpec.getOffset()) ? Constants.TRUE : Constants.FALSE);
                    position += Byte.BYTES;
                    break;

                case LENGTH:
                    object = FieldUtil.getObject(instance, fieldSpec);
                    UNSAFE.putInt(position, UNSAFE.getInt(object, SizeUtil.ARRAY_LENGTH_OFFSET));
                    position += Integer.BYTES;
                    arrayLength = SizeUtil.getArrayLength(object);
                    break;

                //  Arrays are handled specially since their size is not constant.
                //
                //      1. Get a reference on the array
                //      2. Read the array's length field
                //      3. Write the array's length field to the buffer
                //      4. Write the array's content to the buffer

                case BYTE_ARRAY:
                    arraySize = arrayLength * Byte.BYTES;
                    UNSAFE.copyMemory(object, Constants.BYTE_ARRAY_OFFSET, null, position, arraySize);
                    position += arraySize;
                    break;

                case CHAR_ARRAY:
                    arraySize = arrayLength * Character.BYTES;
                    UNSAFE.copyMemory(object, Constants.CHAR_ARRAY_OFFSET, null, position, arraySize);
                    position += arraySize;
                    break;

                case SHORT_ARRAY:
                    arraySize = arrayLength * Short.BYTES;
                    UNSAFE.copyMemory(object, Constants.SHORT_ARRAY_OFFSET, null, position, arraySize);
                    position += arraySize;
                    break;

                case INT_ARRAY:
                    arraySize = arrayLength * Integer.BYTES;
                    UNSAFE.copyMemory(object, Constants.INT_ARRAY_OFFSET, null, position, arraySize);
                    position += arraySize;
                    break;

                case LONG_ARRAY:
                    arraySize = arrayLength * Long.BYTES;
                    UNSAFE.copyMemory(object, Constants.LONG_ARRAY_OFFSET, null, position, arraySize);
                    position += arraySize;
                    break;

                case FLOAT_ARRAY:
                    arraySize = arrayLength * Float.BYTES;
                    UNSAFE.copyMemory(object, Constants.FLOAT_ARRAY_OFFSET, null, position, arraySize);
                    position += arraySize;
                    break;

                case DOUBLE_ARRAY:
                    arraySize = arrayLength * Double.BYTES;
                    UNSAFE.copyMemory(object, Constants.DOUBLE_ARRAY_OFFSET, null, position, arraySize);
                    position += arraySize;
                    break;

                case BOOLEAN_ARRAY:
                    arraySize = arrayLength * Byte.BYTES;
                    UNSAFE.copyMemory(object, Constants.BOOLEAN_ARRAY_OFFSET, null, position, arraySize);
                    position += arraySize;
                    break;

                //  Primitive types are handled above. All other types must be objects.
                //  In this case we retrieve the instance from our object and serialize it.
                //  (This will fail if the reference is null).

                case ENUM:
                case OBJECT:
                    object = FieldUtil.getObject(instance, fieldSpec);
                    position += serialize(object, position);
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getArray(instance, fieldSpec);
                    for (j = 0; j < array.length; j++) {
                        position += serialize(array[j], position);
                    }
                    break;
            }
        }

        return (int) (position - address);
    }


}
