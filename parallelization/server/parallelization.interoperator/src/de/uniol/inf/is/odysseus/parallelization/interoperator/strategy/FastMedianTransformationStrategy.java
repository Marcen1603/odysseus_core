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

/**
 * Inter operator parallelization strategy for RelationalFastMedianAO operators,
 * which contains grouping and not calculate global median. this strategy used a
 * HashFragmentation on the input stream, clones the specific median operator
 * and combines the datastream with a union operator. the fragmentation is based
 * on the grouping attributes
 * 
 * @author ChrisToenjesDeye
 *
 */
public class FastMedianTransformationStrategy extends
		AbstractGroupedOperatorTransformationStrategy<RelationalFastMedianAO> {

	@Override
	public String getName() {
		return "FastMedianTransformationStrategy";
	}

	/**
	 * evaluates the compatibility of this strategy with a given operator. this
	 * strategy is compatible if the median contains a grouping and no global
	 * median calculation
	 * 
	 * @param operator
	 * @return
	 */
	@Override
	public int evaluateCompatibility(RelationalFastMedianAO operator) {
		if (!operator.getGroupingAttributes().isEmpty()
				&& !operator.isAppendGlobalMedian()) {
			// only if the given operator has a grouping and no global median,
			// this strategy
			// works
			return 100;
		}

		// if operator has no grouping, this strategy is
		// incompatible
		return 0;
	}

	/**
	 * do the specific transformation based on the configuration. Instance need
	 * to be initialized via newInstance method
	 * 
	 * @return
	 */
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

	/**
	 * clones the existing fast median operator, based on iteration for naming
	 * 
	 * @param i
	 * @return
	 */
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

	/**
	 * returns a new instance of this strategy
	 */
	@Override
	public IParallelTransformationStrategy<RelationalFastMedianAO> getNewInstance() {
		return new FastMedianTransformationStrategy();
	}
}
