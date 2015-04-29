package de.uniol.inf.is.odysseus.condition.conditionql.parser;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.condition.conditionql.parser.enums.RequestMessageType;

public class ConditionQLQuery implements Serializable {
	
	private static final long serialVersionUID = 5321742341337603519L;
	
	private RequestMessageType msgType;
	private AlgorithmQueryInfo algorithm;
	private StreamQueryInfo stream;
	
	public ConditionQLQuery() {
		
	}
	
	public ConditionQLQuery(RequestMessageType msgType, AlgorithmQueryInfo algorithm, StreamQueryInfo stream) {
		super();
		this.msgType = msgType;
		this.algorithm = algorithm;
		this.stream = stream;
	}
	
	public RequestMessageType getMsgType() {
		return msgType;
	}
	public void setMsgType(RequestMessageType msgType) {
		this.msgType = msgType;
	}

	public AlgorithmQueryInfo getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(AlgorithmQueryInfo algorithm) {
		this.algorithm = algorithm;
	}

	public StreamQueryInfo getStream() {
		return stream;
	}

	public void setStream(StreamQueryInfo stream) {
		this.stream = stream;
	}
	
}
