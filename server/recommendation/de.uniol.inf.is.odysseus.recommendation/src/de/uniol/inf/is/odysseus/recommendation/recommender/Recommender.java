/**********************************************************************************
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
package de.uniol.inf.is.odysseus.recommendation.recommender;

import java.util.Map;

/**
 * An Recommender recommends items for a user-item-pair.
 * 
 * @author Cornelius Ludmann
 */
public interface Recommender {

	/**
	 * Returns the (predicted) ratings of all known items to the given user. If
	 * it is not possible to give recommendations (e. g. the user does not exist
	 * in this model for some implementations), {@code null} is returned.
	 * 
	 * @param user
	 *            The user ID.
	 * @return A map of item IDs to ratings or {@code null}.
	 */
	public Map<Object, Double> recommend(Object user);

	/**
	 * Returns the (predicted) ratings of the top-n rated items to the given
	 * user. If it is not possible to give recommendations (e. g. the user does
	 * not exist in this model for some implementations), {@code null} is
	 * returned.
	 * 
	 * @param user
	 *            The user ID.
	 * @param n
	 *            Number of items.
	 * @return A map of item IDs to ratings or {@code null}.
	 */
	public Map<Object, Double> recommendTopN(Object user, int n);

}
