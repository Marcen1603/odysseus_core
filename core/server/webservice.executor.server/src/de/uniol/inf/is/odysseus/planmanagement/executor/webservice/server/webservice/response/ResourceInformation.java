package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import de.uniol.inf.is.odysseus.core.collection.Resource;

public class ResourceInformation {
	
	private String resourceName;
	private String user;
	
	public ResourceInformation(){
			
	}
	
	public ResourceInformation(Resource resource){
		this.resourceName = resource.getResourceName();
		this.user = resource.getUser();
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
