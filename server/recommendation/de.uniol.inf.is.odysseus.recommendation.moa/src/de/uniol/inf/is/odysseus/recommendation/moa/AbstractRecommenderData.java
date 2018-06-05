/*
 *    AbstractRecommenderData.java
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
import java.util.Iterator;
import java.util.List;

public abstract class AbstractRecommenderData implements RecommenderData {

	/**
	 *
	 */
	private static final long serialVersionUID = -5409390358073330733L;

	protected ArrayList<Updatable> updatables;
	protected boolean disableUpdates = false;

	public AbstractRecommenderData() {
		this.updatables = new ArrayList<Updatable>();
	}

	@Override
	public void disableUpdates(final boolean disable) {
		this.disableUpdates = disable;
	}

	@Override
	public void addUser(final int userID, final List<Integer> ratedItems,
			final List<Double> ratings) {
		final Iterator<Updatable> it = this.updatables.iterator();
		while (it.hasNext()) {
			final Updatable u = it.next();
			if (!this.disableUpdates) {
				u.updateNewUser(userID, ratedItems, ratings);
			}
		}
	}

	@Override
	public void removeUser(final int userID) {
		final Iterator<Updatable> it = this.updatables.iterator();
		while (it.hasNext()) {
			final Updatable u = it.next();
			if (!this.disableUpdates) {
				u.updateRemoveUser(userID);
			}
		}
	}

	@Override
	public void addItem(final int itemID, final List<Integer> ratingUsers,
			final List<Double> ratings) {
		final Iterator<Updatable> it = this.updatables.iterator();
		while (it.hasNext()) {
			final Updatable u = it.next();
			if (!this.disableUpdates) {
				u.updateNewItem(itemID, ratingUsers, ratings);
			}
		}
	}

	@Override
	public void removeItem(final int itemID) {
		final Iterator<Updatable> it = this.updatables.iterator();
		while (it.hasNext()) {
			final Updatable u = it.next();
			if (!this.disableUpdates) {
				u.updateRemoveItem(itemID);
			}
		}
	}

	@Override
	public void setRating(final int userID, final int itemID,
			final double rating) {
		final Iterator<Updatable> it = this.updatables.iterator();
		while (it.hasNext()) {
			final Updatable u = it.next();
			if (!this.disableUpdates) {
				u.updateSetRating(userID, itemID, rating);
			}

		}
	}

	@Override
	public void removeRating(final int userID, final int itemID) {
		final Iterator<Updatable> it = this.updatables.iterator();
		while (it.hasNext()) {
			final Updatable u = it.next();
			if (!this.disableUpdates) {
				u.updateRemoveRating(userID, itemID);
			}
		}
	}

	@Override
	public void attachUpdatable(final Updatable obj) {
		this.updatables.add(obj);
	}

	@Override
	public void clear() {
		this.updatables.clear();
		this.disableUpdates = false;
	}

	@Override
	public void close() {

	}
}
