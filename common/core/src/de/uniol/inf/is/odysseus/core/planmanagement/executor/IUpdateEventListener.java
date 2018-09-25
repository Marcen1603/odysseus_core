package de.uniol.inf.is.odysseus.core.planmanagement.executor;

public interface IUpdateEventListener {
	
	static final String SESSION = "SESSION";
	static final String DATADICTIONARY = "DATADICTIONARY";
	static final String USER = "USER";
	static final String QUERY = "QUERY";
	static final String SCHEDULING = "SCHEDULING";
	
	void eventOccured(String type);

}
