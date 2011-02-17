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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCostCalculator<T> implements ICostCalculator<T> {
	
	@Override
	public T pickBest(Collection<T> candidates) {
		T optimal = null;
		ICost<T> bestScore = null;
		for (T root: candidates) {
			ICost<T> c = calculateCost(root);
			if (optimal == null || bestScore.getScore() > c.getScore()) {
				bestScore = c;
				optimal = root;
			}
		}
		return optimal;
	}

	@Override
	public List<T> pickBest(Collection<T> candidates, int num) {
		final Map<T, ICost<T>> costs = new LinkedHashMap<T, ICost<T>>(candidates.size());
		for (T root : candidates) {
			ICost<T> c = calculateCost(root);
			costs.put(root, c);
		}
		List<T> optimal = new ArrayList<T>(candidates.size());
		optimal.addAll(candidates);
		Collections.sort(optimal, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return costs.get(o1).getScore() - costs.get(o2).getScore();
			}
		});
		if (num < optimal.size()) {
			optimal.subList(num, optimal.size()).clear();
		}
		return optimal;
	}
	
}
