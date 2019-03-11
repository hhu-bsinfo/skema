package de.hhu.bsinfo.skema.examples.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

import de.hhu.bsinfo.skema.Skema;

public class RoundTripTime {

    private long m_sendTime;
    private long m_receivedTime;

    public RoundTripTime(long p_sendTime) {
        m_sendTime = p_sendTime;
    }

    public RoundTripTime(long p_sendTime, long p_receivedTime) {
        m_sendTime = p_sendTime;
        m_receivedTime = p_receivedTime;
    }

    public long getReceivedTime() {
        return m_receivedTime;
    }

    public long getSendTime() {
        return m_sendTime;
    }

    public void setSendTime(long p_sendTime) {
        m_sendTime = p_sendTime;
    }

    public void setReceivedTime(long p_receivedTime) {
        m_receivedTime = p_receivedTime;
    }

    public long getElapsedTime() {
        return m_receivedTime - m_sendTime;
    }

    @Override
    public String toString() {
        return String.format("[%d | %d]", m_sendTime, m_receivedTime);
    }
}
