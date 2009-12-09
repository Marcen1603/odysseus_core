package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import de.uniol.inf.is.odysseus.action.exception.ActuatorCreationException;
import de.uniol.inf.is.odysseus.action.output.WorkflowClient;

/**
 * Singleton Manager for all WorkflowClients
 * @author Simon
 *
 */
public class WorkflowClientManager implements IActuatorManager {
	private JaxWsDynamicClientFactory factory;
	private HashMap<String, WorkflowClient> clients;

	
	public WorkflowClientManager(){
		this.factory = JaxWsDynamicClientFactory.newInstance();
		this.clients = new HashMap<String, WorkflowClient>();
	}

	public IActuator getActuator(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createActuator(String description) throws ActuatorCreationException{
		WorkflowClient client = this.clients.get(description);
		if (client == null){
			try {
				URL url = new URL(description);
				client = new WorkflowClient(factory.createClient(url));
				this.clients.put(description, client);			
			} catch (MalformedURLException e) {
				throw new ActuatorCreationException(e.getMessage());
			} 
		}
	}

	@Override
	public Map<String, List<Class<?>>> getSchema(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getName() {
		return "WorkflowClientManager";
	}


}
