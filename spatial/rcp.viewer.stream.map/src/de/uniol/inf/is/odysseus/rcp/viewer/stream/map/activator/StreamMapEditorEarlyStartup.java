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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWTError;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class StreamMapEditorEarlyStartup implements IStartup {
	
	private static final Logger LOG = LoggerFactory.getLogger(StreamMapEditorEarlyStartup.class);
	
	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					Bundle bundle = ViewerStreamMapPlugIn.getDefault().getBundle();
					ImageRegistry imageRegistry = ViewerStreamMapPlugIn.getDefault().getImageRegistry();
					imageRegistry.put("blank_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/blank_16.png")));
					imageRegistry.put("layers_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/layers_16.png")));
					imageRegistry.put("layers_plus_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/layers_plus_16.png")));
					imageRegistry.put("magnifier_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_16.png")));
					imageRegistry.put("magnifier_move_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_move_16.png")));
					imageRegistry.put("magnifier_move_de_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_move_de_16.png")));
					imageRegistry.put("magnifier_zoom_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_plus_minus_16.png")));
					imageRegistry.put("magnifier_rectangle_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_rectangle_16.png")));
					imageRegistry.put("magnifier_zoom_de_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_plus_minus_de_16.png")));
					imageRegistry.put("magnifier_rectangle_de_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_rectangle_de_16.png")));
					imageRegistry.put("filter_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/filter_16.png")));
					imageRegistry.put("dummy_16", ImageDescriptor.createFromURL(bundle.getEntry("icons/dummy_16.png")));
					
					imageRegistry.put("blank_32", ImageDescriptor.createFromURL(bundle.getEntry("icons/blank_32.png")));
					imageRegistry.put("layers", ImageDescriptor.createFromURL(bundle.getEntry("icons/layers.png")));
					imageRegistry.put("layers_plus", ImageDescriptor.createFromURL(bundle.getEntry("icons/layers_plus.png")));
					imageRegistry.put("magnifier", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier.png")));
					imageRegistry.put("magnifier_move", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_move.png")));
					imageRegistry.put("magnifier_move_de", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_move_de.png")));
					imageRegistry.put("magnifier_zoom", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_plus_minus.png")));
					imageRegistry.put("magnifier_rectangle", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_rectangle.png")));
					imageRegistry.put("magnifier_zoom_de", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_plus_minus_de.png")));
					imageRegistry.put("magnifier_rectangle_de", ImageDescriptor.createFromURL(bundle.getEntry("icons/magnifier_rectangle_de.png")));
					imageRegistry.put("filter", ImageDescriptor.createFromURL(bundle.getEntry("icons/filter.png")));
					imageRegistry.put("dummy", ImageDescriptor.createFromURL(bundle.getEntry("icons/dummy.png")));
				} catch (SWTError e) {
					LOG.error(e.getMessage());
				}
			}

		});

	}

}
