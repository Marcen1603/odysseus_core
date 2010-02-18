package de.uniol.inf.is.odysseus.action.services.actuator.workflow;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorManager;
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
	private DynamicClientFactory factory;
	private Map<String, WorkflowClient> clients;
	private Map<String, Client> clientCache;
	
	private static Pattern descriptionPattern = Pattern.compile("([^;]+);?(.*)?"); 
	
	public WorkflowClientManager(){
		this.factory = JaxWsDynamicClientFactory.newInstance();
		this.clients = new HashMap<String, WorkflowClient>();
		this.clientCache = new HashMap<String, Client>();
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
					//try to fetch client from cache
					Client wsClient = this.clientCache.get(uriString);
					if (wsClient == null){
						URL url = new URL(uriString);
						wsClient = factory.createClient(url);
						this.clientCache.put(uriString, wsClient);
					}
					
					if (correlation != null && correlation.length() > 1){
						client = new WorkflowClient(wsClient, correlation);
					}else {
						client = new WorkflowClient(wsClient);
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
	public IActuator getActuator(String name) throws ActuatorException {
		IActuator client = this.clients.get(name);
		if (client != null){
			return client;
		}
		throw new ActuatorException("Client <"+name+"> does not exist");
	}


	@Override
	public String getName() {
		return "WorkflowClientManager";
	}


	@Override
	public List<String> getRegisteredActuatorNames() {
		return new ArrayList<String>(this.clients.keySet());
	}


	@Override
	public IActuator removeActuator(String name) throws ActuatorException{
		WorkflowClient client = this.clients.remove(name);
		if (client == null){
			throw new ActuatorException(this.getName()+": Actuator <"+name+"> does not exist");
		}
		return client;
	}
}
