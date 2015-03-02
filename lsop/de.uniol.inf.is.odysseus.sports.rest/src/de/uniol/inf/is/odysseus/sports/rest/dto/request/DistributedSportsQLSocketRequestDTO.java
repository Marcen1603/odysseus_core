package de.uniol.inf.is.odysseus.sports.rest.dto.request;

import de.uniol.inf.is.odysseus.rest.dto.request.AbstractRequestDTO;




public class DistributedSportsQLSocketRequestDTO extends AbstractRequestDTO {

	private String sharedQueryId;
	private String username;
	private String password;
	private String clientAddress; // For backup-information only
	private boolean startQuery;
	
	public DistributedSportsQLSocketRequestDTO() {

	}
		
	public DistributedSportsQLSocketRequestDTO(String sharedQueryId, String username, String password, String clientAddress, boolean startQuery) {
		this.username = username;
		this.password = password;
		this.sharedQueryId = sharedQueryId;
		this.clientAddress = clientAddress;
		this.startQuery = startQuery;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(String sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAdress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public boolean isStartQuery() {
		return startQuery;
	}

	public void setStartQuery(boolean startQuery) {
		this.startQuery = startQuery;
	}
	
	
	
}
