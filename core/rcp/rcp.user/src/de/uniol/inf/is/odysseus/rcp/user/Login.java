package de.uniol.inf.is.odysseus.rcp.user;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.user.impl.LoginPreferencesManager;
import de.uniol.inf.is.odysseus.rcp.user.impl.LoginWindow;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class Login {

	public static void login(Shell parent, boolean forceShow, boolean cancelOK) {
		// Daten aus Prefs holen
		String username = LoginPreferencesManager.getInstance().getUsername();
		String password = LoginPreferencesManager.getInstance().getPasswordMD5();

		// Daten ok?
		if (username.length() > 0 && password.length() > 0) {

			if (!forceShow && LoginPreferencesManager.getInstance().getAutoLogin()) {
				// Automatisch anmelden
				try {
					User user = UserManagement.getInstance().getUser(username, password);
					if (user != null) {
						// anmelden ok
						ActiveUser.setActiveUser(user);
						StatusBarManager.getInstance().setMessage("Automatically logged in as " + username);
						return;
					}
				} catch( RuntimeException ex ) {
					ex.printStackTrace();
					MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_ERROR | SWT.YES | SWT.NO);
				    box.setMessage("An error occured during validating the user.\n" +
				    				"Probably the user-store is corrupted. Should the user-store\n" +
				    				"be deleted?");
				    box.setText("Error");
				    if( box.open() == SWT.YES) {
				    	try {
							UserManagement.getInstance().clearUserStore();
							box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION | SWT.OK );
						    box.setMessage("User-store is now deleted. Please restart Odysseus RCP.");
						    box.setText("Information");
						    box.open();
						} catch (Exception ex2) {
							ex.printStackTrace();
						} 
				    }
				    System.exit(1);
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
