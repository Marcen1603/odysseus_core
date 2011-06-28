package de.uniol.inf.is.odysseus.scheduler.slamodel.factories;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Metric;
import de.uniol.inf.is.odysseus.scheduler.slamodel.metric.Accuracy;
import de.uniol.inf.is.odysseus.scheduler.slamodel.metric.Latency;
import de.uniol.inf.is.odysseus.scheduler.slamodel.metric.Throughput;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.RatioUnit;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.ThroughputUnit;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.TimeUnit;

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
