package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

public interface INodeContent {
	
	public Collection<IParam<?>> getConstructParameterList();
	public Collection<IParam<?>> getSetterParameterList();
	public String getName();
	public String getTyp();
	
	public void addNodeContentChangeListener(INodeContentChangeListener<INodeContent> listener);
	public void notifyNodeContentChangeListener();
	
	public boolean isSource();
	public boolean isSink();
	public boolean isPipe();

}