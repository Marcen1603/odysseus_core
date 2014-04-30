/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth;

import java.io.Serializable;
import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.collection.Pair;

/**
 * @author Dennis Geesen
 * 
 * 
 */
public final class CountDescendingPairComparator<A extends Comparable<? super A>, B extends Comparable<? super B>> implements Comparator<Pair<A, B>>, Serializable {
	
	private static final long serialVersionUID = -1075351204679729104L;

	@Override
	public int compare(Pair<A, B> a, Pair<A, B> b) {
		int ret = b.getE2().compareTo(a.getE2());
		return ret;
//		if (ret != 0) {
//			return ret;
//		}
//		return a.getE1().compareTo(b.getE1());
	}
}
