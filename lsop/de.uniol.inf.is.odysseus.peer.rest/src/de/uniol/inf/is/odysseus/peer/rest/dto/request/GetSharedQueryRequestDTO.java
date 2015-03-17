package de.uniol.inf.is.odysseus.peer.rest.dto.request;

import de.uniol.inf.is.odysseus.rest.dto.request.AbstractSessionRequestDTO;

public class GetSharedQueryRequestDTO extends AbstractSessionRequestDTO{
	private String sharedQueryId;
	private String username;
	private String password;
	private String tenant;
	private boolean searchAllPeers;

	public GetSharedQueryRequestDTO() {
		
	}

	
	public String getSharedQueryId() {
		return sharedQueryId;
	}

	public void setSharedQueryId(String sharedQueryId) {
		this.sharedQueryId = sharedQueryId;
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


	public String getTenant() {
		return tenant;
	}


	public void setTenant(String tenant) {
		this.tenant = tenant;
	}


	public boolean isSearchAllPeers() {
		return searchAllPeers;
	}


	public void setSearchAllPeers(boolean searchAllPeers) {
		this.searchAllPeers = searchAllPeers;
	}



	
	
	
	
}
