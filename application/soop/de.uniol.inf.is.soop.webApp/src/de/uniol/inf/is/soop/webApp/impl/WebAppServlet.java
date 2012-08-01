package de.uniol.inf.is.soop.webApp.impl;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import org.osgi.framework.BundleContext;

import com.google.gson.Gson;

import de.uniol.inf.is.soop.control.workflow.WorkflowProcess;
import de.uniol.inf.is.soop.control.workflow.WorkflowProcessInstance;
import de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSPort;
import de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSProxy;
import de.uniol.inf.is.soop.webApp.controlWsClient.SOOPControlWSServiceLocator;
import de.uniol.inf.is.soop.webApp.controlWsClient.StringResponse;

/**
 * @author Jan-Ole Brode
 */
public class WebAppServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7130136553729315073L;
	private final BundleContext context;
	private final MonitorServlet websocket;

	private static SOOPControlWSProxy proxy;
	private static SOOPControlWSPort port;

	private final ServletHelper h;

	public WebAppServlet(final BundleContext context, MonitorServlet websocket) {
		super();
		this.context = context;
		this.websocket = websocket;

		//SOOPControlWSServiceLocator loc = new SOOPControlWSServiceLocator();

		proxy = new SOOPControlWSProxy();
		
		port = proxy.getSOOPControlWS();
		
		h = new ServletHelper();

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// join get and post
		handleRequest(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		// join get and post
		handleRequest(req, resp);
	}

	protected void handleRequest(HttpServletRequest req,
			HttpServletResponse resp) {

		try {

			// fetch session token from cookie
			String token = h.getTokenFromCookie(req);

			String action = null;

			action = req.getRequestURI().replace("/soop/webApp/", "");

			if (token == null && !action.equals("login")) {
				// redirct to login page
				resp.sendRedirect("/soop/webApp/login?from="
						+ URLEncoder.encode(req.getRequestURL().toString(),
								"UTF8"));
			} else {

				if (action.equals("login")) {
					login(token, req, resp);
				} else if (action.equals("logout")) {
					logout(token, req, resp);
				} else if (action.equals("processList")) {
					processList(token, req, resp);
				} else if (action.equals("monitor")) {
					monitor(token, req, resp);
				} else {
					resp.getWriter().write(h.render("", "", "Ungültige Seite"));
				}

			}

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the proxy
	 */
	public static SOOPControlWSProxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy
	 *            the proxy to set
	 */
	public static void setProxy(SOOPControlWSProxy proxy) {
		WebAppServlet.proxy = proxy;
	}

	/**
	 * @return the port
	 */
	public static SOOPControlWSPort getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public static void setPort(SOOPControlWSPort port) {
		WebAppServlet.port = port;
	}

	private void login(String token, HttpServletRequest req,
			HttpServletResponse resp) {

		String error = null;

		try {
			if (req.getParameter("username") != null
					&& req.getParameter("password") != null) {
				StringResponse loginResponse;

				loginResponse = getPort().login(req.getParameter("username"),
						req.getParameter("password"));

				if (loginResponse.isSuccessful()) {

					token = loginResponse.getResponseValue();

					Cookie sessionCookie = new Cookie("token", token);
					resp.addCookie(sessionCookie);

					resp.sendRedirect("/soop/webApp/processList");
					return;
				} else {
					error = "login-error occoured";
				}
			}

			String data = h.readTemplate("login");

			resp.getWriter().write(h.render(data, "Login", error));

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void processList(String token, HttpServletRequest req,
			HttpServletResponse resp) {

		if (req.getParameter("startProcess") != null
				&& req.getParameter("processid") != null) {

			StringResponse startProcessResponse;
			try {
				startProcessResponse = getPort().startProcess(token,
						req.getParameter("processid"));
				// always redirect?
				// String instanceId = startProcessResponse.getResponseValue();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		String content = h.readTemplate("processList");

		StringResponse processListResponse;
		try {
			processListResponse = getPort().getInstalledProcesses(token,
					"localODE");

			if (processListResponse.isSuccessful()) {
				
				HashMap<String, WorkflowProcess> pl;
				ByteArrayInputStream bais;
				
				bais = new ByteArrayInputStream( 
						processListResponse.getResponseValue().getBytes()
				);
				
				XMLDecoder decoder = new XMLDecoder(bais);
				
				pl= (HashMap<String, WorkflowProcess>) decoder.readObject();
				
				decoder.close();

				if (pl instanceof HashMap<?, ?>) {
					String data = new Gson().toJson(pl);
					data =    "<script>" 
							+ "var pl = jQuery.parseJSON('" + data + "');" 
							+ "renderProcessList(pl);"
							+"</script>";

					resp.getWriter().write(
							h.render(content + data , "Prozesse", null));
				}
				
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void monitor(String token, HttpServletRequest req,
			HttpServletResponse resp) {

		String instanceId = null;
		try {

			if (req.getParameter("startProcess") != null
					&& req.getParameter("processid") != null) {
				StringResponse startProcessResponse;

				startProcessResponse = getPort().startProcess(token,
						req.getParameter("processid"));
				instanceId = startProcessResponse.getResponseValue();
			} else if (req.getParameter("instanceId") != null) {
				instanceId = req.getParameter("instanceId");
			}

			String data = h.readTemplate("monitor");

			if (instanceId != null) {
				data = data.replace("##instanceid##", instanceId);
				data = data.replace("##token##", token);
				data = data.replace("##username##", "Controller");
			}

			resp.getWriter().write(h.render(data, "Prozesse", null));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void logout(String token, HttpServletRequest req,
			HttpServletResponse resp) {

		if (req.getParameter("logout") != null) {

			try {
				resp.getWriter().write("implementiere logout!");
			} catch (IOException e) {
				e.printStackTrace();
			}

			token = null;

			Cookie[] cookies = req.getCookies();

			for (Cookie c : cookies) {
				if (c.getName().equals("token")) {
					token = c.getValue();
					break;
				}
			}
			// TODO implement logout in control
			/*
			 * if(token != null){ StringResponse logoutResponse =
			 * getPort().logout(token); }
			 * 
			 * if(logoutResponse.isSuccessful()){ // kill session cookie Cookie
			 * sessionCookie = new Cookie("token", null);
			 * sessionCookie.setMaxAge(0); resp.addCookie(sessionCookie);
			 * resp.sendRedirect
			 * ("http://127.0.0.1/soop/pages/login.html?logout=successful"); }
			 */

		}
	}
}
