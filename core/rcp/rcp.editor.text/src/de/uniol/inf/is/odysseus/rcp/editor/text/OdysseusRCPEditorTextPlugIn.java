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
import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorCompletionProvider;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;

public class OdysseusRCPEditorTextPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor.text";
	public static final String KEYWORD_EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.editor.Keyword";
	public static final String QUERY_TEXT_EXTENSION = "qry";
	public static final String ODYSSEUS_SCRIPT_EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.editor.OdysseusScriptEditor";
	
	public static final String ODYSSEUS_ANNOTATION_HIGHLIGHTING = "de.uniol.inf.is.odysseus.rcp.editor.highlightannotation";

	private static OdysseusRCPEditorTextPlugIn plugin;
	private static IExecutor executor;
	private static IOdysseusScriptParser scriptParser;
	private static List<IEditorCompletionProvider> completionproviders = new ArrayList<IEditorCompletionProvider>();
	
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

	/**
	 * Wird vom Declarative Service aufgerufen. Damit wird der aktuelle
	 * <code>IExecutor</code> auf das gegebene gesetzt. Über die
	 * statische Methode <code>getExecutor()</code> kann diese zurückgeliefert
	 * werden.
	 * <p>
	 * Der Nutzer sollte diese Funktion nicht selbst aufrufen.
	 * 
	 * @param e
	 *            Der neue <code>IExecutor</code>. Darf nicht
	 *            <code>null</code> sein.
	 */
	public void bindExecutor(IExecutor e) {
		executor = e;
	}

	/**
	 * Wird vom Declarative Service aufgerufen. Damit wird der aktuelle
	 * <code>IExecutor</code> auf <code>null</code> gesetzt. Der
	 * Parameter wird ignoriert. Ab sofort liefert die statische Methode
	 * <code>getExecutor()</code> ausschließlich <code>null</code>.
	 * <p>
	 * Der Nutzer sollte diese Funktion nicht selbst aufrufen.
	 * 
	 * @param e
	 *            Der neue <code>IExecutor</code>. Kann
	 *            <code>null</code> sein.
	 */
	public void unbindExecutor(IExecutor e) {
		executor = null;
	}
	
	/**
	 * Liefert den aktuellen, vom Declarative Service gelieferten
	 * <code>IExecutor</code>. Je nach Zeitpunkt des Aufrufs könnte
	 * <code>null</code> zurückgegeben werden.
	 * 
	 * @return Aktuelle <code>IExecutor</code>-Instanz oder
	 *         <code>null</code>.
	 */
	public static IExecutor getExecutor() {
//		while(executor == null){
//		 	try {
//				OdysseusRCPEditorTextPlugIn.class.wait(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		return executor;
	}
	
	/**
	 * 
	 * @param parser
	 */
	public void bindScriptParser(IOdysseusScriptParser parser){
		scriptParser = parser;		
	}
	
	public void unbindScriptParser(IOdysseusScriptParser parser){
		scriptParser = null;
	}

	public static IOdysseusScriptParser getScriptParser() {
		return scriptParser;
	}
	
	
	public static void bindCompletionProvider(IEditorCompletionProvider ecp){
		completionproviders.add(ecp);
	}
	
	public static void unbindCompletionProvider(IEditorCompletionProvider ecp){
		completionproviders.remove(ecp);
	}
	
	public static List<IEditorCompletionProvider> getEditorCompletionProviders(){
		return completionproviders ;
	}
}
