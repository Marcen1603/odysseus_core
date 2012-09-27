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
package de.uniol.inf.is.odysseus.script.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

/**
 * Diese Hilfsklasse verwaltet die vom Declarative Service zur Verf�gung
 * gestellten <code>IExecutor</code>. Der Nutzer kann damit die aktuelle
 * Instanz der Klasse bekommen, falls ben�tigt.
 * <p>
 * Der Declative Service ruft die Methoden <code>bindExecutor()</code> und
 * <code>unbindExecutor</code> selbstst�ndig auf. Der Nutzer sollte sie nicht
 * explizit aufrufen.
 * 
 * @author Timo Michelsen
 * 
 */
public class ExecutorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExecutorHandler.class);
	private static IServerExecutor executor;

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
		if( e instanceof IServerExecutor ) {
			executor =  (IServerExecutor)e;
		} else {
			LOG.error("Bound executor {} is not a ServerExecutor!", e);
		}
	}

	/**
	 * Wird vom Declarative Service aufgerufen. Damit wird der aktuelle
	 * <code>IExecutor</code> auf <code>null</code> gesetzt. Der
	 * Parameter wird ignoriert. Ab sofort liefert die statische Methode
	 * <code>getExecutor()</code> ausschlie�lich <code>null</code>.
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
	 * <code>IExecutor</code>. Je nach Zeitpunkt des Aufrufs k�nnte
	 * <code>null</code> zur�ckgegeben werden.
	 * 
	 * @return Aktuelle <code>IExecutor</code>-Instanz oder
	 *         <code>null</code>.
	 */
	public static IServerExecutor getExecutor() {
		return executor;
	}
	
}
