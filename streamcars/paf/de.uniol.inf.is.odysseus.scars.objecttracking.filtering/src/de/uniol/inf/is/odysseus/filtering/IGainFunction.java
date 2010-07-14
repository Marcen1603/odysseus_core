/**
 * Interface for the implementation of a gain function,
 * that computes the gain for fusing old and new data
 */
package de.uniol.inf.is.odysseus.filtering;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

/**
 * @author dtwumasi
 *
 */
public interface IGainFunction<M extends IProbability> {

	public Double[][] computeGain(MVRelationalTuple<M> Old, ArrayList<int[]> mesurementValuePathsTupleOld, Object[] matrixes);
}
