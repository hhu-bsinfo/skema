package de.hhu.bsinfo.autochunk.demo;

import de.hhu.bsinfo.autochunk.demo.schema.Schema;
import de.hhu.bsinfo.autochunk.demo.util.FieldUtil;
import de.hhu.bsinfo.autochunk.demo.util.SizeUtil;
import de.hhu.bsinfo.autochunk.demo.util.UnsafeProvider;

import static de.hhu.bsinfo.autochunk.demo.schema.SchemaRegistry.getSchema;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.BOOLEAN_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.BYTE_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.CHAR_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.DOUBLE_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.FALSE;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.FLOAT_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.INT_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.LONG_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.SHORT_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.TRUE;

public final class FullSerializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private FullSerializer() {}

    static int serialize(final Object p_object, final byte[] p_buffer, final int p_offset) {
        Schema schema = getSchema(p_object.getClass());
        int position = p_offset;
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

                case LENGTH:
                    object = FieldUtil.getObject(p_object, fieldSpec);
                    UNSAFE.putInt(p_buffer, BYTE_ARRAY_OFFSET + position, UNSAFE.getInt(object, SizeUtil.ARRAY_LENGTH_OFFSET));
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
                    UNSAFE.copyMemory(object, BYTE_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case CHAR_ARRAY:
                    arraySize = arrayLength * Character.BYTES;
                    UNSAFE.copyMemory(object, CHAR_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case SHORT_ARRAY:
                    arraySize = arrayLength * Short.BYTES;
                    UNSAFE.copyMemory(object, SHORT_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case INT_ARRAY:
                    arraySize = arrayLength * Integer.BYTES;
                    UNSAFE.copyMemory(object, INT_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case LONG_ARRAY:
                    arraySize = arrayLength * Long.BYTES;
                    UNSAFE.copyMemory(object, LONG_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case FLOAT_ARRAY:
                    arraySize = arrayLength * Float.BYTES;
                    UNSAFE.copyMemory(object, FLOAT_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case DOUBLE_ARRAY:
                    arraySize = arrayLength * Double.BYTES;
                    UNSAFE.copyMemory(object, DOUBLE_ARRAY_OFFSET, p_buffer, BYTE_ARRAY_OFFSET + position, arraySize);
                    position += arraySize;
                    break;

                case BOOLEAN_ARRAY:
                    arraySize = arrayLength * Byte.BYTES;
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
                    for (j = 0; j < array.length; j++) {
                        position += serialize(array[j], p_buffer, position);
                    }
                    break;
            }
        }

        return position - p_offset;
    }


}
