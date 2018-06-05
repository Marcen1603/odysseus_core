/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package de.uniol.inf.is.odysseus.wrapper.opcua;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.opcua.common.utilities.RegisterClasses;

/**
 * The activator of the wrapper bundle.
 */
public class Activator implements BundleActivator {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(Activator.class);

	/** The context. */
	private static BundleContext context;

	@Override
	public void start(BundleContext ctx) throws Exception {
		log.info("Starting client bundle...");
		context = ctx;
		// Force-load every OPC UA class
		RegisterClasses.registerEnums();
		RegisterClasses.registerStructures();
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		// Finish bundle
		log.info("Stopping client bundle...");
		context = null;
	}

	/**
	 * Gets the bundle context.
	 *
	 * @return the context
	 */
	public static BundleContext getContext() {
		return context;
	}
}