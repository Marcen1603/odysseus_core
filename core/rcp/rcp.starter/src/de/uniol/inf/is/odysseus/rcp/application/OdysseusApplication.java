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
package de.uniol.inf.is.odysseus.rcp.application;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.ChooseWorkspaceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class controls all aspects of the application's execution
 */
@SuppressWarnings("restriction")
public class OdysseusApplication implements IApplication {

	private static Logger LOG = LoggerFactory.getLogger(OdysseusApplication.class);	

	@Override
	public synchronized Object start(IApplicationContext context) {

		Display display = PlatformUI.createDisplay();
		try {
			
			if( !chooseWorkspace(display) ) {
				return IApplication.EXIT_OK;
			}

			

			return PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor()) 
					== PlatformUI.RETURN_RESTART ? IApplication.EXIT_RESTART : IApplication.EXIT_OK; 
		} catch (Throwable t) {
			LOG.error("Exception during running application", t);
			return null;
		} finally {
			display.dispose();
		}
	}

	@Override
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}	

	private static boolean chooseWorkspace(Display display) {
		try {
			File file = new File(System.getProperty("user.home"), "workspace");
			String path = file.getAbsolutePath().replace(File.separatorChar, '/');
			
			URL url = new URL("file", null, path); 
			ChooseWorkspaceData data = new ChooseWorkspaceData(url);

			ChooseWorkspaceDialogExtended dialog = new ChooseWorkspaceDialogExtended(display.getActiveShell(), data, false, true);
			dialog.prompt(false);
			
			
			// in case that the workspace was automatically selected
			if( data.getSelection() != null && !Platform.getInstanceLocation().isSet() ) {
				if( !releaseAndSetLocation(data.getSelection()) ) {
					
					// force showing dialog
					dialog = new ChooseWorkspaceDialogExtended(display.getActiveShell(), data, false, true);
					dialog.setErrorMessage("Could not set workspace.\nPlease choose a different one.");
					dialog.prompt(true);
				}
			}
			
			return dialog.getReturnCode() == Window.OK;
			
		} catch (Exception e) {
			LOG.error("Exception during choosing workspace", e);
			return false;
		}
	}
	
	private static boolean releaseAndSetLocation(String selection) {
		try {
			Location instanceLoc = Platform.getInstanceLocation();
			if (instanceLoc.isSet()) {
				instanceLoc.release();
			}
	
			return instanceLoc.set(new URL("file", null, selection), true);
		} catch( Exception ex ) {
			LOG.error("Could not release and set location", ex);
			return false;
		}
	}	
}
