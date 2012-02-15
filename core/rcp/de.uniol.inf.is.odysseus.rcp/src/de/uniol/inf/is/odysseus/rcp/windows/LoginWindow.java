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
package de.uniol.inf.is.odysseus.rcp.windows;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.Login;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.util.LoginPreferencesManager;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

@SuppressWarnings("unused")
public class LoginWindow {

	private static final String TITLE_TEXT = "Odysseus RCP Login";

	private static final String USER_TEXT = "Username";
	private static final String PASSWORD_TEXT = "Password";

	private static final String OK_BUTTON_TEXT = "OK";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";

	private static final String AUTO_LOGIN_TEXT = "Login automatically";

	private Shell wnd;

	private Label usernameLabel;
	private Label passwordLabel;
	private Text usernameInput;
	private Text passwordInput;
	private Button okButton;
	private Button cancelButton;
	private Button autoLoginCheck;

	private final String startUserName;
	private final boolean cancelOK;
	private boolean loginOK = false;
	private final Display display;

	public LoginWindow(Display parent, boolean cancelOK) {
		this(parent, "", cancelOK);
	}

	public LoginWindow(Display parent, String userName, boolean cancelOK) {
		Assert.isNotNull(userName);
		Assert.isNotNull(parent);
		startUserName = userName;
		this.cancelOK = cancelOK;
		this.display = parent;
	}

	public void show() {
		wnd = createWindow();
		wnd.setVisible(true);

		while (!wnd.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
	}

	private Shell createWindow() {
		Shell wnd = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);

		wnd.setLayout(new GridLayout());
		wnd.setText(TITLE_TEXT);
		wnd.setSize(400, 180);

		wnd.setLayout(new GridLayout());

		wnd.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (loginOK)
					return;

				if (!cancelOK)
					System.exit(0);
			}

		});

		createInput(wnd);

		autoLoginCheck = new Button(wnd, SWT.CHECK);
		autoLoginCheck.setText(OdysseusNLS.LoginAutomatically);
		autoLoginCheck.setSelection(LoginPreferencesManager.getInstance()
				.getAutoLogin());

		createButtons(wnd);

		wnd.layout();
		usernameInput.setFocus();

		return wnd;
	}

	private void createInput(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);

		usernameLabel = new Label(comp, SWT.NONE);
		usernameLabel.setText(OdysseusNLS.Username + ":");
		usernameInput = new Text(comp, SWT.BORDER | SWT.SINGLE);
		usernameInput.setText(startUserName);
		usernameInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		passwordLabel = new Label(comp, SWT.NONE);
		passwordLabel.setText(OdysseusNLS.Password + ":");
		passwordInput = new Text(comp, SWT.BORDER | SWT.SINGLE);
		passwordInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		passwordInput.setEchoChar('*');
		passwordInput.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					tryToLogin();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
	}

	private void markRed() {
		usernameLabel.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));
		passwordLabel.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));
	}

	private void createButtons(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);

		okButton = new Button(comp, SWT.PUSH);
		okButton.setText(OdysseusNLS.OK);
		okButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tryToLogin();
			}
		});

		cancelButton = new Button(comp, SWT.PUSH);
		cancelButton.setText(OdysseusNLS.Cancel);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
	}

	// versucht, sich mit den eingegebenen Daten anzumelden
	private void tryToLogin() {
		// password ist klartext, daher false
		ISession user = Login.realLogin(usernameInput.getText(),
				passwordInput.getText());
		if (user != null) {
			// anmeldung ok
			loginOK = true;

			// Nutzerdaten nur speichern, wenn AutoLogin gew�hlt
			if (autoLoginCheck.getSelection()) {
				LoginPreferencesManager.getInstance().setUsername(
						user.getUser().getName());
				LoginPreferencesManager.getInstance().setPassword(
						passwordInput.getText());
			}
			LoginPreferencesManager.getInstance().setAutoLogin(
					autoLoginCheck.getSelection());
			LoginPreferencesManager.getInstance().save();

			wnd.dispose();
		} else {
			// anmeldung nicht ok
			loginOK = false;
			markRed();
		}
	}
}
