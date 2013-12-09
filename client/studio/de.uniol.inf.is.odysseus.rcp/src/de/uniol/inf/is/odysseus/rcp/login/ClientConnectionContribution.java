package de.uniol.inf.is.odysseus.rcp.login;

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
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

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
		return "Connection";
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
		instanceText.setEchoChar('*');
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
	public boolean onFinish() {
		IExecutor executor = OdysseusRCPPlugIn.getExecutor();
		String wsdlLocation = "http://"+host+":"+port+"/"+instance+"?wsdl";
		boolean connected = false;
		if (executor instanceof IClientExecutor) {
			String serviceNamespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/";
			String service = "WebserviceServerService";
			connected = ((IClientExecutor) executor).connect(wsdlLocation + ";" + serviceNamespace + ";" + service);
		}

		if (connected) {
			// TODO: eventuell im OdysseusRCPPlugin was speichern
			StatusBarManager.getInstance().setMessage(OdysseusNLS.AutomaticallyConnectedTo + " " + wsdlLocation);
		}else{
			LOG.error("Could not connect to server at '" +wsdlLocation+ "'");
		}

		return connected;		
	}

}
