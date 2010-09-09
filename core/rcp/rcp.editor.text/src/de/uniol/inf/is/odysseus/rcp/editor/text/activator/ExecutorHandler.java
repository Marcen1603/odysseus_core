package de.uniol.inf.is.odysseus.rcp.editor.text.activator;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

/**
 * Diese Hilfsklasse verwaltet die vom Declarative Service zur Verfügung
 * gestellten <code>IAdvancedExecutor</code>. Der Nutzer kann damit die aktuelle
 * Instanz der Klasse bekommen, falls benötigt.
 * <p>
 * Der Declative Service ruft die Methoden <code>bindExecutor()</code> und
 * <code>unbindExecutor</code> selbstständig auf. Der Nutzer sollte sie nicht
 * explizit aufrufen.
 * 
 * @author Timo Michelsen
 * 
 */
public class ExecutorHandler {

	private static IAdvancedExecutor executor;

	/**
	 * Wird vom Declarative Service aufgerufen. Damit wird der aktuelle
	 * <code>IAdvancedExecutor</code> auf das gegebene gesetzt. Über die
	 * statische Methode <code>getExecutor()</code> kann diese zurückgeliefert
	 * werden.
	 * <p>
	 * Der Nutzer sollte diese Funktion nicht selbst aufrufen.
	 * 
	 * @param e
	 *            Der neue <code>IAdvancedExecutor</code>. Darf nicht
	 *            <code>null</code> sein.
	 */
	public void bindExecutor(IAdvancedExecutor e) {
		executor = e;
	}

	/**
	 * Wird vom Declarative Service aufgerufen. Damit wird der aktuelle
	 * <code>IAdvancedExecutor</code> auf <code>null</code> gesetzt. Der
	 * Parameter wird ignoriert. Ab sofort liefert die statische Methode
	 * <code>getExecutor()</code> ausschließlich <code>null</code>.
	 * <p>
	 * Der Nutzer sollte diese Funktion nicht selbst aufrufen.
	 * 
	 * @param e
	 *            Der neue <code>IAdvancedExecutor</code>. Kann
	 *            <code>null</code> sein.
	 */
	public void unbindExecutor(IAdvancedExecutor e) {
		executor = null;
	}

	/**
	 * Liefert den aktuellen, vom Declarative Service gelieferten
	 * <code>IAdvancedExecutor</code>. Je nach Zeitpunkt des Aufrufs könnte
	 * <code>null</code> zurückgegeben werden.
	 * 
	 * @return Aktuelle <code>IAdvancedExecutor</code>-Instanz oder
	 *         <code>null</code>.
	 */
	public static IAdvancedExecutor getExecutor() {
		return executor;
	}
}
