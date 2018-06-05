package de.uniol.inf.is.odysseus.fastflowerdelivery.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonData;
import de.uniol.inf.is.odysseus.fastflowerdelivery.json.JsonError;

/**
 * This is the abstract class for every web service in the application.
 * These web services only serve JSON data to their clients and
 * only accept POST requests.
 * Any Exception thrown during request handling will be send back
 * to the client via JSON.
 *
 * @author Weert Stamm
 * @version 1.0
 */
abstract public class AbstractWebService extends HttpServlet {

	private static final long serialVersionUID = 1825442512039296999L;
	
	/**
	 * The web url at which the webservice will be accessible
	 */
	private String webPath = "/";
	
	/**
	 * Retrieves the web path of this service
	 * @return the web path of this service
	 */
	public String getWebPath() {
		return webPath;
	}

	/**
	 * Sets the web path at which this services will be accessible
	 * from within the WWW
	 * @param webPath
	 * 			the web path to be set
	 */
	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}

	/**
	 * Passes the HttpServletRequest as a ServiceRequest object
	 * to the serve() method.
	 * Also sets response headers to handle XHR client requests.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		ServiceRequest request = new ServiceRequest(req);
		JsonData data;
		try {
			data = serve(request);
		} catch(Exception ex) {
			data = new JsonError(ex.getMessage());
		}
		
		resp.getWriter().println(data);
		
		addServiceHeaders(resp);
	}

	/**
	 * This method handles any request from the client.
	 * @param request
	 * 			An object to easily retrieve POST parameters
	 * @return a JsonData object to be sent back to the client
	 */
	abstract protected JsonData serve(ServiceRequest request);

	/**
	 * Sets specific headers necessary for old browsers handling
	 * XHR responses.
	 * @param resp
	 * 			the HttpServletResponse to add the headers to
	 */
	private void addServiceHeaders(HttpServletResponse resp) {
		resp.addHeader("Content-type", "application/json");
		resp.addHeader("Expires", "Sat, 05 Nov 2005 00:00:00 GMT");
		resp.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
		resp.addHeader("Pragma", "no-cache");
	}
}
