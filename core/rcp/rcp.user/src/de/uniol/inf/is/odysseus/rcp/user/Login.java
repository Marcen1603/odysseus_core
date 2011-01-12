package de.uniol.inf.is.odysseus.rcp.user;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.user.impl.LoginPreferencesManager;
import de.uniol.inf.is.odysseus.rcp.user.impl.LoginWindow;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryFactory;

public class Login {

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
				GlobalState.setActiveUser(user);
				GlobalState.setActiveDatadictionary(DataDictionaryFactory.getDefaultDataDictionary("RCP"));
				StatusBarManager.getInstance().setMessage(
						"Automatically logged in as " + username);
				return user;
			}
			return null;
		} catch (RuntimeException ex) {
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
