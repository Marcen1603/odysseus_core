package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.util.GenericDownstreamGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.OperatorIdLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.multithreaded.helper.SDFAttributeHelper;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;

public abstract class AbstractMultithreadedTransformationStrategy<T extends ILogicalOperator>
		implements IMultithreadedTransformationStrategy<T> {

	@SuppressWarnings("unchecked")
	public Class<T> getOperatorType() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	
	protected boolean areSettingsValid(MultithreadedOperatorSettings settingsForOperator){
		if (settingsForOperator.getDegreeOfParallelization() == 1){
			return false;
		}
		return true;
	}

	protected AbstractFragmentAO createFragmentAO(
			Class<? extends AbstractFragmentAO> fragmentClass,
			int degreeOfParallelization, String namePostfix,
			List<SDFAttribute> hashAttributes, String rangeAttributeString,
			List<String> rangeRanges) throws InstantiationException,
			IllegalAccessException {
		AbstractFragmentAO fragmentAO = fragmentClass.newInstance();
		fragmentAO.setNumberOfFragments(degreeOfParallelization);
		

		if (fragmentAO instanceof HashFragmentAO) {
			HashFragmentAO hashFragmentAO = (HashFragmentAO) fragmentAO;
			if (hashAttributes != null && !hashAttributes.isEmpty()) {
				hashFragmentAO.setAttributes(hashAttributes);
			} else {
				throw new IllegalArgumentException(
						"Attributes must not be null for creating HashFragment");
			}
		} else if (fragmentAO instanceof RangeFragmentAO) {
			RangeFragmentAO rangeFragmentAO = (RangeFragmentAO) fragmentAO;
			if (rangeAttributeString != null && rangeRanges != null
					&& !rangeAttributeString.isEmpty()
					&& !rangeRanges.isEmpty()) {
				rangeFragmentAO.setRanges(rangeRanges);
				rangeFragmentAO.setAttribute(rangeAttributeString);
			}
		}
		// set postfix
		fragmentAO.setName(fragmentAO.getName()+namePostfix);

		return fragmentAO;
	}

	protected ILogicalOperator getNextOperator(ILogicalOperator currentOperator) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ILogicalOperator findOperatorWithId(String endParallelizationId,
			ILogicalOperator logicalOperator) {
		OperatorIdLogicalGraphVisitor<ILogicalOperator> idVisitor = new OperatorIdLogicalGraphVisitor<ILogicalOperator>(
				endParallelizationId);
		GenericDownstreamGraphWalker walker = new GenericDownstreamGraphWalker();
		walker.prefixWalk(logicalOperator, idVisitor);
		return idVisitor.getResult().get(endParallelizationId);
	}
	
	protected void checkIfWayToEndPointIsValid(
			ILogicalOperator operatorForTransformation,
			MultithreadedOperatorSettings settingsForOperator,
			boolean aggregatesWithGroupingAllowed) {
		if (!settingsForOperator.getEndParallelizationId().isEmpty()) {
			ILogicalOperator endOperator = findOperatorWithId(
					settingsForOperator.getEndParallelizationId(),
					operatorForTransformation);
			if (endOperator == null) {
				// end operator id is invalid
				throw new IllegalArgumentException("End operator with id"
						+ settingsForOperator.getEndParallelizationId()
						+ " does not exist.");
			} else {
				ILogicalOperator currentOperator = getNextOperator(operatorForTransformation);
				while (currentOperator != null) {
					if (currentOperator instanceof IStatefulAO) {
						if (currentOperator instanceof AggregateAO) {
							AggregateAO aggregateOperator = (AggregateAO) currentOperator;
							if (!aggregateOperator.getGroupingAttributes()
									.isEmpty()
									&& !aggregatesWithGroupingAllowed) {
								throw new IllegalArgumentException(
										"No aggregations with grouping allowed between start and end of parallelization");
							}
						} else {
							// stateful operators are not allowed
							throw new IllegalArgumentException(
									"No stateful operators allowed between start and end of parallelization");
						}
					} else if (currentOperator instanceof SelectAO) {
						SelectAO selectOperator = (SelectAO) currentOperator;
						IPredicate<?> predicate = selectOperator.getPredicate();
						if (predicate instanceof RelationalPredicate) {
							RelationalPredicate relPredicate = (RelationalPredicate) predicate;
							IExpression<?> expression = relPredicate
									.getExpression().getMEPExpression();
							if (SDFAttributeHelper.expressionContainsStatefulFunction(expression)) {
								// stateful functions not allowed
								throw new IllegalArgumentException(
										"No operators with stateful functions allowed between start and end of parallelization");
							}
						}
					} else if (currentOperator instanceof MapAO) {
						MapAO mapOperator = (MapAO) currentOperator;
						List<SDFExpression> expressionList = mapOperator
								.getExpressionList();
						for (SDFExpression sdfExpression : expressionList) {
							IExpression<?> mepExpression = sdfExpression
									.getMEPExpression();
							if (SDFAttributeHelper.expressionContainsStatefulFunction(mepExpression)) {
								// stateful functions not allowed
								throw new IllegalArgumentException(
										"No operators with stateful functions allowed between start and end of parallelization");
							}
						}
					} else if (currentOperator.isSinkOperator()) {
						if (currentOperator.getUniqueIdentifier()
								.equalsIgnoreCase(
										settingsForOperator
												.getEndParallelizationId())) {
							// sink operators must not be parallelized, abort
							throw new IllegalArgumentException(
									"Sink operator for end of parallelization is not allowed");
						} else {
							// sink is reached but operator not found, abort
							throw new IllegalArgumentException(
									"Sink reached, but operator not found. Parallelization with defined endpoint not possible.");
						}
					}

					if (currentOperator.getUniqueIdentifier() != null){
						if (currentOperator.getUniqueIdentifier().equalsIgnoreCase(
								settingsForOperator.getEndParallelizationId())) {
							// all operators including end operator are valid,
							// validation successful
							return;
						}						
					}
					currentOperator = getNextOperator(currentOperator);
				}
			}
		}
	}
}
