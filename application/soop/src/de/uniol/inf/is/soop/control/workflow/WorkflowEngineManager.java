/**
 * 
 */
package de.uniol.inf.is.soop.control.workflow;

import java.util.HashMap;

/**
 * @author jbrode
 *
 */
public class WorkflowEngineManager {
	
	private static WorkflowEngineManager manager = null;
	
	private HashMap<String, WorkflowEngine> workflowEngines = new HashMap<String, WorkflowEngine>();
	
	/**
	 * Singelton Implementation of WorkflowEngineManager
	 * @return WorkflowEngineManager
	 */
	public static WorkflowEngineManager getInstance(){
		if(manager  == null ) {
			manager = new WorkflowEngineManager();
			
			//prototype first instance object
			WorkflowEngine wfe = new WorkflowEngine("localODE", "http://localhost:8080/ode/processes/ProcessManagement/");
			wfe.setUsername("System");
			wfe.setPassword("manager");
			wfe.setAdapter(new ODEWorkflowEngineAdapter(wfe.getEndpoint()));
			
			// prototype first process item due to API insuficiences
			String endpointUrl = "http://localhost:8080/ode/processes/SoopMissions/StepOver/StepOverProcess/Control";
			WorkflowProcess pi = new WorkflowProcess("stepOverProcess", endpointUrl, "Übersteige-Prozess");
			pi.setAdapter(new ODEWorkflowProcessAdapter(endpointUrl));
			wfe.addProcess(pi);
			
			manager.workflowEngines.put(wfe.getId(),wfe);
			
		}	
		return manager;
	}
	
	/**
	 * permit external Constructor Calls
	 */
	private WorkflowEngineManager() {}

	public HashMap<String, WorkflowEngine> getWorkflowEngineInstances() {
		return workflowEngines;
	}

	public void setWorkflowEngines(
			HashMap<String, WorkflowEngine> workflowEngineInstances) {
		this.workflowEngines = workflowEngineInstances;
	}
	
	public void addWorkflowEngine(String id, WorkflowEngine workflowEngine) {
		workflowEngines.put(id, workflowEngine);
	}
	
	public void removeWorkflowEngine(String id) {
		workflowEngines.remove(id);
	}
	
	public WorkflowEngine getWorkflowEngine(String id){
		return workflowEngines.get(id);
	}
	
	
	
}
