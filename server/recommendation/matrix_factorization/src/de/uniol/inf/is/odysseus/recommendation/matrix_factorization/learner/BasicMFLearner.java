/**
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
package de.uniol.inf.is.odysseus.recommendation.matrix_factorization.learner;

import it.unimi.dsi.fastutil.longs.Long2DoubleMap;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap.Entry;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner;
import de.uniol.inf.is.odysseus.recommendation.matrix_factorization.model.MFModel;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.RatingPredictor;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.ImmutableRecommendationCandidatesSet;
import de.uniol.inf.is.odysseus.recommendation.model.recommendation_candidates_model.RecommendationCandidates;

/**
 * @author Cornelius Ludmann
 *
 */
@SuppressWarnings("unused")
public class BasicMFLearner
		implements
		RecommendationLearner<Tuple<ITimeInterval>, ITimeInterval, Long, Long, Double> {

	/**
	 * The index of the user, item, and rating attribute in the input tuples.
	 */
	protected final int userAttributeIndex, itemAttributeIndex,
			ratingAttributeIndex;

	/**
	 * The map that maps user to map of item to ratings. This is corresponds to
	 * the rating matrix.
	 */
	protected Long2ReferenceMap<Long2DoubleMap> userToItemToRatingMap;
	/**
	 * This map holds the items and the number of occurrences.
	 */
	protected Long2LongMap itemMultiset;
	/**
	 * true, if the model is trained, false otherwise
	 */
	protected boolean isModelTrained = false;

	/**
	 * The user and item features for the matrix factorization model.
	 */
	protected Long2ReferenceMap<double[]> userFeatures, itemFeatures;

	/**
	 * The global mean as fallback prediction value.
	 */
	protected double globalMean;
	/**
	 * The number of ratings to calculate the global mean.
	 */
	protected long noRatings;

	/**
	 * The number of features (algorithm parameter).
	 */
	protected int numOfFeatures = 20;

	/**
	 * The maximum number of steps until the algorithms stops (algorithm
	 * parameter).
	 */
	protected int maxSteps = 5000;
	/**
	 * The learning rate (algorithm parameter).
	 */
	protected double learningRate = 0.0002;
	/**
	 * The factor for the regularization influence (algorithm parameter).
	 */
	protected double regularizationFactor = 0.02;
	/**
	 * The algorithm stops when it converges, which means that the model error
	 * does not decrease anymore. This parameter controls when two model errors
	 * are regarded as equal.
	 */
	protected double convergenceTolerance = 0.0001;

	/**
	 * This parameter controls, if the model should be build from scratch (the
	 * feature vectors will be dropped for each model build) or if the model
	 * build should use the previous feature vectors as starting point.
	 */
	protected boolean buildFromScratch = false;

	protected boolean resetChangedFeatureVectors = false;

	/**
	 * The random generator to init the feature vectors.
	 */
	protected Random random = new Random();

	private Map<String, Object> additionalInfo = null;

	/**
	 * @param inputschema
	 * @param userAttribute
	 * @param itemAttribute
	 * @param ratingAttribute
	 * @param options
	 */
	public BasicMFLearner(final SDFSchema inputschema,
			final SDFAttribute userAttribute, final SDFAttribute itemAttribute,
			final SDFAttribute ratingAttribute,
			final Map<String, String> options) {
		this.userAttributeIndex = inputschema.indexOf(userAttribute);
		this.itemAttributeIndex = inputschema.indexOf(itemAttribute);
		this.ratingAttributeIndex = inputschema.indexOf(ratingAttribute);

		if (this.userAttributeIndex == -1) {
			throw new IllegalArgumentException("User attribute not found.");
		}
		if (this.itemAttributeIndex == -1) {
			throw new IllegalArgumentException("Item attribute not found.");
		}
		if (this.ratingAttributeIndex == -1) {
			throw new IllegalArgumentException("Rating attribute not found.");
		}

		if (options != null) {
			if (options.get("max_steps") != null) {
				try {
					this.maxSteps = Integer.parseInt(options.get("max_steps"));
				} catch (final NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (options.get("learning_rate") != null) {
				try {
					this.learningRate = Double.parseDouble(options
							.get("learning_rate"));
				} catch (final NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (options.get("regularization_factor") != null) {
				try {
					this.regularizationFactor = Double.parseDouble(options
							.get("regularization_factor"));
				} catch (final NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (options.get("convergence_tolerance") != null) {
				try {
					this.convergenceTolerance = Double.parseDouble(options
							.get("convergence_tolerance"));
				} catch (final NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (options.get("num_of_features") != null) {
				try {
					this.numOfFeatures = Integer.parseInt(options
							.get("num_of_features"));
				} catch (final NumberFormatException e) {
					e.printStackTrace();
				}
			}

			if (options.get("build_from_scratch") != null) {
				this.buildFromScratch = Boolean.parseBoolean(options
						.get("build_from_scratch"));
			}

			if (options.get("random_seed") != null) {
				final String randomSeed = options.get("random_seed");
				try {
					this.random = new Random(Long.parseLong(randomSeed));
				} catch (final NumberFormatException e) {
					this.random = new Random();
				}
			}

			if (options.get("add_additional_info") != null) {

				if (Boolean.parseBoolean(options.get("add_additional_info"))) {
					this.additionalInfo = new HashMap<String, Object>();
				}
			}

			if (options.get("reset_changed_feature_vectors") != null) {

				if (Boolean.parseBoolean(options
						.get("reset_changed_feature_vectors"))) {
					this.resetChangedFeatureVectors = true;
				}
			}

		}

		this.clear();
	}

	/**
	 * Returns the user attribute in the passed tuple.
	 *
	 * @param tuple
	 *            The tuple.
	 * @return The user.
	 */
	protected long getUserInTuple(final Tuple<ITimeInterval> tuple) {
		return tuple.getAttribute(this.userAttributeIndex);
	}

	/**
	 * Returns the item attribute in the passed tuple.
	 *
	 * @param tuple
	 *            The tuple.
	 * @return The item.
	 */
	protected long getItemInTuple(final Tuple<ITimeInterval> tuple) {
		return tuple.getAttribute(this.itemAttributeIndex);
	}

	/**
	 * Returns the rating attribute in the passed tuple.
	 *
	 * @param tuple
	 *            The tuple.
	 * @return The rating.
	 */
	protected double getRatingInTuple(final Tuple<ITimeInterval> tuple) {
		return tuple.getAttribute(this.ratingAttributeIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.Learner#getName()
	 */
	@Override
	public String getName() {
		return "BasicMFLearner";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#addLearningData
	 * (de.uniol.inf.is.odysseus.core.metadata.IStreamObject)
	 */
	@Override
	public void addLearningData(final Tuple<ITimeInterval> newLearningObject) {
		final long user = this.getUserInTuple(newLearningObject);
		final long item = this.getItemInTuple(newLearningObject);
		final double rating = this.getRatingInTuple(newLearningObject);
		this.setRating(user, item, rating);
		this.isModelTrained = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#addLearningData
	 * (java.util.Set)
	 */
	@Override
	public void addLearningData(
			final Set<Tuple<ITimeInterval>> newLearningObjects) {
		for (final Tuple<ITimeInterval> tuple : newLearningObjects) {
			final long user = this.getUserInTuple(tuple);
			final long item = this.getItemInTuple(tuple);
			final double rating = this.getRatingInTuple(tuple);
			this.setRating(user, item, rating);
		}
		this.isModelTrained = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#removeLearningData
	 * (de.uniol.inf.is.odysseus.core.metadata.IStreamObject)
	 */
	@Override
	public void removeLearningData(final Tuple<ITimeInterval> oldLearningObject) {
		final long user = this.getUserInTuple(oldLearningObject);
		final long item = this.getItemInTuple(oldLearningObject);
		final double rating = this.getRatingInTuple(oldLearningObject);
		this.removeRating(user, item, rating);
		this.isModelTrained = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#removeLearningData
	 * (java.util.Set)
	 */
	@Override
	public void removeLearningData(
			final Set<Tuple<ITimeInterval>> oldLearningObjects) {
		for (final Tuple<ITimeInterval> tuple : oldLearningObjects) {
			final long user = this.getUserInTuple(tuple);
			final long item = this.getItemInTuple(tuple);
			final double rating = this.getRatingInTuple(tuple);
			this.removeRating(user, item, rating);
		}
		this.isModelTrained = false;
	}

	private double getRating(final long user, final long item) {
		final Long2DoubleMap l2dmap = this.userToItemToRatingMap.get(user);
		if (l2dmap != null) {
			return l2dmap.get(item);
		} else {
			return Double.NaN;
		}
	}

	private void setRating(final long user, final long item, final double rating) {
		double oldValue = Double.NaN;
		if (this.userToItemToRatingMap.containsKey(user)) {
			oldValue = this.userToItemToRatingMap.get(user).put(item, rating);

		} else {
			final Long2DoubleMap l2dmap = new Long2DoubleOpenHashMap();
			l2dmap.defaultReturnValue(Double.NaN);
			l2dmap.put(item, rating);
			this.userToItemToRatingMap.put(user, l2dmap);
		}

		if (Double.isNaN(oldValue)) {
			this.itemMultiset.put(item, this.itemMultiset.get(item) + 1);

			++this.noRatings;
			this.globalMean += (rating - this.globalMean) / this.noRatings;
		} else {
			// final double ratingBias = this.globalMean + (oldValue - rating);
			// this.globalMean += (ratingBias - this.globalMean) /
			// this.noRatings;
			final double ratingBias = oldValue - rating;
			this.globalMean += ratingBias / this.noRatings;
		}

		if (this.resetChangedFeatureVectors) {
			this.userFeatures.put(user,
					this.getRandomDoubleArray(this.numOfFeatures));
			this.itemFeatures.put(item,
					this.getRandomDoubleArray(this.numOfFeatures));
		}
	}

	private void removeRating(final long user, final long item,
			final double rating) {
		final Long2DoubleMap l2dmap = this.userToItemToRatingMap.get(user);
		if (l2dmap != null) {
			if (Double.compare(l2dmap.get(item), rating) == 0) {
				l2dmap.remove(item);

				if (l2dmap.size() == 0) {
					this.userToItemToRatingMap.remove(user);
				}

				if (this.itemMultiset.get(item) == 1) {
					this.itemMultiset.remove(item);
				} else {
					this.itemMultiset
							.put(item, this.itemMultiset.get(item) - 1);
				}

				--this.noRatings;
				this.globalMean -= (rating - this.globalMean) / this.noRatings;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.machine_learning.learner.Learner#clear()
	 */
	@Override
	public void clear() {
		this.userToItemToRatingMap = new Long2ReferenceOpenHashMap<>();
		this.userFeatures = new Long2ReferenceOpenHashMap<double[]>();
		this.userFeatures.defaultReturnValue(new double[0]);
		this.itemFeatures = new Long2ReferenceOpenHashMap<double[]>();
		this.itemFeatures.defaultReturnValue(new double[0]);
		this.itemMultiset = new Long2LongOpenHashMap();
		this.noRatings = 0;
		this.globalMean = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#isTrained()
	 */
	@Override
	public boolean isTrained() {
		return this.isModelTrained;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.learner.Learner#trainModel()
	 */
	@Override
	public synchronized void trainModel() {

		final LongSet userSet = this.userToItemToRatingMap.keySet();
		final LongSet itemSet = this.itemMultiset.keySet();

		if (this.buildFromScratch) {
			this.userFeatures.clear();
			this.itemFeatures.clear();
		}

		this.prepareUserFeatures(userSet);
		this.prepareItemFeatures(itemSet);

		double lastModelError = Double.MAX_VALUE;

		for (int i = 0; i < this.maxSteps; ++i) {

			for (final Entry<Long2DoubleMap> userEntry : this.userToItemToRatingMap
					.long2ReferenceEntrySet()) {
				final long user = userEntry.getLongKey();
				final double[] userFeatureVector = this.userFeatures.get(user);

				for (final Long2DoubleMap.Entry itemEntry : userEntry
						.getValue().long2DoubleEntrySet()) {
					final long item = itemEntry.getLongKey();
					final double rating = itemEntry.getDoubleValue();
					final double[] itemFeatureVector = this.itemFeatures
							.get(item);

					final double error = rating
							- MFModel.dotProduct(userFeatureVector,
									itemFeatureVector);

					for (int k = 0; k < this.numOfFeatures; ++k) {
						userFeatureVector[k] = userFeatureVector[k]
								+ this.learningRate
								* (2 * error * itemFeatureVector[k] - this.regularizationFactor
										* userFeatureVector[k]);
						itemFeatureVector[k] = itemFeatureVector[k]
								+ this.learningRate
								* (2 * error * userFeatureVector[k] - this.regularizationFactor
										* itemFeatureVector[k]);
					}
				}
			}

			if (i == this.maxSteps - 1) {
				// this was the last step
				if (this.additionalInfo != null) {
					final double modelError = this.calcModelError();
					this.additionalInfo.put("break at model error", modelError);
					this.additionalInfo.put("break at step", i);
				}
			} else {
				final double modelError = this.calcModelError();
				if (modelError + this.convergenceTolerance >= lastModelError) {
					if (this.additionalInfo != null) {
						this.additionalInfo.put("break at model error",
								modelError);
						this.additionalInfo.put("break at step", i);
					}
					break;
				}
				lastModelError = modelError;
			}
		}
		this.isModelTrained = true;
	}

	/**
	 * @return
	 */
	private double calcModelError() {
		double modelError = 0;
		int count = 0;
		final ObjectIterator<Entry<Long2DoubleMap>> iter = this.userToItemToRatingMap
				.long2ReferenceEntrySet().iterator();
		while (iter.hasNext()) {
			final Entry<Long2DoubleMap> entry = iter.next();
			final long user = entry.getKey();
			final double[] userFeatureVector = this.userFeatures.get(user);

			final ObjectIterator<Long2DoubleMap.Entry> iter2 = entry.getValue()
					.long2DoubleEntrySet().iterator();
			while (iter2.hasNext()) {
				final Long2DoubleMap.Entry entry2 = iter2.next();
				final long item = entry2.getLongKey();
				final double rating = entry2.getDoubleValue();

				final double[] itemFeatureVector = this.itemFeatures.get(item);

				final double predictedRating = MFModel.dotProduct(
						userFeatureVector, itemFeatureVector);

				++count;
				// square error
				double error = Math.pow(rating - predictedRating, 2);

				// regularization
				double regularization = 0;
				for (int k = 0; k < this.numOfFeatures; ++k) {
					regularization += this.regularizationFactor
							* (Math.pow(userFeatureVector[k], 2) + Math.pow(
									itemFeatureVector[k], 2));
				}
				error = 0.5 * (error + regularization);

				// mean square error
				modelError += (error - modelError) / count;

			}
		}
		return Math.sqrt(modelError);
	}

	/**
	 * @param userSet
	 */
	private void prepareUserFeatures(final LongSet userSet) {

		// remove all user features that does not exist in userSet anymore
		final ObjectIterator<Entry<double[]>> iter = this.userFeatures
				.long2ReferenceEntrySet().iterator();
		while (iter.hasNext()) {
			if (!userSet.contains(iter.next().getLongKey())) {
				iter.remove();
			}
		}

		// add new user features for new users in userSet
		for (final Long user : userSet) {
			if (!this.userFeatures.containsKey(user)) {
				this.userFeatures.put(user,
						this.getRandomDoubleArray(this.numOfFeatures));
			}
		}

	}

	/**
	 * @param itemSet
	 */
	private void prepareItemFeatures(final LongSet itemSet) {

		// remove all item features that does not exist in itemSet anymore
		final ObjectIterator<Long2ReferenceMap.Entry<double[]>> iter = this.itemFeatures
				.long2ReferenceEntrySet().iterator();
		while (iter.hasNext()) {
			if (!itemSet.contains(iter.next().getLongKey())) {
				iter.remove();
			}
		}

		// add new item features for new items in itemSet
		for (final Long item : itemSet) {
			if (!this.itemFeatures.containsKey(item)) {
				this.itemFeatures.put(item,
						this.getRandomDoubleArray(this.numOfFeatures));
			}
		}

	}

	/**
	 * @param numOfFeatures2
	 * @return
	 */
	private double[] getRandomDoubleArray(final int numOfFeatures) {
		final double[] value = new double[numOfFeatures];
		for (int i = 0; i < value.length; ++i) {
			value[i] = this.random.nextDouble();
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #getModel()
	 */
	@Override
	public RatingPredictor<Tuple<ITimeInterval>, ITimeInterval, Long, Long, Double> getModel() {
		if (this.additionalInfo != null) {
			return new MFModel(this.userFeatures, this.itemFeatures,
					this.globalMean, this.additionalInfo);
		} else {
			return new MFModel(this.userFeatures, this.itemFeatures,
					this.globalMean);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #getModel(boolean)
	 */
	@Override
	public RatingPredictor<Tuple<ITimeInterval>, ITimeInterval, Long, Long, Double> getModel(
			final boolean train) {
		if (train && !this.isModelTrained) {
			this.trainModel();
		}

		if (this.additionalInfo != null) {
			return new MFModel(this.userFeatures, this.itemFeatures,
					this.globalMean, this.additionalInfo);
		} else {
			return new MFModel(this.userFeatures, this.itemFeatures,
					this.globalMean);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #getRecommendationCandidatesModel()
	 */
	@Override
	public RecommendationCandidates<Long, Long> getRecommendationCandidatesModel() {
		final LongSet ratedItems = this.itemMultiset.keySet();
		final Map<Long, ImmutableSet<Long>> unratedItemsByUser = new Long2ObjectOpenHashMap<>(
				this.userToItemToRatingMap.size());
		for (final long user : this.userToItemToRatingMap.keySet()) {
			unratedItemsByUser.put(user, ImmutableSet
					.copyOf(this.userToItemToRatingMap.get(user).keySet()));
		}

		return new ImmutableRecommendationCandidatesSet<Long, Long>(
				ImmutableSet.copyOf(ratedItems),
				ImmutableMap.copyOf(unratedItemsByUser));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.learner.RecommendationLearner
	 * #getInfo()
	 */
	@Override
	public Map<? extends String, ? extends String> getInfo() {
		final Map<String, String> info = new HashMap<>();
		info.put("Tuples in Model (2)", String.valueOf(this.noRatings));
		info.put("Users in Model", String.valueOf(this.userFeatures.size()));
		info.put("Items in Model", String.valueOf(this.itemFeatures.size()));
		return info;
	}

}
