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
package de.uniol.inf.is.odysseus.recommendation.matrix_factorization.model;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.AbstractTupleBasedRatingPredictor;

/**
 * This is an immutable matrix factorization model.
 * 
 * @author Cornelius Ludmann
 *
 */
public class MFModel
		extends
		AbstractTupleBasedRatingPredictor<Tuple<ITimeInterval>, ITimeInterval, Long, Long, Double> {

	private final Long2ReferenceMap<double[]> userFeatures;
	private final Long2ReferenceMap<double[]> itemFeatures;
	private final double fallbackPrediction;

	private int noOfFeatures = -1;

	private final Map<String, Object> additionalInfo;

	/**
	 * Creates an immutable matrix factorization model.
	 * 
	 * @param userFeatures
	 *            A map of user IDs to user feature vectors. All user feature
	 *            vectors need to have the same number of features.
	 * @param itemFeatures
	 *            A map of item IDs to item feature vectors. All item feature
	 *            vectors need to have the same number of features (and also the
	 *            same number of features as the user feature vectors).
	 * @param fallbackPrediction
	 *            This value is used as a prediction for each prediction request
	 *            that could not be calculated by the feature vectors (e.g. for
	 *            a non-existing user or item). A common value is the global
	 *            mean.
	 */
	public MFModel(final Map<Long, double[]> userFeatures,
			final Map<Long, double[]> itemFeatures,
			final double fallbackPrediction) {

		// make safe copies to ensure immutability
		this.userFeatures = this.saveCopy(userFeatures);
		this.itemFeatures = this.saveCopy(itemFeatures);

		this.fallbackPrediction = fallbackPrediction;

		this.additionalInfo = null;
	}

	/**
	 * Creates an immutable matrix factorization model.
	 * 
	 * @param userFeatures
	 *            A map of user IDs to user feature vectors. All user feature
	 *            vectors need to have the same number of features.
	 * @param itemFeatures
	 *            A map of item IDs to item feature vectors. All item feature
	 *            vectors need to have the same number of features (and also the
	 *            same number of features as the user feature vectors).
	 * @param fallbackPrediction
	 *            This value is used as a prediction for each prediction request
	 *            that could not be calculated by the feature vectors (e.g. for
	 *            a non-existing user or item). A common value is the global
	 *            mean.
	 * @param additionalInfo
	 *            Additional information that is added to the
	 *            <code>toString()</code> output for debug reasons.
	 */
	public MFModel(final Map<Long, double[]> userFeatures,
			final Map<Long, double[]> itemFeatures,
			final double fallbackPrediction,
			final Map<String, Object> additionalInfo) {

		// make safe copies to ensure immutability
		this.userFeatures = this.saveCopy(userFeatures);
		this.itemFeatures = this.saveCopy(itemFeatures);

		this.fallbackPrediction = fallbackPrediction;

		this.additionalInfo = ImmutableMap.copyOf(additionalInfo);
	}

	/**
	 * This method returns a save copy of the <code>featuresMap</code>.
	 * 
	 * @param featuresMap
	 *            The map which should be copied.
	 * @return A save copy of the map.
	 */
	private Long2ReferenceMap<double[]> saveCopy(
			final Map<Long, double[]> featuresMap) {

		final Long2ReferenceMap<double[]> returnMap = new Long2ReferenceOpenHashMap<double[]>(
				featuresMap.size());

		if (featuresMap instanceof Long2ReferenceMap) {
			final ObjectIterator<Long2ReferenceMap.Entry<double[]>> iter = ((Long2ReferenceMap<double[]>) featuresMap)
					.long2ReferenceEntrySet().iterator();
			while (iter.hasNext()) {
				final Long2ReferenceMap.Entry<double[]> entry = iter.next();
				final double[] dest = this.copyAndCheckFeatureVector(entry
						.getValue());
				returnMap.put(entry.getLongKey(), dest);
			}
		} else if (featuresMap instanceof Long2ObjectMap) {
			final ObjectIterator<Long2ObjectMap.Entry<double[]>> iter = ((Long2ObjectMap<double[]>) featuresMap)
					.long2ObjectEntrySet().iterator();
			while (iter.hasNext()) {
				final Long2ObjectMap.Entry<double[]> entry = iter.next();
				final double[] dest = this.copyAndCheckFeatureVector(entry
						.getValue());
				returnMap.put(entry.getLongKey(), dest);
			}
		} else {
			final Iterator<Entry<Long, double[]>> iter = featuresMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				final Entry<Long, double[]> entry = iter.next();
				final double[] dest = this.copyAndCheckFeatureVector(entry
						.getValue());
				returnMap.put(entry.getKey(), dest);
			}
		}
		return returnMap;
	}

	/**
	 * Copy the vector to a new vector to get a save copy. Additionally, this
	 * method checks if all features have the same length.
	 * 
	 * @param featureVector
	 *            The vector that should be copied.
	 * @return The copy of the vector.
	 */
	private double[] copyAndCheckFeatureVector(final double[] featureVector) {
		if (this.noOfFeatures == -1) {
			this.noOfFeatures = featureVector.length;
		}
		if (featureVector.length != this.noOfFeatures) {
			throw new IllegalArgumentException(
					"No of features are not equal in all feature vectors.");
		}
		final double[] dest = new double[featureVector.length];
		System.arraycopy(featureVector, 0, dest, 0, featureVector.length);
		return dest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.
	 * AbstractTupleBasedRatingPredictor#predict(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Double predict(final Long user, final Long item) {
		if (this.userFeatures.containsKey(user)
				&& this.itemFeatures.containsKey(item)) {
			return dotProduct(this.userFeatures.get(user),
					this.itemFeatures.get(item));
		} else {
			return this.fallbackPrediction;
		}
	}

	/**
	 * This method calculates the dot product of the two vectors. It is used to
	 * predict the rating for by the user and the item feature vector.
	 * 
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return The dot product of the two vectors.
	 * @throws IllegalArgumentException
	 *             if the size of the vectors are unequal.
	 */
	public static double dotProduct(final double[] v1, final double[] v2) {
		if (v1.length != v2.length) {
			throw new IllegalArgumentException(
					"The size of the two vectors are not the same. Size of v1: "
							+ v1.length + ". Size of v2: " + v2.length + ".");
		}
		double value = 0;
		for (int i = 0; i < v1.length; ++i) {
			value += v1[i] * v2[i];
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName()).append(" [");
		sb.append("fallback prediction value: ")
				.append(this.fallbackPrediction);
		sb.append(", #users: ").append(this.userFeatures.size());
		sb.append(", #items: ").append(this.itemFeatures.size());
		sb.append(", #features: ").append(this.noOfFeatures);
		if (this.additionalInfo != null) {
			for (final Entry<String, Object> entry : this.additionalInfo
					.entrySet()) {
				sb.append(", ").append(entry.getKey()).append(": ")
						.append(entry.getValue());
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
