package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {

    private final ByteBuffer buf;

    public ByteBufferInputStream(final ByteBuffer buf) {
        this.buf = buf;
    }

    @Override
    public int read() throws IOException {
        if (!this.buf.hasRemaining()) {
            return -1;
        }
        return this.buf.get() & 0xFF;
    }

    @Override
    public int read(final byte[] bytes, final int off, final int len) throws IOException {
        if (!this.buf.hasRemaining()) {
            return -1;
        }

        final int length = Math.min(len, this.buf.remaining());
        this.buf.get(bytes, off, length);
        return length;
    }
}