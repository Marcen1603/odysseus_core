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
