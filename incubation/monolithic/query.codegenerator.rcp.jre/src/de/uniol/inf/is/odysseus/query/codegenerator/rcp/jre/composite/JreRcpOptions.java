package de.uniol.inf.is.odysseus.query.codegenerator.rcp.jre.composite;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.query.codegenerator.rcp.modell.AbstractRCPOptionComposite;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.registry.IRCPSpecialOption;


public class JreRcpOptions implements IRCPSpecialOption{

	String targetPlatform = "JRE";
	
	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	@Override
	public AbstractRCPOptionComposite getComposite(Composite parent,int style) {
		return new JreRcpComposite(parent,style);
	}
	
}
