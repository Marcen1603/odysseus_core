package de.uniol.inf.is.odysseus.ruleengine.system;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlow;

public interface IWorkingEnvironment<T> {
	public T getConfiguration();
	public IRuleFlow getRuleFlow();
	public WorkingMemory getWorkingMemory();
}
