/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.dashboard.extension.DashboardPartExtensionPointResolver;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;

public class DashboardPlugIn extends AbstractUIPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPlugIn.class);
	
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.dashboard";
	public static final String EXTENSION_POINT_ID = "de.uniol.inf.is.odysseus.rcp.DashboardPart";
	public static final String DASHBOARD_PART_EXTENSION = "prt";
	
	public static final String ADD_DASHBOARD_PART_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.AddDashboardPart";
	
	private static DashboardPartExtensionPointResolver extensionResolver;

	private static DashboardPlugIn plugin;
	private static IOdysseusScriptParser scriptParser;
	private static IExecutor executor;
	
	public DashboardPlugIn() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		extensionResolver = new DashboardPartExtensionPointResolver();
		Platform.getExtensionRegistry().addListener(extensionResolver, DashboardPlugIn.EXTENSION_POINT_ID);
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		Platform.getExtensionRegistry().removeListener(extensionResolver);
	}

	public static DashboardPlugIn getDefault() {
		return plugin;
	}
	
	public void bindScriptParser( IOdysseusScriptParser parser ) {
		LOG.debug("ScriptParser {} bound." , parser);
		
		scriptParser = parser;
	}
	
	public void unbindScriptParser( IOdysseusScriptParser parser ) {
		if( parser == scriptParser ) {
			LOG.debug("ScriptParser {} unbound.", parser);
			
			scriptParser = null;
		}
	}
	
	public static IOdysseusScriptParser getScriptParser() {
		return scriptParser;
	}
	
	public void bindExecutor( IExecutor exec ) {
		executor = exec;
		
		LOG.debug("Executor {} bound." , exec);
	}
	
	public void unbindExecutor( IExecutor exec) {
		if( exec == executor ) {
			executor = null;
			
			LOG.debug("Executor {} unbound." , exec);
		}
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
	
}
