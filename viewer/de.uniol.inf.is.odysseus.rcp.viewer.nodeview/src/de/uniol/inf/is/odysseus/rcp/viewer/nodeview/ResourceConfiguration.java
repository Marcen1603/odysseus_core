package de.uniol.inf.is.odysseus.rcp.viewer.nodeview;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.rcp.resource.IResourceConfiguration;

public class ResourceConfiguration implements IResourceConfiguration {

	private final Map<String, URL> resources = new HashMap<String, URL>();
	
	public ResourceConfiguration( Bundle bundle ) {
		resources.put("hide", bundle.getEntry("images/hide.png"));
		resources.put("pin", bundle.getEntry("images/pin.png"));
		resources.put("pinned", bundle.getEntry("images/pinned.png"));
		resources.put("select", bundle.getEntry("images/select.png"));
		resources.put("show", bundle.getEntry("images/show.png"));
		resources.put("metaNew", bundle.getEntry("images/metaNew.png"));
		resources.put("metaRemove", bundle.getEntry("images/metaRemove.png"));
		resources.put("metaReset", bundle.getEntry("images/metaReset.png"));
		resources.put("metaReload", bundle.getEntry("images/metaReload.png"));
	}
	
	@Override
	public Map<String, URL> getResources() {
		return resources;
	}

}
