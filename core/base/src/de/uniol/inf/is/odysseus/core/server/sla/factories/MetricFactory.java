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
package de.uniol.inf.is.odysseus.core.server.sla.factories;

import de.uniol.inf.is.odysseus.core.server.sla.Metric;
import de.uniol.inf.is.odysseus.core.server.sla.metric.Accuracy;
import de.uniol.inf.is.odysseus.core.server.sla.metric.Latency;
import de.uniol.inf.is.odysseus.core.server.sla.metric.Throughput;
import de.uniol.inf.is.odysseus.core.server.sla.unit.RatioUnit;
import de.uniol.inf.is.odysseus.core.server.sla.unit.ThroughputUnit;
import de.uniol.inf.is.odysseus.core.server.sla.unit.TimeUnit;

public class MetricFactory {

	public static final String METRIC_LATENCY = "latency";
	public static final String METRIC_THROUGHPUT = "throughput";
	public static final String METRIC_ACCURACY = "accuracy";

	public Metric<?> buildMetric(String metricID, double value, Enum<?> unit) {
		try {
			if (METRIC_ACCURACY.equals(metricID)) {
				return new Accuracy(value, (RatioUnit)unit);
			} else if (METRIC_LATENCY.equals(metricID)) {
				return new Latency(value, (TimeUnit)unit); 
			} else if (METRIC_THROUGHPUT.equals(metricID)) {
				return new Throughput(value, (ThroughputUnit)unit);
			} else {
				throw new RuntimeException("Unknown metric id");
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot create metric", e);
		}
	}

}
