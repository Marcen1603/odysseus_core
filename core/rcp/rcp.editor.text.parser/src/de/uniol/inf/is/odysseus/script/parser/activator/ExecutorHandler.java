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
package de.uniol.inf.is.odysseus.script.parser.activator;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

/**
 * Diese Hilfsklasse verwaltet die vom Declarative Service zur Verfügung
 * gestellten <code>IExecutor</code>. Der Nutzer kann damit die aktuelle
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

	private static IExecutor executor;

	/**
	 * Wird vom Declarative Service aufgerufen. Damit wird der aktuelle
	 * <code>IExecutor</code> auf das gegebene gesetzt. Ãœber die
	 * statische Methode <code>getExecutor()</code> kann diese zurÃ¼ckgeliefert
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
