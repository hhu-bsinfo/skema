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

    public static final class Encoder extends MessageToByteEncoder<RoundTripTime> {
        @Override
        protected void encode(ChannelHandlerContext p_ctx, RoundTripTime p_msg, ByteBuf p_out) throws Exception {
            p_out.writeBytes(Skema.serialize(p_msg));
        }
    }

    public static final class Decoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext p_ctx, ByteBuf p_in, List<Object> p_out) throws Exception {
            if (p_in.readableBytes() < 2 * Long.BYTES) {
                return;
            }

            byte[] bytes = new byte[2 * Long.BYTES];
            p_in.readBytes(bytes);
            p_out.add(Skema.deserialize(RoundTripTime.class, bytes));
        }
    }
}
