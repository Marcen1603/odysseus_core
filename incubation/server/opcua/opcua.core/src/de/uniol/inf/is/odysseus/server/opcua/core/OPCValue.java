package de.uniol.inf.is.odysseus.server.opcua.core;

public class OPCValue<T> {

	private Long timestamp;
	private T value;
	private Integer quality;
	private Long error;

	public OPCValue(Long timestamp, T value, Integer quality, Long error) {
		this.timestamp = timestamp;
		this.value = value;
		this.quality = quality;
		this.error = error;
	}

	public Long getError() {
		return error;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public Integer getQuality() {
		return quality;
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value + "[timestamp=" + timestamp + ", quality=" + quality + ", error=" + error + "]";
	}
}