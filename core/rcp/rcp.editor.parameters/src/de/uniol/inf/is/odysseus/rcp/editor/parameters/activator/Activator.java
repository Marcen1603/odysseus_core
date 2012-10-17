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
package de.uniol.inf.is.odysseus.rcp.editor.parameters.activator;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.ImageManager;

public class Activator extends AbstractUIPlugin {

	private static ImageManager imageManager;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		imageManager = new ImageManager(context.getBundle());
		imageManager.register("addIcon", "icons/add.gif");
		imageManager.register("removeIcon", "icons/remove.gif");
		imageManager.register("upIcon", "icons/arrow-up.jpg");
		imageManager.register("downIcon", "icons/arrow-down.jpg");
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		imageManager.disposeAll();
	}
	
//	public static ImageDescriptor getImageDescriptor(String path) {
//		return imageDescriptorFromPlugin(IParametersConstants.PLUGIN_ID, path);
//	}
	
	public static ImageManager getImageManager() {
		return imageManager;
	}
}
