package de.uniol.inf.is.odysseus.spatial.aggregation;

import java.util.Collection;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class AggregationFunctionBuilder implements IAggregateFunctionBuilder {
	private final static String MERGE_BELIEFE_GRID = "MERGEBELIEFEGRID";
	private final static String MERGE_PLAUSABILITY_GRID = "MERGEPLAUSABILITYGRID";
	private final static String PMERGE = "PMERGE";

	private static Collection<String> names = new LinkedList<String>();
	{
		names.add(MERGE_BELIEFE_GRID);
		names.add(MERGE_PLAUSABILITY_GRID);
		names.add(PMERGE);
	}

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
		IAggregateFunction<RelationalTuple<?>, RelationalTuple<?>> aggFunc = null;
		if (key.getName().equalsIgnoreCase(MERGE_PLAUSABILITY_GRID)) {
			aggFunc = new MergePlausabilityGrid(pos);
		} else if (key.getName().equalsIgnoreCase(MERGE_BELIEFE_GRID)) {
			aggFunc = new MergeBeliefeGrid(pos);
		} else {
			throw new IllegalArgumentException(String.format(
					"No such Aggregatefunction: %s", key.getName()));
		}
		return aggFunc;
	}

}
