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
package de.uniol.inf.is.odysseus.rcp.dashboard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWTError;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

public class StartUp implements IStartup {

	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					Bundle bundle = DashboardPlugIn.getDefault().getBundle();
					ImageRegistry imageRegistry = DashboardPlugIn.getDefault().getImageRegistry();
					imageRegistry.put("dashboardPart", ImageDescriptor.createFromURL(bundle.getEntry("icons/dashboardPart.jpg")));
					imageRegistry.put("dashboard", ImageDescriptor.createFromURL(bundle.getEntry("icons/dashboard.jpg")));
				} catch (SWTError e) {
				}
			}

		});
	}

}
