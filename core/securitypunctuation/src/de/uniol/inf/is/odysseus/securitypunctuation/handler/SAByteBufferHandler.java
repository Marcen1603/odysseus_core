package de.uniol.inf.is.odysseus.securitypunctuation.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class SAByteBufferHandler<T> extends ByteBufferHandler<T> {
	
	private IDataHandler<ISecurityPunctuation> securityPunctuationHandler;
	
	public SAByteBufferHandler(IDataHandler<T> dataHandler) {
		super(dataHandler);
	}

	@SuppressWarnings("unchecked")
	public synchronized T createSecurityAware(String spType) throws IOException, ClassNotFoundException, BufferUnderflowException {
		if(securityPunctuationHandler == null) {
			if(spType.equals("attribute")) {
				securityPunctuationHandler = new SecurityPunctuationHandler();
			} else {
				securityPunctuationHandler = new PredicateSPHandler();
			}
		}
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
