package de.uniol.inf.is.odysseus.rcp.login;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.client.common.ClientSessionStore;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public class UsernameLoginContribution implements ILoginContribution {

	private static final Logger LOG = LoggerFactory
			.getLogger(UsernameLoginContribution.class);

	private String tenant;
	private String username;
	private String password;
	boolean suppressOnFinish = false;

	@Override
	public void onInit() {

	}

	@Override
	public void onLoad(Map<String, String> savedConfig) {
		tenant = savedConfig.get("user.tenant");
		username = savedConfig.get("user.name");
		password = savedConfig.get("user.password");

		tenant = tenant != null ? tenant : "";
		username = username != null ? username : "";
		password = password != null ? password : "";
	}

	@Override
	public String getTitle() {
		return "User";
	}

	@Override
	public void createPartControl(Composite parent,
			final ILoginContributionContainer container) {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		rootComposite.setLayout(new GridLayout(2, false));

		createLabel(rootComposite, "Username");
		final Text usernameText = createText(rootComposite, username);
		usernameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				username = usernameText.getText();
				container.changed();

				updateMessages(container);
			}
		});

		createLabel(rootComposite, "Password");
		final Text passwordText = createText(rootComposite, password);
		passwordText.setEchoChar('*');
		passwordText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				password = passwordText.getText();
				container.changed();

				updateMessages(container);
			}
		});

		Label separator = new Label(rootComposite, SWT.NONE);
		separator = new Label(rootComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createLabel(rootComposite, "Tenant");
		final Text tenantText = createText(rootComposite, tenant);
		tenantText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				tenant = tenantText.getText();
				container.changed();

				updateMessages(container);
			}
		});

		updateMessages(container);
	}

	private void updateMessages(ILoginContributionContainer container) {
		if (Strings.isNullOrEmpty(username)) {
			container.setErrorMessage("Username must be specified");
		} else if (Strings.isNullOrEmpty(password)) {
			container.setErrorMessage("Password must be specified");
		} else {
			container.setErrorMessage(null);
		}
	}

	private static Text createText(Composite rootComposite, String txt) {
		Text text = new Text(rootComposite, SWT.BORDER);
		text.setText(txt);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return text;
	}

	private static Label createLabel(Composite rootComposite, String txt) {
		Label label = new Label(rootComposite, SWT.NONE);
		label.setText(txt);
		return label;
	}

	@Override
	public boolean isValid() {
		return !Strings.isNullOrEmpty(username)
				&& !Strings.isNullOrEmpty(password);
	}

	@Override
	public void dispose() {

	}

	@Override
	public Map<String, String> onSave() {
		HashMap<String, String> map = Maps.newHashMap();

		map.put("user.tenant", tenant);
		map.put("user.name", username);
		map.put("user.password", password);

		return map;
	}

	@Override
	public boolean onFinish(Collection<ILoginContribution> otherContributions) {
		if (suppressOnFinish) {
			return true;
		} 
		return realLogin(username, password, tenant);
	}

	private static boolean realLogin(String username, String password,
			String tenant) {
		try {
			IExecutor executor = OdysseusRCPPlugIn.getExecutor();

			ISession session = Strings.isNullOrEmpty(tenant) ? executor.login(
					username, password.getBytes()) : executor.login(username,
					password.getBytes(), tenant);

			if (session == null) {
				return false;
			}

			session.setConnectionName("");
			OdysseusRCPPlugIn.setActiveSession(session);
			ClientSessionStore.addSession(session.getConnectionName(), session);
			StringBuffer message = new StringBuffer(OdysseusNLS.LoggedInAs)
					.append(" ").append(username);
			if (tenant.length() > 0) {
				message.append(" [").append(tenant).append("]");
			}
			StatusBarManager.getInstance().setMessage(message.toString());
			StatusBarManager.getInstance().setMessage(StatusBarManager.USER_ID,
					"User " + session.getUser().getName());
			executor.reloadStoredQueries(session);
			return true;

		} catch (Throwable ex) {
			LOG.error("Could not login user '" + username + "'", ex);
			return false;
		}
	}

	@Override
	public int getPriority() {
		return 0;
	}

	public String getTenant() {
		return tenant;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setSuppressOnFinish(boolean suppressOnFinish) {
		this.suppressOnFinish = suppressOnFinish;
	}
}
