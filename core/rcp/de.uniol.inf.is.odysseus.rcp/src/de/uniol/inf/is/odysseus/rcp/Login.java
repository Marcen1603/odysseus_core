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

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.util.LoginPreferencesManager;
import de.uniol.inf.is.odysseus.rcp.windows.LoginWindow;

public final class Login {

	private static final Logger LOG = LoggerFactory.getLogger(Login.class);
	
	private Login() {
	}

	public static void loginWindow(Display parent, boolean forceShow, boolean cancelOK) {
		String username = LoginPreferencesManager.getInstance().getUsername();
		String password = LoginPreferencesManager.getInstance().getPassword();
		String tenant =  LoginPreferencesManager.getInstance().getTenant();

		// Daten ok und automatisches anmelden erlaubt?
		if (username.length() > 0 && password.length() > 0 && !forceShow && LoginPreferencesManager.getInstance().isAutoLogin()) {
			Optional<ISession> optSession = realLogin(username, password, tenant);
			
			if (!optSession.isPresent()) {
				LoginWindow wnd = new LoginWindow(parent, username, tenant, cancelOK);
				wnd.show();
			}else{
				StatusBarManager.getInstance().setMessage(StatusBarManager.USER_ID, "User " + optSession.get().getUser().getName());
			}
		} else {
			LoginWindow wnd = new LoginWindow(parent, username, tenant, cancelOK);
			wnd.show();
		}
	}

	public static Optional<ISession> realLogin(String username, String password, String tenant) {
		try {
			IExecutor executor = OdysseusRCPPlugIn.getExecutor();
			ISession user = executor.login(username, password.getBytes(), tenant);

			if (user != null) {
				// anmelden ok
				OdysseusRCPPlugIn.setActiveSession(user);
				StringBuffer message = new StringBuffer(OdysseusNLS.LoggedInAs).append(" ").append(username);
				if (tenant.length() > 0){
					message.append(" [").append(tenant).append("]");
				}
				StatusBarManager.getInstance().setMessage(message.toString());
				executor.reloadStoredQueries(user);
				return Optional.of(user);
			}
			return null;
		} catch (RuntimeException ex) {
			LOG.error("Could not login user '" + username + "'", ex);
			
			showErrorMessage();
			
			System.exit(0); 
		}
		return Optional.absent();
	}

	private static void showErrorMessage() {
		MessageBox box = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR | SWT.OK);
		box.setMessage("An error occured during validating the user.\n" + "Please contact your administrator. The application\n" + "will be closed.");
		box.setText("Error");
		box.open();
	}
}
