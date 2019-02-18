package de.hhu.bsinfo.skema.data;

public class TextMessage extends Message {

    private final String m_content;

    public TextMessage(short m_senderId, short m_receiverId, String m_content) {
        super(m_senderId, m_receiverId);
        this.m_content = m_content;
    }

    public String getContent() {
        return m_content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TextMessage that = (TextMessage) o;
        return m_content.equals(that.m_content);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + m_content.hashCode();
    }
}
