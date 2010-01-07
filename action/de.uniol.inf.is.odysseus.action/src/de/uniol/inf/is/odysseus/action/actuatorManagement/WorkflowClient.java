package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.util.List;

import org.apache.cxf.endpoint.Client;

import de.uniol.inf.is.odysseus.action.output.ActionMethod;
import de.uniol.inf.is.odysseus.action.output.IActuator;


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
	public List<ActionMethod> getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

}
