package de.uniol.inf.is.odysseus.rest2.server;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		
		Application.context = context;
		
		Thread runner = new Thread() {
			@Override
			public void run() {
				int port = OdysseusConfiguration.instance.getInt("rest2.port", 8888);
				new MicroservicesRunner(port)
						.addGlobalRequestInterceptor(new SecurityAuthInterceptor())
						.addExceptionMapper(new ExceptionMapper<RuntimeException>() {
							@Override
							public Response toResponse(RuntimeException t) {
								LOGGER.error("Interal Server Error", t);
								return Response
										.status(Status.INTERNAL_SERVER_ERROR)
										.entity(t.getMessage())
										.type(MediaType.TEXT_PLAIN)
										.build();
							}
						})
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
		Application.context = null;
	}

}
