/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare.textual;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IHasTextualAttributes;

/**
 * Interface for measuring textual distance between two <tt>IHasTextualAttributes</tt>
 * objects.
 * 
 * @author marcus
 *
 */
public interface ITextualDistance {

	/**
	 * Calculates and returns the <i>textual distance</i> between two <tt>IHasTextualAttributes</tt> objects. 
	 * By contract this returns a value in range of <i>[0,1]</i>. If the distance returned
	 * is <i>0</i> both <tt>IHasTextualAttributes</tt> are <i>identical</i>. The higher the returned value the more apart 
	 * are both <tt>IHasTextualAttributes</tt>. A distance value of <i>1</i> means that the distance between both objects
	 * is at its most possible.
	 * 
	 * @param o1 the first <tt>IHasTextualAttributes</tt>
	 * @param o2 the second <tt>IHasTextualAttributes</tt>
	 * @return the <i>textual distance</i> in range of [0,1]
	 */
	public double getDistance(IHasTextualAttributes o1, IHasTextualAttributes o2);
}