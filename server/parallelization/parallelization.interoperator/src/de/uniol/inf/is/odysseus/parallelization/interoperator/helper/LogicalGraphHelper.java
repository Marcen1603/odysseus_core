/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.interoperator.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.expression.IRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.util.GenericDownstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.util.GenericUpstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.util.OperatorIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.parallelization.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.SemanticChangeException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.AbstractParallelTransformationStrategy;

public class LogicalGraphHelper {

	static final private InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(AbstractParallelTransformationStrategy.class);

	/**
	 * Searches for a operator with the given id only downstream from the given
	 * operator
	 * 
	 * @param endParallelizationId
	 * @param logicalOperator
	 * @return the operator or null if not found
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator findDownstreamOperatorWithId(
			String endParallelizationId, ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				endParallelizationId);
		GenericDownstreamGraphWalker walker = new GenericDownstreamGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(endParallelizationId);
	}

	/**
	 * Searches for a operator with the given id only upstream from the given
	 * operator
	 * 
	 * @param endParallelizationId
	 * @param logicalOperator
	 * @return the operator or null if not found
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator findUpstreamOperatorWithId(
			String endParallelizationId, ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				endParallelizationId);
		GenericUpstreamGraphWalker walker = new GenericUpstreamGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(endParallelizationId);
	}

	/**
	 * Searches for a operator with the given id upstream and downstream from
	 * the given operator
	 * 
	 * @param operatorId
	 * @param logicalOperator
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator findOperatorWithId(String operatorId,
			ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				operatorId);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(operatorId);
	}

	/**
	 * checks if the combination of start end end id of the operators is valid.
	 * Only if the endoperator id is on downstream the combination is valid
	 * 
	 * @param startParallelizationId
	 * @param endParallelizationId
	 * @param logicalPlan
	 * @return
	 */
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

	/**
	 * calculates a new source out port. This is needed because a output port
	 * can be subscribed only to one other port. If the existing subscription
	 * could not be unsubscribed, but another operator needs to be subscribed, a
	 * new output port need to be calculated and checked if this port is not in
	 * use
	 * 
	 * @param sourceSubscription
	 * @param iteration
	 * @return
	 */
	public static int calculateNewSourceOutPort(
			LogicalSubscription sourceSubscription, int iteration) {
		Map<Integer, SDFSchema> outputSchemaMap = sourceSubscription
				.getSource().getOutputSchemaMap();
		int newSourceOutPort = outputSchemaMap.size() + iteration;
		// if this source out port is already in use, try another one
		while (outputSchemaMap.containsKey(newSourceOutPort)) {
			newSourceOutPort++;
		}

		return newSourceOutPort;
	}

	/**
	 * Validates a given select operator. the select operator normally is a
	 * stateless operator, but can contain stateful functions. an exeception is
	 * thrown if assureSemanticCorrectness is set to true
	 * 
	 * @param assureSemanticCorrectness
	 * @param currentOperator
	 * @return
	 * @throws SemanticChangeException
	 */
	public static void validateSelectAO(boolean assureSemanticCorrectness,
			ILogicalOperator currentOperator) throws SemanticChangeException {
		SelectAO selectOperator = (SelectAO) currentOperator;
		IPredicate<?> predicate = selectOperator.getPredicate();
		if (predicate instanceof IRelationalExpression) {
			RelationalExpression<?> relPredicate = (RelationalExpression<?>) predicate;
			IMepExpression<?> expression = relPredicate.getMEPExpression();
			if (SDFAttributeHelper
					.expressionContainsStatefulFunction(expression)) {
				if (assureSemanticCorrectness) {
					throw new IllegalArgumentException(
							"No operators with stateful functions allowed "
									+ "between start and end of parallelization");
				} else {
					throw new SemanticChangeException(
							"The path between start and end operator contains select"
							+ " operator with stateful functions");
				}
			}
		}
	}

	/**
	 * Validates a given map operator. the map operator normally is a stateless
	 * operator, but can contain stateful functions. an exeception is thrown if
	 * assureSemanticCorrectness is set to true
	 * 
	 * @param assureSemanticCorrectness
	 * @param currentOperator
	 * @return
	 * @throws SemanticChangeException
	 */
	public static void validateMapAO(boolean assureSemanticCorrectness,
			ILogicalOperator currentOperator) throws SemanticChangeException {
		MapAO mapOperator = (MapAO) currentOperator;
		List<SDFExpression> expressionList = mapOperator.getExpressionList();
		for (SDFExpression sdfExpression : expressionList) {
			IMepExpression<?> mepExpression = sdfExpression.getMEPExpression();
			if (SDFAttributeHelper
					.expressionContainsStatefulFunction(mepExpression)) {
				if (assureSemanticCorrectness) {
					throw new IllegalArgumentException(
							"No operators with stateful functions allowed "
									+ "between start and end of parallelization");
				} else {
					throw new SemanticChangeException(
							"The path between start and end operator "
							+ "contains map operator with stateful functions");
				}
			}
		}
	}

	/**
	 * returns the next operator on downstream side if there is only one
	 * subscription. if there are more than one downstream operators an
	 * execption is thrown because this is not allowed between start and end
	 * operator in inter operator parallelization or in optimization
	 * 
	 * @param currentOperator
	 * @return
	 */
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
			return subscriptions.get(0).getSink();
		}
	}

	/**
	 * checks if the way between two operators is valid. checks if there are no
	 * splits in logical plan between these operators or stateful operators or
	 * functions between these operators. an exeception is thrown if
	 * assureSemanticCorrectness is set to true
	 * 
	 * @param operatorForTransformation
	 * @param aggregatesWithGroupingAllowed
	 * @param endParallelizationId
	 * @param assureSemanticCorrectness
	 */
	public static boolean checkWayToEndPoint(
			ILogicalOperator operatorForTransformation,
			String endParallelizationId, boolean assureSemanticCorrectness) {
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

					// validation if stateful operators or stateful functions
					// exists is only needed if semantic correctness is needed
					try {
						if (currentOperator instanceof IStatefulAO) {
							if (assureSemanticCorrectness) {
								throw new IllegalArgumentException(
										"No stateful operators allowed "
												+ "between start and end of parallelization");
							} else {
								throw new SemanticChangeException(
										"The path between start and end operator contains stateful operators");
							}
						} else if (currentOperator instanceof SelectAO) {
							LogicalGraphHelper.validateSelectAO(
									assureSemanticCorrectness, currentOperator);
						} else if (currentOperator instanceof MapAO) {
							LogicalGraphHelper.validateMapAO(
									assureSemanticCorrectness, currentOperator);
						}
					} catch (SemanticChangeException sce) {
						INFO_SERVICE
								.info("Parallelization between start and end id possibly "
										+ "results in a semantic change of the given plan. Cause: "
										+ sce.getMessage());
					}

					// if parameter with id is reached, parallelization is done
					if (currentOperator.getUniqueIdentifier() != null) {
						if (currentOperator.getUniqueIdentifier()
								.equalsIgnoreCase(endParallelizationId)) {
							return true;
						}
					}

					// if end operator is not reached, we need to get the next
					// operator
					currentOperator = LogicalGraphHelper
							.getNextOperator(currentOperator);
				}
			}
		}
		return false;
	}
}
