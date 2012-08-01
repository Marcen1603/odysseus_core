package de.uniol.inf.is.soop.control.workflow;

import java.util.HashMap;

public interface IWorkflowEngineAdapter {
	public HashMap<String, WorkflowProcess> listProcesses();
}
