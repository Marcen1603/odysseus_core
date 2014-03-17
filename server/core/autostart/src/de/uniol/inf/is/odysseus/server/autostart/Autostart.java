package de.uniol.inf.is.odysseus.server.autostart;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class Autostart implements BundleActivator {

	private static final String AUTOSTARTFILE = "/autostart/autostart.qry";
	static private BundleContext context;
	static private IExecutor executor;

	private void runAutostart() {
		if (context != null && executor != null) {
			try {
				Bundle bundle = context.getBundle();
				URL fileURL = bundle.getEntry(AUTOSTARTFILE);
				InputStream inputStream = fileURL.openConnection()
						.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						inputStream));
				String inputLine;
				StringBuffer query = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					query.append(inputLine).append("\n");
				}
				if (query.length() > 0) {
					ISession user = UserManagementProvider
							.getSessionmanagement().loginSuperUser(null);
					executor.addQuery(query.toString(), "OdysseusScript", user,
							"Standard", Context.empty());
				}
				in.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void unbindExecutor(IExecutor exec) {
		executor = null;
	}

	public void bindExecutor(IExecutor exec) {
		executor = exec;
		runAutostart();
	}

	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
		runAutostart();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
