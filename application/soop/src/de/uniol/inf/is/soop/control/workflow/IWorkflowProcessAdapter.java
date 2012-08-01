package de.uniol.inf.is.soop.control.workflow;

import de.uniol.inf.is.soop.control.dsms.DatastreamManagementSystem;
import de.uniol.inf.is.soop.control.sensorregistry.SensorRegistry;

public interface IWorkflowProcessAdapter {
	public WorkflowProcessInstance startProcess(WorkflowEngine wfe, DatastreamManagementSystem dsms, SensorRegistry sr);
	public String Signal__leave_step(String instanceId);
}
