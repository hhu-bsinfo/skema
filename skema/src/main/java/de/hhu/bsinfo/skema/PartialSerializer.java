package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.Constants;
import de.hhu.bsinfo.skema.util.FieldUtil;
import de.hhu.bsinfo.skema.util.Operation;
import de.hhu.bsinfo.skema.util.SizeUtil;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

import static de.hhu.bsinfo.skema.util.OperationUtil.saveState;

final class PartialSerializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private PartialSerializer() {}

    static int serializeNormal(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
        Object tmpObject;
        Object[] array;
        Object object = p_operation.getRoot();
        Schema schema = SchemaRegistry.getSchema(object.getClass());
        int position = p_offset;
        int bytesLeft = p_length;
        int length;
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
                    UNSAFE.putByte(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(object, fieldSpec.getOffset()));
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case BOOLEAN:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putByte(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getBoolean(object, fieldSpec.getOffset()) ? Constants.TRUE : Constants.FALSE);
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case CHAR:
                    if (bytesLeft < Character.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Character.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putChar(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getChar(object, fieldSpec.getOffset()));
                    position += Character.BYTES;
                    bytesLeft -= Character.BYTES;
                    break;

                case SHORT:
                    if (bytesLeft < Short.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Short.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putShort(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getShort(object, fieldSpec.getOffset()));
                    position += Short.BYTES;
                    bytesLeft -= Short.BYTES;
                    break;

                case INT:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getInt(object, fieldSpec.getOffset()));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case LONG:
                    if (bytesLeft < Long.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Long.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putLong(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getLong(object, fieldSpec.getOffset()));
                    position += Long.BYTES;
                    bytesLeft -= Long.BYTES;
                    break;

                case FLOAT:
                    if (bytesLeft < Float.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Float.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putFloat(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getFloat(object, fieldSpec.getOffset()));
                    position += Float.BYTES;
                    bytesLeft -= Float.BYTES;
                    break;

                case DOUBLE:
                    if (bytesLeft < Double.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Double.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putDouble(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getDouble(object, fieldSpec.getOffset()));
                    position += Double.BYTES;
                    bytesLeft -= Double.BYTES;
                    break;

                case LENGTH:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    if (bytesLeft < Integer.BYTES) {
                        saveState(p_operation, fieldSpec, tmpObject, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getInt(tmpObject, SizeUtil.ARRAY_LENGTH_OFFSET));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case BYTE_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Byte.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.BYTE_ARRAY_OFFSET, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case CHAR_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Character.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.CHAR_ARRAY_OFFSET, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case SHORT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Short.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.SHORT_ARRAY_OFFSET, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case INT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Integer.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.INT_ARRAY_OFFSET, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case LONG_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Long.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.LONG_ARRAY_OFFSET, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case FLOAT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Float.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.FLOAT_ARRAY_OFFSET, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case DOUBLE_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Double.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.DOUBLE_ARRAY_OFFSET, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case BOOLEAN_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Byte.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.BOOLEAN_ARRAY_OFFSET, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case ENUM:
                case OBJECT:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    p_operation.setRoot(tmpObject);
                    size = serializeNormal(p_operation, p_buffer, position, bytesLeft);
                    position += size;
                    bytesLeft -= size;
                    if (p_operation.isInterrupted()) {
                        p_operation.pushIndex(i);
                        i = fields.length;
                    }
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getArray(object, fieldSpec);
                    for (j = p_operation.getObjectArrayIndex(); j < array.length; j++) {
                        p_operation.setRoot(array[j]);
                        size = serializeNormal(p_operation, p_buffer, position, bytesLeft);
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

                default:
                    break;
            }
        }

        p_operation.setRoot(object);

        return position - p_offset;
    }

    static int serializeNormal(final Operation p_operation, final long p_address, final int p_length) {
        Object tmpObject;
        Object[] array;
        Object object = p_operation.getRoot();
        Schema schema = SchemaRegistry.getSchema(object.getClass());
        long position = p_address;
        int bytesLeft = p_length;
        int length;
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
                    UNSAFE.putByte(position, UNSAFE.getByte(object, fieldSpec.getOffset()));
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case BOOLEAN:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putByte(position, UNSAFE.getBoolean(object, fieldSpec.getOffset()) ? Constants.TRUE : Constants.FALSE);
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case CHAR:
                    if (bytesLeft < Character.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Character.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putChar(position, UNSAFE.getChar(object, fieldSpec.getOffset()));
                    position += Character.BYTES;
                    bytesLeft -= Character.BYTES;
                    break;

                case SHORT:
                    if (bytesLeft < Short.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Short.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putShort(position, UNSAFE.getShort(object, fieldSpec.getOffset()));
                    position += Short.BYTES;
                    bytesLeft -= Short.BYTES;
                    break;

                case INT:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(position, UNSAFE.getInt(object, fieldSpec.getOffset()));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case LONG:
                    if (bytesLeft < Long.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Long.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putLong(position, UNSAFE.getLong(object, fieldSpec.getOffset()));
                    position += Long.BYTES;
                    bytesLeft -= Long.BYTES;
                    break;

                case FLOAT:
                    if (bytesLeft < Float.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Float.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putFloat(position, UNSAFE.getFloat(object, fieldSpec.getOffset()));
                    position += Float.BYTES;
                    bytesLeft -= Float.BYTES;
                    break;

                case DOUBLE:
                    if (bytesLeft < Double.BYTES) {
                        saveState(p_operation, fieldSpec, object, i, Double.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putDouble(position, UNSAFE.getDouble(object, fieldSpec.getOffset()));
                    position += Double.BYTES;
                    bytesLeft -= Double.BYTES;
                    break;

                case LENGTH:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    if (bytesLeft < Integer.BYTES) {
                        saveState(p_operation, fieldSpec, tmpObject, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(position, UNSAFE.getInt(tmpObject, SizeUtil.ARRAY_LENGTH_OFFSET));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case BYTE_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Byte.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.BYTE_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case CHAR_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Character.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.CHAR_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case SHORT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Short.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.SHORT_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case INT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Integer.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.INT_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case LONG_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Long.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.LONG_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case FLOAT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Float.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.FLOAT_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case DOUBLE_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Double.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.DOUBLE_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case BOOLEAN_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    length = SizeUtil.getArrayLength(tmpObject);
                    size = length * Byte.BYTES;
                    if (bytesLeft < size) {
                        saveState(p_operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.BOOLEAN_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case ENUM:
                case OBJECT:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    p_operation.setRoot(tmpObject);
                    size = serializeNormal(p_operation, position, bytesLeft);
                    position += size;
                    bytesLeft -= size;
                    if (p_operation.isInterrupted()) {
                        p_operation.pushIndex(i);
                        i = fields.length;
                    }
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getArray(object, fieldSpec);
                    for (j = p_operation.getObjectArrayIndex(); j < array.length; j++) {
                        p_operation.setRoot(array[j]);
                        size = serializeNormal(p_operation, position, bytesLeft);
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

                default:
                    break;
            }
        }

        p_operation.setRoot(object);

        return (int) (position - p_address);
    }

    static int serializeInterrupted(final Operation p_operation, final byte[] p_buffer, final int p_offset, final int p_length) {
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
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(target, fieldSpec.getOffset() + fieldProcessed));
                }
                break;

            case BOOLEAN:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getBoolean(target, fieldSpec.getOffset() + fieldProcessed) ? Constants.TRUE : Constants.FALSE);
                }
                break;

            case LENGTH:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(p_buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(target, SizeUtil.ARRAY_LENGTH_OFFSET + fieldProcessed));
                }
                break;


            case BYTE_ARRAY:
                UNSAFE.copyMemory(target, Constants.BYTE_ARRAY_OFFSET + fieldProcessed, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case CHAR_ARRAY:
                UNSAFE.copyMemory(target, Constants.CHAR_ARRAY_OFFSET + fieldProcessed, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case SHORT_ARRAY:
                UNSAFE.copyMemory(target, Constants.SHORT_ARRAY_OFFSET + fieldProcessed, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case INT_ARRAY:
                UNSAFE.copyMemory(target, Constants.INT_ARRAY_OFFSET + fieldProcessed, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case LONG_ARRAY:
                UNSAFE.copyMemory(target, Constants.LONG_ARRAY_OFFSET + fieldProcessed, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case FLOAT_ARRAY:
                UNSAFE.copyMemory(target, Constants.FLOAT_ARRAY_OFFSET + fieldProcessed, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case DOUBLE_ARRAY:
                UNSAFE.copyMemory(target, Constants.DOUBLE_ARRAY_OFFSET + fieldProcessed, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case BOOLEAN_ARRAY:
                UNSAFE.copyMemory(target, Constants.BOOLEAN_ARRAY_OFFSET + fieldProcessed, p_buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
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

    static int serializeInterrupted(final Operation p_operation, final long p_address, final int p_length) {
        if (p_length == 0) {
            return 0;
        }

        Object target = p_operation.getTarget();
        Schema.FieldSpec fieldSpec = p_operation.getFieldSpec();
        int fieldProcessed = p_operation.getFieldProcessed();
        int fieldLeft = p_operation.getFieldLeft();
        long position = p_address;
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
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(position, UNSAFE.getByte(target, fieldSpec.getOffset() + fieldProcessed));
                }
                break;

            case BOOLEAN:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(position, UNSAFE.getBoolean(target, fieldSpec.getOffset() + fieldProcessed) ? Constants.TRUE : Constants.FALSE);
                }
                break;

            case LENGTH:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(position, UNSAFE.getByte(target, SizeUtil.ARRAY_LENGTH_OFFSET + fieldProcessed));
                }
                break;


            case BYTE_ARRAY:
                UNSAFE.copyMemory(target, Constants.BYTE_ARRAY_OFFSET + fieldProcessed, null, position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case CHAR_ARRAY:
                UNSAFE.copyMemory(target, Constants.CHAR_ARRAY_OFFSET + fieldProcessed, null, position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case SHORT_ARRAY:
                UNSAFE.copyMemory(target, Constants.SHORT_ARRAY_OFFSET + fieldProcessed, null, position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case INT_ARRAY:
                UNSAFE.copyMemory(target, Constants.INT_ARRAY_OFFSET + fieldProcessed, null, position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case LONG_ARRAY:
                UNSAFE.copyMemory(target, Constants.LONG_ARRAY_OFFSET + fieldProcessed, null, position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case FLOAT_ARRAY:
                UNSAFE.copyMemory(target, Constants.FLOAT_ARRAY_OFFSET + fieldProcessed, null, position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case DOUBLE_ARRAY:
                UNSAFE.copyMemory(target, Constants.DOUBLE_ARRAY_OFFSET + fieldProcessed, null, position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case BOOLEAN_ARRAY:
                UNSAFE.copyMemory(target, Constants.BOOLEAN_ARRAY_OFFSET + fieldProcessed, null, position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            default:
                break;
        }

        p_operation.setFieldProcessed(fieldProcessed);
        p_operation.setFieldLeft(fieldLeft);

        if (fieldLeft == 0) {
            p_operation.setStatus(Operation.Status.NONE);
        }

        return (int) (position - p_address);
    }
}
