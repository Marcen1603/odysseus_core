package de.uniol.inf.is.odysseus.rcp.user;

import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.base.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.user.impl.LoginPreferencesManager;
import de.uniol.inf.is.odysseus.rcp.user.impl.LoginWindow;

public class Login {

	public static void login(Shell parent, boolean forceShow, boolean cancelOK) {
		// Daten aus Prefs holen
		String username = LoginPreferencesManager.getInstance().getUsername();
		String password = LoginPreferencesManager.getInstance().getPasswordMD5();

		// Daten ok?
		if (username.length() > 0 && password.length() > 0) {

			if (!forceShow && LoginPreferencesManager.getInstance().getAutoLogin()) {
				// Automatisch anmelden
				User user = UserManagement.getInstance().getUser(username, password);
				if (user != null) {
					// anmelden ok
					ActiveUser.setActiveUser(user);
					StatusBarManager.getInstance().setMessage("Automatically logged in as " + username);
					return;
				}
			}
			// fehlerhafte anmeldung..
			LoginWindow wnd = new LoginWindow(parent, username, cancelOK);
			wnd.show();

		} else {
			// Leeres Loginfenster
			LoginWindow wnd = new LoginWindow(parent, cancelOK);
			wnd.show();
		}

	}
}
