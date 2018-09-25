package de.uniol.inf.is.odysseus.core.collection;

import java.nio.ByteBuffer;

public class ByteBufferWrapper 
{
	final private ByteBuffer buffer;
	
	public ByteBufferWrapper(ByteBuffer buffer)
	{
		this.buffer = buffer;
	}

	public ByteBuffer getBuffer()
	{
		return buffer;
	}
}
