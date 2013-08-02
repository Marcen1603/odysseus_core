package de.uniol.inf.is.odysseus.rcp.dashboard;

import org.eclipse.swt.widgets.Composite;

public interface IDashboardPartConfigurer<T extends IDashboardPart> {

	public void init( T dashboardPartToConfigure );
	
	public void createPartControl( Composite parent );
	
	public void dispose();
	
}
