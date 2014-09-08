package de.uniol.inf.is.odysseus.core.objecthandler;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public class ByteBufferUtil {

	private ByteBufferUtil() {
	}

	
	public static IPunctuation createPunctuation(ByteBuffer messageBuffer) {
		final byte[] rawData = new byte[messageBuffer.remaining()];
		messageBuffer.get(rawData, 0, rawData.length);

		return (IPunctuation) ObjectByteConverter.bytesToObject(rawData);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends IStreamObject> T createStreamObject(ByteBuffer byteBuffer, IDataHandler<T> dataHandler) throws BufferUnderflowException {
		T retval = null;
		try {
			retval = dataHandler.readData(byteBuffer);

			if (byteBuffer.remaining() > 0) {
				
				int metadataBytesLength = byteBuffer.getInt();
				byte[] metadataBytes = new byte[metadataBytesLength];
				byteBuffer.get(metadataBytes);

				IMetaAttribute metadata = (IMetaAttribute) ObjectByteConverter.bytesToObject(metadataBytes);
				retval.setMetadata(metadata);
				
				if( byteBuffer.remaining() > 0 ) {
					int metadataMapBytesLength = byteBuffer.getInt();
					byte[] metadataMapBytes = new byte[metadataMapBytesLength];
					byteBuffer.get(metadataMapBytes);
					
					Map<String, Object> metadataMap = (Map<String, Object>) ObjectByteConverter.bytesToObject(metadataMapBytes);
					System.err.println(metadataMap);
					
					retval.setMetadataMap(metadataMap);
				}
			}
		} finally {
			byteBuffer.clear();
		}

		return retval;
	}
}
