package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

public interface INodeContent {
	
	public Collection<IParamConstruct<?>> getConstructParameterList();
	public Collection<IParamSetter<?>> getSetterParameterList();
	public String getName();
	public String getTyp();
	
	public void addNodeContentChangeListener(INodeContentChangeListener<INodeContent> listener);
	public void notifyNodeContentChangeListener();
	
	public boolean isSource();
	public boolean isSink();
	public boolean isPipe();

}