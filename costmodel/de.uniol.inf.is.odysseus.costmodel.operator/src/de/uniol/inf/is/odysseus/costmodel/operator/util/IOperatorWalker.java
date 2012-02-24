package de.uniol.inf.is.odysseus.costmodel.operator.util;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

/**
 * Visitor-Klasse, welcher mit {@link GraphWalker} eingesetzt wird.
 * Die einzige Methode <code>walk</code> wird für jeden physischen
 * Operator aufgerufen.
 * 
 * @author Timo Michelsen
 *
 */
public interface IOperatorWalker {

	/**
	 * Wird für jeden Operator aufgerufen. Je nach Implementierung werden hier
	 * unterschiedliche Berechnungen durchgeführt oder Daten aus einem physischen
	 * Operatorgraphen gesammelt.
	 * 
	 * @param operator Aktuell besuchter physischer Operator
	 */
	public void walk( IPhysicalOperator operator);
}
