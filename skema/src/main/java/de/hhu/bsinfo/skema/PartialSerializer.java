package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.Constants;
import de.hhu.bsinfo.skema.util.FieldUtil;
import de.hhu.bsinfo.skema.util.Operation;
import de.hhu.bsinfo.skema.util.SizeUtil;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

import java.lang.foreign.MemorySegment;

import static de.hhu.bsinfo.skema.util.OperationUtil.saveState;

@SuppressWarnings("sunapi")
final class PartialSerializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private PartialSerializer() {}

    static int serializeNormal(final Operation operation, final byte[] buffer, final int offset, final int length) {
        Object tmpObject;
        Object[] array;
        Object object = operation.getRoot();
        Schema schema = SchemaRegistry.getSchema(object.getClass());
        int position = offset;
        int bytesLeft = length;
        int arrayLength;
        int size;
        int j;

        Schema.FieldSpec fieldSpec;
        Schema.FieldSpec[] fields = schema.getFields();

        int i = operation.popIndex();
        for (; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case BYTE:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putByte(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(object, fieldSpec.getOffset()));
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case BOOLEAN:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putByte(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getBoolean(object, fieldSpec.getOffset()) ? Constants.TRUE : Constants.FALSE);
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case CHAR:
                    if (bytesLeft < Character.BYTES) {
                        saveState(operation, fieldSpec, object, i, Character.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putChar(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getChar(object, fieldSpec.getOffset()));
                    position += Character.BYTES;
                    bytesLeft -= Character.BYTES;
                    break;

                case SHORT:
                    if (bytesLeft < Short.BYTES) {
                        saveState(operation, fieldSpec, object, i, Short.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putShort(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getShort(object, fieldSpec.getOffset()));
                    position += Short.BYTES;
                    bytesLeft -= Short.BYTES;
                    break;

                case INT:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getInt(object, fieldSpec.getOffset()));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case LONG:
                    if (bytesLeft < Long.BYTES) {
                        saveState(operation, fieldSpec, object, i, Long.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putLong(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getLong(object, fieldSpec.getOffset()));
                    position += Long.BYTES;
                    bytesLeft -= Long.BYTES;
                    break;

                case FLOAT:
                    if (bytesLeft < Float.BYTES) {
                        saveState(operation, fieldSpec, object, i, Float.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putFloat(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getFloat(object, fieldSpec.getOffset()));
                    position += Float.BYTES;
                    bytesLeft -= Float.BYTES;
                    break;

                case DOUBLE:
                    if (bytesLeft < Double.BYTES) {
                        saveState(operation, fieldSpec, object, i, Double.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putDouble(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getDouble(object, fieldSpec.getOffset()));
                    position += Double.BYTES;
                    bytesLeft -= Double.BYTES;
                    break;

                case LENGTH:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, fieldSpec, tmpObject, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getInt(tmpObject, SizeUtil.ARRAY_LENGTH_OFFSET));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case BYTE_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Byte.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.BYTE_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case CHAR_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Character.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.CHAR_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case SHORT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Short.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.SHORT_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case INT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Integer.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.INT_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case LONG_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Long.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.LONG_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case FLOAT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Float.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.FLOAT_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case DOUBLE_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Double.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.DOUBLE_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case BOOLEAN_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Byte.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.BOOLEAN_ARRAY_OFFSET, buffer, Constants.BYTE_ARRAY_OFFSET + position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case ENUM:
                case OBJECT:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    operation.setRoot(tmpObject);
                    size = serializeNormal(operation, buffer, position, bytesLeft);
                    position += size;
                    bytesLeft -= size;
                    if (operation.isInterrupted()) {
                        operation.pushIndex(i);
                        i = fields.length;
                    }
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getArray(object, fieldSpec);
                    for (j = operation.getObjectArrayIndex(); j < array.length; j++) {
                        operation.setRoot(array[j]);
                        size = serializeNormal(operation, buffer, position, bytesLeft);
                        position += size;
                        bytesLeft -= size;
                        if (operation.isInterrupted()) {
                            operation.setObjectArrayIndex(j);
                            operation.pushIndex(i);
                            i = fields.length;
                            j = array.length;
                        }
                    }

                    if (!operation.isInterrupted()) {
                        operation.setObjectArrayIndex(0);
                    }

                    break;

                default:
                    break;
            }
        }

        operation.setRoot(object);

        return position - offset;
    }

    static int serializeNormal(final Operation operation, final long address, final int length) {
        Object tmpObject;
        Object[] array;
        Object object = operation.getRoot();
        Schema schema = SchemaRegistry.getSchema(object.getClass());
        long position = address;
        int bytesLeft = length;
        int arrayLength;
        int size;
        int j;

        Schema.FieldSpec fieldSpec;
        Schema.FieldSpec[] fields = schema.getFields();

        int i = operation.popIndex();
        for (; i < fields.length; i++) {
            fieldSpec = fields[i];
            switch (fieldSpec.getFieldType()) {

                case BYTE:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putByte(position, UNSAFE.getByte(object, fieldSpec.getOffset()));
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case BOOLEAN:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putByte(position, UNSAFE.getBoolean(object, fieldSpec.getOffset()) ? Constants.TRUE : Constants.FALSE);
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case CHAR:
                    if (bytesLeft < Character.BYTES) {
                        saveState(operation, fieldSpec, object, i, Character.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putChar(position, UNSAFE.getChar(object, fieldSpec.getOffset()));
                    position += Character.BYTES;
                    bytesLeft -= Character.BYTES;
                    break;

                case SHORT:
                    if (bytesLeft < Short.BYTES) {
                        saveState(operation, fieldSpec, object, i, Short.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putShort(position, UNSAFE.getShort(object, fieldSpec.getOffset()));
                    position += Short.BYTES;
                    bytesLeft -= Short.BYTES;
                    break;

                case INT:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(position, UNSAFE.getInt(object, fieldSpec.getOffset()));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case LONG:
                    if (bytesLeft < Long.BYTES) {
                        saveState(operation, fieldSpec, object, i, Long.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putLong(position, UNSAFE.getLong(object, fieldSpec.getOffset()));
                    position += Long.BYTES;
                    bytesLeft -= Long.BYTES;
                    break;

                case FLOAT:
                    if (bytesLeft < Float.BYTES) {
                        saveState(operation, fieldSpec, object, i, Float.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putFloat(position, UNSAFE.getFloat(object, fieldSpec.getOffset()));
                    position += Float.BYTES;
                    bytesLeft -= Float.BYTES;
                    break;

                case DOUBLE:
                    if (bytesLeft < Double.BYTES) {
                        saveState(operation, fieldSpec, object, i, Double.BYTES);
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
                        saveState(operation, fieldSpec, tmpObject, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(position, UNSAFE.getInt(tmpObject, SizeUtil.ARRAY_LENGTH_OFFSET));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case BYTE_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Byte.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.BYTE_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case CHAR_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Character.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.CHAR_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case SHORT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Short.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.SHORT_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case INT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Integer.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.INT_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case LONG_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Long.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.LONG_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case FLOAT_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Float.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.FLOAT_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case DOUBLE_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Double.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(tmpObject, Constants.DOUBLE_ARRAY_OFFSET, null, position, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case BOOLEAN_ARRAY:
                    tmpObject = FieldUtil.getObject(object, fieldSpec);
                    arrayLength = SizeUtil.getArrayLength(tmpObject);
                    size = arrayLength * Byte.BYTES;
                    if (bytesLeft < size) {
                        saveState(operation, fieldSpec, tmpObject, i, size);
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
                    operation.setRoot(tmpObject);
                    size = serializeNormal(operation, position, bytesLeft);
                    position += size;
                    bytesLeft -= size;
                    if (operation.isInterrupted()) {
                        operation.pushIndex(i);
                        i = fields.length;
                    }
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getArray(object, fieldSpec);
                    for (j = operation.getObjectArrayIndex(); j < array.length; j++) {
                        operation.setRoot(array[j]);
                        size = serializeNormal(operation, position, bytesLeft);
                        position += size;
                        bytesLeft -= size;
                        if (operation.isInterrupted()) {
                            operation.setObjectArrayIndex(j);
                            operation.pushIndex(i);
                            i = fields.length;
                            j = array.length;
                        }
                    }

                    if (!operation.isInterrupted()) {
                        operation.setObjectArrayIndex(0);
                    }

                    break;

                default:
                    break;
            }
        }

        operation.setRoot(object);

        return (int) (position - address);
    }

    static int serializeNormal(final Operation operation, final MemorySegment segment) {
        return serializeNormal(operation, segment.address(), (int) segment.byteSize());
    }

    static int serializeInterrupted(final Operation operation, final byte[] buffer, final int offset, final int length) {
        if (length == 0) {
            return 0;
        }

        Object target = operation.getTarget();
        Schema.FieldSpec fieldSpec = operation.getFieldSpec();
        int fieldProcessed = operation.getFieldProcessed();
        int fieldLeft = operation.getFieldLeft();
        int position = offset;
        int byteCap;


        if (length >= fieldLeft) {
            byteCap = fieldProcessed + fieldLeft;
        } else {
            byteCap = fieldProcessed + length;
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
                    UNSAFE.putByte(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(target, fieldSpec.getOffset() + fieldProcessed));
                }
                break;

            case BOOLEAN:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getBoolean(target, fieldSpec.getOffset() + fieldProcessed) ? Constants.TRUE : Constants.FALSE);
                }
                break;

            case LENGTH:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(buffer, Constants.BYTE_ARRAY_OFFSET + position, UNSAFE.getByte(target, SizeUtil.ARRAY_LENGTH_OFFSET + fieldProcessed));
                }
                break;


            case BYTE_ARRAY:
                UNSAFE.copyMemory(target, Constants.BYTE_ARRAY_OFFSET + fieldProcessed, buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case CHAR_ARRAY:
                UNSAFE.copyMemory(target, Constants.CHAR_ARRAY_OFFSET + fieldProcessed, buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case SHORT_ARRAY:
                UNSAFE.copyMemory(target, Constants.SHORT_ARRAY_OFFSET + fieldProcessed, buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case INT_ARRAY:
                UNSAFE.copyMemory(target, Constants.INT_ARRAY_OFFSET + fieldProcessed, buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case LONG_ARRAY:
                UNSAFE.copyMemory(target, Constants.LONG_ARRAY_OFFSET + fieldProcessed, buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case FLOAT_ARRAY:
                UNSAFE.copyMemory(target, Constants.FLOAT_ARRAY_OFFSET + fieldProcessed, buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case DOUBLE_ARRAY:
                UNSAFE.copyMemory(target, Constants.DOUBLE_ARRAY_OFFSET + fieldProcessed, buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            case BOOLEAN_ARRAY:
                UNSAFE.copyMemory(target, Constants.BOOLEAN_ARRAY_OFFSET + fieldProcessed, buffer, Constants.BYTE_ARRAY_OFFSET + position, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                break;

            default:
                break;
        }

        operation.setFieldProcessed(fieldProcessed);
        operation.setFieldLeft(fieldLeft);

        if (fieldLeft == 0) {
            operation.setStatus(Operation.Status.NONE);
        }

        return position - offset;
    }

    static int serializeInterrupted(final Operation operation, final long address, final int length) {
        if (length == 0) {
            return 0;
        }

        Object target = operation.getTarget();
        Schema.FieldSpec fieldSpec = operation.getFieldSpec();
        int fieldProcessed = operation.getFieldProcessed();
        int fieldLeft = operation.getFieldLeft();
        long position = address;
        int byteCap;


        if (length >= fieldLeft) {
            byteCap = fieldProcessed + fieldLeft;
        } else {
            byteCap = fieldProcessed + length;
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

        operation.setFieldProcessed(fieldProcessed);
        operation.setFieldLeft(fieldLeft);

        if (fieldLeft == 0) {
            operation.setStatus(Operation.Status.NONE);
        }

        return (int) (position - address);
    }

    static int serializeInterrupted(final Operation operation, final MemorySegment segment) {
        return serializeInterrupted(operation, segment.address(), (int) segment.byteSize());
    }
}
