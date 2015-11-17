package de.uniol.inf.is.odysseus.codegenerator.jre.model;

import de.uniol.inf.is.odysseus.codegenerator.model.AbstractTargetPlatformOption;
/**
 * this class represented the model for the 
 * special options for the jre targetPlatform
 * 
 * @author MarcPreuschaft
 *
 */
public class JreTargetplatformOption extends AbstractTargetPlatformOption{
	
	//autobild option
	private boolean autobuild;
	
	public boolean isAutobuild() {
		return autobuild;
	}

	public void setAutobuild(boolean autobuild) {
		this.autobuild = autobuild;
	}
	

}
