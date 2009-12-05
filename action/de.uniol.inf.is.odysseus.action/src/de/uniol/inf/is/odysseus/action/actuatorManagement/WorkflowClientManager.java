package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.net.URL;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import de.uniol.inf.is.odysseus.action.output.WorkflowClient;

public class WorkflowClientManager {
	private JaxWsDynamicClientFactory factory;

	private static WorkflowClientManager instance = new WorkflowClientManager();
	
	
	private WorkflowClientManager(){
		this.factory = JaxWsDynamicClientFactory.newInstance();
	}

	public WorkflowClient createWorkflowClient (URL wsdlUrl) {
		return new WorkflowClient(factory.createClient(wsdlUrl));
	}
	
	public static WorkflowClientManager getInstance() {
		return instance;
	}


}
