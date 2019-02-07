package de.hhu.bsinfo.skema.util;

import java.lang.reflect.Field;

import de.hhu.bsinfo.skema.scheme.Scheme;

public class Operation {

    public static final Scheme.FieldSpec ARRAY_LENGTH_FIELD;

    static {
        try {
            sun.misc.Unsafe unsafe = UnsafeProvider.getUnsafe();
            Field field = Operation.class.getDeclaredField("m_arrayLength");
            long offset = unsafe.objectFieldOffset(field);
            ARRAY_LENGTH_FIELD = new Scheme.FieldSpec(
                    FieldType.INT,
                    offset,
                    "m_arrayLength",
                    field
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Could not find array length field", e);
        }
    }

    public enum Status {
        NONE, INTERRUPTED
    }

    private Object m_root;

    private Object m_target;

    private Object m_parent;

    private Scheme m_scheme;

    private Scheme.FieldSpec m_fieldSpec;

    private int m_bytesProcessed = 0;

    private int m_fieldProcessed = 0;

    private int m_fieldLeft = 0;

    private Status m_status = Status.NONE;

    private final int[] m_indexStack = new int[128];

    private int m_stackPosition = 0;

    private int m_arrayLength = 0;

    private int m_objectArrayIndex = 0;

    public Operation(Object p_result) {
        m_root = p_result;
        m_target = p_result;
    }

    public byte[] getBuffer() {
        return (byte[]) m_root;
    }

    public int getBytesProcessed() {
        return m_bytesProcessed;
    }

    public void setRoot(Object p_root) {
        m_root = p_root;
    }

    public Object getTarget() {
        return m_target;
    }

    public void setTarget(Object p_target) {
        m_target = p_target;
    }

    public void setBytesProcessed(int p_bytesProcessed) {
        m_bytesProcessed = p_bytesProcessed;
    }

    public void addCurrentBytes(int p_bytesProcessed) {
        m_bytesProcessed += p_bytesProcessed;
    }

    public Object getParent() {
        return m_parent;
    }

    public void setParent(Object p_parent) {
        m_parent = p_parent;
    }

    public Status getStatus() {
        return m_status;
    }

    public void setStatus(final Status p_status) {
        m_status = p_status;
    }

    public void pushIndex(final int p_index) {
        m_indexStack[m_stackPosition++] = p_index;
    }

    public int popIndex() {
        return m_stackPosition != 0 ? m_indexStack[--m_stackPosition] : 0;
    }

    public boolean hasStarted() {
        return m_bytesProcessed != 0;
    }

    public void reset() {
        reset(null, 0);
    }

    public int getFieldProcessed() {
        return m_fieldProcessed;
    }

    public void setFieldProcessed(int p_fieldProcessed) {
        m_fieldProcessed = p_fieldProcessed;
    }

    public void reset(final Object p_target, final int p_expectedBytes) {
        m_root = p_target;
        m_bytesProcessed = 0;
    }

    public int getFieldLeft() {
        return m_fieldLeft;
    }

    public void setFieldLeft(int p_fieldLeft) {
        m_fieldLeft = p_fieldLeft;
    }

    public int getArrayLength() {
        return m_arrayLength;
    }

    public void setArrayLength(int p_arrayLength) {
        m_arrayLength = p_arrayLength;
    }

    public Object getRoot() {
        return m_root;
    }

    public Scheme getScheme() {
        return m_scheme;
    }

    public void setScheme(Scheme p_scheme) {
        m_scheme = p_scheme;
    }

    public Scheme.FieldSpec getFieldSpec() {
        return m_fieldSpec;
    }

    public void setFieldSpec(Scheme.FieldSpec p_fieldSpec) {
        m_fieldSpec = p_fieldSpec;
    }

    public int getObjectArrayIndex() {
        return m_objectArrayIndex;
    }

    public void setObjectArrayIndex(int p_objectArrayIndex) {
        m_objectArrayIndex = p_objectArrayIndex;
    }

    public boolean isInterrupted() {
        return m_status == Status.INTERRUPTED;
    }
}