package de.uniol.inf.is.odysseus.ruleengine.rule;

import de.uniol.inf.is.odysseus.ruleengine.system.WorkingMemory;

public interface IRule<T,U> extends Comparable<IRule<T,U>>{
	public int getPriority();	
	public void execute(T operator, U transformConfig);
	public boolean isExecutable(T operator, U transformConfig);
	public String getName();
	public void setCurrentWorkingMemory(WorkingMemory wm);
}