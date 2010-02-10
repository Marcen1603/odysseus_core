package de.uniol.inf.is.odysseus.action.services.actuator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * ActuatorManager for WorkflowClients
 * Descriptions have following syntax:
 * URL[; coorelationID]
 * The URL must point to a WSDL, the correlationID is optional for BPEL
 * processes that dont rely on correlations
 * WARNING requires JDK 6 Update 18! 
 * @author Simon Flandergan
 *
 */
public class WorkflowClientManager implements IActuatorManager {
	private JaxWsDynamicClientFactory factory;
	private HashMap<String, WorkflowClient> clients;
	
	private static Pattern descriptionPattern = Pattern.compile("([^;]+);?(.*)?"); 
	
	public WorkflowClientManager(){
		this.factory = JaxWsDynamicClientFactory.newInstance();
		this.clients = new HashMap<String, WorkflowClient>();
	}


	@Override
	public IActuator createActuator(String name, String description) throws ActuatorException{
		Matcher matcher = descriptionPattern.matcher(description);
		if (matcher.matches()){
			matcher.reset();
			if (matcher.find()){
				String uriString = matcher.group(1).trim();	
				String correlation = matcher.group(2).trim();
				WorkflowClient client = this.clients.get(name);
				if (client != null){
					throw new ActuatorException("Actuator with name: "+name+" is already registered");
				}
				try {
					URL url = new URL(uriString);
					if (correlation != null && correlation.length() > 1){
						client = new WorkflowClient(factory.createClient(url), correlation);
					}else {
						client = new WorkflowClient(factory.createClient(url));
					}
					this.clients.put(name, client);		
					return client;
				} catch (MalformedURLException e) {
					throw new ActuatorException(e.getMessage());
				}
			}
		}
		throw new ActuatorException("Irregular ActuatorDescription");
	}


	@Override
	public String getName() {
		return "WorkflowClientManager";
	}


	@Override
	public IActuator getActuator(String name) throws ActuatorException {
		IActuator client = this.clients.get(name);
		if (client != null){
			return client;
		}
		throw new ActuatorException("Client <"+name+"> does not exist");
	}


	@Override
	public IActuator removeActuator(String name) throws ActuatorException{
		WorkflowClient client = this.clients.remove(name);
		if (client == null){
			throw new ActuatorException(this.getName()+": Actuator <"+name+"> does not exist");
		}
		return client;
	}


	@Override
	public List<String> getRegisteredActuatorNames() {
		return new ArrayList<String>(this.clients.keySet());
	}
}
