package de.uniol.inf.is.odysseus.parallelization.interoperator.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.util.GenericDownstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.GenericUpstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.OperatorIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.parallelization.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.AbstractParallelTransformationStrategy;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class LogicalGraphHelper {

	static final private InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(AbstractParallelTransformationStrategy.class);
	
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator findOperatorWithId(
			String operatorId, ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				operatorId);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(operatorId);
	}

	public static boolean checkStartEndIdCombination(
			String startParallelizationId, String endParallelizationId,
			ILogicalOperator logicalPlan) {
		ILogicalOperator startOperator = findOperatorWithId(
				startParallelizationId, logicalPlan);
		if (startOperator != null) {
			ILogicalOperator endOperator = findDownstreamOperatorWithId(
					endParallelizationId, startOperator);
			if (endOperator != null) {
				return true;
			}
		}
		return false;
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
		} else {
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
	
	public static void checkWayToEndPoint(
			ILogicalOperator operatorForTransformation,
			boolean aggregatesWithGroupingAllowed, String endParallelizationId,
			boolean assureSemanticCorrectness) {
		if (endParallelizationId != null && !endParallelizationId.isEmpty()) {
			ILogicalOperator endOperator = LogicalGraphHelper
					.findDownstreamOperatorWithId(endParallelizationId,
							operatorForTransformation);
			if (endOperator == null) {
				// end operator id is invalid
				ILogicalOperator upstreamOperator = LogicalGraphHelper
						.findUpstreamOperatorWithId(endParallelizationId,
								operatorForTransformation);
				if (upstreamOperator != null) {
					throw new IllegalArgumentException("End operator with id "
							+ endParallelizationId
							+ " need to be on downstream and not upstream.");
				} else {
					throw new IllegalArgumentException("End operator with id"
							+ endParallelizationId + " does not exist.");
				}
			} else {

				ILogicalOperator currentOperator = LogicalGraphHelper
						.getNextOperator(operatorForTransformation);
				while (currentOperator != null) {

					boolean possibleSemanticChange = false;

					// validation if stateful operators or stateful functions
					// exists is only needed if semantic correctness is needed

					if (currentOperator instanceof IStatefulAO) {
						possibleSemanticChange = LogicalGraphHelper
								.validateStatefulAO(assureSemanticCorrectness,
										aggregatesWithGroupingAllowed,
										currentOperator, possibleSemanticChange);
					} else if (currentOperator instanceof SelectAO) {
						possibleSemanticChange = LogicalGraphHelper
								.validateSelectAO(assureSemanticCorrectness,
										currentOperator, possibleSemanticChange);
					} else if (currentOperator instanceof MapAO) {
						possibleSemanticChange = LogicalGraphHelper
								.validateMapAO(assureSemanticCorrectness,
										currentOperator, possibleSemanticChange);
					}

					// if parameter with id is reached, parallelization is done
					if (currentOperator.getUniqueIdentifier() != null) {
						if (currentOperator.getUniqueIdentifier()
								.equalsIgnoreCase(
										endParallelizationId)) {
							if (possibleSemanticChange
									&& !assureSemanticCorrectness) {
								INFO_SERVICE
										.info("Parallelization between start and end id possibly "
												+ "results in a semantic change of the given plan.");
							}

							// all operators including end operator are
							// valid,
							// validation successful
							return;
						}
					}

					// if end operator is not reached, we need to get the next
					// operator
					currentOperator = LogicalGraphHelper
							.getNextOperator(currentOperator);
				}
			}
		}
	}
}
