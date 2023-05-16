package de.hhu.bsinfo.skema.util;

import java.lang.reflect.Field;

import de.hhu.bsinfo.skema.schema.Schema;

@SuppressWarnings("sunapi")
public class Operation {

    public static final Schema.FieldSpec TMP_VALUE_FIELD;

    static {
        try {
            sun.misc.Unsafe unsafe = UnsafeProvider.getUnsafe();
            Field field = Operation.class.getDeclaredField("tmpValue");
            long offset = unsafe.objectFieldOffset(field);
            TMP_VALUE_FIELD = new Schema.FieldSpec(
                    FieldType.INT,
                    offset,
                    "m_tmpValue",
                    field
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Could not find array length field", e);
        }
    }

    public enum Status {
        NONE, INTERRUPTED
    }

    private Status status = Status.NONE;

    private Object root;
    private Object target;
    private Object parent;

    private Schema schema;
    private Schema.FieldSpec fieldSpec;
    private Schema.FieldSpec parentFieldSpec;

    private int bytesProcessed = 0;
    private int fieldProcessed = 0;
    private int fieldLeft = 0;

    private final int[] indexStack = new int[128];
    private int stackPosition = 0;

    private int tmpValue = 0;
    private int objectArrayIndex = 0;

    public Operation(Object result) {
        root = result;
        target = result;
    }

    public Object get() {
        return root;
    }

    public byte[] getBuffer() {
        return (byte[]) root;
    }

    public int getBytesProcessed() {
        return bytesProcessed;
    }

    public void setRoot(Object root) {
        this.root = root;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setBytesProcessed(int bytesProcessed) {
        this.bytesProcessed = bytesProcessed;
    }

    public void addCurrentBytes(int bytesProcessed) {
        this.bytesProcessed += bytesProcessed;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(final Object parent) {
        this.parent = parent;
    }

    public void setParent(final Object parent, final Schema.FieldSpec fieldSpec) {
        this.parent = parent;
        parentFieldSpec = fieldSpec;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status p_status) {
        status = p_status;
    }

    public void pushIndex(final int p_index) {
        indexStack[stackPosition++] = p_index;
    }

    public int popIndex() {
        return stackPosition != 0 ? indexStack[--stackPosition] : 0;
    }

    public boolean hasStarted() {
        return bytesProcessed != 0;
    }

    public void reset() {
        reset(null);
    }

    public int getFieldProcessed() {
        return fieldProcessed;
    }

    public void setFieldProcessed(int p_fieldProcessed) {
        fieldProcessed = p_fieldProcessed;
    }

    public void reset(final Object p_target) {
        root = p_target;
        bytesProcessed = 0;
        fieldProcessed = 0;
        fieldLeft = 0;
        stackPosition = 0;
        tmpValue = 0;
        objectArrayIndex = 0;
        schema = null;
        fieldSpec = null;
        parentFieldSpec = null;
    }

    public void rewind() {
        target = root;
        bytesProcessed = 0;
        fieldProcessed = 0;
        fieldLeft = 0;
        stackPosition = 0;
        tmpValue = 0;
        objectArrayIndex = 0;
        schema = null;
        fieldSpec = null;
        parentFieldSpec = null;
    }

    public int getFieldLeft() {
        return fieldLeft;
    }

    public void setFieldLeft(int p_fieldLeft) {
        fieldLeft = p_fieldLeft;
    }

    public int getTmpValue() {
        return tmpValue;
    }

    public void setTmpValue(int p_tmpValue) {
        tmpValue = p_tmpValue;
    }

    public Object getRoot() {
        return root;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema p_schema) {
        schema = p_schema;
    }

    public Schema.FieldSpec getFieldSpec() {
        return fieldSpec;
    }

    public void setFieldSpec(Schema.FieldSpec p_fieldSpec) {
        fieldSpec = p_fieldSpec;
    }

    public int getObjectArrayIndex() {
        return objectArrayIndex;
    }

    public void setObjectArrayIndex(int p_objectArrayIndex) {
        objectArrayIndex = p_objectArrayIndex;
    }

    public Schema.FieldSpec getParentFieldSpec() {
        return parentFieldSpec;
    }

    public void setParentFieldSpec(Schema.FieldSpec p_parentFieldSpec) {
        parentFieldSpec = p_parentFieldSpec;
    }

    public boolean isInterrupted() {
        return status == Status.INTERRUPTED;
    }
}
