/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.BooleanDatatypeProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.NumberDatatypeProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.PointInTimeDatatypeProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.ViewableDatatypeRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.stream.diagram"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private static BundleContext bundleContext;
	private static IExecutor executor;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		bundleContext= context;
		
		// register all allowed datatypes and convertes
		ViewableDatatypeRegistry.getInstance().register(new NumberDatatypeProvider());		
		ViewableDatatypeRegistry.getInstance().register(new PointInTimeDatatypeProvider());
		ViewableDatatypeRegistry.getInstance().register(new BooleanDatatypeProvider());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		bundleContext = null;
		super.stop(context);		
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}	

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public void bindExecutor( IExecutor exec ) {
		executor = exec;
	}
	
	public void unbindExecutor( IExecutor exec ) {
		if( exec == executor ) {
			executor = null;
		}
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
}
