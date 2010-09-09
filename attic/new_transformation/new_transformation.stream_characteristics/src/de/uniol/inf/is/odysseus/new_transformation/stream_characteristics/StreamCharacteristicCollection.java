package de.uniol.inf.is.odysseus.new_transformation.stream_characteristics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StreamCharacteristicCollection {
	private Map<Class<? extends IStreamCharacteristic>, IStreamCharacteristic> streamMetadatas = new HashMap<Class<? extends IStreamCharacteristic>, IStreamCharacteristic>();

	public <T extends IStreamCharacteristic> void putMetadata(T streamMetadata) {
		streamMetadatas.put(streamMetadata.getClass(), streamMetadata);
	}

	public <T extends IStreamCharacteristic> T getMetadata(Class<T> streamMetadataType) {
		return (T) streamMetadatas.get(streamMetadataType);
	}

	@Override
	public String toString() {
		Collection<IStreamCharacteristic> values = streamMetadatas.values();

		StringBuilder builder = new StringBuilder("StreamCharacteristicCollection:");
		for (IStreamCharacteristic streamCharacteristic : values) {
			builder.append('\n');
			builder.append(" - ");
			builder.append(streamCharacteristic);
		}
		return builder.toString();
	}
}
