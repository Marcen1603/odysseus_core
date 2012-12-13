/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.util.ConnectPreferencesManager;
import de.uniol.inf.is.odysseus.rcp.windows.ClientConnectionWindow;

/**
 * 
 * @author Merlin Wasmann
 * 
 */

public class Connect {

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Connect.class);
		}
		return _logger;
	}

	private Connect() {
	}

	public static void connectWindow(Display parent, boolean forceShow, boolean cancelOK) {
		// fetch data from prefs
		String wsdlLocation = ConnectPreferencesManager.getInstance().getWsdlLocation();
		String serviceNamespace = ConnectPreferencesManager.getInstance().getServiceNamespace();
		String service = ConnectPreferencesManager.getInstance().getService();

		if (wsdlLocation.length() > 0 && service.length() > 0 && serviceNamespace.length() > 0 && !forceShow && ConnectPreferencesManager.getInstance().getAutoConnect()) {
			if (!realConnect(wsdlLocation, service, serviceNamespace)) {
				ClientConnectionWindow wnd = new ClientConnectionWindow(parent, wsdlLocation, cancelOK);
				wnd.show();
			}
		} else {
			ClientConnectionWindow wnd = new ClientConnectionWindow(parent, cancelOK);
			wnd.show();
		}
	}

	public static boolean realConnect(String wsdlLocation, String service, String serviceNamespace) {
		IExecutor executor = OdysseusRCPPlugIn.getExecutor();

		boolean connected = false;
		if (executor instanceof IClientExecutor) {
			connected = ((IClientExecutor) executor).connect(wsdlLocation + ";" + serviceNamespace + ";" + service);
		}

		if (connected) {
			// TODO: eventuell im OdysseusRCPPlugin was speichern
			StatusBarManager.getInstance().setMessage(OdysseusNLS.AutomaticallyConnectedTo + " " + wsdlLocation);
		}

		return connected;
	}
}
