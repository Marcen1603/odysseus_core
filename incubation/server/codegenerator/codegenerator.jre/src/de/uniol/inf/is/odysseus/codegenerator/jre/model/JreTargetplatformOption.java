package de.uniol.inf.is.odysseus.codegenerator.jre.model;

import de.uniol.inf.is.odysseus.codegenerator.modell.AbstractTargetPlatformOption;

public class JreTargetplatformOption extends AbstractTargetPlatformOption{
	
	private boolean autobuild;
	
	public boolean isAutobuild() {
		return autobuild;
	}

	public void setAutobuild(boolean autobuild) {
		this.autobuild = autobuild;
	}
	

}
