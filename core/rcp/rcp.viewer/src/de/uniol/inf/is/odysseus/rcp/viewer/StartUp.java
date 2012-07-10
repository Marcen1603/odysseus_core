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
package de.uniol.inf.is.odysseus.rcp.viewer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWTError;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.rcp.viewer.swt.resource.XMLResourceLoader;

public class StartUp implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					Bundle bundle = OdysseusRCPViewerPlugIn.getDefault().getBundle();
					ImageRegistry imageRegistry = OdysseusRCPViewerPlugIn.getDefault().getImageRegistry();
					imageRegistry.put("stopStream", ImageDescriptor.createFromURL(bundle.getEntry("Icons/control-stop.png")));
					imageRegistry.put("startStream", ImageDescriptor.createFromURL(bundle.getEntry("Icons/control.png")));
				} catch (SWTError e) {
					// Ignore :-)
				}
			}

		});


		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				Bundle bundle = OdysseusRCPViewerPlugIn.getDefault().getBundle();
				XMLResourceLoader.loadImages(
						bundle.getEntry("viewer_cfg/resources.xml"), 
						bundle.getEntry("viewer_cfg/resourcesSchema.xsd"),
						OdysseusRCPViewerPlugIn.getDefault().getImageRegistry());
			}
			
		});
		
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try{
				Bundle bundle = OdysseusRCPViewerPlugIn.getDefault().getBundle();
				ImageRegistry imageRegistry = OdysseusRCPViewerPlugIn.getDefault().getImageRegistry();
				imageRegistry.put("metadata", ImageDescriptor.createFromURL(bundle.getEntry("icons/metadata.gif")));
				imageRegistry.put("attribute", ImageDescriptor.createFromURL(bundle.getEntry("icons/status.png")));
				imageRegistry.put("predicate", ImageDescriptor.createFromURL(bundle.getEntry("icons/status.png")));
				imageRegistry.put("schema", ImageDescriptor.createFromURL(bundle.getEntry("icons/odata.png")));
				imageRegistry.put("pipe_icon", ImageDescriptor.createFromURL(bundle.getEntry("icons/task.png")));
				imageRegistry.put("sink_icon", ImageDescriptor.createFromURL(bundle.getEntry("icons/monitor.png")));
				imageRegistry.put("source_icon", ImageDescriptor.createFromURL(bundle.getEntry("icons/application-import.png")));
				imageRegistry.put("partof_icon", ImageDescriptor.createFromURL(bundle.getEntry("icons/graph.jpg")));
				imageRegistry.put("subscription", ImageDescriptor.createFromURL(bundle.getEntry("icons/arrow-000-small.png")));
				}catch(SWTError e){
					// Ignore :-)
				}
			}
			
		});
	}

}
