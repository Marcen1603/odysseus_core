package de.uniol.inf.is.odysseus.rcp.user.impl;

import java.security.MessageDigest;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
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

import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.base.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.user.ActiveUser;

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
	private final Shell parent;
	
	public LoginWindow( Shell parent,  boolean cancelOK ) {
		this(parent, "", cancelOK);
	}
	
	public LoginWindow( Shell parent,  String userName, boolean cancelOK ) {
		Assert.isNotNull(userName);
		Assert.isNotNull(parent);
		startUserName = userName;
		this.cancelOK = cancelOK;
		this.parent = parent;
	}
	
	public void show() {
		wnd = createWindow();
		wnd.setVisible(true);
	}
	
	private Shell createWindow() {
		Shell wnd = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		
		wnd.setLayout( new GridLayout() );
		wnd.setText(TITLE_TEXT);
		wnd.setSize(400, 180);
		
		wnd.setLayout(new GridLayout());
		
		createInput(wnd);
		
		autoLoginCheck = new Button(wnd, SWT.CHECK);
		autoLoginCheck.setText(AUTO_LOGIN_TEXT);
		autoLoginCheck.setSelection(LoginPreferencesManager.getInstance().getAutoLogin());
		
		createButtons(wnd);
		
		wnd.layout();
		return wnd;
	}
	
	private void createInput( Composite parent ) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);
		
		usernameLabel = new Label(comp, SWT.NONE);
		usernameLabel.setText(USER_TEXT);
		usernameInput = new Text(comp, SWT.BORDER | SWT.SINGLE);
		usernameInput.setText(startUserName);
		usernameInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		passwordLabel = new Label(comp, SWT.NONE);
		passwordLabel.setText(PASSWORD_TEXT);
		passwordInput = new Text(comp, SWT.BORDER | SWT.SINGLE);
		passwordInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void markRed() {
		usernameLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		passwordLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	}
	
	private void createButtons(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);
		
		okButton = new Button(comp, SWT.PUSH);
		okButton.setText(OK_BUTTON_TEXT);
		okButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				User user = UserManagement.getInstance().login(usernameInput.getText(), passwordInput.getText());
				if( user == null ) {
					markRed();
					return;
				}
				LoginPreferencesManager.getInstance().setUsername(usernameInput.getText());
				LoginPreferencesManager.getInstance().setPasswordMD5(md5(passwordInput.getText()));
				LoginPreferencesManager.getInstance().setAutoLogin(autoLoginCheck.getSelection());
				LoginPreferencesManager.getInstance().save();
				ActiveUser.setActiveUser(user);
				StatusBarManager.getInstance().setMessage("Logged in as " + user.getUsername());
				wnd.dispose();
			}
		});
		
		cancelButton = new Button(comp, SWT.PUSH);
		cancelButton.setText(CANCEL_BUTTON_TEXT);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wnd.dispose();
				if( !cancelOK )
					System.exit(1);
			}
		});
	}
	
	static MessageDigest md5;
	private String md5(String password) {
		StringBuffer hexString = new StringBuffer();
		try {
			md5 = MessageDigest.getInstance("MD5");
			if (md5 != null) {
				synchronized (md5) {
					md5.reset();
					md5.update(password.getBytes());
					byte[] result = md5.digest();
					for (int i = 0; i < result.length; i++) {
						if (result[i] <= 15 && result[i] >= 0) {
							hexString.append("0");
						}
						hexString.append(Integer.toHexString(0xFF & result[i]));
					}
				}
				return hexString.toString();
			} else {
				return password;
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
			return "";
		}
	}
}
