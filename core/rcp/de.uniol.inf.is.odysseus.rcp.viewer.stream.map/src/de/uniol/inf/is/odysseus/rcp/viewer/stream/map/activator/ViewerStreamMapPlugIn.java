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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline.StreamMapEditorAdapterFactory;

/**
 *
 * @author Stephan Jansen 
 * @author Kai Pancratz
 * 
 * 
 */
public class ViewerStreamMapPlugIn extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.stream.map"; //$NON-NLS-1$

	// The shared instance
	private static ViewerStreamMapPlugIn plugin;
	
	/**
	 * The constructor
	 */
	public ViewerStreamMapPlugIn() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Platform.getAdapterManager().registerAdapters(
				new StreamMapEditorAdapterFactory(), StreamEditor.class);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return shared instance
	 */
	public static ViewerStreamMapPlugIn getDefault() {
		return plugin;
	}

	public void log(Throwable e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, -1, "Exception",
				e);
		getLog().log(status);
	}

	
}
