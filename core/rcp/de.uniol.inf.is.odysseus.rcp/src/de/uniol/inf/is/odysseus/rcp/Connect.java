package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IClientExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.status.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.util.ConnectPreferencesManager;
import de.uniol.inf.is.odysseus.rcp.windows.ClientConnectionWindow;

public class Connect {

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	private Connect() {
	}

	public static void connectWindow(Display parent, boolean forceShow,
			boolean cancelOK) {
		// fetch data from prefs
		String wsdlLocation = ConnectPreferencesManager.getInstance()
				.getWsdlLocation();
		String service = ConnectPreferencesManager.getInstance().getService();

		if (wsdlLocation.length() > 0 && service.length() > 0 && !forceShow
				&& ConnectPreferencesManager.getInstance().getAutoConnect()) {
			if (!realConnect(wsdlLocation, service)) {
				ClientConnectionWindow wnd = new ClientConnectionWindow(parent,
						wsdlLocation, cancelOK);
				wnd.show();
			}
		} else {
			ClientConnectionWindow wnd = new ClientConnectionWindow(parent,
					cancelOK);
			wnd.show();
		}
	}

	public static boolean realConnect(String wsdlLocation, String service) {
		IExecutor executor = OdysseusRCPPlugIn.getExecutor();

		boolean connected = false;
		if (executor instanceof IClientExecutor) {
			connected = ((IClientExecutor) executor).connect(wsdlLocation + ";"
					+ service);
		}

		if (connected) {
			// TODO: eventuell im OdysseusRCPPlugin was speichern
			StatusBarManager.getInstance().setMessage(
					OdysseusNLS.AutomaticallyConnectedTo + " " + wsdlLocation);
		}
		
		return connected;
	}
}
