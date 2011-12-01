package de.uniol.inf.is.odysseus.wrapper.scai.impl;

import javax.servlet.ServletException;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.scai.Activator;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SCAIHTTPService {

	private static final Logger LOG = LoggerFactory
			.getLogger(SCAIHTTPService.class);

	protected void bindHttpService(final HttpService http) {
		try {
			// Register the DataServlet under /scai
			http.registerServlet("/scai",
					new SCAIDataServlet(Activator.getContext()), null, null);
		} catch (final ServletException e) {
			LOG.error(e.getMessage(), e);
		} catch (final NamespaceException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected void unbindHttpService(final HttpService http) {
		try {
			http.unregister("/scai");
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
