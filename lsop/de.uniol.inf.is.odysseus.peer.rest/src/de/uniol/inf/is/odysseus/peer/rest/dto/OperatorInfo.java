package de.uniol.inf.is.odysseus.peer.rest.dto;

import java.util.Map;

public class OperatorInfo {
	private Map<Integer, Integer> childOperators;
	private String name;
	private Map<String, String> schemaMap;
	private boolean sink;
	private boolean pipe;
	private boolean source;
	private int hashCode;
	private String className;
	private Map<String, String> parameters;
	private String destinationName;
	private String senderPeerId;
	private String receiverPeerId;
	private String pipeID;
	
	public OperatorInfo() {
		
	}

	public String getSenderPeerId() {
		return senderPeerId;
	}



	public void setSenderPeerId(String senderPeerId) {
		this.senderPeerId = senderPeerId;
	}



	public String getReceiverPeerId() {
		return receiverPeerId;
	}



	public void setReceiverPeerId(String receiverPeerId) {
		this.receiverPeerId = receiverPeerId;
	}





	public Map<Integer, Integer> getChildOperators() {
		return childOperators;
	}



	public void setChildOperators(Map<Integer, Integer> childOperators) {
		this.childOperators = childOperators;
	}



	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}



	public void setParameterInfos(Map<String, String> parameters) {
		this.parameters = parameters;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public void setHash(int hashCode) {
		this.hashCode = hashCode;
	}



	public void setSource(boolean source) {
		this.source = source;
	}



	public void setPipe(boolean pipe) {
		this.pipe = pipe;
	}



	public void setSink(boolean sink) {
		this.sink = sink;
	}



	public void setOutputSchema(Map<String, String> schemaMap) {
		this.schemaMap = schemaMap;
	}



	public Map<String, String> getSchemaMap() {
		return schemaMap;
	}



	public void setSchemaMap(Map<String, String> schemaMap) {
		this.schemaMap = schemaMap;
	}



	public int getHashCode() {
		return hashCode;
	}



	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}



	public Map<String, String> getParameters() {
		return parameters;
	}



	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}



	public boolean isSink() {
		return sink;
	}



	public boolean isPipe() {
		return pipe;
	}



	public boolean isSource() {
		return source;
	}



	public String getClassName() {
		return className;
	}



	public String getDestinationName() {
		return destinationName;
	}

	public String getPipeID() {
		return pipeID;
	}

	public void setPipeID(String pipeID) {
		this.pipeID = pipeID;
	}

	
	
	
	
	
}
