package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;

public class ByteBufferUtil {

	private ByteBufferUtil() {
	}

	public static IPunctuation createPunctuation(ByteBuffer messageBuffer) {
		final byte[] rawData = new byte[messageBuffer.remaining()];
		messageBuffer.get(rawData, 0, rawData.length);

		return (IPunctuation) ObjectByteConverter.bytesToObject(rawData);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends IStreamObject> T createStreamObject(
			ByteBuffer byteBuffer, IDataHandler<T> dataHandler)
			throws BufferUnderflowException {
		T retval = null;
		try {
			retval = dataHandler.readData(byteBuffer);

			if (byteBuffer.remaining() > 0) {

				int metadataBytesLength = byteBuffer.getInt();
				byte[] metadataBytes = new byte[metadataBytesLength];
				byteBuffer.get(metadataBytes);

				IMetaAttribute metadata = (IMetaAttribute) ObjectByteConverter
						.bytesToObject(metadataBytes);
				retval.setMetadata(metadata);

//				if (byteBuffer.remaining() > 0) {
//					int metadataMapBytesLength = byteBuffer.getInt();
//					byte[] metadataMapBytes = new byte[metadataMapBytesLength];
//					byteBuffer.get(metadataMapBytes);
//
//					Map<String, Object> metadataMap = (Map<String, Object>) ObjectByteConverter
//							.bytesToObject(metadataMapBytes);
//					System.err.println(metadataMap);
//
//					retval.setKeyValueMap(metadataMap);
//				}
			}
		} finally {
			byteBuffer.clear();
		}

		return retval;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends IStreamObject> void toBuffer(ByteBuffer buffer,
			T object, IStreamObjectDataHandler dataHandler, boolean withJavaSerializedMetadata, boolean withOdysseusSerializedMetadata) {
		dataHandler.writeData(buffer, object, withOdysseusSerializedMetadata);

		if (withJavaSerializedMetadata) {
			Object metadata = object.getMetadata();
			if (metadata != null) {
				byte[] metadataBytes = ObjectByteConverter.objectToBytes(object
						.getMetadata());
				buffer.putInt(metadataBytes.length);
				buffer.put(metadataBytes);
			}

//			Map<String, Object> metadataMap = object.getGetValueMap();
//			if (!metadataMap.isEmpty()) {
//				byte[] metadataMapBytes = ObjectByteConverter
//						.objectToBytes(metadataMap);
//				buffer.putInt(metadataMapBytes.length);
//				buffer.put(metadataMapBytes);
//			}
		}
	}

	/**
	 * Encode an object to a byte array.
	 *
	 * @param obj
	 *            The object to encode.
	 * @return A byte array.
	 * @throws IOException if any error occurs.
	 */
	public static byte[] toByteArray(Serializable obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(obj);
			return baos.toByteArray();
		}
	}

	/**
	 * Decode an object from a byte array using {@code OsgiObjectInputStream}.
	 *
	 * @param bytes
	 *            A byte array to decode.
	 * @return The decoded object.
	 * @throws ClassNotFoundException if the class of the decoded object could not be found.
	 * @throws IOException if any other error occurs.
	 */
	public static Serializable fromByteArray(byte[] bytes) throws ClassNotFoundException, IOException {
		try (OsgiObjectInputStream oois = new OsgiObjectInputStream(new ByteArrayInputStream(bytes))) {
			return (Serializable) oois.readObject();
		}
	}

}
