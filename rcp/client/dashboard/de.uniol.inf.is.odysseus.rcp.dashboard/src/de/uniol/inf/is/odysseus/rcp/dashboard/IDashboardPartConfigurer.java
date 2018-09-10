package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public interface IDashboardPartConfigurer<T extends IDashboardPart> {

	public void init( T dashboardPartToConfigure, Collection<IPhysicalOperator> roots );
	
	public void createPartControl( Composite parent );
	
	public void dispose();
	
	public void addListener( IConfigurerListener listener );
	
	public void removeListener( IConfigurerListener listener );
}
