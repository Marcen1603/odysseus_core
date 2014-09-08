package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.rule;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.newimpl.AbstractHorizontalFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.newimpl.HorizontalFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;

/**
 * An aggregation can be part of a fragment for horizontal fragmentation
 * strategies, if the aggregation function is AVG COUNT or SUM.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractAggregateHorizontalFragmentationRule<Strategy extends AbstractHorizontalFragmentationQueryPartModificator>
		implements IFragmentationRule<Strategy, AggregateAO> {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractAggregateHorizontalFragmentationRule.class);

	/**
	 * All possible aggregation functions for the usage without partial
	 * aggregates.
	 */
	private static final String[] POSSIBLE_AGGREGATE_FUNCTIONS = { "MIN", "MAX" };

	/**
	 * All possible aggregation functions for the usage of partial aggregates.
	 */
	private static final String[] POSSIBLE_AGGREGATE_FUNCTIONS_PA = { "AVG",
			"COUNT", "SUM" };

	@Override
	public boolean canOperatorBePartOfFragments(Strategy strategy,
			AggregateAO operator, AbstractFragmentationHelper helper) {

		List<String> possibleAggFunctions = Arrays
				.asList(POSSIBLE_AGGREGATE_FUNCTIONS_PA);
		possibleAggFunctions.addAll(Arrays.asList(POSSIBLE_AGGREGATE_FUNCTIONS));
		
		for (SDFSchema aggSchema : operator.getAggregations().keySet()) {

			for (AggregateFunction aggFunction : operator.getAggregations()
					.get(aggSchema).keySet()) {

				if (!possibleAggFunctions.contains(aggFunction.getName())) {

					return false;

				}

			}

		}
		
		return true;

	}

	@Override
	public AggregateAO specialHandling(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		Preconditions.checkNotNull(part, "Query part must be not null!");
		Preconditions.checkNotNull(helper,
				"Fragmentation helper must be not null!");
		Preconditions
				.checkArgument(helper instanceof HorizontalFragmentationHelper,
						"Fragmentation helper must be a HorizontalFragmentationHelper!");
		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");

		AggregateAO aggregation = null;

		for (ILogicalOperator operator : part.getOperators()) {

			if (operator instanceof AggregateAO) {

				if (!this.needSpecialHandlingForQueryPart(part,
						(AggregateAO) operator, helper)) {

					continue;

				} else if (aggregation == null) {

					aggregation = HorizontalFragmentationHelper
							.changeAggregation(part, (AggregateAO) operator,
									bundle);
					AbstractAggregateHorizontalFragmentationRule.LOG
							.debug("Found {} as an aggregation, which needs to be changed in {}",
									operator, part);

				} else
					throw new QueryPartModificationException(
							"Can not fragment a query part containing more than one aggregation!");

			}

		}

		if (aggregation == null) {

			throw new QueryPartModificationException(
					"No aggregation found within " + part + "!");

		}

		return aggregation;

	}

	@Override
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			AggregateAO operator, AbstractFragmentationHelper helper) {

		List<String> possibleAggFunctions = Arrays
				.asList(POSSIBLE_AGGREGATE_FUNCTIONS_PA);

		for (SDFSchema aggSchema : operator.getAggregations().keySet()) {

			for (AggregateFunction aggFunction : operator.getAggregations()
					.get(aggSchema).keySet()) {

				if (!possibleAggFunctions.contains(aggFunction.getName())) {

					return false;

				}

			}

		}

		return true;

	}

	@Override
	public Class<AggregateAO> getOperatorClass() {

		return AggregateAO.class;

	}

}