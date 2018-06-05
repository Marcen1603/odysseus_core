package de.uniol.inf.is.odysseus.rcp.webservice;

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

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.login.ILoginContribution;
import de.uniol.inf.is.odysseus.rcp.login.ILoginContributionContainer;
import de.uniol.inf.is.odysseus.rcp.login.UsernameLoginContribution;

public class ClientConnectionContribution implements ILoginContribution {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientConnectionContribution.class);

	private String port;
	private String host;
	private String instance;

	@Override
	public void onInit() {		
	}

	@Override
	public void onLoad(Map<String, String> savedConfig) {
		port = savedConfig.get("server.port");
		host = savedConfig.get("server.host");
		instance = savedConfig.get("server.instance");

		port = port != null ? port : "9669";
		host = host != null ? host : "localhost";
		instance = instance != null ? instance : "odysseus";

	}

	@Override
	public boolean isValid() {
		return !Strings.isNullOrEmpty(host) && !Strings.isNullOrEmpty(port) && !Strings.isNullOrEmpty(instance);
	}

	@Override
	public String getTitle() {
		return "WebService";
	}

	@Override
	public void createPartControl(Composite parent, final ILoginContributionContainer container) {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		rootComposite.setLayout(new GridLayout(2, false));

		createLabel(rootComposite, "Host");
		final Text hostText = createText(rootComposite, host);
		hostText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				host = hostText.getText();
				container.changed();

				updateMessages(container);
			}
		});

		createLabel(rootComposite, "Port");
		final Text portText = createText(rootComposite, port);
		portText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				port = portText.getText();
				container.changed();

				updateMessages(container);
			}
		});

		createLabel(rootComposite, "Instance");
		final Text instanceText = createText(rootComposite, instance);
		instanceText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				instance = instanceText.getText();
				container.changed();

				updateMessages(container);
			}
		});

		updateMessages(container);
	}

	private void updateMessages(ILoginContributionContainer container) {
		if (Strings.isNullOrEmpty(host)) {
			container.setErrorMessage("Host must be specified");
		} else if (Strings.isNullOrEmpty(port)) {
			container.setErrorMessage("Port must be specified");
		} else if (Strings.isNullOrEmpty(instance)) {
			container.setErrorMessage("Instance must be specified");
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
	public void dispose() {
	}

	@Override
	public Map<String, String> onSave() {
		HashMap<String, String> map = Maps.newHashMap();

		map.put("server.host", host);
		map.put("server.port", port);
		map.put("server.instance", instance);

		return map;
	}

	@Override
	public boolean onFinish(Collection<ILoginContribution> otherContributions) {
		
		UsernameLoginContribution loginContrib = null;
		
		for (ILoginContribution contribs: otherContributions){
			if (contribs instanceof UsernameLoginContribution){
				loginContrib = (UsernameLoginContribution) contribs;
				break;
			}
		}
		
		if (loginContrib == null){
			throw new RuntimeException("No User Login found!");
		}
		// Only call login from here
		loginContrib.setSuppressOnFinish(true);
		
		String username = loginContrib.getUsername();
		String password = loginContrib.getPassword();
		String tenantname = loginContrib.getTenant();
		
		IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		String wsdlLocation = "http://"+host+":"+port+"/"+instance+"?wsdl";
		ISession session = null;
		if (executor instanceof IClientExecutor) {
			String serviceNamespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/";
			String service = "WebserviceServerService";
			String connectString = wsdlLocation + ";" + serviceNamespace + ";" + service;
			//connected = ((IClientExecutor) executor).connect(connectString);
			session = ((IClientExecutor) executor).login(username, password.getBytes(), tenantname, connectString);
		}

		if (session != null) {
			StatusBarManager.getInstance().setMessage("Automatically connected to " + wsdlLocation);
			// TODO Store session for every connection
			OdysseusRCPPlugIn.setActiveSession(session);
		}else{
			LOG.error("Could not connect to server at '" +wsdlLocation+ "'");
			return false;
		}

		return true;		
	}

	@Override
	public int getPriority() {
		return 100;
	}
}
