package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.parallelization.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.ParallelOperatorSettings;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;

public abstract class AbstractParallelTransformationStrategy<T extends ILogicalOperator>
		implements IParallelTransformationStrategy<T> {

	@SuppressWarnings("unchecked")
	public Class<T> getOperatorType() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	protected boolean areSettingsValid(
			ParallelOperatorSettings settingsForOperator) {
		if (settingsForOperator.getDegreeOfParallelization() == 1) {
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
		fragmentAO.setName(fragmentAO.getName() + namePostfix); // set postfix
		fragmentAO.setUniqueIdentifier(UUID.randomUUID().toString());

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

		return fragmentAO;
	}

	protected void checkIfWayToEndPointIsValid(
			ILogicalOperator operatorForTransformation,
			ParallelOperatorSettings settingsForOperator,
			boolean aggregatesWithGroupingAllowed) {

		String endParallelizationId = settingsForOperator
				.getEndParallelizationId();
		boolean assureSemanticCorrectness = settingsForOperator
				.isAssureSemanticCorrectness();

		LogicalGraphHelper.checkWayToEndPoint(operatorForTransformation,
				aggregatesWithGroupingAllowed, endParallelizationId,
				assureSemanticCorrectness);

	}

	protected ILogicalOperator doPostParallelization(
			ILogicalOperator existingOperator, ILogicalOperator newOperator,
			String endOperatorId, int iteration,
			List<AbstractFragmentAO> fragments,
			ParallelOperatorSettings settingsForOperator) {

		ILogicalOperator lastClonedOperator = newOperator;
		ILogicalOperator currentExistingOperator = LogicalGraphHelper
				.getNextOperator(existingOperator);
		ILogicalOperator lastExistingOperator = existingOperator;

		while (currentExistingOperator != null) {

			ILogicalOperator currentClonedOperator = currentExistingOperator
					.clone();
			currentClonedOperator.setName(currentClonedOperator.getName() + "_"
					+ iteration);
			currentClonedOperator.setUniqueIdentifier(currentClonedOperator
					.getUniqueIdentifier() + "_" + iteration);

			CopyOnWriteArrayList<LogicalSubscription> operatorSourceSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
			operatorSourceSubscriptions.addAll(currentExistingOperator
					.getSubscribedToSource());
			for (LogicalSubscription sourceSubscription : operatorSourceSubscriptions) {
				if (sourceSubscription.getTarget().equals(lastExistingOperator)) {
					// if target of subscription is last existing operator, set
					// new cloned one
					currentClonedOperator.subscribeToSource(lastClonedOperator,
							sourceSubscription.getSinkInPort(),
							sourceSubscription.getSourceOutPort(),
							lastClonedOperator.getOutputSchema());
				} else {
					// else connect new copied operator to target
					int newSourceOutPort = LogicalGraphHelper
							.calculateNewSourceOutPort(sourceSubscription,
									iteration);

					currentClonedOperator.subscribeToSource(sourceSubscription
							.getTarget(), sourceSubscription.getSinkInPort(),
							newSourceOutPort, sourceSubscription.getTarget()
									.getOutputSchema());
				}
			}

			doStrategySpecificPostParallelization(newOperator,
					currentExistingOperator, currentClonedOperator, iteration,
					fragments, settingsForOperator);

			// if end operator is reached, break loop and return last cloned
			// operator
			lastClonedOperator = currentClonedOperator;
			if (currentExistingOperator.getUniqueIdentifier() != null) {
				if (currentExistingOperator.getUniqueIdentifier()
						.equalsIgnoreCase(endOperatorId)) {
					break;
				}
			}
			lastExistingOperator = currentExistingOperator;
			currentExistingOperator = LogicalGraphHelper
					.getNextOperator(currentExistingOperator);
		}
		return lastClonedOperator;
	}

	protected abstract void doStrategySpecificPostParallelization(
			ILogicalOperator parallelizedOperator,
			ILogicalOperator currentExistingOperator,
			ILogicalOperator currentClonedOperator, int iteration,
			List<AbstractFragmentAO> fragments,
			ParallelOperatorSettings settingsForOperator);
}
