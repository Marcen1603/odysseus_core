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
package de.uniol.inf.is.odysseus.costmodel.operator;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * Repäsentiert die Charakteristika eines Datenstroms.
 * Darunter fallen Datenrate sowie durchschnittliche Länge
 * der Gültigkeitsintervalle.
 * 
 * @author Timo Michelsen
 *
 */
public interface IDataStream {

	/**
	 * Liefert den Operator zurück, dessen Ausgabestrom in dieser
	 * Instanz charakterisiert wird.
	 * 
	 * @return Physischer Operator
	 */
	public IPhysicalOperator getOperator();
	
	/**
	 * Liefert die Datenrate
	 * 
	 * @return Datenrate
	 */
	public double getDataRate();
	
	/**
	 * Liefert die durchschnittliche Länge der Gültigkeitsintervalle
	 * 
	 * @return Durchschnittliche Länge der Gültigkeitsintervalle
	 */
	public double getIntervalLength();
	
}
