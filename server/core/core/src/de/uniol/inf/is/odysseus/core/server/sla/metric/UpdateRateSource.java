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
package de.uniol.inf.is.odysseus.core.server.sla.metric;

import de.uniol.inf.is.odysseus.core.server.sla.Metric;
import de.uniol.inf.is.odysseus.core.server.sla.unit.TimeUnit;

/**
 * This Metric is a measure for the update rate of the input stream.
 * 
 * @author Lena Eylert
 * 
 */
public class UpdateRateSource extends Metric<TimeUnit>{

	/**
	 * creates a new metric for update rate of a source
	 * @param value the metric's value
	 * @param unit the metric's unit
	 */
	public UpdateRateSource(double value, TimeUnit unit) {
		super(value, unit);
	}

	/**
	 * returns always false because thresholds define maximum values
	 */
	@Override
	public boolean valueIsMin() {
		return false;
	}

}
