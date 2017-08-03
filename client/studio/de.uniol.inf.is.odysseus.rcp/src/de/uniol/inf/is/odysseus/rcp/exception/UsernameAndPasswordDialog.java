package de.uniol.inf.is.odysseus.rcp.exception;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.bugreport.BugreportSender;

public class UsernameAndPasswordDialog extends Dialog {

	private static final String DIALOG_TITLE = "Set JIRA account";
	
	private String username;
	private String password;

	private String jira;
	
	protected UsernameAndPasswordDialog(Shell parentShell, String username, String password, String jira ) {
		super(parentShell);
		
		this.username = username != null ? username : "";
		this.password = password != null ? password : "";
		this.jira = jira;
	}

	public final String getPassword() {
		return password;
	}
	
	public final String getUsername() {
		return username;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(DIALOG_TITLE);
		
		super.configureShell(newShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		GridLayout layout = new GridLayout(2, false);
		comp.setLayout(layout);
		
		createLabel(comp, "Username");
		
		final Text usernameText = createText(comp, username);
		GridData usernameGridData = new GridData(GridData.FILL_HORIZONTAL);
		usernameText.setLayoutData(usernameGridData);
		usernameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				username = usernameText.getText();
			}
		});
		
		createLabel(comp, "Password");
		
		final Text passwordText = createText(comp, password);
		GridData passwordGridData = new GridData(GridData.FILL_HORIZONTAL);
		passwordText.setLayoutData(passwordGridData);
		passwordText.setEchoChar('*');
		passwordText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				password = passwordText.getText();
			}
		});
				
		return comp;
	}
	
	@Override
	protected void okPressed() {
		if( BugreportSender.checkLogin(username, password, jira)) {
			setReturnCode(OK);
			close();
		} else {
			MessageDialog.openError(getShell(), "JIRA", "Could not login with specified username and password!");
		}
	}

	private static Text createText(Composite comp, String value) {
		Text t = new Text(comp, SWT.BORDER | SWT.SINGLE);
		t.setText(value);
		return t;
	}

	private static Label createLabel(Composite parent, String string) {
		Label l = new Label(parent, SWT.NONE);
		l.setText(string);
		return l;
	}
}
