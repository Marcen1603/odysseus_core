package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.INonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.aggregation.transform.TAggregationAORule;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.temporaltypes.aggregationfunctions.TemporalIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.temporaltypes.merge.PredictionTimesMetadataUnionMergeFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;

/**
 * Changes a few things at the aggregation in case that it has to work with
 * temporal attributes.
 * 
 * @author Tobias Brandt
 *
 */
public class TTemporalAggregationAORule extends TAggregationAORule {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected IMetadataMergeFunction getMetadataMergeFunction(AggregationAO operator) {
		List<String> metadataSet = operator.getInputSchema().getMetaAttributeNames();
		// Attention: Time meta data is set in aggregation
		metadataSet.remove(ITimeInterval.class.getName());
		// Search for the merge function for ValidTimes manually
		metadataSet.remove(IPredictionTimes.class.getName());

		// Create the merge function for all the other metadatas (maybe zero)
		CombinedMergeFunction cmf = (CombinedMergeFunction) MetadataRegistry.getMergeFunction(metadataSet);

		/*
		 * And add the union merge for the ValidTimes (we need this for the aggregation
		 * instead of the normal merge function which makes an intersection)
		 */
		PredictionTimesMetadataUnionMergeFunction validTimesMetadataUnionMergeFunction = new PredictionTimesMetadataUnionMergeFunction();
		cmf.add(validTimesMetadataUnionMergeFunction);

		return cmf;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<INonIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> getNonIncrementalFunction(
			List<IAggregationFunction> allFunctions, SDFSchema inputSchema) {
		return allFunctions.stream().filter(f -> !f.isIncremental())
				.map(f -> ((INonIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>) f))
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> getIncrementalFunction(
			List<IAggregationFunction> allFunctions, AggregationAO operator) {
		return allFunctions.stream().filter(f -> f.isIncremental())
				.map(f -> (new TemporalIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>(
						(AbstractIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>) f,
						operator.getBaseTimeUnit())))
				.collect(Collectors.toList());
	}

	@Override
	public boolean isExecutable(final AggregationAO operator, final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && inputSchemaContainsTemporalAttribute(operator.getInputSchema());
	}

	protected boolean inputSchemaContainsTemporalAttribute(SDFSchema schema) {
		long numberOfTemporalAttributes = schema.getAttributes().stream()
				.filter(attribute -> TemporalDatatype.isTemporalAttribute(attribute)).count();
		return numberOfTemporalAttributes > 0;
	}

	@Override
	public int getPriority() {
		return 1;
	}

}
