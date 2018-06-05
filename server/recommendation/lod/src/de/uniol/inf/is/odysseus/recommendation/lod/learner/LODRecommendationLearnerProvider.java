package de.uniol.inf.is.odysseus.recommendation.lod.learner;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.learner.TupleBasedRecommendationLearnerProvider;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.TrainRecSysModelPO;

public class LODRecommendationLearnerProvider implements TupleBasedRecommendationLearnerProvider<Tuple<ITimeInterval>, ITimeInterval, Object, Object, Double> {

	@Override
	public String getLearnerName() {
		return "LOD";
	}

	@Override
	public RecommendationLearner<Tuple<ITimeInterval>, ITimeInterval, Object, Object, Double> newInstanceOfLearner(
			final SDFSchema inputschema,
			final SDFAttribute userAttribute,
			final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options) {

		return new LODRecommendationLearner<>(inputschema, userAttribute, itemAttribute, ratingAttribute, options);
	}

	@Override
	public TrainRecSysModelPO<ITimeInterval, Object, Object, Double> newInstanceOfTrainRecSysModelPO(
			final SDFSchema inputschema,
			final SDFAttribute userAttribute,
			final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options,
			final boolean outputRecomCandObj) {

		final RecommendationLearner<Tuple<ITimeInterval>, ITimeInterval, Object, Object, Double> learner
			= newInstanceOfLearner(inputschema, userAttribute, itemAttribute, ratingAttribute, options);

		final TrainRecSysModelPO<ITimeInterval, Object, Object, Double> operator
			= new TrainRecSysModelPO<ITimeInterval, Object, Object, Double>(learner, outputRecomCandObj);

		return operator;
	}
}
