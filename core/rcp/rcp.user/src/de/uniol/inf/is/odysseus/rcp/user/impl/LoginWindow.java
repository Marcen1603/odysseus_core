package de.uniol.inf.is.odysseus.rcp.user.impl;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.user.ActiveUser;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

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
		
		wnd.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if( loginOK ) 
					return;
				
				if( !cancelOK )
					System.exit(1);
			}
			
		});
		
		createInput(wnd);
		
		autoLoginCheck = new Button(wnd, SWT.CHECK);
		autoLoginCheck.setText(AUTO_LOGIN_TEXT);
		autoLoginCheck.setSelection(LoginPreferencesManager.getInstance().getAutoLogin());
		
		createButtons(wnd);
		
		wnd.layout();
		usernameInput.setFocus();
		
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
		passwordInput.setEchoChar('*');
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
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					User user = UserManagement.getInstance().login(usernameInput.getText(), passwordInput.getText());
					if( user == null ) {
						markRed();
						return;
					}
					LoginPreferencesManager.getInstance().setUsername(user.getUsername());
					LoginPreferencesManager.getInstance().setPasswordMD5(user.getPassword());
					LoginPreferencesManager.getInstance().setAutoLogin(autoLoginCheck.getSelection());
					LoginPreferencesManager.getInstance().save();
					ActiveUser.setActiveUser(user);
					StatusBarManager.getInstance().setMessage("Logged in as " + user.getUsername());
					loginOK = true;
					wnd.dispose();
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
		});
		
		cancelButton = new Button(comp, SWT.PUSH);
		cancelButton.setText(CANCEL_BUTTON_TEXT);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wnd.dispose();
			}
		});
	}
}
