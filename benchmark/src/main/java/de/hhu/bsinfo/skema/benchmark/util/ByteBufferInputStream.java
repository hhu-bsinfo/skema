package de.hhu.bsinfo.skema.benchmark.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {

    private final ByteBuffer buffer;

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        return buffer.get();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        buffer.get(b, off, len);
        return len;
    }

    @Override
    public int available() throws IOException {
        return buffer.remaining();
    }

    @Override
    public void reset() throws IOException {
        buffer.reset();
    }
}
