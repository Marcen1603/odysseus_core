package de.uniol.inf.is.soop.control.workflow;

import java.util.HashMap;

public class WorkflowProcess {

	private HashMap<String, WorkflowProcessInstance> instances = new HashMap<String, WorkflowProcessInstance>();
	
	private String pid;
	
	private String name;
	
	private String endpoint;
	
	private IWorkflowProcessAdapter adapter;
	
	public WorkflowProcess(String pid, String endpoint, String name){
		setPid(pid);
		setEndpoint(endpoint);
		setName(name);
	}
	
	public WorkflowProcess(){}
	
	public HashMap<String, WorkflowProcessInstance> getInstances() {
		return instances;
	}

	public void setInstances(HashMap<String, WorkflowProcessInstance> instances) {
		this.instances = instances;
	}
	
	public void addInstance(WorkflowProcessInstance instance) {
		this.instances.put(instance.getPid(), instance);
	}

	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @param pid the pid to set
	 */
	public void setPid(String pid) {
		this.pid = pid;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the adapter
	 */
	public IWorkflowProcessAdapter getAdapter() {
		return adapter;
	}

	/**
	 * @param adapter the adapter to set
	 */
	public void setAdapter(IWorkflowProcessAdapter adapter) {
		this.adapter = adapter;
	}
	
	
	
}
