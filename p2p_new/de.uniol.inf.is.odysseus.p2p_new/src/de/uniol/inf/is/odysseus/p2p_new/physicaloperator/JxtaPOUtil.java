package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.p2p_new.util.ObjectByteConverter;

final class JxtaPOUtil {
	
	private JxtaPOUtil() {
	}
	
	public static final byte NONE_BYTE = 0;
	public static final byte PUNCTUATION_BYTE = 1;
	public static final byte DATA_BYTE = 2;
	public static final byte CONTROL_BYTE = 3;
	
	public static final byte OPEN_SUBBYTE = 0;
	public static final byte CLOSE_SUBBYTE = 1;

	public static byte[] generateControlPacket( byte type ) {
		byte[] packet = new byte[2];
		packet[0] = CONTROL_BYTE;
		packet[1] = type;
		return packet;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends IStreamObject> T createStreamObject(ByteBuffer byteBuffer, TupleDataHandler dataHandler) throws BufferUnderflowException {
		T retval = null;
		try {
			retval = (T) dataHandler.readData(byteBuffer);

			if (byteBuffer.remaining() > 0) {
				byte[] metadataBytes = new byte[byteBuffer.remaining()];
				byteBuffer.get(metadataBytes);

				IMetaAttribute metadata = (IMetaAttribute) ObjectByteConverter.bytesToObject(metadataBytes);
				retval.setMetadata(metadata);
			}
		} finally {
			byteBuffer.clear();
		}
		
		return retval;
	}
	
	public static IPunctuation createPunctuation(ByteBuffer messageBuffer) {
		byte[] rawData = new byte[messageBuffer.remaining()];
		messageBuffer.get(rawData, 0, rawData.length);
		
		return (IPunctuation)ObjectByteConverter.bytesToObject(rawData);
	}

}
