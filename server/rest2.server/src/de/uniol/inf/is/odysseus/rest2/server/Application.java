package de.uniol.inf.is.odysseus.rest2.server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.wso2.msf4j.MicroservicesRunner;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.rest2.server.api.DatastreamsApi;
import de.uniol.inf.is.odysseus.rest2.server.api.DatatypesApi;
import de.uniol.inf.is.odysseus.rest2.server.api.FunctionsApi;
import de.uniol.inf.is.odysseus.rest2.server.api.OperatorsApi;
import de.uniol.inf.is.odysseus.rest2.server.api.ParsersApi;
import de.uniol.inf.is.odysseus.rest2.server.api.QueriesApi;
import de.uniol.inf.is.odysseus.rest2.server.api.ServicesApi;
import de.uniol.inf.is.odysseus.rest2.server.api.SinksApi;
import de.uniol.inf.is.odysseus.rest2.server.api.UsersApi;
import de.uniol.inf.is.odysseus.rest2.server.query.QueryResultWebsocketEndpoint;

public class Application implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		
		Thread runner = new Thread() {
			@Override
			public void run() {
				int port = OdysseusConfiguration.instance.getInt("rest2.port", 8888);
				new MicroservicesRunner(port).addGlobalRequestInterceptor(new SecurityAuthInterceptor())
						.deployWebSocketEndpoint(new QueryResultWebsocketEndpoint())
						.deploy(
								new DatastreamsApi(),
								new DatatypesApi(),
								new FunctionsApi(),
								new OperatorsApi(),
								new ParsersApi(),
								new QueriesApi(),
								new ServicesApi(),
								new SinksApi(),
								new UsersApi()
						).start();				
			}
		};
		runner.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
