package de.uniol.inf.is.odysseus.action.output;

import org.apache.cxf.endpoint.Client;

public class WorkflowClient implements IAction {
	private Client client;
	
	public WorkflowClient(Client client){
		this.client = client;
	}

	@Override
	public void executeMethod(String methodName, Object... parameters) {
		// TODO Auto-generated method stub
		
	}

}
