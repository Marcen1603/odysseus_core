package de.uniol.inf.is.odysseus.rest.dto.request;


public abstract class AbstractLoginRequestDTO extends AbstractRequestDTO{
	private String tenant;
	private String username;
	private String password;

	public AbstractLoginRequestDTO() {
		
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
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
	
	
	
	
}
