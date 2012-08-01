package de.uniol.inf.is.soop.scaiport.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SCAIDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7130136553729315073L;
	private final BundleContext context;

	public SCAIDataServlet(final BundleContext context) {
		super();
		this.context = context;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Call the SCAISensorPool to get the client sensor instance
		final SCAISensorPool source = SCAISensorPool.getInstance(
				req.getRemoteAddr(), this.context);
		// Forward the inputstream for processing
		resp.setStatus(source.process(req.getInputStream()) ? HttpServletResponse.SC_OK
				: HttpServletResponse.SC_NOT_ACCEPTABLE);
		resp.getWriter().close();
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Call the SCAISensorPool to get the client sensor instance
		final SCAISensorPool source = SCAISensorPool.getInstance(
				req.getRemoteAddr(), this.context);
		// Forward the inputstream for processing
		resp.setStatus(source.process(req.getInputStream()) ? HttpServletResponse.SC_OK
				: HttpServletResponse.SC_NOT_ACCEPTABLE);
		resp.getWriter().close();
	}
}
