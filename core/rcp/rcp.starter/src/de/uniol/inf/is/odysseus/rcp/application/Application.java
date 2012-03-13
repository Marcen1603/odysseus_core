/** Copyright [2011] [The Odysseus Team]
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

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.Connect;
import de.uniol.inf.is.odysseus.rcp.Login;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.ConnectPreferencesManager;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) {
		Display display = PlatformUI.createDisplay();
		try {
			// TODO: use a servicetracker instead of sleep...
			IExecutor executor = null;
			while((executor = OdysseusRCPPlugIn.getExecutor()) == null){
				Thread.sleep(2000);
			}
			if(executor instanceof IClientExecutor) {
				String wsdlLocation = "http://localhost:9669/odysseus?wsdl";
				String service = "WebserviceServerService";
				String serviceNamespace = "http://webservice.webserviceexecutor.executor.planmanagement.odysseus.is.inf.uniol.de/";
				// TODO: Wo woll das sonst passieren?
				ConnectPreferencesManager.getInstance().setWdslLocation(wsdlLocation);
				ConnectPreferencesManager.getInstance().setService(service);
				ConnectPreferencesManager.getInstance().setServiceNamespace(serviceNamespace);
				Connect.connectWindow(display, false, false);
			}
			
			Login.loginWindow(display, false, false);

			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} catch(Throwable t){
			t.printStackTrace();
			return null;
		} finally {
			display.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
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
	
	
}
