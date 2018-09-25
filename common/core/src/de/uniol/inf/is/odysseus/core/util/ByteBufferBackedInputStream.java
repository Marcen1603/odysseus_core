package de.uniol.inf.is.odysseus.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Wraps a ByteBuffer in an Inputstream
 * 
 * @author Henrik Surm <henrik.surm@uni-oldenburg.de>
 * 
 */

public class ByteBufferBackedInputStream extends InputStream 
{
    ByteBuffer buf;

    public ByteBufferBackedInputStream(ByteBuffer buf) 
    {
        this.buf = buf;
    }

    @Override
    public int read() throws IOException 
    {
        if (!buf.hasRemaining()) 
        	return -1;
        
        return buf.get() & 0xFF;
    }

    @Override
    public int read(byte[] bytes, int off, int len) 
    {
        if (!buf.hasRemaining())
        	return -1;

        len = Math.min(len, buf.remaining());
        buf.get(bytes, off, len);
        return len;
    }
    
    @Override
    public int available()
    {
    	return buf.remaining();
    }
}