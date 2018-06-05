/*
 *    MemRecommenderData.java
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MemRecommenderData extends AbstractRecommenderData {

	private static final long serialVersionUID = 2844235954903772074L;

	class EntityStats implements Serializable {
		private static final long serialVersionUID = -8933750377510577120L;
		public double sum = 0;
		public double num = 0;
	}

	protected Map<Integer, Map<Integer, Double>> ratingsUser;
	protected Map<Integer, Map<Integer, Double>> ratingsItem;
	protected Map<Integer, EntityStats> usersStats;
	protected Map<Integer, EntityStats> itemsStats;

	protected int nItems = 0;
	protected int nUsers = 0;
	protected double sumRatings = 0;
	protected int nRatings = 0;
	protected double minRating = 0;
	protected double maxRating = 0;

	protected class RatingIterator implements Iterator<Rating> {
		private int currentUser = -1;
		private Iterator<Integer> userIt = null;
		private Iterator<Entry<Integer, Double>> ratsIt = null;
		private boolean calculated = false;
		private boolean result = true;

		RatingIterator() throws Exception {
		}

		@Override
		public boolean hasNext() {
			if (this.calculated) {
				return this.result;
			}

			this.calculated = true;
			this.result = false;
			if (this.ratsIt == null) {
				if (!MemRecommenderData.this.ratingsUser.isEmpty()) {
					this.userIt = MemRecommenderData.this.ratingsUser.keySet()
							.iterator();
					if (this.userIt.hasNext()) {
						final Integer first = this.userIt.next();
						this.currentUser = first;
						this.ratsIt = MemRecommenderData.this.ratingsUser
								.get(first).entrySet().iterator();
						if (this.ratsIt.hasNext()) {
							this.result = true;
						}
					}
				}
			} else {
				if (this.ratsIt.hasNext()) {
					this.result = true;
				} else if (this.userIt.hasNext()) {
					final Integer first = this.userIt.next();
					this.currentUser = first;
					this.ratsIt = MemRecommenderData.this.ratingsUser
							.get(first).entrySet().iterator();
					if (this.ratsIt.hasNext()) {
						this.result = true;
					}
				}
			}
			return this.result;
		}

		@Override
		public Rating next() {
			if (!this.calculated) {
				hasNext();
			}
			this.calculated = false;
			final Entry<Integer, Double> pair = this.ratsIt.next();

			return new Rating(this.currentUser, pair.getKey(), pair.getValue());
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}

	}

	public MemRecommenderData() {
		super();
		this.ratingsItem = new HashMap<Integer, Map<Integer, Double>>();
		this.ratingsUser = new HashMap<Integer, Map<Integer, Double>>();
		this.usersStats = new HashMap<Integer, EntityStats>();
		this.itemsStats = new HashMap<Integer, EntityStats>();
	}

	@Override
	public void addUser(final int userID, final List<Integer> ratedItems,
			final List<Double> ratings) {
		super.addUser(userID, ratedItems, ratings);

		this.ratingsUser.put(userID, new HashMap<Integer, Double>());
		this.usersStats.put(userID, new EntityStats());

		final int n = ratedItems.size();

		for (int i = 0; i < n; ++i) {
			auxSetRating(userID, ratedItems.get(i), ratings.get(i));
		}
	}

	// FIXME: have to update item stats!!!
	@Override
	public void removeUser(final int userID) {
		super.removeUser(userID);
		this.ratingsUser.remove(userID);
		this.usersStats.remove(userID);
	}

	@Override
	public void addItem(final int itemID, final List<Integer> ratingUsers,
			final List<Double> ratings) {
		super.addItem(itemID, ratingUsers, ratings);

		this.ratingsItem.put(itemID, new HashMap<Integer, Double>());
		this.itemsStats.put(itemID, new EntityStats());
		final int n = ratingUsers.size();
		for (int i = 0; i < n; ++i) {
			auxSetRating(ratingUsers.get(i), itemID, ratings.get(i));
		}
	}

	// FIXME: have to update user stats!!!
	@Override
	public void removeItem(final int itemID) {
		super.removeItem(itemID);
		this.ratingsItem.remove(itemID);
		this.itemsStats.remove(itemID);
	}

	private void auxSetRating(final int userID, final int itemID,
			final double rating) {
		if (this.nRatings == 0) {
			this.minRating = rating;
			this.maxRating = rating;
		} else {
			this.minRating = Math.min(this.minRating, rating);
			this.maxRating = Math.max(this.maxRating, rating);
		}

		EntityStats userStats = this.usersStats.get(userID);
		EntityStats itemStats = this.itemsStats.get(itemID);
		if (userStats == null) {
			++this.nUsers;
			this.ratingsUser.put(userID, new HashMap<Integer, Double>());
			userStats = new EntityStats();
			this.usersStats.put(userID, userStats);
		}

		if (itemStats == null) {
			++this.nItems;
			this.ratingsItem.put(itemID, new HashMap<Integer, Double>());
			itemStats = new EntityStats();
			this.itemsStats.put(itemID, itemStats);
		}

		final Map<Integer, Double> ratUser = this.ratingsUser.get(userID);
		final Map<Integer, Double> ratItem = this.ratingsItem.get(itemID);
		final Double rat = ratUser.get(itemID);
		if (rat != null) {
			this.sumRatings -= rat;
			userStats.sum -= rat;
			userStats.num--;
			itemStats.sum -= rat;
			itemStats.num--;
			--this.nRatings;
		}

		userStats.sum += rating;
		userStats.num++;
		itemStats.sum += rating;
		itemStats.num++;
		this.sumRatings += rating;
		++this.nRatings;
		ratUser.put(itemID, rating);
		ratItem.put(userID, rating);
	}

	@Override
	public void setRating(final int userID, final int itemID,
			final double rating) {
		super.setRating(userID, itemID, rating);
		auxSetRating(userID, itemID, rating);
	}

	@Override
	public void removeRating(final int userID, final int itemID) {
		super.removeRating(userID, itemID);

		final Map<Integer, Double> ratUser = this.ratingsUser.get(userID);
		final Map<Integer, Double> ratItem = this.ratingsItem.get(itemID);
		final Double rat = ratUser.get(itemID);
		final EntityStats userStats = this.usersStats.get(userID);
		final EntityStats itemStats = this.itemsStats.get(itemID);
		if (rat != null) {
			this.sumRatings -= rat;
			--this.nRatings;
			userStats.sum -= rat;
			userStats.num--;
			itemStats.sum -= rat;
			itemStats.num--;
			ratUser.remove(itemID);
			ratItem.remove(userID);
		}
	}

	@Override
	public SparseVector getRatingsUser(final int userID) {
		final Map<Integer, Double> ratUser = this.ratingsUser.get(userID);
		return new SparseVector(ratUser);
	}

	@Override
	public double getRating(final int userID, final int itemID) {
		final Map<Integer, Double> ratUser = this.ratingsUser.get(userID);
		return (ratUser.get(itemID) != null ? ratUser.get(itemID) : 0);
	}

	@Override
	public int getNumItems() {
		return this.nItems;
	}

	@Override
	public int getNumUsers() {
		return this.nUsers;
	}

	@Override
	public double getAvgRatingUser(final int userID) {
		final EntityStats stats = this.usersStats.get(userID);
		final double sum = (stats != null ? stats.sum : 0);
		final double num = (stats != null ? stats.num : 0);
		final double mean = (this.nRatings > 0 ? this.sumRatings
				/ this.nRatings : (this.minRating + this.maxRating) / 2.0);
		return (mean * 25 + sum) / (25 + num);
	}

	@Override
	public double getAvgRatingItem(final int itemID) {
		final EntityStats stats = this.itemsStats.get(itemID);
		final double sum = (stats != null ? stats.sum : 0);
		final double num = (stats != null ? stats.num : 0);
		final double mean = (this.nRatings > 0 ? this.sumRatings
				/ this.nRatings : (this.minRating + this.maxRating) / 2.0);
		return (mean * 25 + sum) / (25 + num);
	}

	@Override
	public double getMinRating() {
		return this.minRating;
	}

	@Override
	public double getMaxRating() {
		return this.maxRating;
	}

	@Override
	public Set<Integer> getUsers() {
		return this.usersStats.keySet();
	}

	@Override
	public SparseVector getRatingsItem(final int itemID) {
		final Map<Integer, Double> ratItem = this.ratingsItem.get(itemID);
		return new SparseVector(ratItem);
	}

	@Override
	public Set<Integer> getItems() {
		return this.itemsStats.keySet();
	}

	@Override
	public double getGlobalMean() {
		return (this.nRatings > 0 ? this.sumRatings / this.nRatings
				: (this.minRating + this.maxRating) / 2.0);
	}

	@Override
	public int countRatingsUser(final int userID) {
		final EntityStats stats = this.usersStats.get(userID);
		return (stats != null ? (int) stats.num : 0);
	}

	@Override
	public int countRatingsItem(final int itemID) {
		final EntityStats stats = this.itemsStats.get(itemID);
		return (stats != null ? (int) stats.num : 0);
	}

	@Override
	public Iterator<Rating> ratingIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumRatings() {
		return this.nRatings;
	}

	@Override
	public boolean userExists(final int userID) {
		return this.usersStats.containsKey(userID);
	}

	@Override
	public boolean itemExists(final int itemID) {
		return this.itemsStats.containsKey(itemID);
	}

	@Override
	public void clear() {
		this.usersStats.clear();
		this.itemsStats.clear();
		this.minRating = this.maxRating = this.nItems = this.nUsers = 0;
		this.sumRatings = this.nRatings = 0;
		this.ratingsUser.clear();
		this.ratingsItem.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.moa.RecommenderData#getSumRatings
	 * ()
	 */
	@Override
	public double getSumRatings() {
		return this.sumRatings;
	}
}
