/**
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.mahout.learner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.AbstractRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.ItemAverageRecommender;
import org.apache.mahout.cf.taste.impl.recommender.ItemUserAverageRecommender;
import org.apache.mahout.cf.taste.impl.recommender.RandomRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.ParallelSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.RatingSGDFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.mahout.recommender.MahoutRecommender;
import de.uniol.inf.is.odysseus.recommendation.util.ObjectIdToLongId;

/**
 * @author Cornelius Ludmann
 * 
 */
public class MahoutRecommendationLearner<M extends ITimeInterval> implements
RecommendationLearner<M> {

	/**
	 * Possible recommender by Mahout (all of type {@link AbstractRecommender}).
	 * 
	 * @author Cornelius Ludmann
	 */
	private enum OptionRecommender {
		GenericItemBasedRecommender, GenericUserBasedRecommender, ItemAverageRecommender, ItemUserAverageRecommender, RandomRecommender, SVDRecommender
	}

	/**
	 * Possible similarity metrics for item similarity by Mahout (all of type
	 * {@link ItemSimilarity}).
	 * 
	 * @author Cornelius Ludmann
	 */
	private enum OptionItemSimilarity {
		EuclideanDistanceSimilarity, PearsonCorrelationSimilarity, UncenteredCosineSimilarity, CityBlockSimilarity, LogLikelihoodSimilarity, TanimotoCoefficientSimilarity
	}

	/**
	 * Possible similarity metrics for item similarity by Mahout (all of type
	 * {@link UserSimilarity}).
	 * 
	 * @author Cornelius Ludmann
	 */
	private enum OptionUserSimilarity {
		EuclideanDistanceSimilarity, PearsonCorrelationSimilarity, UncenteredCosineSimilarity, CityBlockSimilarity, LogLikelihoodSimilarity, SpearmanCorrelationSimilarity, TanimotoCoefficientSimilarity
	}

	/**
	 * Possible user neighborhoods by Mahout (all of type
	 * {@link UserNeighborhood}).
	 * 
	 * @author Cornelius Ludmann
	 */
	private enum OptionUserNeighborhood {
		NearestNUserNeighborhood, ThresholdUserNeighborhood
	}

	/**
	 * Possible factorizer by Mahout (all of type {@link Factorizer}).
	 * 
	 * @author Cornelius Ludmann
	 */
	private enum OptionFactorizer {
		ALSWRFactorizer, ParallelSGDFactorizer, RatingSGDFactorizer, SVDPlusPlusFactorizer
	}

	public static final String NAME = "Mahout";

	private static final String N_NEIGHBORHOOD_USER = "n_neighborhood_user";

	private static final String THRESHOLD_NEIGHBORHOOD_USER = "threshold_neighborhood_user";

	private static final String NUM_FEATURES = "num_features";

	private static final String LAMBDA = "lambda";

	private static final String NUM_ITERATIONS = "num_iterations";

	private static final String NUM_EMPOCHS = "num_empochs";

	private int userAttributeIndex = -1;
	private int itemAttributeIndex = -1;
	private int ratingAttributeIndex = -1;

	private Map<String, String> options;

	/**
	 * @param inputschema
	 *            The schema of the input tuples.
	 * @param userAttribute
	 *            The attribute that denotes the user id.
	 * @param itemAttribute
	 *            The attribute that denotes the item id.
	 * @param ratingAttribute
	 *            The attribute that denotes the rating.
	 * @param options
	 *            The options for the learner (optional, can be null)
	 */
	public MahoutRecommendationLearner(final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options) {
		this.userAttributeIndex = inputschema.indexOf(userAttribute);
		this.itemAttributeIndex = inputschema.indexOf(itemAttribute);
		this.ratingAttributeIndex = inputschema.indexOf(ratingAttribute);
		if (options == null) {
			this.options = null;
		} else {
			this.options = new HashMap<String, String>(options);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #createRecommender(java.util.List)
	 */
	@Override
	public de.uniol.inf.is.odysseus.recommendation.recommender.Recommender createRecommender(
			final List<Tuple<M>> tuples) {
		if (userAttributeIndex == -1 || itemAttributeIndex == -1
				|| ratingAttributeIndex == -1) {
			throw new IllegalStateException(
					"user, item, or rating attribute index is not set.");
		}

		if (tuples == null || tuples.size() == 0) {
			return null;
		}

		try {

			final DataModel dataModel = toDataModel(tuples);
			final Recommender recommender = getRecommender(dataModel);
			System.out.println("RECOMMENDER: " + recommender);
			return new MahoutRecommender(recommender);

		} catch (final TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param dataModel
	 *            DataModel
	 * @return
	 * @throws TasteException
	 */
	private Recommender getRecommender(final DataModel dataModel)
			throws TasteException {
		Recommender recommender = null;

		final OptionRecommender optionRecommender = getEnumOption(
				OptionRecommender.class,
				OptionRecommender.GenericItemBasedRecommender.name());

		switch (optionRecommender) {
		case GenericItemBasedRecommender:
			final ItemSimilarity itemSimilarity = getItemSimilarity(dataModel);
			recommender = new GenericItemBasedRecommender(dataModel,
					itemSimilarity);
			break;
		case GenericUserBasedRecommender:
			final UserSimilarity similarity = getUserSimilarity(dataModel);
			final UserNeighborhood neighborhood = getUserNeighborhood(
					dataModel, similarity);
			recommender = new GenericUserBasedRecommender(dataModel,
					neighborhood, similarity);
			break;
		case ItemAverageRecommender:
			recommender = new ItemAverageRecommender(dataModel);
			break;
		case ItemUserAverageRecommender:
			recommender = new ItemUserAverageRecommender(dataModel);
			break;
		case RandomRecommender:
			recommender = new RandomRecommender(dataModel);
			break;
		case SVDRecommender:
			final UserSimilarity similarity2 = getUserSimilarity(dataModel);
			final Factorizer factorizer = getFactorizer(dataModel, similarity2);
			recommender = new SVDRecommender(dataModel, factorizer);
			break;
		}

		return recommender;
	}

	/**
	 * @return
	 * @throws TasteException
	 */
	private ItemSimilarity getItemSimilarity(final DataModel dataModel)
			throws TasteException {
		final OptionItemSimilarity optionItemSimilarity = getEnumOption(
				OptionItemSimilarity.class,
				OptionItemSimilarity.EuclideanDistanceSimilarity.name());
		ItemSimilarity itemSimilarity = null;
		switch (optionItemSimilarity) {
		case EuclideanDistanceSimilarity:
			itemSimilarity = new EuclideanDistanceSimilarity(dataModel);
			break;
		case CityBlockSimilarity:
			itemSimilarity = new CityBlockSimilarity(dataModel);
			break;
		case LogLikelihoodSimilarity:
			itemSimilarity = new LogLikelihoodSimilarity(dataModel);
			break;
		case PearsonCorrelationSimilarity:
			itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
			break;
		case TanimotoCoefficientSimilarity:
			itemSimilarity = new TanimotoCoefficientSimilarity(dataModel);
			break;
		case UncenteredCosineSimilarity:
			itemSimilarity = new UncenteredCosineSimilarity(dataModel);
			break;
		}

		return itemSimilarity;
	}

	private UserSimilarity getUserSimilarity(final DataModel dataModel)
			throws TasteException {
		final OptionUserSimilarity optionUserSimilarity = getEnumOption(
				OptionUserSimilarity.class,
				OptionUserSimilarity.EuclideanDistanceSimilarity.name());
		UserSimilarity similarity = null;
		switch (optionUserSimilarity) {
		case EuclideanDistanceSimilarity:
			similarity = new EuclideanDistanceSimilarity(dataModel);
			break;
		case CityBlockSimilarity:
			similarity = new CityBlockSimilarity(dataModel);
			break;
		case LogLikelihoodSimilarity:
			similarity = new LogLikelihoodSimilarity(dataModel);
			break;
		case PearsonCorrelationSimilarity:
			similarity = new PearsonCorrelationSimilarity(dataModel);
			break;
		case TanimotoCoefficientSimilarity:
			similarity = new TanimotoCoefficientSimilarity(dataModel);
			break;
		case UncenteredCosineSimilarity:
			similarity = new UncenteredCosineSimilarity(dataModel);
			break;
		case SpearmanCorrelationSimilarity:
			similarity = new SpearmanCorrelationSimilarity(dataModel);
		}

		return similarity;
	}

	private UserNeighborhood getUserNeighborhood(final DataModel dataModel,
			final UserSimilarity userSimilarity) throws TasteException {
		final OptionUserNeighborhood optionNh = getEnumOption(
				OptionUserNeighborhood.class,
				OptionUserNeighborhood.NearestNUserNeighborhood.name());

		UserNeighborhood nh = null;

		switch (optionNh) {
		case NearestNUserNeighborhood:
			final int n = getIntOption(N_NEIGHBORHOOD_USER, 5);
			nh = new NearestNUserNeighborhood(n, userSimilarity, dataModel);
			break;
		case ThresholdUserNeighborhood:
			final double threshold = getDoubleOption(
					THRESHOLD_NEIGHBORHOOD_USER, 0.7);
			nh = new ThresholdUserNeighborhood(threshold, userSimilarity,
					dataModel);
			break;
		}

		return nh;
	}

	private Factorizer getFactorizer(final DataModel dataModel,
			final UserSimilarity userSimilarity) throws TasteException {
		final OptionFactorizer optionFact = getEnumOption(
				OptionFactorizer.class, OptionFactorizer.ALSWRFactorizer.name());

		Factorizer factorizer = null;

		// TODO: find good defaults
		final int numFeatures = getIntOption(NUM_FEATURES, 50);
		final double lambda = getDoubleOption(LAMBDA, 0.02);
		final int numIterations = getIntOption(NUM_ITERATIONS, 100);
		switch (optionFact) {
		case ALSWRFactorizer:
			// TODO: more options
			factorizer = new ALSWRFactorizer(dataModel, numFeatures, lambda,
					numIterations);
			break;
		case ParallelSGDFactorizer:
			// TODO: find good defaults
			final int numEpochs = getIntOption(NUM_EMPOCHS, 10);
			// TODO: more options
			factorizer = new ParallelSGDFactorizer(dataModel, numFeatures,
					lambda, numEpochs);
			break;
		case RatingSGDFactorizer:
			// TODO: more options
			factorizer = new RatingSGDFactorizer(dataModel, numFeatures,
					numIterations);
			break;
		case SVDPlusPlusFactorizer:
			// TODO: more options
			factorizer = new SVDPlusPlusFactorizer(dataModel, numFeatures,
					numIterations);
			break;
		}

		return factorizer;
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private int getIntOption(final String key, final int defaultValue) {
		if (options == null || !options.containsKey(key)) {
			return defaultValue;
		}
		final String val = options.get(key);
		try {
			final int v = Integer.parseInt(val);
			return v;
		} catch (final NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return defaultValue;
	}

	private double getDoubleOption(final String key, final double defaultValue) {
		if (options == null || !options.containsKey(key)) {
			return defaultValue;
		}
		final String val = options.get(key);
		try {
			final double v = Double.parseDouble(val);
			return v;
		} catch (final NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * 
	 * @param enumeration
	 * @param defaultValue
	 * @return
	 */
	private <T extends Enum<T>> T getEnumOption(final Class<T> enumeration,
			final String defaultValue) {
		if (options == null
				|| !options.containsKey(enumeration.getSimpleName())) {
			return Enum.valueOf(enumeration, defaultValue);
		}
		return Enum.valueOf(enumeration,
				options.get(enumeration.getSimpleName()));
	}

	/**
	 * Converts the tuples to a Mahout {@link DataModel}.
	 * 
	 * @param tuples
	 *            The tuples to convert.
	 * @return The data model.
	 */
	private DataModel toDataModel(final List<Tuple<M>> tuples) {

		final Map<Object, List<Tuple<M>>> userItemMap = toUserPartition(tuples);

		final FastByIDMap<PreferenceArray> userdata = new FastByIDMap<PreferenceArray>(
				userItemMap.size());
		final FastByIDMap<FastByIDMap<Long>> timestamps = new FastByIDMap<FastByIDMap<Long>>(
				userItemMap.size());

		for (final Object user : userItemMap.keySet()) {
			final List<Tuple<M>> userItems = userItemMap.get(user);

			final PreferenceArray preferenceArray = new GenericUserPreferenceArray(
					userItems.size());
			final FastByIDMap<Long> userTimestamps = new FastByIDMap<Long>(
					userItems.size());

			int itemNo = 0;
			for (final Tuple<M> tuple : userItems) {
				assert user.equals(tuple.getAttribute(userAttributeIndex));
				// assert user == tuple.getAttribute(userAttributeIndex);
				final Object item = tuple.getAttribute(itemAttributeIndex);
				final double rating = tuple.getAttribute(ratingAttributeIndex);
				final long timestamp = tuple.getMetadata().getStart()
						.getMainPoint();

				preferenceArray.setUserID(itemNo, ObjectIdToLongId
						.getInstance().objectIDAsLong("user", user));
				preferenceArray.setItemID(itemNo, ObjectIdToLongId
						.getInstance().objectIDAsLong("item", item));
				preferenceArray.setValue(itemNo, (float) rating);

				userTimestamps.put(ObjectIdToLongId.getInstance()
						.objectIDAsLong("item", item), timestamp);

				++itemNo;
			}
			userdata.put(
					ObjectIdToLongId.getInstance().objectIDAsLong("user", user),
					preferenceArray);
			timestamps
			.put(ObjectIdToLongId.getInstance().objectIDAsLong("user",
					user), userTimestamps);
		}

		return new GenericDataModel(userdata, timestamps);
	}

	/**
	 * This method partition the tuples by users.
	 * 
	 * @param tuples
	 *            The tuples to partitioned.
	 * @return The partitioned tuples.
	 */
	private Map<Object, List<Tuple<M>>> toUserPartition(
			final List<Tuple<M>> tuples) {
		final Map<Object, List<Tuple<M>>> userItemMap = new HashMap<Object, List<Tuple<M>>>();

		for (final Tuple<M> tuple : tuples) {
			final Object user = tuple.getAttribute(userAttributeIndex);

			List<Tuple<M>> tuplesOfUser;
			if (userItemMap.containsKey(user)) {
				tuplesOfUser = userItemMap.get(user);
			} else {
				tuplesOfUser = new LinkedList<Tuple<M>>();
			}
			tuplesOfUser.add(tuple);

			userItemMap.put(user, tuplesOfUser);
		}

		return userItemMap;
	}

}
