package de.uniol.inf.is.soop.control.workflow;

import java.util.HashMap;

public class WorkflowEngine {
	
	private String id;
	
	private String endpoint;
	
	private String username;
	
	private String password;
	
	private IWorkflowEngineAdapter adapter;
	
	HashMap<String, WorkflowProcess> processes = new HashMap<String, WorkflowProcess>();

	public WorkflowEngine(String id, String endpoint){
		setId(id);
		setEndpoint(endpoint);
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the processes
	 */
	public HashMap<String, WorkflowProcess> getProcesses() {
		if(processes.size() < 1) {
			processes = getAdapter().listProcesses();
		}
		return processes;
	}

	/**
	 * @param processes the processes to set
	 */
	public void setProcesses(HashMap<String, WorkflowProcess> processes) {
		this.processes = processes;
	}
	
	/**
	 * @param processes the processes to set
	 */
	public void addProcess(WorkflowProcess p) {
		this.processes.put(p.getPid(), p);
	}
	
	
	public WorkflowProcess getWorkflowProcess(String id){
		return processes.get(id);
	}

	/**
	 * @return the endpoint
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * @return the adapter
	 */
	public IWorkflowEngineAdapter getAdapter() {
		return adapter;
	}

	/**
	 * @param adapter the adapter to set
	 */
	public void setAdapter(IWorkflowEngineAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
}
