/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.costmodel;

import java.util.Collection;
import java.util.List;

/**
 * Calculates cost of an specific object type. ICostCalculator can pick the best
 * <code>num</code> alternatives out of a collection.
 * 
 * @author Tobias Witt
 * 
 * @param <T>
 *            Object type, which is rated.
 */
public interface ICostCalculator<T> {
	/**
	 * Calculates the {@link ICost} of <code>candidate</code>.
	 * 
	 * @param candidate
	 * @return Cost.
	 */
	public ICost<T> calculateCost(T candidate);

	/**
	 * Picks the best alternative out of the <code>candidates</code>.
	 * 
	 * @param candidates
	 *            {@link Collection} of candidates.
	 * @return Best candidate.
	 */
	public T pickBest(Collection<T> candidates);

	/**
	 * Picks the <code>num</code> best alternatives out of the
	 * <code>candidates</code>.
	 * 
	 * @param candidates
	 *            {@link Collection} of candidates.
	 * @param num
	 *            Number of alternatives to choose.
	 * @return {@link List} of best alternatives.
	 */
	public List<T> pickBest(Collection<T> candidates, int num);
}
