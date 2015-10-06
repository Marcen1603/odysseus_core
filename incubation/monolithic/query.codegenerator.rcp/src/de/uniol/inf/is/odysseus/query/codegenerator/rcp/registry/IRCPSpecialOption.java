package de.uniol.inf.is.odysseus.query.codegenerator.rcp.registry;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.query.codegenerator.rcp.modell.ICRCPOptionComposite;



public interface IRCPSpecialOption {

	public String getTargetPlatform();
	
	
	public ICRCPOptionComposite getComposite(Composite parent,int style);
	
	


	
}
