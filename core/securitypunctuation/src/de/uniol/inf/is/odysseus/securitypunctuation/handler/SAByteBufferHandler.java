package de.uniol.inf.is.odysseus.securitypunctuation.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;

public class SAByteBufferHandler<T> extends ByteBufferHandler<T> {
	
	private SecurityPunctuationHandler securityPunctuationHandler = new SecurityPunctuationHandler();
	
	public SAByteBufferHandler(IDataHandler<T> dataHandler) {
		super(dataHandler);
	}

	@SuppressWarnings("unchecked")
	public synchronized T createSecurityAware() throws IOException, ClassNotFoundException, BufferUnderflowException {
		T retval = null;
		ByteBuffer byteBuffer = getByteBuffer();
		synchronized(byteBuffer){
			byteBuffer.flip();
			retval = (T)this.securityPunctuationHandler.readData(byteBuffer);
			byteBuffer.clear();
		}
		return retval;
	}

}
