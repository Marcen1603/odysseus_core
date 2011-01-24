package de.uniol.inf.is.odysseus.rcp.editor.text;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;


/**
 * The activator class controls the plug-in life cycle
 */
public class OdysseusRCPEditorTextPlugIn extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor.text";
	public static final String KEYWORD_EXTENSION_ID = "de.uniol.inf.is.odysseus.rcp.editor.Keyword";
	public static final String QUERY_TEXT_EXTENSION = "qry";
	public static final String QUERY_TEXT_EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.editor.QueryTextEditor";

	// The shared instance
	private static OdysseusRCPEditorTextPlugIn plugin;
	private static IExecutor executor;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		KeywordRegistry.getInstance().loadExtensions();
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
		return executor;
	}
}
