package de.uniol.inf.is.odysseus.ruleengine.system;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlow;

public abstract class AbstractWorkingEnvironment<T> implements IWorkingEnvironment<T> {
	
	private T configuration = null;
	private WorkingMemory workingMemory;
	private IRuleFlow ruleFlow;
	
	public AbstractWorkingEnvironment(T config, IRuleFlow ruleflow){
		this.configuration = config;
		this.ruleFlow = ruleflow;
		this.workingMemory = new WorkingMemory(this);
	}
	
	@Override
	public T getConfiguration(){
		return this.configuration;
	}

	public void processEnvironment() {
		this.workingMemory.process();		
	}

	@Override
	public WorkingMemory getWorkingMemory() {
		return this.workingMemory;
	}
	
	@Override
	public IRuleFlow getRuleFlow() {
		return this.ruleFlow;
	}
}
