package de.uniol.inf.is.odysseus.query.codegenerator.rcp.registry;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.query.codegenerator.rcp.modell.AbstractRCPOptionComposite;



public interface IRCPSpecialOption {

	public String getTargetPlatform();
	public AbstractRCPOptionComposite getComposite(Composite parent,int style);
	
}
