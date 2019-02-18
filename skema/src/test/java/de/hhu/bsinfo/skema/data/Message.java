package de.hhu.bsinfo.skema.data;

public class Message {

    private final short m_senderId;

    private final short m_receiverId;

    public Message(short m_senderId, short m_receiverId) {
        this.m_senderId = m_senderId;
        this.m_receiverId = m_receiverId;
    }

    public short getSenderId() {
        return m_senderId;
    }

    public short getReceiverId() {
        return m_receiverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return m_senderId == message.m_senderId &&
                m_receiverId == message.m_receiverId;
    }

    @Override
    public int hashCode() {
        return Short.hashCode(m_senderId) + Short.hashCode(m_receiverId);
    }
}
