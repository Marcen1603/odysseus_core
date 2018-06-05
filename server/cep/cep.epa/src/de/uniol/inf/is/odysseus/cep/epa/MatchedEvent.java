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
package de.uniol.inf.is.odysseus.cep.epa;

/**
 * Einfache Datenstruktur, um eine verkettete Liste aller konsumierter Events zu
 * realisieren
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class MatchedEvent<R> {

	/**
	 * Referenz auf das zuvor konsumierte Event. Ist null für das erste
	 * konsumierte Event und nicht null für alle anderen.
	 */
	private MatchedEvent<R> previous;
	/**
	 * Das eigentliche Event, das zwischengespeichert werden soll
	 */
	private R event;
	private StateMachineInstance<R> stmi;

	/**
	 * Erzegt ein neues Listenelement.
	 * 
	 * @param previous
	 *            Referenz auf das vorherige Listenelement oder null, wenn das
	 *            neue Element das erste in der Liste sein soll.
	 * @param event
	 *            Das Event, das konsumiert und in die Liste eingehängt wird.
	 */
	public MatchedEvent(MatchedEvent<R> previous, R event, StateMachineInstance<R> stmi) {
		this.event = event;
		this.previous = previous;
		this.stmi = stmi;
	}


	/**
	 * Liefert das vorherige Listenelement
	 * 
	 * @return Das vorherige Lsitenelement oder null, wenn es sich um das erste
	 *         Element der Liste handelt.
	 */
	public MatchedEvent<R> getPrevious() {
		return previous;
	}

	/**
	 * Liefert das im Listenelement gespeicherte Event.
	 * 
	 * @return Das im Listenelement gespeicherte Event.
	 */
	public R getEvent() {
		return event;
	}
	
	public StateMachineInstance<R> getStateMachineInstance() {
		return stmi;
	}
	
	/**
	 * Gibt eine tiefe Kopie des MatchedEvent-Objekts zurück.
	 */
	@Override
	public MatchedEvent<R> clone() {
		MatchedEvent<R> newPrevious = null;
		if (this.previous != null) {
			newPrevious = this.previous.clone();
		}
		return new MatchedEvent<R>(newPrevious, this.event, this.stmi);
	}
	
	@Override
	public String toString() {
		return ""+event+" in ("+stmi.hashCode()+")";
	}

}
