package de.uniol.inf.is.odysseus.sports.rest.dto.response;

import de.uniol.inf.is.odysseus.rest.dto.response.AbstractResponseDTO;
import de.uniol.inf.is.odysseus.rest.socket.SocketInfo;




public class ExecuteSportsQLResponseDTO extends AbstractResponseDTO {

	private String token;
	private SocketInfo socketInfo;
	private int queryId;
	
	
	public ExecuteSportsQLResponseDTO() {

	}
		
	public ExecuteSportsQLResponseDTO(String token, SocketInfo socketInfo, int queryId) {
		this.token = token;
		this.socketInfo = socketInfo;
		this.queryId = queryId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public SocketInfo getSocketInfo() {
		return socketInfo;
	}

	public void setSocketInfo(SocketInfo socketInfo) {
		this.socketInfo = socketInfo;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}


	
	
	
	
	
	
}
