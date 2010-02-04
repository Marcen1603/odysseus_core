package de.uniol.inf.is.odysseus.action.services.actuator;

import java.util.List;

import org.apache.cxf.endpoint.Client;



public class WorkflowClient implements IActuator {
	private Client client;
	
	public WorkflowClient(Client client){
		this.client = client;
	}

	@Override
	public void executeMethod(String method, Class<?>[] types, Object[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ActionMethod> getFullSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionMethod> getReducedSchema() {
		// TODO Auto-generated method stub
		return null;
	}

}
