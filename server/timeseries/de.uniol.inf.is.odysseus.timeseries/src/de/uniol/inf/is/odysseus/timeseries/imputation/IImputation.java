package de.uniol.inf.is.odysseus.timeseries.imputation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * 
 * @author Christoph Schröer
 *
 */
public interface IImputation<M extends ITimeInterval> {

	/**
	 * @param newElement
	 * 
	 * @return a List with data to impute, which replace missing data.
	 */
	public List<Tuple<M>> getImputationData(Tuple<M> newElement);

}
