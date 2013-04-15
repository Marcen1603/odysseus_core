package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;

@SuppressWarnings("rawtypes")
public class JxtaByteBufferHandler<T extends IStreamObject> extends ByteBufferHandler<T> {
	
	private static final Logger LOG = LoggerFactory.getLogger(JxtaByteBufferHandler.class);

	public JxtaByteBufferHandler(IDataHandler<?> dataHandler) {
		super(dataHandler);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized T create() throws IOException, ClassNotFoundException, BufferUnderflowException {
		T retval = null;
		ByteBuffer byteBuffer = getByteBuffer();
		synchronized(byteBuffer){		
			byteBuffer.flip();
			retval = (T)getDataHandler().readData(byteBuffer);
			
			byte[] metadataBytes = new byte[byteBuffer.remaining()];
			byteBuffer.get(metadataBytes);
			
			IMetaAttribute metadata = (IMetaAttribute) fromBytes(metadataBytes);
			retval.setMetadata(metadata);
			byteBuffer.clear();
		}
		return retval;
	}
	
	private Object fromBytes( byte[] data ) {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		  return in.readObject(); 
		} catch (IOException | ClassNotFoundException e) {
			LOG.error("Could not read object", e);
		} finally {
			try {
				if( bis != null  ) {
					bis.close();
				}
				if( in != null ) {
					in.close();
				}
			} catch( IOException ex ) {
			}
		}
		
		return null;
	}
}
