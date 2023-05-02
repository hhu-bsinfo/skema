package de.hhu.bsinfo.skema.examples.netty.handler;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.util.Operation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class SkemaInboundHandler extends ByteToMessageDecoder {

    private final Operation operation = new Operation(null);

    private int available = 0;
    private int length = 0;
    private short type = 0;
    private int readerIndex = 0;
    private int bytesRead = 0;

    private DecoderState state = DecoderState.READ_LENGTH;

    @Override
    protected void decode(ChannelHandlerContext p_ctx, ByteBuf p_in, List<Object> p_out) {
        switch (state) {
            case READ_LENGTH:

                // Wait for more data if length field can't be read in one go
                if (p_in.readableBytes() < Integer.BYTES) {
                    return;
                }

                // Read length field and switch state to READ_TYPE
                length = p_in.readInt();
                state = DecoderState.READ_TYPE;

            case READ_TYPE:

                // Wait for more data if type field can't be read in one go
                if (p_in.readableBytes() < Short.BYTES) {
                    return;
                }

                // Read type field, reset the serializer operation and switch state to READ_PAYLOAD
                type = p_in.readShort();
                operation.reset(Skema.newInstance(type));
                state = DecoderState.READ_PAYLOAD;

            case READ_PAYLOAD:

                // Get readable bytes and the current reader index increment it later
                available = p_in.readableBytes();
                readerIndex = p_in.readerIndex();

                // Deserialize the available data using the current operation
                Skema.deserialize(operation, p_in.memoryAddress() + readerIndex, available);

                // Adjust the buffer's reader index
                p_in.readerIndex(readerIndex + available);
                bytesRead += available;

                // Hand object to the next handler, if it was deserialized completely
                if (bytesRead == length) {
                    p_out.add(operation.get());
                    state = DecoderState.READ_LENGTH;
                    bytesRead = 0;
                }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }
}
