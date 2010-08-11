package de.uniol.inf.is.odysseus.transform.rule;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.transform.engine.WorkingMemory;

public interface ITransformRule<T> extends Comparable<ITransformRule<T>>{
	public int getPriority();	
	public void transform(T operator, TransformationConfiguration transformConfig);
	public boolean isExecutable(T operator, TransformationConfiguration transformConfig);
	public String getName();
	public void setCurrentWorkingMemory(WorkingMemory wm);
}
