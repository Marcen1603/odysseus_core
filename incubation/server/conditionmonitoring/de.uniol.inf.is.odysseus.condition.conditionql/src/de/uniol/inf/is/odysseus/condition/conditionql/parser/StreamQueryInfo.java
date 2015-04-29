package de.uniol.inf.is.odysseus.condition.conditionql.parser;

import java.io.Serializable;

public class StreamQueryInfo implements Serializable {
	
	private static final long serialVersionUID = 486034817889611134L;
	private String streamName;

	public StreamQueryInfo(String streamName) {
		super();
		this.streamName = streamName;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

}
