package de.uniol.inf.is.odysseus.codegenerator.rcp.registry;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.codegenerator.rcp.model.AbstractRcpOptionComposite;



public interface IRcpSpecialOption {

	public String getTargetPlatform();
	public AbstractRcpOptionComposite getComposite(Composite parent,int style);
	
}
