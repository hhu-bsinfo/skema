package de.hhu.bsinfo.autochunk.demo.util;

public class Operation {

    private Object m_object;

    private int m_expectedBytes;

    private int m_currentBytes;

    private final int[] m_sourceIndexStack = new int[128];

    private final int[] m_targetIndexStack = new int[128];

    private int m_sourceStackPosition = 0;

    private int m_targetStackPosition = 0;

    private int m_offset = 0;

    public Operation(Object p_object, int p_expectedBytes) {
        this(p_object, p_expectedBytes, 0);
    }

    public Operation(Object p_object, int p_expectedBytes, int p_currentBytes) {
        m_object = p_object;
        m_expectedBytes = p_expectedBytes;
        m_currentBytes = p_currentBytes;
    }

    public Object getObject() {
        return m_object;
    }

    public byte[] getBuffer() {
        return (byte[]) m_object;
    }

    public int getExpectedBytes() {
        return m_expectedBytes;
    }

    public int getCurrentBytes() {
        return m_currentBytes;
    }

    public void setTarget(Object p_target) {
        m_object = p_target;
    }

    public void setExpectedBytes(int p_expectedBytes) {
        m_expectedBytes = p_expectedBytes;
    }

    public void setCurrentBytes(int p_currentBytes) {
        m_currentBytes = p_currentBytes;
    }

    public void addCurrentBytes(int p_bytesProcessed) {
        m_currentBytes += p_bytesProcessed;
    }

    public void pushSourceIndex(final int p_index) {
        m_sourceIndexStack[m_sourceStackPosition++] = p_index;
    }

    public int popSourceIndex() {
        return m_sourceIndexStack[--m_sourceStackPosition];
    }

    public void pushTargetIndex(final int p_index) {
        m_targetIndexStack[m_targetStackPosition++] = p_index;
    }

    public int popTargetIndex() {
        return m_targetIndexStack[--m_targetStackPosition];
    }

    public boolean hasStarted() {
        return m_currentBytes != 0;
    }

    public void reset() {
        reset(null, 0);

    }

    public int getOffset() {
        return m_offset;
    }

    public void setOffset(int p_offset) {
        m_offset = p_offset;
    }

    public void reset(final Object p_target, final int p_expectedBytes) {
        m_object = p_target;
        m_expectedBytes = p_expectedBytes;
        m_currentBytes = 0;
    }

    public boolean isFinished() {
        return m_object != null && m_expectedBytes == m_currentBytes;
    }
}
