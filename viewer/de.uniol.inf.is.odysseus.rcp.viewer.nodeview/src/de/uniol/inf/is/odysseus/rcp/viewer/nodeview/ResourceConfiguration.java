package de.uniol.inf.is.odysseus.rcp.viewer.nodeview;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.resource.IResourceConfiguration;

public class ResourceConfiguration implements IResourceConfiguration {

	private static final Map<String, String> resources = new HashMap<String, String>();
	private final Bundle bundle;
	
	static {
		resources.put("hide", "images/hide.png");
		resources.put("pin", "images/pin.png");
		resources.put("pinned", "images/pinned.png");
		resources.put("select", "images/select.png");
		resources.put("show", "images/show.png");
		resources.put("metaNew", "images/metaNew.png");
		resources.put("metaRemove", "images/metaRemove.png");
		resources.put("metaReset", "images/metaReset.png");
		resources.put("metaReload", "images/metaReload.png");
	}
	
	public ResourceConfiguration( Bundle bundle ) {
		this.bundle = bundle;
	}
	
	@Override
	public Map<String, String> getResources() {
		return resources;
	}

	@Override
	public Bundle getBundle() {
		return bundle;
	}

}
