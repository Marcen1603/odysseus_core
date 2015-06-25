package de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.IStatefulAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RangeFragmentAO;

public abstract class AbstractMultithreadedTransformationStrategy<T extends ILogicalOperator>
		implements IMultithreadedTransformationStrategy<T> {

	final private InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(AbstractMultithreadedTransformationStrategy.class);

	@SuppressWarnings("unchecked")
	public Class<T> getOperatorType() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	protected boolean areSettingsValid(
			MultithreadedOperatorSettings settingsForOperator) {
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
			MultithreadedOperatorSettings settingsForOperator,
			boolean aggregatesWithGroupingAllowed) {
		if (settingsForOperator.getEndParallelizationId() != null
				&& !settingsForOperator.getEndParallelizationId().isEmpty()) {
			ILogicalOperator endOperator = LogicalGraphHelper
					.findDownstreamOperatorWithId(
							settingsForOperator.getEndParallelizationId(),
							operatorForTransformation);
			if (endOperator == null) {
				// end operator id is invalid
				ILogicalOperator upstreamOperator = LogicalGraphHelper
						.findUpstreamOperatorWithId(
								settingsForOperator.getEndParallelizationId(),
								operatorForTransformation);
				if (upstreamOperator != null) {
					throw new IllegalArgumentException("End operator with id "
							+ settingsForOperator.getEndParallelizationId()
							+ " need to be on downstream and not upstream.");
				} else {
					throw new IllegalArgumentException("End operator with id"
							+ settingsForOperator.getEndParallelizationId()
							+ " does not exist.");
				}
			} else {
				
				ILogicalOperator currentOperator = LogicalGraphHelper.getNextOperator(operatorForTransformation);
				while (currentOperator != null) {

					boolean possibleSemanticChange = false;

					// validation if stateful operators or stateful functions
					// exists is only needed if semantic correctness is needed
					if (currentOperator instanceof IStatefulAO) {
						possibleSemanticChange = LogicalGraphHelper.validateStatefulAO(
								settingsForOperator.isAssureSemanticCorrectness(),
								aggregatesWithGroupingAllowed, currentOperator,
								possibleSemanticChange);
					} else if (currentOperator instanceof SelectAO) {
						possibleSemanticChange = LogicalGraphHelper.validateSelectAO(
								settingsForOperator.isAssureSemanticCorrectness(), currentOperator,
								possibleSemanticChange);
					} else if (currentOperator instanceof MapAO) {
						possibleSemanticChange = LogicalGraphHelper.validateMapAO(
								settingsForOperator.isAssureSemanticCorrectness(), currentOperator,
								possibleSemanticChange);
					}

					// if parameter with id is reached, parallelization is done
					if (currentOperator.getUniqueIdentifier() != null) {
						if (currentOperator.getUniqueIdentifier()
								.equalsIgnoreCase(
										settingsForOperator
												.getEndParallelizationId())) {
							if (possibleSemanticChange
									&& !settingsForOperator
											.isAssureSemanticCorrectness()){
								INFO_SERVICE.info("Parallelization between start and end id possibly "
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
					currentOperator = LogicalGraphHelper.getNextOperator(currentOperator);
				}
			}
		}
	}

	protected ILogicalOperator doPostParallelization(
			ILogicalOperator existingOperator, ILogicalOperator newOperator,
			String endOperatorId, int iteration,
			List<AbstractFragmentAO> fragments,
			MultithreadedOperatorSettings settingsForOperator) {

		ILogicalOperator lastClonedOperator = newOperator;
		ILogicalOperator currentExistingOperator = LogicalGraphHelper.getNextOperator(existingOperator);
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
					int newSourceOutPort = LogicalGraphHelper.calculateNewSourceOutPort(
							sourceSubscription, iteration);

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
			currentExistingOperator = LogicalGraphHelper.getNextOperator(currentExistingOperator);
		}
		return lastClonedOperator;
	}

	protected abstract void doStrategySpecificPostParallelization(
			ILogicalOperator parallelizedOperator,
			ILogicalOperator currentExistingOperator,
			ILogicalOperator currentClonedOperator, int iteration,
			List<AbstractFragmentAO> fragments,
			MultithreadedOperatorSettings settingsForOperator);
}
