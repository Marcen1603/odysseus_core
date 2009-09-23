package de.uniol.inf.is.odysseus.visualquerylanguage.model.operators;

import java.util.Collection;

import org.eclipse.swt.graphics.Image;

public interface INodeContent {
	
	public Collection<IParamConstruct<?>> getConstructParameterList();
	public Collection<IParamSetter<?>> getSetterParameterList();
	public String getName();
	public String getType();
	public Image getImage();
	
	public void addNodeContentChangeListener(INodeContentChangeListener<INodeContent> listener);
	public void notifyNodeContentChangeListener();
	
	public boolean isOnlySource();
	public boolean isOnlySink();
	public boolean isPipe();
	
	public Collection<IParamConstruct<?>> getNewConstructParameterListInstance();
	public Collection<IParamSetter<?>> getNewSetterParameterListInstance();

}