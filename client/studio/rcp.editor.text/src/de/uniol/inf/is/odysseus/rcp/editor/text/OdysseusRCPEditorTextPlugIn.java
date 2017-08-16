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
package de.uniol.inf.is.odysseus.rcp.editor.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorLanguagePropertiesProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.templates.OdysseusScriptTemplateRegistry;

public class OdysseusRCPEditorTextPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor.text";
	public static final String KEYWORD_EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.editor.Keyword";
	public static final String QUERY_TEXT_EXTENSION = "qry";
	public static final String ODYSSEUS_SCRIPT_EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.editor.OdysseusScriptEditor";

	public static final String ODYSSEUS_ANNOTATION_HIGHLIGHTING = "de.uniol.inf.is.odysseus.rcp.editor.highlightannotation";

	private static OdysseusRCPEditorTextPlugIn plugin;
	private static IExecutor executor;
	private static Map<String, IEditorLanguagePropertiesProvider> completionproviders = new HashMap<String, IEditorLanguagePropertiesProvider>();
	private List<String> cachedAggregateFunctions;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);

	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static OdysseusRCPEditorTextPlugIn getDefault() {
		return plugin;
	}

	public void bindExecutor(IExecutor e) {
		try {
			executor = e;
			Platform.getExtensionRegistry().addListener(KeywordRegistry.getInstance());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void unbindExecutor(IExecutor e) {
		if (executor == e) {
			executor = null;
			Platform.getExtensionRegistry().removeListener(KeywordRegistry.getInstance());
			OdysseusScriptTemplateRegistry.getInstance().unregisterAll();
		}
	}

	/**
	 * Liefert den aktuellen, vom Declarative Service gelieferten
	 * <code>IExecutor</code>. Je nach Zeitpunkt des Aufrufs könnte
	 * <code>null</code> zurückgegeben werden.
	 * 
	 * @return Aktuelle <code>IExecutor</code>-Instanz oder <code>null</code>.
	 */
	public static IExecutor getExecutor() {
		// while(executor == null){
		// try {
		// OdysseusRCPEditorTextPlugIn.class.wait(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		return executor;
	}

	public static void bindCompletionProvider(IEditorLanguagePropertiesProvider ecp) {
		completionproviders.put(ecp.getSupportedParser(), ecp);
	}

	public static void unbindCompletionProvider(IEditorLanguagePropertiesProvider ecp) {
		completionproviders.remove(ecp.getSupportedParser());
	}

	public static Collection<IEditorLanguagePropertiesProvider> getEditorCompletionProviders() {
		return completionproviders.values();
	}

	public static IEditorLanguagePropertiesProvider getEditorCompletionProvider(String parserName) {
		return completionproviders.get(parserName);
	}

	public static List<String> getDatatypeNames() {
		List<String> list = new ArrayList<>();
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		Set<SDFDatatype> dts = OdysseusRCPEditorTextPlugIn.getExecutor().getRegisteredDatatypes(caller);
		for (SDFDatatype dt : dts) {
			list.add(dt.getQualName());
		}
		Collections.sort(list);
		return list;
	}

	public synchronized List<String> getInstalledAggregateFunctions(
			@SuppressWarnings("rawtypes") Class<? extends IStreamObject> datamodel) {
		if (cachedAggregateFunctions == null) {
			cachedAggregateFunctions = new ArrayList<String>(getExecutor()
					.getRegisteredAggregateFunctions(datamodel.getName(), OdysseusRCPPlugIn.getActiveSession()));
			Collections.sort(cachedAggregateFunctions);
		}
		return cachedAggregateFunctions;
	}
}
