/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.ac;

/**
 * Schnittstelle, welche von Klassen implementiert werden muss, damit sie über
 * Überlastungen in der {@link IAdmissionControl} informiert werden. Sie müssen
 * sind mittels der Methode <code>registerListener</code> der Klasse
 * {@link IAdmissionControl} registrieren.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IAdmissionListener {

	/**
	 * Wird aufgerufen, wenn eine Überlastung erkannt wurde. Eine Überlastung
	 * tritt auf, wenn die Gesamtkosten des Ausführungsplans die Maximalkosten
	 * übersteigen. Diese Methode wird nur dann aufgerufen, wenn die
	 * AdmissionControl Kostenschätzungen vornimmt.
	 * 
	 * @param sender
	 *            Instanz von {@link IAdmissionControl}, welcher die Überlastung
	 *            entdeckt hat.
	 */
	public void overloadOccured(IAdmissionControl sender);
}
