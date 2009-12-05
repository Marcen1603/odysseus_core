package de.uniol.inf.is.odysseus.action.output;

import org.apache.cxf.endpoint.Client;

public class WorkflowClient extends AbstractActuator {
	private Client client;
	
	public WorkflowClient(Client client){
		this.client = client;
	}

}
