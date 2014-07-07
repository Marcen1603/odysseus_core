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
package de.uniol.inf.is.odysseus.recommendation.mahout.recommender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import de.uniol.inf.is.odysseus.recommendation.util.ObjectIdToLongId;

/**
 * @author Cornelius Ludmann
 * 
 */
public class MahoutRecommender implements
de.uniol.inf.is.odysseus.recommendation.recommender.Recommender {

	private final Recommender recommender;

	public MahoutRecommender(final Recommender recommender) {
		this.recommender = recommender;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.recommender.Recommender#recommend
	 * (java.lang.Object)
	 */
	@Override
	public Map<Object, Double> recommend(final Object user) {
		if (recommender == null) {
			return null;
		}
		try {
			final List<RecommendedItem> recommendedItems = recommender
					.recommend(
							ObjectIdToLongId.getInstance().objectIDAsLong(
									"user", user), Integer.MAX_VALUE);
			return recommendedItemsToMap(recommendedItems);
		} catch (final TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param recommendedItems
	 * @return
	 */
	private Map<Object, Double> recommendedItemsToMap(
			final List<RecommendedItem> recommendedItems) {
		final Map<Object, Double> items = new HashMap<Object, Double>();
		for (final RecommendedItem recomendedItem : recommendedItems) {
			items.put(
					ObjectIdToLongId.getInstance().longAsObjectId("item",
							recomendedItem.getItemID()),
							(double) recomendedItem.getValue());
		}
		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.recommendation.recommender.Recommender#
	 * recommendTopN(java.lang.Object, int)
	 */
	@Override
	public Map<Object, Double> recommendTopN(final Object user, final int n) {
		if (recommender == null) {
			return null;
		}
		try {
			final List<RecommendedItem> recommendedItems = recommender
					.recommend(
							ObjectIdToLongId.getInstance().objectIDAsLong(
									"user", user), n);
			return recommendedItemsToMap(recommendedItems);
		} catch (final TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
