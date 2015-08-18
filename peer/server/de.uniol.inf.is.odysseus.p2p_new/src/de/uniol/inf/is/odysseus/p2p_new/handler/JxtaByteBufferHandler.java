package de.uniol.inf.is.odysseus.p2p_new.handler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.peer.util.ObjectByteConverter;

public class JxtaByteBufferHandler<T  extends IStreamObject<IMetaAttribute>> extends ByteBufferHandler<T> {

	public JxtaByteBufferHandler(IStreamObjectDataHandler<?> dataHandler) {
		super(dataHandler);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized T create() throws IOException, ClassNotFoundException, BufferUnderflowException {
		T retval = null;
		ByteBuffer byteBuffer = getByteBuffer();
		synchronized (byteBuffer) {
			byteBuffer.flip();
			try {
				retval = (T) getDataHandler().readData(byteBuffer);

				if (byteBuffer.remaining() > 0) {
					byte[] metadataBytes = new byte[byteBuffer.remaining()];
					byteBuffer.get(metadataBytes);

					IMetaAttribute metadata = (IMetaAttribute) ObjectByteConverter.bytesToObject(metadataBytes);
					retval.setMetadata(metadata);
				}
			} finally {
				byteBuffer.clear();
			}
		}
		return retval;
	}
}
