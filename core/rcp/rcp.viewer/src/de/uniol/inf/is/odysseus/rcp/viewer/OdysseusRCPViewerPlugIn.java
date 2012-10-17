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
package de.uniol.inf.is.odysseus.rcp.viewer;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.ImageManager;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.StreamEditorRegistry;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.resource.XMLResourceLoader;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.impl.XMLSymbolConfiguration;

public class OdysseusRCPViewerPlugIn extends AbstractUIPlugin {

	public static final String SHOW_STREAM_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.ShowStreamCommand";
	public static final String CALL_GRAPH_EDITOR_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.CallGraphEditorCommand";
	public static final String CALL_ACTIVE_GRAPH_EDITOR_COMMAND_ID = "de.uniol.inf.is.odysseus.rcp.commands.CallActiveGraphEditor";

	public static final String STREAM_EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.editors.StreamEditor";
	public static final String GRAPH_EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.editors.GraphEditor";

	public static final String STREAM_EDITOR_TYPE_PARAMETER_ID = "de.uniol.inf.is.odysseus.rcp.parameters.StreamEditorTypeParameter";

	public static final String OUTLINE_CONTEXT_MENU_ID = "de.uniol.inf.is.odysseus.rcp.viewer.view.outline";

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer";

	public static OdysseusRCPViewerPlugIn context = null;

	public static ISymbolConfiguration SYMBOL_CONFIGURATION = null;

	private static ImageManager imageManager;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);

		Platform.getExtensionRegistry().addListener(StreamEditorRegistry.getInstance(), StreamEditorRegistry.EXTENSION_ID);

		Bundle bundle = bundleContext.getBundle();
		imageManager = new ImageManager(bundle);
		imageManager.register("stopStream", "Icons/control-stop.png");
		imageManager.register("startStream", "Icons/control.png");
		imageManager.register("metadata", "icons/metadata.gif");
		imageManager.register("attribute", "icons/status.png");
		imageManager.register("predicate", "icons/status.png");
		imageManager.register("schema", "icons/odata.png");
		imageManager.register("pipe_icon", "icons/task.png");
		imageManager.register("sink_icon", "icons/monitor.png");
		imageManager.register("source_icon", "icons/application-import.png");
		imageManager.register("partof_icon", "icons/graph.jpg");
		imageManager.register("string", "icons/string.jpg");
		imageManager.register("subscription", "icons/arrow-000-small.png");

		XMLResourceLoader.loadImages(bundle.getEntry("viewer_cfg/resources.xml"), bundle.getEntry("viewer_cfg/resourcesSchema.xsd"));

		context = this;
		SYMBOL_CONFIGURATION = new XMLSymbolConfiguration(bundleContext.getBundle().getEntry("viewer_cfg/symbol.xml"), bundleContext.getBundle().getEntry("viewer_cfg/symbolSchema.xsd"));
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);

		Platform.getExtensionRegistry().removeListener(StreamEditorRegistry.getInstance());
		imageManager.disposeAll();

		context = null;
	}
//
//	public static OdysseusRCPViewerPlugIn getDefault() {
//		return context;
//	}

	private static IExecutor executor;

	/**
	 * Wird vom Declarative Service aufgerufen. Damit wird der aktuelle
	 * <code>IExecutor</code> auf das gegebene gesetzt. Über die statische
	 * Methode <code>getExecutor()</code> kann diese zurückgeliefert werden.
	 * <p>
	 * Der Nutzer sollte diese Funktion nicht selbst aufrufen.
	 * 
	 * @param e
	 *            Der neue <code>IExecutor</code>. Darf nicht <code>null</code>
	 *            sein.
	 */
	public void bindExecutor(IExecutor e) {
		executor = e;
	}

	/**
	 * Wird vom Declarative Service aufgerufen. Damit wird der aktuelle
	 * <code>IExecutor</code> auf <code>null</code> gesetzt. Der Parameter wird
	 * ignoriert. Ab sofort liefert die statische Methode
	 * <code>getExecutor()</code> ausschließlich <code>null</code>.
	 * <p>
	 * Der Nutzer sollte diese Funktion nicht selbst aufrufen.
	 * 
	 * @param e
	 *            Der neue <code>IExecutor</code>. Kann <code>null</code> sein.
	 */
	public void unbindExecutor(IExecutor e) {
		executor = null;
	}

	/**
	 * Liefert den aktuellen, vom Declarative Service gelieferten
	 * <code>IExecutor</code>. Je nach Zeitpunkt des Aufrufs könnte
	 * <code>null</code> zurückgegeben werden.
	 * 
	 * @return Aktuelle <code>IExecutor</code>-Instanz oder <code>null</code>.
	 */
	public static IExecutor getExecutor() {
		return executor;
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

	public static ImageManager getImageManager() {
		return imageManager;
	}
}
