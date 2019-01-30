package de.hhu.bsinfo.autochunk.demo.util;

import de.hhu.bsinfo.autochunk.demo.schema.Schema;

public class Operation {

    public enum Status {
        NONE, INTERRUPTED
    }

    private Object m_result;

    private Object m_target;

    private Schema m_schema;

    private Schema.FieldSpec m_fieldSpec;

    private int m_bytesProcessed;

    private int m_fieldProcessed = 0;

    private int m_fieldLeft = 0;

    private Status m_status = Status.NONE;

    private final int[] m_indexStack = new int[128];

    private int m_stackPosition = 0;

    public Operation(Object p_result) {
        m_result = p_result;
    }

    public Object getObject() {
        return m_result;
    }

    public byte[] getBuffer() {
        return (byte[]) m_result;
    }


    public int getBytesProcessed() {
        return m_bytesProcessed;
    }

    public void setResult(Object p_result) {
        m_result = p_result;
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
        m_result = p_target;
        m_bytesProcessed = 0;
    }

    public int getFieldLeft() {
        return m_fieldLeft;
    }

    public void setFieldLeft(int p_fieldLeft) {
        m_fieldLeft = p_fieldLeft;
    }

    public Object getResult() {
        return m_result;
    }

    public Schema getSchema() {
        return m_schema;
    }

    public void setSchema(Schema p_schema) {
        m_schema = p_schema;
    }

    public Schema.FieldSpec getFieldSpec() {
        return m_fieldSpec;
    }

    public void setFieldSpec(Schema.FieldSpec p_fieldSpec) {
        m_fieldSpec = p_fieldSpec;
    }
}
