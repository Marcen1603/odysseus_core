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
package de.uniol.inf.is.odysseus.rcp.queryview.logical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

public class LogicalQueryViewDataProviderPlugIn implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(LogicalQueryViewDataProviderPlugIn.class);
	private static IExecutor executor;
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
	
	public void bindExecutor(IExecutor exec) {
		if( executor == null ) {
			executor = exec;
			LOG.debug("Executor " + exec + " bound ");
		} else {
			LOG.error("Multiple executors encountered.");
		}
	}

	public void unbindExecutor(IExecutor exec) {
		if( exec == executor ) {
			LOG.debug("Executor " + exec + " unbound");
			executor = null;
		}
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
}
