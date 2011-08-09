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
package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.util.LoginPreferencesManager;
import de.uniol.inf.is.odysseus.rcp.windows.LoginWindow;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class Login {

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}
	
	private Login() {}
	
	public static void loginWindow(Display parent, boolean forceShow,
			boolean cancelOK) {
		// Daten aus Prefs holen
		String username = LoginPreferencesManager.getInstance().getUsername();
		String password = LoginPreferencesManager.getInstance()
				.getPasswordMD5();

		// Daten ok und automatisches anmelden erlaubt?
		if (username.length() > 0 && password.length() > 0 && !forceShow
				&& LoginPreferencesManager.getInstance().getAutoLogin()) {
			// Automatisch anmelden (password ist md5)
			if (realLogin(username, password, true) == null) {
				// fehlerhafte anmeldung..
				// Fenster anzeigen, damit der Nutzer das
				// korrigieren kann.
				LoginWindow wnd = new LoginWindow(parent, username, cancelOK);
				wnd.show();
			}
		} else {
			// Leeres Loginfenster
			LoginWindow wnd = new LoginWindow(parent, cancelOK);
			wnd.show();
		}

	}

	public static User realLogin(String username, String password,
			boolean passwordIsMD5) {
		try {
			User user = null;
			user = UserManagement.getInstance().login(username, password,
					passwordIsMD5);

			if (user != null) {
				// anmelden ok
				GlobalState.setActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN, user);
				GlobalState.setActiveDatadictionary(DataDictionaryFactory.getDefaultDataDictionary(OdysseusDefaults.get("defaultDataDictionaryName")));
				StatusBarManager.getInstance().setMessage(
						"Automatically logged in as " + username);
				return user;
			}
			return null;
		} catch (RuntimeException ex) {
//			getLogger().error(ex.getMessage(), ex);
			ex.printStackTrace();
			MessageBox box = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR
					| SWT.OK);
			box.setMessage("An error occured during validating the user.\n"
					+ "Please contact your administrator. The application\n"
					+ "will be closed.");
			box.setText("Error");
			box.open();
			System.exit(0); // programm beenden
		}
		return null;
	}
}
