package de.uniol.inf.is.odysseus.query.codegenerator.jre.model;

import de.uniol.inf.is.odysseus.query.codegenerator.modell.TargetPlatformOption;

public class JreTargetplatformOption extends TargetPlatformOption{
	
	private boolean autobuild;
	
	public boolean isAutobuild() {
		return autobuild;
	}

	public void setAutobuild(boolean autobuild) {
		this.autobuild = autobuild;
	}
	

}