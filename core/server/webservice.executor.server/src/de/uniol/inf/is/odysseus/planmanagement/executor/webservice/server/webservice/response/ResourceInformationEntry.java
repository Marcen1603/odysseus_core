package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class ResourceInformationEntry {
	
	private ILogicalOperator operator;
	private ResourceInformation resource;
	
	public ResourceInformationEntry(){
		
	}
	
	public ResourceInformationEntry(ResourceInformation ri, ILogicalOperator op){
		this.resource = ri;
		this.operator = op;
	}
	
	public ILogicalOperator getOperator() {
		return operator;
	}
	public void setOperator(ILogicalOperator operator) {
		this.operator = operator;
	}
	public ResourceInformation getResource() {
		return resource;
	}
	public void setResource(ResourceInformation resource) {
		this.resource = resource;
	}

}
