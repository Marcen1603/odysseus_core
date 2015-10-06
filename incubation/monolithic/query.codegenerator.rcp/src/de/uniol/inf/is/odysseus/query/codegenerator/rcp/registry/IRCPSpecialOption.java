package de.uniol.inf.is.odysseus.query.codegenerator.rcp.registry;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.query.codegenerator.rcp.modell.AbstractRcpOptionComposite;



public interface IRcpSpecialOption {

	public String getTargetPlatform();
	public AbstractRcpOptionComposite getComposite(Composite parent,int style);
	
}
