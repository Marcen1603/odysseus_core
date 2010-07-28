package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import org.eclipse.core.runtime.IConfigurationElement;

public class StreamExtensionDefinition {

	private String id;
	private IConfigurationElement configElement;
	
	public StreamExtensionDefinition() {}
	
	public void setID( String id ) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}

	public IConfigurationElement getConfigElement() {
		return configElement;
	}

	public void setConfigElement(IConfigurationElement configElement) {
		this.configElement = configElement;
	}
	
	
}
