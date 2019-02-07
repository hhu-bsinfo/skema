package de.hhu.bsinfo.autochunk.demo;

import de.hhu.bsinfo.autochunk.demo.schema.Schema;
import de.hhu.bsinfo.autochunk.demo.util.FieldUtil;
import de.hhu.bsinfo.autochunk.demo.util.Operation;
import de.hhu.bsinfo.autochunk.demo.util.UnsafeProvider;

import static de.hhu.bsinfo.autochunk.demo.schema.SchemaRegistry.getSchema;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.BOOLEAN_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.BYTE_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.CHAR_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.DOUBLE_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.FLOAT_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.INT_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.LONG_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.SHORT_ARRAY_OFFSET;
import static de.hhu.bsinfo.autochunk.demo.util.Constants.TRUE;
import static de.hhu.bsinfo.autochunk.demo.util.StateUtil.saveState;

public final class PartialDeserializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private PartialDeserializer() {}

    static int deserializeNormal(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
        Object tmpObject;
        Object[] array;
        Object object = p_operation.getRoot();
        Schema schema = getSchema(object.getClass());
        int position = p_offset;
        int bytesLeft = p_length;
        int size;
        int j;

        Schema.FieldSpec fieldSpec;
        Schema.FieldSpec[] fields = schema.getFields();

        int i = p_operation.popIndex();
        for (; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case BYTE:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putByte(object, fieldSpec.getOffset(), UNSAFE.getByte(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case BOOLEAN:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putBoolean(object, fieldSpec.getOffset(), UNSAFE.getByte(p_buffer, BYTE_ARRAY_OFFSET + position) == TRUE);
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case CHAR:
                    if (bytesLeft < Character.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Character.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putChar(object, fieldSpec.getOffset(), UNSAFE.getChar(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Character.BYTES;
                    bytesLeft -= Character.BYTES;
                    break;

                case SHORT:
                    if (bytesLeft < Short.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Short.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putShort(object, fieldSpec.getOffset(), UNSAFE.getShort(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Short.BYTES;
                    bytesLeft -= Short.BYTES;
                    break;

                case INT:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(object, fieldSpec.getOffset(), UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case LONG:
                    if (bytesLeft < Long.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Long.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putLong(object, fieldSpec.getOffset(), UNSAFE.getLong(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Long.BYTES;
                    bytesLeft -= Long.BYTES;
                    break;

                case FLOAT:
                    if (bytesLeft < Float.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Float.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putFloat(object, fieldSpec.getOffset(), UNSAFE.getFloat(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Float.BYTES;
                    bytesLeft -= Float.BYTES;
                    break;

                case DOUBLE:
                    if (bytesLeft < Double.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Double.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putDouble(object, fieldSpec.getOffset(), UNSAFE.getDouble(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Double.BYTES;
                    bytesLeft -= Double.BYTES;
                    break;

                case LENGTH:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(p_operation, Operation.ARRAY_LENGTH_FIELD, p_operation, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(p_operation, Operation.ARRAY_LENGTH_FIELD.getOffset(), UNSAFE.getInt(p_buffer, BYTE_ARRAY_OFFSET + position));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case BYTE_ARRAY:
                    byte[] bytes = new byte[p_operation.getArrayLength()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), bytes);
                    size = bytes.length * Byte.BYTES;
                    if (bytesLeft < size) {
                        p_operation.setParent(object);
                        saveState(p_operation, fieldSpec, bytes, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, bytes, BYTE_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case CHAR_ARRAY:
                    char[] chars = new char[p_operation.getArrayLength()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), chars);
                    size = chars.length * Character.BYTES;
                    if (bytesLeft < size) {
                        p_operation.setParent(object);
                        saveState(p_operation, fieldSpec, chars, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, chars, CHAR_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case SHORT_ARRAY:
                    short[] shorts = new short[p_operation.getArrayLength()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), shorts);
                    size = shorts.length * Short.BYTES;
                    if (bytesLeft < size) {
                        p_operation.setParent(object);
                        saveState(p_operation, fieldSpec, shorts, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, shorts, SHORT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case INT_ARRAY:
                    int[] ints = new int[p_operation.getArrayLength()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), ints);
                    size = ints.length * Integer.BYTES;
                    if (bytesLeft < size) {
                        p_operation.setParent(object);
                        saveState(p_operation, fieldSpec, ints, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, ints, INT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case LONG_ARRAY:
                    long[] longs = new long[p_operation.getArrayLength()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), longs);
                    size = longs.length * Long.BYTES;
                    if (bytesLeft < size) {
                        p_operation.setParent(object);
                        saveState(p_operation, fieldSpec, longs, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, longs, LONG_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case FLOAT_ARRAY:
                    float[] floats = new float[p_operation.getArrayLength()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), floats);
                    size = floats.length * Float.BYTES;
                    if (bytesLeft < size) {
                        p_operation.setParent(object);
                        saveState(p_operation, fieldSpec, floats, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, floats, FLOAT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case DOUBLE_ARRAY:
                    double[] doubles = new double[p_operation.getArrayLength()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), doubles);
                    size = doubles.length * Double.BYTES;
                    if (bytesLeft < size) {
                        p_operation.setParent(object);
                        saveState(p_operation, fieldSpec, doubles, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, doubles, DOUBLE_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case BOOLEAN_ARRAY:
                    boolean[] booleans = new boolean[p_operation.getArrayLength()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), booleans);
                    size = booleans.length * Byte.BYTES;
                    if (bytesLeft < size) {
                        p_operation.setParent(object);
                        saveState(p_operation, fieldSpec, booleans, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, booleans, BOOLEAN_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case OBJECT:
                    tmpObject = FieldUtil.getOrAllocateObject(object, fieldSpec);
                    p_operation.setRoot(tmpObject);
                    size = deserializeNormal(p_operation, p_buffer, position, bytesLeft);
                    position += size;
                    bytesLeft -= size;
                    if (p_operation.isInterrupted()) {
                        p_operation.pushIndex(i);
                        i = fields.length;
                    }
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getOrAllocateArray(object, fieldSpec, p_operation.getArrayLength());
                    for (j = p_operation.getObjectArrayIndex(); j < array.length; j++) {
                        p_operation.setRoot(FieldUtil.getOrAllocateComponent(array, fieldSpec, j));
                        size = deserializeNormal(p_operation, p_buffer, position, bytesLeft);
                        position += size;
                        bytesLeft -= size;
                        if (p_operation.isInterrupted()) {
                            p_operation.setObjectArrayIndex(j);
                            p_operation.pushIndex(i);
                            i = fields.length;
                            j = array.length;
                        }
                    }

                    if (!p_operation.isInterrupted()) {
                        p_operation.setObjectArrayIndex(0);
                    }

                    break;
            }
        }

        p_operation.setRoot(object);

        return position - p_offset;
    }

    static int deserializeInterrupted(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        Object target = p_operation.getTarget();
        Schema.FieldSpec fieldSpec = p_operation.getFieldSpec();
        int fieldProcessed = p_operation.getFieldProcessed();
        int fieldLeft = p_operation.getFieldLeft();
        int position = p_offset;
        int byteCap;


        if (p_length >= fieldLeft) {
            byteCap = fieldProcessed + fieldLeft;
        } else {
            byteCap = fieldProcessed + p_length;
        }

        switch (fieldSpec.getFieldType()) {

            case BYTE:
            case CHAR:
            case SHORT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case LENGTH:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(target, fieldSpec.getOffset() + fieldProcessed, UNSAFE.getByte(p_buffer, BYTE_ARRAY_OFFSET + position));
                }
                break;

            case BOOLEAN:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putBoolean(target, fieldSpec.getOffset() + fieldProcessed, UNSAFE.getByte(p_buffer, BYTE_ARRAY_OFFSET + position) == TRUE);
                }
                break;

            case BYTE_ARRAY:
                UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, target, BYTE_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(p_operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case CHAR_ARRAY:
                UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, target, CHAR_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(p_operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case SHORT_ARRAY:
                UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, target, SHORT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(p_operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case INT_ARRAY:
                UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, target, INT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(p_operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case LONG_ARRAY:
                UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, target, LONG_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(p_operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case FLOAT_ARRAY:
                UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, target, FLOAT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(p_operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case DOUBLE_ARRAY:
                UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, target, DOUBLE_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(p_operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case BOOLEAN_ARRAY:
                UNSAFE.copyMemory(p_buffer, BYTE_ARRAY_OFFSET + position, target, BOOLEAN_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(p_operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            default:
                break;
        }

        p_operation.setFieldProcessed(fieldProcessed);
        p_operation.setFieldLeft(fieldLeft);

        if (fieldLeft == 0) {
            p_operation.setStatus(Operation.Status.NONE);
        }

        return position - p_offset;
    }
}
