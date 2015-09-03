package de.uniol.inf.is.odysseus.parallelization.interoperator.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.exception.ParallelizationStrategyException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.TransformationResult.State;
import de.uniol.inf.is.odysseus.relational_interval.logicaloperator.RelationalFastMedianAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class FastMedianTransformationStrategy extends
		AbstractGroupedOperatorTransformationStrategy<RelationalFastMedianAO> {

	@Override
	public String getName() {
		return "FastMedianTransformationStrategy";
	}

	@Override
	public int evaluateCompatibility(RelationalFastMedianAO operator) {
		if (!operator.getGroupingAttributes().isEmpty()) {
			// only if the given operator has a grouping, this strategy
			// works
			return 100;
		}

		// if operator has no grouping, this strategy is
		// incompatible
		return 0;
	}

	@Override
	public TransformationResult transform(RelationalFastMedianAO operator,
			ParallelOperatorConfiguration configurationForOperator) {
		super.operator = operator;
		super.configuration = configurationForOperator;
		super.transformationResult = new TransformationResult(State.SUCCESS);

		try {
			super.doValidation();

			// prepare and validate values
			prepareTransformation();

			// create fragment operator
			createFragmentOperator();
		} catch (ParallelizationStrategyException pse) {
			transformationResult.setState(State.FAILED);
			return transformationResult;
		}

		// subscribe fragment operator
		subscribeFragementAO();

		// create union operator
		createUnionOperator();

		// for every degree, clone existing operator and connect it to
		// fragement and union operator
		for (int i = 0; i < configuration.getDegreeOfParallelization(); i++) {
			// create buffer operator
			BufferAO buffer = createBufferOperator(i);
			// clone existing aggregate operator
			RelationalFastMedianAO newAggregateOperator = cloneFastMedianOperator(i);
			buffer.subscribeToSource(fragmentAO, 0, i,
					fragmentAO.getOutputSchema());
			newAggregateOperator.subscribeToSource(buffer, 0, 0,
					buffer.getOutputSchema());
			doPostParallelizationIfNeeded(i, newAggregateOperator);
		}
		// connect fragmented data stream with union
		doFinalConnection();
		return transformationResult;
	}
	
	private RelationalFastMedianAO cloneFastMedianOperator(int i) {
		RelationalFastMedianAO newFastMedianOperator = operator.clone();
		newFastMedianOperator.setName(operator.getName() + "_" + i);
		newFastMedianOperator.setUniqueIdentifier(UUID.randomUUID().toString());
		return newFastMedianOperator;
	}

	/**
	 * prepares transformation and validates values
	 * 
	 * @param groupingAttributes
	 * @throws ParallelizationStrategyException
	 */
	private void prepareTransformation()
			throws ParallelizationStrategyException {
		// if there is no grouping
		groupingAttributes = operator.getGroupingAttributes();
		if (groupingAttributes.isEmpty()) {
			throw new ParallelizationStrategyException(
					"Strategy needs gouping attributes");
		}

		transformationResult.setAllowsModificationAfterUnion(true);
	}
	
	/**
	 * returns a list of compatible fragmentation types (e.g. HashFragementAO)
	 * 
	 * @return
	 */
	@Override
	public List<Class<? extends AbstractStaticFragmentAO>> getAllowedFragmentationTypes() {
		List<Class<? extends AbstractStaticFragmentAO>> allowedFragmentTypes = new ArrayList<Class<? extends AbstractStaticFragmentAO>>();
		allowedFragmentTypes.add(HashFragmentAO.class);
		return allowedFragmentTypes;
	}

	/**
	 * returns the preferred fragementation type. this is needed if the user
	 * doesnt select a fragmentation
	 * 
	 * @return
	 */
	@Override
	public Class<? extends AbstractStaticFragmentAO> getPreferredFragmentationType() {
		return HashFragmentAO.class;
	}

	@Override
	public IParallelTransformationStrategy<RelationalFastMedianAO> getNewInstance() {
		return new FastMedianTransformationStrategy();
	}
}
