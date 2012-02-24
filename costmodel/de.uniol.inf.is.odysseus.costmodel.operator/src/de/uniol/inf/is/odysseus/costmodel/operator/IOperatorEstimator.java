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
