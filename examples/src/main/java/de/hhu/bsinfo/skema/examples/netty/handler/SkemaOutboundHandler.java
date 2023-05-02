package de.hhu.bsinfo.skema.examples.netty.handler;

import de.hhu.bsinfo.skema.Skema;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class SkemaOutboundHandler extends ChannelOutboundHandlerAdapter {

    private static final int MESSAGE_HEADER_SIZE = Integer.BYTES + Short.BYTES;
    private int payloadSize = 0;
    private int writerIndex = 0;
    private short type = 0;

    @Override
    public void write(ChannelHandlerContext context, Object message, ChannelPromise promise) {

        // Get the messages identifier and it's size using Skema
        type = Skema.resolveIdentifier(message.getClass());
        payloadSize = Skema.sizeOf(message);

        // Allocate a direct buffer big enough to hold the header and the payload
        ByteBuf buffer = context.alloc().directBuffer(payloadSize + MESSAGE_HEADER_SIZE);

        // Write out the length and type information
        buffer.writeInt(payloadSize);
        buffer.writeShort(type);

        // Remember the current writer index, write out data to native memory
        // and adjust writer index according to the amount of written bytes
        writerIndex = buffer.writerIndex();
        Skema.serialize(message, buffer.memoryAddress() + writerIndex);
        buffer.writerIndex(writerIndex + payloadSize);

        // Write out the data
        context.writeAndFlush(buffer, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }
}
