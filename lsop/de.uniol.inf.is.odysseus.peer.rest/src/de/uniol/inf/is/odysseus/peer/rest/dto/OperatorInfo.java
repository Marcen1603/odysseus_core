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
	private String pipeId;
	private boolean sender;
	private boolean receiver;
	private String ownerIDs;
	private String peerName;
	
	public OperatorInfo() {
		
	}
	
	


	public String getPipeId() {
		return pipeId;
	}




	public void setPipeId(String pipeId) {
		this.pipeId = pipeId;
	}




	public boolean isSender() {
		return sender;
	}




	public void setSender(boolean sender) {
		this.sender = sender;
	}




	public boolean isReceiver() {
		return receiver;
	}




	public void setReceiver(boolean receiver) {
		this.receiver = receiver;
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




	public void setOwnerIDs(String ownerIDs) {
		this.ownerIDs = ownerIDs;
	}	
	
	public String getOwnerIDs() {
		return this.ownerIDs;
	}




	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}




	public String getPeerName() {
		return peerName;
	}
	
	
	
	
}
