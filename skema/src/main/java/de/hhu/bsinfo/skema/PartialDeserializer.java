package de.hhu.bsinfo.skema;

import de.hhu.bsinfo.skema.schema.Schema;
import de.hhu.bsinfo.skema.schema.SchemaRegistry;
import de.hhu.bsinfo.skema.util.Constants;
import de.hhu.bsinfo.skema.util.FieldUtil;
import de.hhu.bsinfo.skema.util.Operation;
import de.hhu.bsinfo.skema.util.UnsafeProvider;

import static de.hhu.bsinfo.skema.util.OperationUtil.saveState;

@SuppressWarnings("sunapi")
final class PartialDeserializer {

    private static final sun.misc.Unsafe UNSAFE = UnsafeProvider.getUnsafe();

    private PartialDeserializer() {}

    static int deserializeNormal(final Operation operation, final byte[] buffer, final int offset, final int length) {
        Object tmpObject;
        Object[] array;
        Object object = operation.getRoot();
        Schema schema = SchemaRegistry.getSchema(object.getClass());
        int position = offset;
        int bytesLeft = length;
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
                    UNSAFE.putByte(object, fieldSpec.getOffset(), UNSAFE.getByte(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case BOOLEAN:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putBoolean(object, fieldSpec.getOffset(), UNSAFE.getByte(buffer, Constants.BYTE_ARRAY_OFFSET + position) == Constants.TRUE);
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case CHAR:
                    if (bytesLeft < Character.BYTES) {
                        saveState(operation, fieldSpec, object, i, Character.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putChar(object, fieldSpec.getOffset(), UNSAFE.getChar(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Character.BYTES;
                    bytesLeft -= Character.BYTES;
                    break;

                case SHORT:
                    if (bytesLeft < Short.BYTES) {
                        saveState(operation, fieldSpec, object, i, Short.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putShort(object, fieldSpec.getOffset(), UNSAFE.getShort(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Short.BYTES;
                    bytesLeft -= Short.BYTES;
                    break;

                case INT:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(object, fieldSpec.getOffset(), UNSAFE.getInt(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case LONG:
                    if (bytesLeft < Long.BYTES) {
                        saveState(operation, fieldSpec, object, i, Long.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putLong(object, fieldSpec.getOffset(), UNSAFE.getLong(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Long.BYTES;
                    bytesLeft -= Long.BYTES;
                    break;

                case FLOAT:
                    if (bytesLeft < Float.BYTES) {
                        saveState(operation, fieldSpec, object, i, Float.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putFloat(object, fieldSpec.getOffset(), UNSAFE.getFloat(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Float.BYTES;
                    bytesLeft -= Float.BYTES;
                    break;

                case DOUBLE:
                    if (bytesLeft < Double.BYTES) {
                        saveState(operation, fieldSpec, object, i, Double.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putDouble(object, fieldSpec.getOffset(), UNSAFE.getDouble(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Double.BYTES;
                    bytesLeft -= Double.BYTES;
                    break;

                case LENGTH:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, Operation.TMP_VALUE_FIELD, operation, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(operation, Operation.TMP_VALUE_FIELD.getOffset(), UNSAFE.getInt(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case BYTE_ARRAY:
                    byte[] bytes = new byte[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), bytes);
                    size = bytes.length * Byte.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, bytes, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, bytes, Constants.BYTE_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case CHAR_ARRAY:
                    char[] chars = new char[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), chars);
                    size = chars.length * Character.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, chars, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, chars, Constants.CHAR_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case SHORT_ARRAY:
                    short[] shorts = new short[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), shorts);
                    size = shorts.length * Short.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, shorts, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, shorts, Constants.SHORT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case INT_ARRAY:
                    int[] ints = new int[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), ints);
                    size = ints.length * Integer.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, ints, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, ints, Constants.INT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case LONG_ARRAY:
                    long[] longs = new long[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), longs);
                    size = longs.length * Long.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, longs, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, longs, Constants.LONG_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case FLOAT_ARRAY:
                    float[] floats = new float[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), floats);
                    size = floats.length * Float.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, floats, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, floats, Constants.FLOAT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case DOUBLE_ARRAY:
                    double[] doubles = new double[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), doubles);
                    size = doubles.length * Double.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, doubles, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, doubles, Constants.DOUBLE_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case BOOLEAN_ARRAY:
                    boolean[] booleans = new boolean[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), booleans);
                    size = booleans.length * Byte.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, booleans, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, booleans, Constants.BOOLEAN_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case ENUM:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    tmpObject = deserializeEnum(fieldSpec, buffer, offset);
                    UNSAFE.putObject(object, fieldSpec.getOffset(), tmpObject);
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case OBJECT:
                    tmpObject = FieldUtil.getOrAllocateObject(object, fieldSpec);
                    operation.setRoot(tmpObject);
                    size = deserializeNormal(operation, buffer, position, bytesLeft);
                    position += size;
                    bytesLeft -= size;
                    if (operation.isInterrupted()) {
                        operation.pushIndex(i);
                        i = fields.length;
                    }
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getOrAllocateArray(object, fieldSpec, operation.getTmpValue());
                    for (j = operation.getObjectArrayIndex(); j < array.length; j++) {
                        operation.setRoot(FieldUtil.getOrAllocateComponent(array, fieldSpec, j));
                        size = deserializeNormal(operation, buffer, position, bytesLeft);
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
            }
        }

        operation.setRoot(object);

        return position - offset;
    }

    static int deserializeNormal(final Operation operation, final long address, final int length) {
        Object tmpObject;
        Object[] array;
        Object object = operation.getRoot();
        Schema schema = SchemaRegistry.getSchema(object.getClass());
        long position = address;
        int bytesLeft = length;
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
                    UNSAFE.putByte(object, fieldSpec.getOffset(), UNSAFE.getByte(position));
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case BOOLEAN:
                    if (bytesLeft < Byte.BYTES) {
                        saveState(operation, fieldSpec, object, i, Byte.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putBoolean(object, fieldSpec.getOffset(), UNSAFE.getByte(position) == Constants.TRUE);
                    position += Byte.BYTES;
                    bytesLeft -= Byte.BYTES;
                    break;

                case CHAR:
                    if (bytesLeft < Character.BYTES) {
                        saveState(operation, fieldSpec, object, i, Character.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putChar(object, fieldSpec.getOffset(), UNSAFE.getChar(position));
                    position += Character.BYTES;
                    bytesLeft -= Character.BYTES;
                    break;

                case SHORT:
                    if (bytesLeft < Short.BYTES) {
                        saveState(operation, fieldSpec, object, i, Short.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putShort(object, fieldSpec.getOffset(), UNSAFE.getShort(position));
                    position += Short.BYTES;
                    bytesLeft -= Short.BYTES;
                    break;

                case INT:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(object, fieldSpec.getOffset(), UNSAFE.getInt(position));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case LONG:
                    if (bytesLeft < Long.BYTES) {
                        saveState(operation, fieldSpec, object, i, Long.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putLong(object, fieldSpec.getOffset(), UNSAFE.getLong(position));
                    position += Long.BYTES;
                    bytesLeft -= Long.BYTES;
                    break;

                case FLOAT:
                    if (bytesLeft < Float.BYTES) {
                        saveState(operation, fieldSpec, object, i, Float.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putFloat(object, fieldSpec.getOffset(), UNSAFE.getFloat(position));
                    position += Float.BYTES;
                    bytesLeft -= Float.BYTES;
                    break;

                case DOUBLE:
                    if (bytesLeft < Double.BYTES) {
                        saveState(operation, fieldSpec, object, i, Double.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putDouble(object, fieldSpec.getOffset(), UNSAFE.getDouble(position));
                    position += Double.BYTES;
                    bytesLeft -= Double.BYTES;
                    break;

                case LENGTH:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, Operation.TMP_VALUE_FIELD, operation, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.putInt(operation, Operation.TMP_VALUE_FIELD.getOffset(), UNSAFE.getInt(position));
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case BYTE_ARRAY:
                    byte[] bytes = new byte[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), bytes);
                    size = bytes.length * Byte.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, bytes, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(null, position, bytes, Constants.BYTE_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case CHAR_ARRAY:
                    char[] chars = new char[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), chars);
                    size = chars.length * Character.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, chars, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(null, position, chars, Constants.CHAR_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case SHORT_ARRAY:
                    short[] shorts = new short[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), shorts);
                    size = shorts.length * Short.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, shorts, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(null, position, shorts, Constants.SHORT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case INT_ARRAY:
                    int[] ints = new int[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), ints);
                    size = ints.length * Integer.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, ints, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(null, position, ints, Constants.INT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case LONG_ARRAY:
                    long[] longs = new long[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), longs);
                    size = longs.length * Long.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, longs, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(null, position, longs, Constants.LONG_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case FLOAT_ARRAY:
                    float[] floats = new float[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), floats);
                    size = floats.length * Float.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, floats, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(null, position, floats, Constants.FLOAT_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case DOUBLE_ARRAY:
                    double[] doubles = new double[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), doubles);
                    size = doubles.length * Double.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, doubles, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(null, position, doubles, Constants.DOUBLE_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case BOOLEAN_ARRAY:
                    boolean[] booleans = new boolean[operation.getTmpValue()];
                    UNSAFE.putObject(object, fieldSpec.getOffset(), booleans);
                    size = booleans.length * Byte.BYTES;
                    if (bytesLeft < size) {
                        operation.setParent(object);
                        saveState(operation, fieldSpec, booleans, i, size);
                        i = fields.length;
                        break;
                    }
                    UNSAFE.copyMemory(null, position, booleans, Constants.BOOLEAN_ARRAY_OFFSET, size);
                    position += size;
                    bytesLeft -= size;
                    break;

                case ENUM:
                    if (bytesLeft < Integer.BYTES) {
                        saveState(operation, fieldSpec, object, i, Integer.BYTES);
                        i = fields.length;
                        break;
                    }
                    tmpObject = deserializeEnum(fieldSpec, position);
                    UNSAFE.putObject(object, fieldSpec.getOffset(), tmpObject);
                    position += Integer.BYTES;
                    bytesLeft -= Integer.BYTES;
                    break;

                case OBJECT:
                    tmpObject = FieldUtil.getOrAllocateObject(object, fieldSpec);
                    operation.setRoot(tmpObject);
                    size = deserializeNormal(operation, position, bytesLeft);
                    position += size;
                    bytesLeft -= size;
                    if (operation.isInterrupted()) {
                        operation.pushIndex(i);
                        i = fields.length;
                    }
                    break;

                case OBJECT_ARRAY:
                    array = FieldUtil.getOrAllocateArray(object, fieldSpec, operation.getTmpValue());
                    for (j = operation.getObjectArrayIndex(); j < array.length; j++) {
                        operation.setRoot(FieldUtil.getOrAllocateComponent(array, fieldSpec, j));
                        size = deserializeNormal(operation, position, bytesLeft);
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
            }
        }

        operation.setRoot(object);

        return (int) (position - address);
    }

    static int deserializeInterrupted(final Operation operation, final byte[] buffer, final int offset, final int length) {
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
            case LENGTH:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(target, fieldSpec.getOffset() + fieldProcessed, UNSAFE.getByte(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                }
                break;

            case BOOLEAN:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putBoolean(target, fieldSpec.getOffset() + fieldProcessed, UNSAFE.getByte(buffer, Constants.BYTE_ARRAY_OFFSET + position) == Constants.TRUE);
                }
                break;

            case BYTE_ARRAY:
                UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, target, Constants.BYTE_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case CHAR_ARRAY:
                UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, target, Constants.CHAR_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case SHORT_ARRAY:
                UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, target, Constants.SHORT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case INT_ARRAY:
                UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, target, Constants.INT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case LONG_ARRAY:
                UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, target, Constants.LONG_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case FLOAT_ARRAY:
                UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, target, Constants.FLOAT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case DOUBLE_ARRAY:
                UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, target, Constants.DOUBLE_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case BOOLEAN_ARRAY:
                UNSAFE.copyMemory(buffer, Constants.BYTE_ARRAY_OFFSET + position, target, Constants.BOOLEAN_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case ENUM:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(operation, Operation.TMP_VALUE_FIELD.getOffset() + fieldProcessed, UNSAFE.getByte(buffer, Constants.BYTE_ARRAY_OFFSET + position));
                }
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getTarget(), fieldSpec.getOffset() , deserializeEnum(fieldSpec, operation.getTmpValue()));
                }
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

    static int deserializeInterrupted(final Operation operation, final long address, final int length) {
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
            case LENGTH:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(target, fieldSpec.getOffset() + fieldProcessed, UNSAFE.getByte(position));
                }
                break;

            case BOOLEAN:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putBoolean(target, fieldSpec.getOffset() + fieldProcessed, UNSAFE.getByte(position) == Constants.TRUE);
                }
                break;

            case BYTE_ARRAY:
                UNSAFE.copyMemory(null, position, target, Constants.BYTE_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case CHAR_ARRAY:
                UNSAFE.copyMemory(null, position, target, Constants.CHAR_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case SHORT_ARRAY:
                UNSAFE.copyMemory(null, position, target, Constants.SHORT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case INT_ARRAY:
                UNSAFE.copyMemory(null, position, target, Constants.INT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case LONG_ARRAY:
                UNSAFE.copyMemory(null, position, target, Constants.LONG_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case FLOAT_ARRAY:
                UNSAFE.copyMemory(null, position, target, Constants.FLOAT_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case DOUBLE_ARRAY:
                UNSAFE.copyMemory(null, position, target, Constants.DOUBLE_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case BOOLEAN_ARRAY:
                UNSAFE.copyMemory(null, position, target, Constants.BOOLEAN_ARRAY_OFFSET + fieldProcessed, byteCap - fieldProcessed);
                position += byteCap - fieldProcessed;
                fieldLeft -= byteCap - fieldProcessed;
                fieldProcessed += byteCap - fieldProcessed;
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getParent(), fieldSpec.getOffset(), target);
                }
                break;

            case ENUM:
                for (; fieldProcessed < byteCap; fieldProcessed++, position++, fieldLeft--) {
                    UNSAFE.putByte(operation, Operation.TMP_VALUE_FIELD.getOffset() + fieldProcessed, UNSAFE.getByte(position));
                }
                if (fieldLeft == 0) {
                    UNSAFE.putObject(operation.getTarget(), fieldSpec.getOffset() , deserializeEnum(fieldSpec, operation.getTmpValue()));
                }
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

    static Object deserializeEnum(final Schema.FieldSpec fieldSpec, final int ordinal) {
        return SchemaRegistry.getSchema(fieldSpec.getType()).getEnumConstant(ordinal);
    }

    static Object deserializeEnum(final Schema.FieldSpec fieldSpec, final byte[] buffer, final int offset) {
        Schema schema = SchemaRegistry.getSchema(fieldSpec.getType());
        final int ordinal = UNSAFE.getInt(buffer, Constants.BYTE_ARRAY_OFFSET + offset);
        return schema.getEnumConstant(ordinal);
    }

    static Object deserializeEnum(final Schema.FieldSpec fieldSpec, final long address) {
        Schema schema = SchemaRegistry.getSchema(fieldSpec.getType());
        final int ordinal = UNSAFE.getInt(address);
        return schema.getEnumConstant(ordinal);
    }
}
