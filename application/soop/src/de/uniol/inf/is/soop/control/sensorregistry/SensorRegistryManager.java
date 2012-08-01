/**
 * 
 */
package de.uniol.inf.is.soop.control.sensorregistry;

import java.util.HashMap;

import de.uniol.inf.is.soop.control.workflow.ODEWorkflowEngineAdapter;
import de.uniol.inf.is.soop.control.workflow.WorkflowEngine;

/**
 * @author jbrode
 *
 */
public class SensorRegistryManager {

private static SensorRegistryManager manager = null;
	
	HashMap<String, SensorRegistry> sensorRegistryInstances = new HashMap<String, SensorRegistry>();
	
	/**
	 * Singelton Implementation of SensorRegistryManager
	 * @return
	 */
	public static SensorRegistryManager getInstance(){
		if(manager  == null ) {
			manager = new SensorRegistryManager();
			
			//prototype first instance object
			SensorRegistry sr = new SensorRegistry();
			sr.setId("localSensorRegistry");
			sr.setWebserviceUrl("http://localhost:9675/sensor");
			sr.setUsername("System");
			sr.setPassword("manager");
			
			
			manager.sensorRegistryInstances.put(sr.getId(),sr);
		}	
		return manager;
	}
	
	/**
	 * permit external Constructor Calls
	 */
	private SensorRegistryManager() {}

	public HashMap<String, SensorRegistry> getSensorRegistryaddInstances() {
		return sensorRegistryInstances;
	}

	public void setSensorRegistryInstances(
			HashMap<String, SensorRegistry> sensorRegistryInstances) {
		this.sensorRegistryInstances = sensorRegistryInstances;
	}
	
	public void addSensorRegistryInstance(String id, SensorRegistry sensorRegistryInstance) {
		sensorRegistryInstances.put(id, sensorRegistryInstance);
	}
	
	public SensorRegistry getSensorRegistryInstance(String id){
		return sensorRegistryInstances.get(id);
	}
	
}
