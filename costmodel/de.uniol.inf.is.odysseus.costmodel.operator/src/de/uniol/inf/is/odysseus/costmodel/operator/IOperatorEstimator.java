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

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

/**
 * Repräsentiert einen Schätzer eines Operators. Über den Generic-Typ wird
 * angegeben, für welchen Typ von Operator der Schätzer eingesetzt werden soll.
 * 
 * @author Timo Michelsen
 * 
 * @param <T>
 *            Typ des zu Schätzenden Operators
 */
public interface IOperatorEstimator<T extends IPhysicalOperator> {

	/**
	 * Liefert die Klassendefinition des zu schätzenden Operatortyps.
	 * 
	 * @return Klassendefinition
	 */
	public Class<T> getOperatorClass();

	/**
	 * Schätzt zu einem gegebenen physischen Operator die einzelen Informationen
	 * wie Histogramme, Datenrate, Selektivität sowie Speicher- und
	 * Prozessorkosten ab. Die genaue Vorgehensweise ist vom Typ des Operators
	 * abhängig und in konkreten Implementierungen hinterlegt.
	 * 
	 * @param instance
	 *            Konkreter physischer Operator, dessen Eigenschaften
	 *            abgeschätzt werden soll.
	 * @param prevOperators
	 *            Abschätzungen aller Vorgängeroperatoren
	 * @param baseHistograms
	 *            Basishistogramme aller Attribute, die in der Anfrage
	 *            ausgewertet werden.
	 * @return Zusammenstellung aller abgeschätzen Eigenschaften des gegebenen
	 *         Operators
	 */
	public OperatorEstimation estimateOperator(T instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms);

}
