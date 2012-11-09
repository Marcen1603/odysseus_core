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

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

/**
 * Zentrale Schnittstelle der Admission Control in Odysseus. Die aktuelle
 * Implementierung kann über ein Service angefordert werden. Die
 * Standardimplementierung ist die Klasse {@link StandardAC}.
 * 
 * @author Timo Michelsen
 * 
 */
public interface IAdmissionControl {

	/**
	 * Gibt zurück, ob die gegebene {@link ILogicalQuery} unter Berücksichtigung der
	 * aktuellen Gesamtkosten ausgeführt werden könnte, ohne die maximalen
	 * Kosten zu übersteigen.
	 * 
	 * @param query
	 *            {@link ILogicalQuery}, dessen potenzielle Ausführung geprüft werden
	 *            soll.
	 * 
	 * @return <code>true</code>, falls die {@link ILogicalQuery} ohne Überschreitung
	 *         der maximalen Kosten ausgeführt werden kann, sonst
	 *         <code>false</code>.
	 */
	public boolean canStartQuery(IPhysicalQuery query);

	/**
	 * Liefert die aktuelle Kostenschätzung zur gegebenen {@link ILogicalQuery} zurück.
	 * Liegt keine Kostenschätzung vor (bspw. die Anfrage wurde dem
	 * Ausführungsplan noch nicht hinzugefügt), wird <code>null</code>
	 * zurückgegeben.
	 * 
	 * @param query
	 *            Anfrage, dessen Kostenschätzung zurückgegeben werden soll.
	 * @return Kostenschätzung als Instanz von {@link ICost}, oder
	 *         <code>null</code>, falls noch keine Kostenschätzung vorliegt.
	 */
	public ICost getCost(IPhysicalQuery query);

	/**
	 * Erzwingt eine erneute Kostenschätzung des Ausführungsplans, sodass die
	 * aktuellen Gesamtkosten aktualisiert werden. Aktualisiert demenstprechend
	 * die Kostenabschätzungen aller laufenden Anfragen.
	 */
	public void updateEstimations();

	/**
	 * Liefert die aktuellen Gesamtkosten des Ausführungsplans zurück.
	 * 
	 * @return Gesamtkosten des Ausführungsplans
	 */
	public ICost getActualCost();

	/**
	 * Liefert die maximal erlaubten Kosten zurück. Die Admission Control sorgt
	 * dafür, dass die Gesamtkosten aller laufenen Anfragen die Maximalkosten
	 * nicht übersteigen.
	 * 
	 * @return Maximalkosten
	 */
	public ICost getMaximumCost();
	
	/**
	 * Liefert den Schwellwert an Kosten, die - wenn unterschritten - f�r ein erneutes
	 * Starten vom AC gestoppter Anfragen sorgt.
	 * 
	 * @return Minimalkosten
	 */
	public ICost getMinimumCost();

	/**
	 * Liefert eine Liste der Namen aller vorhandenden Kostenmodelle zurück. Der
	 * Name ist gleich dem Klassennamen des Kostenmodells.
	 * 
	 * @return
	 */
	public Set<String> getRegisteredCostModels();

	/**
	 * Wählt ein Kostenmodell aus, welches in Zukunft für die Kostenschätzungen
	 * verwendet wird. Es sollte darauf geachtet werden, das Kostenmodell nicht
	 * bei laufenden Anfragen zu wechseln.
	 * 
	 * @param name
	 *            Name der Klasse des Kostenmodells, welches fortan benutzt
	 *            werden soll
	 */
	public void selectCostModel(String name);

	/**
	 * Liefert den Namen des aktuell gewählten Kostenmodells zurück, oder
	 * <code>null</code>, falls kein Kostenmodell gewählt wurde.
	 * 
	 * @return
	 */
	public String getSelectedCostModel();

	/**
	 * Fügt einen {@link IAdmissionListener} hinzu. Dieser wird fortan über
	 * Überlastungen informiert.
	 * 
	 * @param listener
	 *            Hinzuzufügender Listener. Darf nicht <code>null</code> sein.
	 */
	public void addListener(IAdmissionListener listener);

	/**
	 * Entfernt einen {@link IAdmissionListener}. Ab diesem Zeitpunkt wird der
	 * Listener nicht mehr über Überlastungen informiert.
	 * 
	 * @param listener
	 *            Zu entfernender Listener. Wird <code>null</code> angegeben,
	 *            geschieht nichts.
	 */
	public void removeListener(IAdmissionListener listener);

	/**
	 * Liefert eine Liste von {@link IPossibleExecution}, welche Vorschläge von
	 * Anfragekombinationen beinhaltet. Jede Kombination schlägt vor, welche
	 * Anfragen gestoppt werden sollten, um eine Überlastung zu vermeiden. Bei
	 * jedem Aufruf wird die Liste neu generiert. Ist aktuelle keine Überlastung
	 * vorhanden, so wird nur ein Vorschlag - alle Anfragen auszuführen -
	 * zurückgegeben.
	 * 
	 * @param user User, welcher angibt, welche Anfragen genau betrachtet werden m�ssen.
	 * 
	 * @return Liste an Vorschlägen, welche Anfragen laufen und welche Anfragen
	 *         gestoppt werden sollten, um eine Überlastung aufzulösen.
	 */
	public List<IPossibleExecution> getPossibleExecutions(IUser user);
	
	/**
	 * Liefert <code>true</code>, wenn die letzte Kostensch�tzung eine �berlastung
	 * festgestellt hat, <code>false</code> sonst.
	 * 
	 * @return <code>true</code> bei �berlastungssituation, sonts <code>false</code>.
	 */
	public boolean isOverloaded();
}
