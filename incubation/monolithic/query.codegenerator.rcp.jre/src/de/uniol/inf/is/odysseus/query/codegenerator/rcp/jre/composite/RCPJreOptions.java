package de.uniol.inf.is.odysseus.query.codegenerator.rcp.jre.composite;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.query.codegenerator.rcp.modell.ICRCPOptionComposite;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.registry.IRCPSpecialOption;


public class RCPJreOptions implements IRCPSpecialOption{

	String targetPlatform = "JRE";
	
	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	@Override
	public ICRCPOptionComposite getComposite(Composite parent,int style) {
		return new JreRCPComposite(parent,style);
	}
	
}
