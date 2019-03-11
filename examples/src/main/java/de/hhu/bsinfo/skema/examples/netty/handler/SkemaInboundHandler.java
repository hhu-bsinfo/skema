package de.hhu.bsinfo.skema.examples.netty.handler;

import de.hhu.bsinfo.skema.Skema;
import de.hhu.bsinfo.skema.util.Operation;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class SkemaInboundHandler extends ReplayingDecoder<DecoderState> {

    private final Operation m_operation = new Operation(null);

    private int m_available = 0;
    private int m_length = 0;
    private short m_type = 0;
    private int m_readerIndex = 0;

    public SkemaInboundHandler() {
        super(DecoderState.READ_LENGTH);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        switch (state()) {
            case READ_LENGTH:
                m_length = in.readInt();
                checkpoint(DecoderState.READ_TYPE);
                System.out.printf("Read length %d\n", m_length);
            case READ_TYPE:
                m_type = in.readShort();
                m_operation.reset(Skema.newInstance(m_type));
                checkpoint(DecoderState.READ_PAYLOAD);
                System.out.printf("Read type %d\n", m_type);
            case READ_PAYLOAD:
                m_available = in.readableBytes();
                m_readerIndex = in.readerIndex();
                Skema.deserialize(m_operation, in.memoryAddress() + in.readerIndex(), m_available);
                m_length -= m_available;
                in.readerIndex(m_readerIndex + m_available);
                if (m_length != 0) {
                    return;
                }

                out.add(m_operation.get());
                checkpoint(DecoderState.READ_LENGTH);
                break;
        }
    }
}
