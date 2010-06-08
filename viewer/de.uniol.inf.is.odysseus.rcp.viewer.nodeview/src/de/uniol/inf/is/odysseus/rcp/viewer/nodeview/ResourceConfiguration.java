package de.uniol.inf.is.odysseus.rcp.viewer.nodeview;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.rcp.resource.IResourceConfiguration;

public class ResourceConfiguration implements IResourceConfiguration {

	private final Map<String, URL> resources = new HashMap<String, URL>();
	
	public ResourceConfiguration( Bundle bundle ) {		
		resources.put("metadata", bundle.getEntry("images/metadata_icon.gif"));
		resources.put("node", bundle.getEntry("images/node.png"));
	}
	
	@Override
	public Map<String, URL> getResources() {
		return resources;
	}

}
