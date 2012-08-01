package de.uniol.inf.is.soop.webApp.impl;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.soop.webApp.Activator;


/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class WebHTTPService {

	private MonitorServlet websocket = null;
	
	private static final Logger LOG = LoggerFactory
			.getLogger(WebHTTPService.class);

	protected void bindHttpService(final HttpService http) {
		
		this.websocket = MonitorServlet.getInstance();

		try {
			// Register the DataServlet under /webApp
			
			http.registerServlet(  "/soop/webApp", new WebAppServlet(Activator.getContext(), this.websocket), null, null);
			
			http.registerServlet(  "/soop/webAppWebsocket", this.websocket, null, null);
			
			http.registerResources("/soop/pages","/resources/pages",null);  
			System.out.println("webapp loaded");
		} catch (final ServletException e) {
			LOG.error(e.getMessage(), e);
		} catch (final NamespaceException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected void unbindHttpService(final HttpService http) {
		try {
			http.unregister("/soop/webApp");
			http.unregister("/soop/webAppWebsocket");
			http.unregister("/soop/pages");
			this.websocket = null;
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
