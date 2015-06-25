package de.uniol.inf.is.odysseus.multithreaded.interoperator.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.util.GenericDownstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.GenericUpstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.OperatorIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.multithreaded.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class LogicalGraphHelper {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator findDownstreamOperatorWithId(
			String endParallelizationId, ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				endParallelizationId);
		GenericDownstreamGraphWalker walker = new GenericDownstreamGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(endParallelizationId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator findUpstreamOperatorWithId(
			String endParallelizationId, ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				endParallelizationId);
		GenericUpstreamGraphWalker walker = new GenericUpstreamGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(endParallelizationId);
	}

	public static int calculateNewSourceOutPort(
			LogicalSubscription sourceSubscription, int iteration) {
		Map<Integer, SDFSchema> outputSchemaMap = sourceSubscription
				.getTarget().getOutputSchemaMap();
		int newSourceOutPort = outputSchemaMap.size() + iteration;
		// if this source out port is already in use, try another one
		while (outputSchemaMap.containsKey(newSourceOutPort)) {
			newSourceOutPort++;
		}

		return newSourceOutPort;
	}

	public static boolean validateStatefulAO(boolean assureSemanticCorrectness,
			boolean aggregatesWithGroupingAllowed,
			ILogicalOperator currentOperator, boolean possibleSemanticChange) {
		if (currentOperator instanceof AggregateAO) {
			AggregateAO aggregateOperator = (AggregateAO) currentOperator;
			if ((!aggregateOperator.getGroupingAttributes().isEmpty() && !aggregatesWithGroupingAllowed)
					|| aggregateOperator.getGroupingAttributes().isEmpty()) {
				if (assureSemanticCorrectness) {
					throw new IllegalArgumentException(
							"No aggregations allowed between start "
									+ "and end of parallelization for this strategy");
				} else {
					possibleSemanticChange = true;
				}
			}
		} else if (!(currentOperator instanceof UnionAO)){
			if (assureSemanticCorrectness) {
				throw new IllegalArgumentException(
						"No stateful operators allowed between start "
								+ "and end of parallelization");
			} else {
				possibleSemanticChange = true;
			}
		}
		return possibleSemanticChange;
	}

	public static boolean validateSelectAO(boolean assureSemanticCorrectness,
			ILogicalOperator currentOperator, boolean possibleSemanticChange) {
		SelectAO selectOperator = (SelectAO) currentOperator;
		IPredicate<?> predicate = selectOperator.getPredicate();
		if (predicate instanceof RelationalPredicate) {
			RelationalPredicate relPredicate = (RelationalPredicate) predicate;
			IExpression<?> expression = relPredicate.getExpression()
					.getMEPExpression();
			if (SDFAttributeHelper
					.expressionContainsStatefulFunction(expression)) {
				if (assureSemanticCorrectness) {
					throw new IllegalArgumentException(
							"No operators with stateful functions allowed "
									+ "between start and end of parallelization");
				} else {
					possibleSemanticChange = true;
				}
			}
		}
		return possibleSemanticChange;
	}

	public static boolean validateMapAO(boolean assureSemanticCorrectness,
			ILogicalOperator currentOperator, boolean possibleSemanticChange) {
		MapAO mapOperator = (MapAO) currentOperator;
		List<SDFExpression> expressionList = mapOperator.getExpressionList();
		for (SDFExpression sdfExpression : expressionList) {
			IExpression<?> mepExpression = sdfExpression.getMEPExpression();
			if (SDFAttributeHelper
					.expressionContainsStatefulFunction(mepExpression)) {
				if (assureSemanticCorrectness) {
					throw new IllegalArgumentException(
							"No operators with stateful functions allowed "
									+ "between start and end of parallelization");
				} else {
					possibleSemanticChange = true;
				}
			}
		}
		return possibleSemanticChange;
	}

	public static ILogicalOperator getNextOperator(
			ILogicalOperator currentOperator) {
		List<LogicalSubscription> subscriptions = new ArrayList<LogicalSubscription>(
				currentOperator.getSubscriptions());
		if (subscriptions.size() > 1) {
			// splits between operators are not allowed. If there is more than
			// on subscription, parallelization is not possible
			throw new IllegalArgumentException(
					"Splits between start and end operator are not allowed");
		} else {
			return subscriptions.get(0).getTarget();
		}
	}
}
