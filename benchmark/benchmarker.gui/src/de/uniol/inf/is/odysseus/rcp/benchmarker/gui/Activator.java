/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker.BufferplacementstrategyServiceTrackerCustomizer;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker.QueryLanguageServiceTrackerCustomizer;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker.SchedulerServiceTrackerCustomizer;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller.serviceTracker.SchedulingstrategyServiceTrackerCustomizer;
import de.uniol.inf.is.odysseus.rcp.benchmarker.utils.BenchmarkStoreUtil;
import de.uniol.inf.is.odysseus.scheduler.ISchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.benchmarker.gui"; //$NON-NLS-1$

	public final static String BENCHMARK_FILE = "benchmarks.xml";

	// The shared instance
	private static Activator plugin;
	private ServiceTracker schedulerServiceTracker;
	private ServiceTracker schedulingstrategyServiceTracker;
	private ServiceTracker bufferplacementstrategyServiceTracker;
	private ServiceTracker queryLanguageServiceTracker;
	private final List<String> schedulerNames;
	private final List<String> schedulingstrategyNames;
	private final List<String> bufferplacementstrategyNames;
	private final List<String> queryLanguageNames;

	private Set<String> allSingleTypes;

	public Set<String> getMetadataTypes() {
		return allSingleTypes;
	}

	public String[] getSchedulerServices() {
		return schedulerNames.toArray(new String[schedulerNames.size()]);
	}

	public String[] getSchedulingstrategyServices() {
		return schedulingstrategyNames.toArray(new String[schedulingstrategyNames.size()]);
	}

	public String[] getBufferplacementstrategyServices() {
		return bufferplacementstrategyNames.toArray(new String[bufferplacementstrategyNames.size()]);
	}

	public String[] getQueryLanguageServices() {
		return queryLanguageNames.toArray(new String[queryLanguageNames.size()]);
	}

	/**
	 * The constructor
	 */
	public Activator() {
		schedulerNames = new ArrayList<String>();
		schedulingstrategyNames = new ArrayList<String>();
		bufferplacementstrategyNames = new ArrayList<String>();
		queryLanguageNames = new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		IDE.registerAdapters();

		Set<Set<String>> allTypeCombination = MetadataRegistry.getAvailableMetadataCombinations();
		allSingleTypes = new HashSet<String>();
		for (Set<String> x : allTypeCombination) {
			allSingleTypes.addAll(x);
		}

		// ///////////////////////SCHEDULER////////////////////////////

		SchedulerServiceTrackerCustomizer customerScheduler = new SchedulerServiceTrackerCustomizer(context,
				schedulerNames);
		schedulerServiceTracker = new ServiceTracker(context, ISchedulerFactory.class.getName(), customerScheduler);
		schedulerServiceTracker.open();

		// ///////////////////SCHEDULINGSTRATEGIEN////////////////////////
		SchedulingstrategyServiceTrackerCustomizer customerSchedulingstrategy = new SchedulingstrategyServiceTrackerCustomizer(
				context, schedulingstrategyNames);
		schedulingstrategyServiceTracker = new ServiceTracker(context, ISchedulingFactory.class.getName(),
				customerSchedulingstrategy);
		schedulingstrategyServiceTracker.open();

		// ///////////////////BufferplacementStrategien////////////////////////
		BufferplacementstrategyServiceTrackerCustomizer customerBufferplacementsstrategy = new BufferplacementstrategyServiceTrackerCustomizer(
				context, bufferplacementstrategyNames);
		bufferplacementstrategyServiceTracker = new ServiceTracker(context, IBufferPlacementStrategy.class.getName(),
				customerBufferplacementsstrategy);
		bufferplacementstrategyServiceTracker.open();

		// ///////////////////QUERYLANGUAGE////////////////////////
		QueryLanguageServiceTrackerCustomizer customerQueryLanguage = new QueryLanguageServiceTrackerCustomizer(
				context, queryLanguageNames);
		queryLanguageServiceTracker = new ServiceTracker(context, IQueryParser.class.getName(), customerQueryLanguage);
		queryLanguageServiceTracker.open();

		BenchmarkStoreUtil.loadAllBenchmarks();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;

		schedulerServiceTracker.close();
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

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

//	private static IExecutor executor;
//
//	public static IExecutor getExecutor() {
//		return executor;
//	}
}
