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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.table.activator;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.ImageManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class ViewerStreamTablePlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.stream.table"; //$NON-NLS-1$

	private static ImageManager imageManager;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		imageManager = new ImageManager(context.getBundle());
		imageManager.register("filter", "icons/filter.png");
		imageManager.register("desync", "icons/desync.png");
		imageManager.register("metadata", "icons/metadata.gif");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		imageManager.disposeAll();
	}
	
	public static ImageManager getImageManager() {
		return imageManager;
	}
}
