package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.resource;

import java.util.Map;

import org.osgi.framework.Bundle;


public interface IResourceConfiguration {

	public Map<String,String> getResources();
	public Bundle getBundle();
}
