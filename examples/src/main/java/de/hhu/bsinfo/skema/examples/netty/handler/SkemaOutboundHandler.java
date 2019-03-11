package de.hhu.bsinfo.skema.examples.netty.handler;

import de.hhu.bsinfo.skema.Skema;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class SkemaOutboundHandler extends ChannelOutboundHandlerAdapter {

    private static final int MESSAGE_HEADER_SIZE = Integer.BYTES + Short.BYTES;
    private int m_payloadSize = 0;
    private int m_writerIndex = 0;
    private short m_type = 0;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        System.out.println("WRITE");
        m_type = Skema.resolveIdentifier(msg.getClass());
        m_payloadSize = Skema.sizeOf(msg);

        System.out.printf("Writing out type %d with size %d\n", m_type, m_payloadSize);

        ByteBuf buffer = ctx.alloc().directBuffer(m_payloadSize + MESSAGE_HEADER_SIZE);
        buffer.writeInt(m_payloadSize);
        buffer.writeShort(m_type);

        System.out.println("Header written out");

        m_writerIndex = buffer.writerIndex();
        System.out.printf("Serializing. Writer index is at %d\n", m_writerIndex);
        Skema.serialize(msg, buffer.memoryAddress() + m_writerIndex);
        System.out.printf("Done. Setting writer index to %d\n", m_writerIndex + m_payloadSize);
        buffer.writerIndex(m_writerIndex + m_payloadSize);
        System.out.println("WRITE OUT");
        ctx.writeAndFlush(buffer, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
