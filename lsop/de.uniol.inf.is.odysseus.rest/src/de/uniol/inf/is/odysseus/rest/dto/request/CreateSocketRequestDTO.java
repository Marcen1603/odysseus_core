package de.uniol.inf.is.odysseus.rest.dto.request;




public class CreateSocketRequestDTO extends AbstractSessionRequestDTO {


	private int rootPort;
	
	private int queryId;
	
	
	public CreateSocketRequestDTO() {

	}
	
	public CreateSocketRequestDTO(String token, int queryId, int rootPort) {
		super(token);
		this.queryId = queryId;
		this.rootPort = rootPort;
	}

	public int getRootPort() {
		return rootPort;
	}

	public void setRootPort(int rootPort) {
		this.rootPort = rootPort;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	
	
	
	
	
	
	
}
