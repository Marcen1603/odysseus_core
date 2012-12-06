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
package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.application.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.util.LoginPreferencesManager;
import de.uniol.inf.is.odysseus.rcp.windows.LoginWindow;

public class Login {

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Login.class);
		}
		return _logger;
	}

	private Login() {
	}

	public static void loginWindow(Display parent, boolean forceShow, boolean cancelOK) {
		// Daten aus Prefs holen
		String username = LoginPreferencesManager.getInstance().getUsername();
		String password = LoginPreferencesManager.getInstance().getPassword();

		// Daten ok und automatisches anmelden erlaubt?
		if (username.length() > 0 && password.length() > 0 && !forceShow && LoginPreferencesManager.getInstance().getAutoLogin()) {
			// Automatisch anmelden (password ist md5)
			ISession user = realLogin(username, password);
			if (user == null) {
				// fehlerhafte anmeldung..
				// Fenster anzeigen, damit der Nutzer das
				// korrigieren kann.
				LoginWindow wnd = new LoginWindow(parent, username, cancelOK);
				wnd.show();
			}else{
				StatusBarManager.getInstance().setMessage(StatusBarManager.USER_ID, "User " + user.getUser().getName());
			}
		} else {
			// Leeres Loginfenster
			LoginWindow wnd = new LoginWindow(parent, cancelOK);
			wnd.show();
		}

	}

	public static ISession realLogin(String username, String password) {
		try {
			IExecutor executor = OdysseusRCPPlugIn.getExecutor();

			// TODO: Kann der Executor hier noch nicht gebunden sein??

			ISession user = null;
			user = executor.login(username, password.getBytes());

			if (user != null) {
				// anmelden ok
				OdysseusRCPPlugIn.setActiveSession(user);
				StatusBarManager.getInstance().setMessage(OdysseusNLS.AutomaticallyLoggedInAs + " " + username);
				executor.reloadStoredQueries(user);
				return user;
			}
			return null;
		} catch (RuntimeException ex) {
			// getLogger().error(ex.getMessage(), ex);
			ex.printStackTrace();
			MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
			box.setMessage("An error occured during validating the user.\n" + "Please contact your administrator. The application\n" + "will be closed.");
			box.setText("Error");
			box.open();
			System.exit(0); // programm beenden
		}
		return null;
	}
}
