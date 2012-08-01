/**
 * 
 */
package de.uniol.inf.is.soop.control.dsms;

import java.util.HashMap;

/**
 * @author jbrode
 *
 */
public class DatastreamManagementSystemManager {

private static DatastreamManagementSystemManager manager = null;
	
	HashMap<String, DatastreamManagementSystem> datastreamManagementSystemInstances = new HashMap<String, DatastreamManagementSystem>();
	
	/**
	 * Singelton Implementation of WorkflowEngineManager
	 * @return
	 */
	public static DatastreamManagementSystemManager getInstance(){
		if(manager  == null ) {
			manager = new DatastreamManagementSystemManager();
			
			//prototype first instance object
			DatastreamManagementSystem dsms = new DatastreamManagementSystem();
			dsms.setId("localOdysseus");
			dsms.setWebserviceUrl("http://localhost:9669/odysseus");
			dsms.setUsername("System");
			dsms.setPassword("manager");
			
			
			manager.datastreamManagementSystemInstances.put(dsms.getId(),dsms);
		}	
		return manager;
	}
	
	/**
	 * permit external Constructor Calls
	 */
	private DatastreamManagementSystemManager() {}

	public HashMap<String, DatastreamManagementSystem> getDatastreamManagementSystemInstances() {
		return datastreamManagementSystemInstances;
	}

	public void setDatastreamManagementSystemManagerInstances(
			HashMap<String, DatastreamManagementSystem> workflowEngineInstances) {
		this.datastreamManagementSystemInstances = workflowEngineInstances;
	}
	
	public void addDatastreamManagementSystemInstance(String id, DatastreamManagementSystem datastreamManagementSystemInstance) {
		datastreamManagementSystemInstances.put(id, datastreamManagementSystemInstance);
	}
	
	public DatastreamManagementSystem getDatastreamManagementSystemInstance(String id){
		return datastreamManagementSystemInstances.get(id);
	}
	
}
