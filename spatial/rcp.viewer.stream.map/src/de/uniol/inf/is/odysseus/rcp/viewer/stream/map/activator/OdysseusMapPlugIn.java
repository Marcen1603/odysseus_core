/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerTypeRegistry;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;


/**
 *
 * @author Stephan Jansen 
 * @author Kai Pancratz
 * 
 * 
 */
public class OdysseusMapPlugIn extends AbstractUIPlugin {

	// The plug-in ID
	public static final String ODYSSEUS_MAP_PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.stream.map.MapEditor";
	public static final String ODYSSEYS_MAP_EXTENSION = "map";

	private static OdysseusMapPlugIn plugin;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		LayerTypeRegistry.register(new VectorLayer(new LayerConfiguration(null)));
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		
		ColorManager.getInstance().dispose();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return shared instance
	 */
	public static OdysseusMapPlugIn getDefault() {
		return plugin;
	}

	public void log(Throwable e) {
		IStatus status = new Status(IStatus.ERROR, ODYSSEUS_MAP_PLUGIN_ID, -1, "Exception", e);
		getLog().log(status);
	}
	
	
}
