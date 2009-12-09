package de.uniol.inf.is.odysseus.action.output;

import java.util.List;

import org.apache.cxf.endpoint.Client;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorMethod;
import de.uniol.inf.is.odysseus.action.actuatorManagement.IActuator;

public class WorkflowClient implements IActuator {
	private Client client;
	
	public WorkflowClient(Client client){
		this.client = client;
	}

	@Override
	public void executeMethod(String method, Object[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ActuatorMethod> getSchema() {
		// TODO Auto-generated method stub
		return null;
	}

}
