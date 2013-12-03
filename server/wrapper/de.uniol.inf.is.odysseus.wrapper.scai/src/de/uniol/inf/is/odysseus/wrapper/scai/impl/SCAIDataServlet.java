/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.scai.impl;

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
