package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.AbstractRelationalMergeFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <K>
 * @param <T>
 */
public class ProbabilisticMergeFunction<T extends Tuple<K>, K extends ITimeIntervalProbabilistic> extends AbstractRelationalMergeFunction<T, K> implements IDataMergeFunction<T, K> {

	public ProbabilisticMergeFunction(final int resultSchemaSize) {
		super(resultSchemaSize);
	}

	protected ProbabilisticMergeFunction(final ProbabilisticMergeFunction<T, K> original) {
		super(original.schemaSize);
	}

	@Override
	public T merge(final T left, final T right, final IMetadataMergeFunction<K> metamerge, final Order order) {
		return (T) left.merge(left, right, metamerge, order);
	}

	@Override
	public void init() {
	}

	@Override
	public ProbabilisticMergeFunction<T, K> clone() {
		return new ProbabilisticMergeFunction<T, K>(this);
	}

}
