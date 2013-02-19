package de.uniol.inf.is.odysseus.fastflowerdelivery;

import java.util.ArrayList;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventReceiver;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventReceiverRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSender;
import de.uniol.inf.is.odysseus.fastflowerdelivery.io.EventSenderRegistry;
import de.uniol.inf.is.odysseus.fastflowerdelivery.service.AbstractWebService;

/**
 * The EventWebApplication handles the web server, database and 
 * event IO initialization.
 * Any used WebService, EventSender or EventReceiver needs
 * to be registered with the application before starting it.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class EventWebApplication {

	/**
	 * Keeps track of all registered web services
	 */
	private ArrayList<AbstractWebService> services = new ArrayList<AbstractWebService>();
	
	/**
	 * Registers a web service at the application
	 * @param service
	 * 			the web service to be registered
	 */
	public void register(AbstractWebService service) {
		services.add(service);
	}
	
	/**
	 * keeps track of all registered event receivers
	 */
	private EventReceiverRegistry eventReceiverRegistry = EventReceiverRegistry.getInstance();
	
	/**
	 * Registers an event receiver at the application
	 * @param receiver
	 * 			the event receiver to be registered
	 */
	public void register(EventReceiver receiver) {
		eventReceiverRegistry.register(receiver);
	}
	
	/**
	 * Keeps track of all registered event senders
	 */
	private EventSenderRegistry eventSenderRegistry = EventSenderRegistry.getInstance();
	
	/**
	 * Registers an event sender at the application
	 * @param sender
	 * 			the event sender to be registered
	 */
	public void register(EventSender sender) {
		eventSenderRegistry.register(sender);
	}
	
	/**
	 * Starts the entire application.
	 * Order of systems to start:
	 * - event senders
	 * - event receivers (time delayed)
	 * - mysql driver initialization
	 * - Jetty web server
	 * @param port
	 * 			the port to start the jetty web server on
	 */
	public void start(int port) {
		eventSenderRegistry.startAll();
		registerMysqlDriver();
		startJetty(port);
	}

	/**
	 * Registers the MySQL driver
	 */
	private void registerMysqlDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Starts the jetty server, setting all web services into context.
	 * This jetty server also serves all non java files.
	 * @param port
	 * 			the port to start the jetty server on
	 */
	private void startJetty(int port) {
		try {
			Server server = new Server();
			
			SelectChannelConnector connector = new SelectChannelConnector();
			connector.setPort(port);
			server.addConnector(connector);
	 
			HandlerList handlers = getHandlerList();
			server.setHandler(handlers);
			
			server.start();
			server.join();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the handler list to be set in the jetty server
	 * @return the handler list
	 */
	private HandlerList getHandlerList() {
		HandlerList result = new HandlerList();
		
		ResourceHandler resourceHandler = getResourceHandler();
		ServletContextHandler servletContextHandler = getContextHandler();
		
		result.setHandlers(new Handler[] { servletContextHandler, resourceHandler, new DefaultHandler() });
		
		return result;
	}

	/**
	 * Creates the servlet context handlers.
	 * Uses all registered web services and add their path to the context.
	 * @return the servlet context handlers
	 */
	private ServletContextHandler getContextHandler() {
		ServletContextHandler result = new ServletContextHandler(ServletContextHandler.SESSIONS);
		result.setContextPath("/");
		for(AbstractWebService service : services)
			result.addServlet(new ServletHolder(service), service.getWebPath());
		return result;
	}

	/**
	 * Creates the resource handler to serve non java files
	 * @return the resource handler
	 */
	private ResourceHandler getResourceHandler() {
		ResourceHandler result = new ResourceHandler();
		result.setDirectoriesListed(true);
		result.setWelcomeFiles(new String[]{ "index.html" });
		result.setResourceBase(".");
		return result;
	}
	
}
