/**
 * 
 */
package de.uniol.inf.is.odysseus.benchmarker;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.core.Persist;

/**
 * @author Jonas Jacobi
 */
public class DescriptiveStatistics {
	private long[] values = new long[10000];
	@Element(name = "N")
	private int count = 0;
	boolean isEvaluated = false;
	@Element
	private double standardDeviation;
	@Element
	private double variance;
	private double sum = 0;
	private int offset;
	final private double[] percentiles = new double[100];
	//percentileMap is a little hack to only have selected values occur in xml serialization
	@ElementMap(attribute = true, entry = "percentile", key = "percent", value = "value")
	private Map<Integer, Double> percentileMap;
	@Element
	private double mean;
	

	public int getN() {
		return this.count;
	}

	public void addValue(long value) {
		if (isEvaluated) {
			throw new RuntimeException(
					"can't add any more values after first read");
		}
		if (count + 1 == values.length) {
			long[] tmp = values;
			values = new long[tmp.length * 3 / 2 + 1];
			System.arraycopy(tmp, 0, values, 0, tmp.length);
		}
		values[count++] = value;
		sum += value;
	}

	public double getMean() {
		prepareStats();
		return this.mean;
	}

	public double getStandardDeviation() {
		prepareStats();
		return this.standardDeviation;
	}

	@Persist
	private void prepareStats() {
		if (isEvaluated) {
			return;
		}
		isEvaluated = true;
		double accum = 0.0;
		double dev = 0.0;
		double accum2 = 0.0;
		this.mean = ((double) this.sum) / this.count;
		Arrays.sort(this.values);
		this.offset = this.values.length - this.count;
		for (int i = offset; i < this.values.length; ++i) {
			dev = this.values[i] - mean;
			accum += dev * dev;
			accum2 += dev;
		}
		this.variance = (accum - (accum2 * accum2 / count)) / count;
		this.standardDeviation = Math.sqrt(this.variance);

		//precalculate percentiles, so memory for values can be freed 
		for (int i = 0; i < 100; ++i) {
			this.percentiles[i] = calculatePercentile(i);
		}
		this.values = null;
		
		this.percentileMap = new TreeMap<Integer, Double>();
		addPersistablePercentiles(5, 10, 25, 50, 75, 90, 95);
	}

	private void addPersistablePercentiles(int... percentiles) {
		for (int p : percentiles) {
			this.percentileMap.put(p, this.percentiles[p]);
		}
	}

	public double getVariance() {
		prepareStats();
		return this.variance;
	}

	public long getMin() {
		prepareStats();
		return this.values[offset];
	}

	public long getMax() {
		prepareStats();
		return this.values[this.values.length - 1];
	}

	public double getPercentile(int i) {
		prepareStats();
		return this.percentiles[i];
	}

	private double calculatePercentile(int i) {
		prepareStats();
		int pos = offset + count * i / 100;
		if (count % 2 == 0) {
			return (this.values[pos] + this.values[pos - 1]) / 2d;
		} else {
			return this.values[pos];
		}
	}
}