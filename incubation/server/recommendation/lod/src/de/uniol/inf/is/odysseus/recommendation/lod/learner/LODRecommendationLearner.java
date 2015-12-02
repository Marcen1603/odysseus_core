package de.uniol.inf.is.odysseus.recommendation.lod.learner;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.AbstractTupleBasedRecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.lod.learner.LODRatingPredictor.UserItemRatingMap;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;

/**
 * @author Christopher Schwarz
 */
public class LODRecommendationLearner<T extends Tuple<M>, M extends IMetaAttribute, U, I, D> extends AbstractTupleBasedRecommendationLearner<T, M, U, I, Double> implements RecommendationLearner<T, M, U, I, Double> {

	private final int dataAttributeIndex;
	private RatingPredictor<T, M, U, I, Double> model;

	public LODRecommendationLearner(final SDFSchema inputschema,
									final SDFAttribute userAttribute,
									final SDFAttribute itemAttribute,
									final SDFAttribute ratingAttribute,
									final Map<String, String> options) {
		super(inputschema, userAttribute, itemAttribute, ratingAttribute);
		
		if(options.get("data") != null) {
			this.dataAttributeIndex = inputschema.findAttributeIndex(options.get("data"));
		} else {
			this.dataAttributeIndex = inputschema.findAttributeIndex("data");
		}

		if (this.dataAttributeIndex == -1) {
			throw new IllegalArgumentException("Data attribute not found.");
		}
	}

	@Override
	public RatingPredictor<T, M, U, I, Double> getModel(final boolean train) {
		if (!this.isModelUpToDate) {
			trainModel();
		}
		return this.model;
	}

	@Override
	public String getName() {
		return "LODLearner";
	}

	@Override
	public void trainModel() {
		final UserItemRatingMap<U, I> userItemRatings = new UserItemRatingMap<U, I>();
		final HashMap<I, D> additionalData = new HashMap<I, D>();
		
		for (final T tuple : this.learningData) {
			U user = getUserInTuple(tuple);
			I item = getItemInTuple(tuple);
			D data = getDataInTuple(tuple);
			double rating = getRatingInTuple(tuple);

			userItemRatings.addUserItemRating(user, item, rating);
			additionalData.put(item, data);
		}

		this.model = new LODRatingPredictor<>(userItemRatings, ImmutableMap.copyOf(additionalData));
		isModelUpToDate = true;
	}

	protected D getDataInTuple(final T tuple) {
		return tuple.getAttribute(this.dataAttributeIndex);
	}
}
