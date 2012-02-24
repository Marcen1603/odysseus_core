package de.uniol.inf.is.odysseus.core.server.sla.factories;

import de.uniol.inf.is.odysseus.core.server.sla.metric.Accuracy;
import de.uniol.inf.is.odysseus.core.server.sla.metric.Latency;
import de.uniol.inf.is.odysseus.core.server.sla.metric.Throughput;
import de.uniol.inf.is.odysseus.core.server.sla.unit.RatioUnit;
import de.uniol.inf.is.odysseus.core.server.sla.unit.ThroughputUnit;
import de.uniol.inf.is.odysseus.core.sla.Metric;
import de.uniol.inf.is.odysseus.core.sla.unit.TimeUnit;

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
