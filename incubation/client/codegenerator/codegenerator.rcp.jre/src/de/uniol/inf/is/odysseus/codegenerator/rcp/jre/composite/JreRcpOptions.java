package de.uniol.inf.is.odysseus.codegenerator.rcp.jre.composite;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.codegenerator.rcp.modell.AbstractRcpOptionComposite;
import de.uniol.inf.is.odysseus.codegenerator.rcp.registry.IRcpSpecialOption;


public class JreRcpOptions implements IRcpSpecialOption{

	String targetPlatform = "JRE";
	
	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	/**
	 * generate a new instanz of the JreRcpComposite 
	 */
	@Override
	public AbstractRcpOptionComposite getComposite(Composite parent,int style) {
		return new JreRcpComposite(parent,style);
	}
	
}
