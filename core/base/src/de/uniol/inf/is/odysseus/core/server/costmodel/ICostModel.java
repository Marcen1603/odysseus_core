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
package de.uniol.inf.is.odysseus.core.server.costmodel;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * Repräsentiert ein Kostenmodell. Mit dieser lassen sich zu gegebenen
 * Operatormengen Kostenschätzungen durchführen.
 * 
 * @author Timo Michelsen
 * 
 */
public interface ICostModel {

	/**
	 * Führt eine Kostenschätzung mit den gegebenen physischen Operatoren durch
	 * und liefert das Ergebnis als {@link ICost} zurück.
	 * 
	 * @param operators
	 *            Liste der physischen Operatoren
	 * @param onUpdate
	 *            Gibt an, ob eine Aktualisierung der Kostenschätzung
	 *            durchgeführt wird, oder nicht. 
	 * @return Kostenschätzung
	 */
	public ICost estimateCost(List<IPhysicalOperator> operators, boolean onUpdate);

	/**
	 * Liefert die Maximalekosten
	 * 
	 * @return Maximalkosten
	 */
	public ICost getMaximumCost();
	
	/**
	 * Liefert Kosten, die nicht mit der Verarbeitung zu tun haben.
	 * 
	 * @return Hintergrundkosten
	 */
	public ICost getOverallCost();

	/**
	 * Liefert eine Instanz der Kosten des Kostenmodells mit 0.
	 * 
	 * @return Null-Kosten
	 */
	public ICost getZeroCost();

}
