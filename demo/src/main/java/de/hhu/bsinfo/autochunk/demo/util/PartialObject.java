package de.hhu.bsinfo.autochunk.demo.util;

public class PartialObject {

    private Object m_object;

    private int m_expectedBytes;

    private int m_currentBytes;

    public PartialObject(Object p_object, int p_expectedBytes) {
        this(p_object, p_expectedBytes, 0);
    }

    public PartialObject(Object p_object, int p_expectedBytes, int p_currentBytes) {
        m_object = p_object;
        m_expectedBytes = p_expectedBytes;
        m_currentBytes = p_currentBytes;
    }

    public Object get() {
        return m_object;
    }

    public int getExpectedBytes() {
        return m_expectedBytes;
    }

    public int getCurrentBytes() {
        return m_currentBytes;
    }

    public void set(Object p_target) {
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

    public void reset() {
        reset(null, 0);

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