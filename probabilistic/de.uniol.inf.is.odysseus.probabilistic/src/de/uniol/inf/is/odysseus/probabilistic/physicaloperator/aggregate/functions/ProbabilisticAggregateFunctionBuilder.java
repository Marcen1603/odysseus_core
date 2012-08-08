package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticAggregateFunctionBuilder implements
		IAggregateFunctionBuilder {
	private static Collection<String> names = new LinkedList<String>();
	{
	};

	@Override
	public String getDatamodel() {
		return "relational";
	}

	@Override
	public Collection<String> getFunctionNames() {
		return names;
	}

	@Override
	public IAggregateFunction<?, ?> createAggFunction(AggregateFunction key,
			int[] pos) {
		IAggregateFunction<Tuple<?>, Tuple<?>> aggFunc = null;
		if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.NAMESPACE + "AVG")) {

		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.NAMESPACE + "SUM")) {
			aggFunc = ProbabilisticSum.getInstance(pos[0]);
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.NAMESPACE + "COUNT")) {
			aggFunc = ProbabilisticCount.getInstance(pos[0]);
		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.NAMESPACE + "MIN")) {

		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.NAMESPACE + "MAX")) {

		} else if (key.getName().equalsIgnoreCase(
				ProbabilisticConstants.NAMESPACE + "STDDEV")) {

		} else {
			throw new IllegalArgumentException("No such Aggregatefunction");
		}
		return aggFunc;
	}

}
