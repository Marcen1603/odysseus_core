package de.uniol.inf.is.odysseus.rcp;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public interface IOperatorDetailProvider<T extends IPhysicalOperator> {

	public Collection<Class<? extends T>> getOperatorTypes();
	
	public void createPartControl( Composite parent, T operator);
	
	public String getTitle();
	
	public void dispose();
}
