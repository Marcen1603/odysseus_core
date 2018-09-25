package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public interface IOperatorGeneralDetailProvider {
	
	public void createPartControl( Composite parent, IPhysicalOperator operator);
	
	public String getTitle();
	
	public void dispose();

}
