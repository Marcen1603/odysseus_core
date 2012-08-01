package de.uniol.inf.is.soop.control.workflow;


public class WorkflowProcessInstance {

	private String pid;
	
	private String endpoint;
	
	public WorkflowProcessInstance(String pid){
		setPid(pid);
	}
	
	public WorkflowProcessInstance(){}

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
	
}
