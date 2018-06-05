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

package de.uniol.inf.is.odysseus.trajectory.compare.owd.data;

import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;

/**
 * An Owd query trajectory which utilize <tt>OwdData</tt> for an appropriate representation.
 * @author marcus
 *
 */
public class OwdQueryTrajectory extends AbstractConvertedQueryTrajectory<OwdData> {

	/**
	 * Creates an instance of <tt>OwdQueryTrajectory</tt>.
	 *  
	 * @param rawTrajectory the <tt>RawQueryTrajectory</tt> to encapsulate
	 * @param convertedData the converted <tt>OwdData</tt>
	 * @param textualAttributes the textual attributes
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt> or <tt>rawTrajectory == null</tt> 
	 */
	public OwdQueryTrajectory(final RawQueryTrajectory rawTrajectory,
			final OwdData convertedData, final Map<String, String> textualAttributes) {
		super(rawTrajectory, convertedData, textualAttributes);
	}

}
