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

package de.uniol.inf.is.odysseus.trajectory.compare.uots.data;

import de.uniol.inf.is.odysseus.trajectory.compare.data.AbstractConvertedDataTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.data.RawDataTrajectory;

/**
 * An Uots data trajectory which utilize <tt>UotsData</tt> for an appropriate representation.
 * @author marcus
 *
 */
public class UotsDataTrajectory extends AbstractConvertedDataTrajectory<UotsData> {

	/**
	 * Creates an instance of <tt>UotsDataTrajectory</tt>.
	 * 
	 * @param rawTrajectory the <tt>RawDataTrajectory</tt> to encapsulate
	 * @param convertedData the converted <tt>UotsData</tt>
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt>
	 */
	public UotsDataTrajectory(final RawDataTrajectory rawTrajectory, final UotsData convertedData) {
		super(rawTrajectory, convertedData);
	}
}
