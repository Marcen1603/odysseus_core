package de.uniol.inf.is.odysseus.rest.dto.request;



public class LoginRequestDTO {


	private String username;
	
	private String password;
	
	private String tenant;
	
	public LoginRequestDTO() {

	}
	public LoginRequestDTO(String username,String password, String tenant) {
		this.username = username;
		this.password = password;
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

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	
	
	
}
