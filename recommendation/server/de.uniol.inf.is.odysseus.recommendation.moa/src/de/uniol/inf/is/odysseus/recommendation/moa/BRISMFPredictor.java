/*
 *    BRISMFPredictor.java
 *    Copyright (C) 2012 Universitat Politecnica de Catalunya
 *    @author Alex Catarineu (a.catarineu@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *
 */

package de.uniol.inf.is.odysseus.recommendation.moa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.AbstractTupleBasedRatingPredictor;

/**
 * Implementation of the algorithm described in Scalable Collaborative Filtering
 * Approaches for Large Recommender Systems (Gábor Takács, István Pilászy,
 * Bottyán Németh, and Domonkos Tikk). A feature vector is learned for every
 * user and item, so that the prediction of a rating is roughly the dot product
 * of the corresponding user and item vector. Stochastic gradient descent is
 * used to train the model, minimizing its prediction error. Both Tikhonov
 * regularization and early stopping are used to reduce overfitting. The
 * algorithm allows batch training (from scratch, using all ratings available at
 * the moment) as well as incremental, by retraining only the affected user and
 * item vectors when a new rating is inserted.
 *
 * <p>
 * Parameters:
 * </p>
 * <ul>
 * <li>features - the number of features to be trained for each user and item</li>
 * <li>learning rate - the learning rate used in the regularization</li>
 * <li>ratio - the regularization ratio to be used in the Tikhonov
 * regularization</li>
 * <li>iterations - the number of iterations to be used when retraining user and
 * item features (online training).</li> </lu>
 *
 */

public class BRISMFPredictor extends AbstractTupleBasedRatingPredictor<Tuple<ITimeInterval>, ITimeInterval,Integer, Integer, Double> implements Updatable {

	protected RecommenderData data;
	protected int nFeatures;
	protected HashMap<Integer, float[]> userFeature;
	protected HashMap<Integer, float[]> itemFeature;
	protected Random rnd;
	protected double lRate = 0.001;
	protected double rFactor = 0.01;
	protected int nIterations = 100;

	public void setLRate(final double lRate) {
		this.lRate = lRate;
	}

	public void setRFactor(final double rFactor) {
		this.rFactor = rFactor;
	}

	public void setNIterations(final int nIterations) {
		this.nIterations = nIterations;
	}

	public RecommenderData getData() {
		return this.data;
	}

	public BRISMFPredictor(final int nFeatures, final RecommenderData data,
			final boolean train) {
		this.data = data;
		this.nFeatures = nFeatures;
		this.userFeature = new HashMap<Integer, float[]>();
		this.itemFeature = new HashMap<Integer, float[]>();
		this.rnd = new Random(12345);
		data.attachUpdatable(this);
		if (train) {
			train();
		}
	}

	public BRISMFPredictor(final int nFeatures, final RecommenderData data,
			final double lRate, final double rFactor, final boolean train) {
		this.data = data;
		this.nFeatures = nFeatures;
		this.userFeature = new HashMap<Integer, float[]>();
		this.itemFeature = new HashMap<Integer, float[]>();
		this.rnd = new Random(12345);
		this.lRate = lRate;
		this.rFactor = rFactor;
		data.attachUpdatable(this);
		if (train) {
			train();
		}
	}

	public BRISMFPredictor getImmutableCopy() {
		final MemRecommenderData data = new MemRecommenderData();
		data.maxRating = this.data.getMaxRating();
		data.minRating = this.data.getMinRating();
		data.nRatings = this.data.getNumRatings();
		data.sumRatings = this.data.getSumRatings();

		final BRISMFPredictor copy = new BRISMFPredictor(this.nFeatures, data,
				false);
		final HashMap<Integer, float[]> copyUserFeature = new HashMap<>();
		final HashMap<Integer, float[]> copyItemFeature = new HashMap<>();

		for (final Entry<Integer, float[]> entry : this.userFeature.entrySet()) {
			copyUserFeature.put(entry.getKey(),
					Arrays.copyOf(entry.getValue(), entry.getValue().length));
		}
		copy.userFeature = copyUserFeature;

		for (final Entry<Integer, float[]> entry : this.itemFeature.entrySet()) {
			copyItemFeature.put(entry.getKey(),
					Arrays.copyOf(entry.getValue(), entry.getValue().length));
		}
		copy.itemFeature = copyItemFeature;

		return copy;
	}

	private void resetFeatures(final float[] feats, final boolean userFeats) {
		final int n = feats.length;
		for (int i = 0; i < n; ++i) {
			feats[i] = (float) 0.01 * (this.rnd.nextFloat() * 2 - 1);
		}
		if (userFeats) {
			feats[0] = 1;
		} else {
			feats[1] = 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.model.RatingPredictor#predictRating
	 * (java.lang.Object, java.lang.Object)
	 */
	@Override
	public Double predict(final Integer userID, final Integer itemID) {
		final float[] userFeats = this.userFeature.get(userID);
		final float[] itemFeats = this.itemFeature.get(itemID);
		return predictRating(userFeats, itemFeats);
	}

	public double predictRating(final float userFeats[],
			final float itemFeats[]) {
		double ret = this.data.getGlobalMean();
		if (userFeats != null && itemFeats != null) {
			for (int i = 0; i < this.nFeatures; ++i) {
				ret += userFeats[i] * itemFeats[i];
			}
		}

		if (ret < this.data.getMinRating()) {
			ret = this.data.getMinRating();
		} else if (ret > this.data.getMaxRating()) {
			ret = this.data.getMaxRating();
		}

		return ret;
	}

	public float[] trainUserFeats(final List<Integer> itm,
			final List<Double> rat, final int nIts) {
		final float[] userFeats = new float[this.nFeatures];
		resetFeatures(userFeats, true);

		final int n = itm.size();
		for (int k = 0; k < nIts; ++k) {
			for (int i = 0; i < n; ++i) {
				final int itemID = itm.get(i);
				final float[] itemFeats = this.itemFeature.get(itemID);
				final double rating = rat.get(i);
				final double pred = predictRating(userFeats, itemFeats);
				final double err = rating - pred;

				if (itemFeats != null) {
					for (int j = 1; j < this.nFeatures; ++j) {
						userFeats[j] += this.lRate
								* (err * itemFeats[j] - this.rFactor
										* userFeats[j]);
					}
				}
			}
		}

		return userFeats;
	}

	public float[] trainItemFeats(final int itemID, final List<Integer> usr,
			final List<Double> rat, final int nIts) {
		final float[] itemFeats = new float[this.nFeatures];
		resetFeatures(itemFeats, false);

		final int n = usr.size();
		for (int k = 0; k < nIts; ++k) {
			for (int i = 0; i < n; ++i) {
				final int userID = usr.get(i);
				final float[] userFeats = this.userFeature.get(userID);
				final double rating = rat.get(i);
				final double pred = predictRating(userFeats, itemFeats);
				final double err = rating - pred;

				if (userFeats != null) {
					itemFeats[0] += this.lRate
							* (err * userFeats[0] - this.rFactor * itemFeats[0]);
					for (int j = 2; j < this.nFeatures; ++j) {
						itemFeats[j] += this.lRate
								* (err * userFeats[j] - this.rFactor
										* itemFeats[j]);
					}
				}
			}
		}

		return itemFeats;
	}

	public void trainUser(final int userID, final List<Integer> itm,
			final List<Double> rat, final int nIts) {
		this.userFeature.put(userID, trainUserFeats(itm, rat, nIts));
	}

	public void trainUser(final int userID, final int nIts) {
		final SparseVector usrRats = this.data.getRatingsUser(userID);
		final ArrayList<Integer> itm = new ArrayList<Integer>();
		final ArrayList<Double> rat = new ArrayList<Double>();
		final Iterator<Pair<Integer, Double>> it = usrRats.iterator();

		while (it.hasNext()) {
			final Pair<Integer, Double> p = it.next();
			itm.add(p.getFirst());
			rat.add(p.getSecond());
		}
		trainUser(userID, itm, rat, nIts);
	}

	public void trainUser(final int userID, final List<Integer> itm,
			final List<Double> rat) {
		this.userFeature
				.put(userID, trainUserFeats(itm, rat, this.nIterations));
	}

	public void trainItem(final int itemID) {
		final SparseVector itmRats = this.data.getRatingsItem(itemID);
		final ArrayList<Integer> usr = new ArrayList<Integer>();
		final ArrayList<Double> rat = new ArrayList<Double>();
		final Iterator<Pair<Integer, Double>> it = itmRats.iterator();

		while (it.hasNext()) {
			final Pair<Integer, Double> p = it.next();
			usr.add(p.getFirst());
			rat.add(p.getSecond());
		}
		trainItem(itemID, usr, rat);
	}

	public void trainItem(final int itemID, final int nIts) {
		final SparseVector itmRats = this.data.getRatingsItem(itemID);
		final ArrayList<Integer> usr = new ArrayList<Integer>();
		final ArrayList<Double> rat = new ArrayList<Double>();
		final Iterator<Pair<Integer, Double>> it = itmRats.iterator();

		while (it.hasNext()) {
			final Pair<Integer, Double> p = it.next();
			usr.add(p.getFirst());
			rat.add(p.getSecond());
		}
		trainItem(itemID, usr, rat, nIts);
	}

	public void trainUser(final int userID) {
		final SparseVector usrRats = this.data.getRatingsUser(userID);
		final ArrayList<Integer> itm = new ArrayList<Integer>();
		final ArrayList<Double> rat = new ArrayList<Double>();
		final Iterator<Pair<Integer, Double>> it = usrRats.iterator();

		while (it.hasNext()) {
			final Pair<Integer, Double> p = it.next();
			itm.add(p.getFirst());
			rat.add(p.getSecond());
		}
		trainUser(userID, itm, rat);
	}

	public void trainItem(final int itemID, final List<Integer> usr,
			final List<Double> rat) {
		this.itemFeature.put(itemID,
				trainItemFeats(itemID, usr, rat, this.nIterations));
	}

	public void trainItem(final int itemID, final List<Integer> usr,
			final List<Double> rat, final int nIts) {
		this.itemFeature.put(itemID, trainItemFeats(itemID, usr, rat, nIts));
	}

	public void train() {
		this.userFeature.clear();
		this.itemFeature.clear();

		final int n = this.data.getNumRatings();

		Iterator<Integer> it = this.data.getUsers().iterator();
		while (it.hasNext()) {
			final float[] feats = new float[this.nFeatures];
			resetFeatures(feats, true);
			this.userFeature.put(it.next(), feats);
		}

		it = this.data.getItems().iterator();
		while (it.hasNext()) {
			final float[] feats = new float[this.nFeatures];
			resetFeatures(feats, false);
			this.itemFeature.put(it.next(), feats);
		}

		int exit = 0;
		double lastRMSE = 1e20;

		int count = 0;
		final int trainDiv = Math.max(20, n / 1000000);
		final ArrayList<Rating> ratTest = new ArrayList<Rating>(n / trainDiv);
		do {
			final long start = System.currentTimeMillis();
			final Iterator<Rating> ratIt = this.data.ratingIterator();
			int idx = 0;

			while (ratIt.hasNext()) {
				final Rating rat = ratIt.next();
				if (idx % trainDiv == 0) {
					if (count == 0) {
						ratTest.add(rat);
					}
				} else {
					final int userID = rat.userID;
					final int itemID = rat.itemID;
					final double rating = rat.rating;
					final float[] userFeats = this.userFeature.get(userID);
					final float[] itemFeats = this.itemFeature.get(itemID);

					final double pred = predictRating(userFeats, itemFeats);
					final double err = rating - pred;

					itemFeats[0] += this.lRate
							* (err * userFeats[0] - this.rFactor * itemFeats[0]);
					userFeats[1] += this.lRate
							* (err * itemFeats[1] - this.rFactor * userFeats[1]);
					for (int j = 2; j < this.nFeatures; ++j) {
						final double uv = userFeats[j];
						userFeats[j] += this.lRate
								* (err * itemFeats[j] - this.rFactor
										* userFeats[j]);
						itemFeats[j] += this.lRate
								* (err * uv - this.rFactor * itemFeats[j]);
					}
				}
				++idx;
			}
			final int nTest = ratTest.size();

			double sum = 0;
			for (int i = 0; i < nTest; ++i) {
				final int userID = ratTest.get(i).userID;
				final int itemID = ratTest.get(i).itemID;
				final double rating = ratTest.get(i).rating;
				final double pred = predictRating(userID, itemID);
				sum += Math.pow(rating - pred, 2);
			}

			final double curRMSE = Math.sqrt(sum / nTest);
			System.out.println(curRMSE + " "
					+ (System.currentTimeMillis() - start) / 1000);
			if (curRMSE + 0.0001 >= lastRMSE) {
				++exit;
			}
			lastRMSE = curRMSE;
			++count;
		} while (exit < 1);
	}

	/**
	 * @param userID
	 * @param itemID
	 * @return
	 */
	private double predictRating(int userID, int itemID) {
		return predict(userID, itemID);
	}

	public float[] getUserFeatures(final int userID) {
		return this.userFeature.get(userID);
	}

	public float[] getItemFeatures(final int itemID) {
		return this.itemFeature.get(itemID);
	}

	public int getNumFeatures() {
		return this.nFeatures;
	}

	@Override
	public void updateNewUser(final int userID, final List<Integer> ratedItems,
			final List<Double> ratings) {
		if (!ratedItems.isEmpty()) {
			trainUser(userID, ratedItems, ratings);
		}
	}

	@Override
	public void updateNewItem(final int itemID,
			final List<Integer> ratingUsers, final List<Double> ratings) {
		if (!ratingUsers.isEmpty()) {
			trainItem(itemID, ratingUsers, ratings);
		}
	}

	@Override
	public void updateRemoveUser(final int userID) {
		this.userFeature.remove(userID);
	}

	@Override
	public void updateRemoveItem(final int itemID) {
		this.itemFeature.remove(itemID);
	}

	// We retrain the user/item separately, depending on a probability
	// calculated using the error when predicting the new rating
	// TODO: parametrize this
	@Override
	public void updateSetRating(final int userID, final int itemID,
			final double rating) {
		final double nUsr = this.data.countRatingsUser(userID);
		final double nItm = this.data.countRatingsItem(itemID);
		final double prob1 = Math.pow(0.99, nUsr);
		final double prob2 = Math.pow(0.99, nItm);

		if (nUsr < 5 || this.rnd.nextDouble() < prob1) {
			final SparseVector usrRats = this.data.getRatingsUser(userID);
			final ArrayList<Integer> itm = new ArrayList<Integer>();
			final ArrayList<Double> rat = new ArrayList<Double>();

			// Train user
			boolean found = false;
			final Iterator<Pair<Integer, Double>> it = usrRats.iterator();
			while (it.hasNext()) {
				final Pair<Integer, Double> p = it.next();
				itm.add(p.getFirst());
				if (p.getFirst() == itemID) {
					found = true;
					rat.add(rating);
				} else {
					rat.add(p.getSecond());
				}
			}
			if (!found) {
				itm.add(itemID);
				rat.add(rating);
			}
			trainUser(userID, itm, rat);
		}

		if (nItm < 5 || this.rnd.nextDouble() < prob2) {
			final SparseVector itmRats = this.data.getRatingsItem(itemID);
			// Train item
			final Iterator<Pair<Integer, Double>> it = itmRats.iterator();
			boolean found = false;
			final ArrayList<Integer> usr = new ArrayList<Integer>();
			final ArrayList<Double> rat = new ArrayList<Double>();
			while (it.hasNext()) {
				final Pair<Integer, Double> p = it.next();
				usr.add(p.getFirst());
				if (p.getFirst() == userID) {
					found = true;
					rat.add(rating);
				} else {
					rat.add(p.getSecond());
				}
			}
			if (!found) {
				usr.add(itemID);
				rat.add(rating);
			}
			trainItem(itemID, usr, rat);
		}
	}



	public List<Double> predictRatings(final int userID,
			final List<Integer> itemIDS) {
		final int n = itemIDS.size();
		final ArrayList<Double> ret = new ArrayList<Double>(n);
		for (int i = 0; i < n; ++i) {
			ret.add(predictRating(userID, itemIDS.get(i)));
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [num ratings: "
				+ this.data.getNumRatings() + ", global mean: "
				+ this.data.getGlobalMean() + ", max rating: "
				+ this.data.getMaxRating() + ", min rating: "
				+ this.data.getMinRating() + "]";
	}



	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.recommendation.moa.Updatable#updateRemoveRating(int, int)
	 */
	@Override
	public void updateRemoveRating(int userID, int itemID) {
	}



}
