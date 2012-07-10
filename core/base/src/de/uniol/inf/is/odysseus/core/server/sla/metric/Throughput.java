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
import de.uniol.inf.is.odysseus.core.server.sla.unit.ThroughputUnit;

/**
 * This metric is a measure for throughput
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Throughput extends Metric<ThroughputUnit> {

	/**
	 * creates a new throughput metric
	 * 
	 * @param value
	 *            the metric's value
	 * @param unit
	 *            the metric's unit
	 */
	public Throughput(double value, ThroughputUnit unit) {
		super(value, unit);
	}

	/**
	 * returns always true, because thresholds ar defined as minimium values
	 */
	@Override
	public boolean valueIsMin() {
		return true;
	}

}
