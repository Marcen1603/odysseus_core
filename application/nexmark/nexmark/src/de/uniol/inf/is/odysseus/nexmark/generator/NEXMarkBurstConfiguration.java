/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
 ******************************************************************************/
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
package de.uniol.inf.is.odysseus.nexmark.generator;

/**
 * Die NEXMarkBurstConfiguration kapselt die Parameter fuer einen Burst.
 * 
 * @see NEXMarkGeneratorConfiguration
 * @author Bernd Hochschulz
 * 
 */
public class NEXMarkBurstConfiguration {
	public int minTimeBetweenBursts;
	public int maxTimeBetweenBursts;

	public int minBurstDuration;
	public int maxBurstDuration;

	public int burstAccelerationFactor;

	public NEXMarkBurstConfiguration(int minTimeBetweenBursts, int maxTimeBetweenBursts,
			int minBurstDuration, int maxBurstDuration, int burstAccelerationFactor) {
		this.minTimeBetweenBursts = minTimeBetweenBursts;
		this.maxTimeBetweenBursts = maxTimeBetweenBursts;

		this.minBurstDuration = minBurstDuration;
		this.maxBurstDuration = maxBurstDuration;

		if (burstAccelerationFactor == 0) {
			this.burstAccelerationFactor = 1;
		} else {
			this.burstAccelerationFactor = burstAccelerationFactor;
		}
	}

	@Override
	public String toString() {
		return "burst [" + minTimeBetweenBursts + ", " + maxTimeBetweenBursts + "], ["
				+ minBurstDuration + ", " + maxBurstDuration + "], [" + burstAccelerationFactor
				+ "]";
	}
}
