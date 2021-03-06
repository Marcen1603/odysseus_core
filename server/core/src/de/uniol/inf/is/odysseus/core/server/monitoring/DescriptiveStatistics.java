/** Copyright [2011] [The Odysseus Team]
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
/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.monitoring;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.core.Persist;

import de.uniol.inf.is.odysseus.core.WriteOptions;

/**
 * @author Jonas Jacobi
 */
public class DescriptiveStatistics implements IDescriptiveStatistics{
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
	// percentileMap is a little hack to only have selected values occur in xml
	// serialization
	@ElementMap(attribute = true, entry = "percentile", key = "percent", value = "value")
	private Map<Integer, Double> percentileMap = new TreeMap<Integer, Double>();
	@Element
	private double mean;
	@Element
	private long min;
	@Element
	private long max;
	
	public int getN() {
		return this.count;
	}

	@Override
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
		@SuppressWarnings("unused")
		double accum2 = 0.0;
		this.mean = this.sum / this.count;
		Arrays.sort(this.values);
		this.offset = this.values.length - this.count;
		if (offset == this.values.length) {
			return;
		}
		this.min = this.values[offset];
		this.max = this.values[this.values.length - 1];

		for (int i = offset; i < this.values.length; ++i) {
			dev = this.values[i] - mean;
			accum += dev * dev;
			accum2 += dev;
		}
//		this.variance = (accum - (accum2 * accum2 / count)) / count;TODO was hab ich hier berechnet? :)
		this.variance = accum/(count-1);
		this.standardDeviation = Math.sqrt(this.variance);

		// precalculate percentiles, so memory for values can be freed
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
		return this.min;
	}

	public long getMax() {
		prepareStats();
		return this.max;
	}

	public double getPercentile(int i) {
		prepareStats();
		return this.percentiles[i];
	}

	private double calculatePercentile(int i) {
		prepareStats();
		// Avoid overflow
		long iL = i;
		Long posL = offset + count * iL / 100L;
		int pos = posL.intValue();
		if (count % 2 == 0) {
			return (this.values[pos] + this.values[pos - 1]) / 2d;
		}
        return this.values[pos];
	}
	
	public void mergeValues(DescriptiveStatistics otherStats) {
		if (isEvaluated|| otherStats.isEvaluated) {
			throw new RuntimeException(
					"can't add any more values after first read");
		}
		
		long[] tmp = values;
		values = new long[count + otherStats.count];
		System.arraycopy(tmp, 0, values, 0, count);
		System.arraycopy(otherStats.values, 0, values, 0, otherStats.count);
		this.count += otherStats.count;
		this.sum += otherStats.sum;
	}
	
	@Override
	public String toString(){
		this.prepareStats();
		String str = "";
		str += "Min: " + this.getMin() + "\n";
		str += "Max: " + this.getMax() + "\n";
		str += "Mean: " + this.getMean() + "\n";
		str += "Value count: " + this.getN() + "\n";
		str += "Standard deviation: " + this.getStandardDeviation() + "\n";
		str += "Variance: " + this.getVariance() + "\n";
		str += "95 percentile: " + this.getPercentile(95) + "\n";
		str += "90 percentile: " + this.getPercentile(90) + "\n";
		str += "75 percentile: " + this.getPercentile(75) + "\n";
		str += "50 percentile: " + this.getPercentile(50);
		return str;
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		char delimiter = options.getDelimiter();
		NumberFormat floatingFormatter = options.getFloatingFormatter();
		NumberFormat numberFormatter = options.getNumberFormatter();
		prepareStats();
		if (floatingFormatter != null && numberFormatter != null){
		return numberFormatter.format(getMin())+delimiter+numberFormatter.format(getMax())+delimiter+floatingFormatter.format(getMean())+delimiter+numberFormatter.format(getN())+delimiter+
				floatingFormatter.format(getStandardDeviation())+delimiter+floatingFormatter.format(getVariance())+delimiter
				+floatingFormatter.format(getPercentile(5))+delimiter+
				floatingFormatter.format(getPercentile(10))+delimiter+
				floatingFormatter.format(getPercentile(25))+delimiter+
				floatingFormatter.format(getPercentile(50))+delimiter+
				floatingFormatter.format(getPercentile(75))+delimiter+
				floatingFormatter.format(getPercentile(90))+delimiter+
				floatingFormatter.format(getPercentile(95));
		}
		if (floatingFormatter != null){
			return getMin()+delimiter+getMax()+delimiter+floatingFormatter.format(getMean())+delimiter+getN()+delimiter+
					floatingFormatter.format(getStandardDeviation())+delimiter+floatingFormatter.format(getVariance())+delimiter
							+floatingFormatter.format(getPercentile(5))+delimiter+
							floatingFormatter.format(getPercentile(10))+delimiter+
							floatingFormatter.format(getPercentile(25))+delimiter+
							floatingFormatter.format(getPercentile(50))+delimiter+
							floatingFormatter.format(getPercentile(75))+delimiter+
							floatingFormatter.format(getPercentile(90))+delimiter+
							floatingFormatter.format(getPercentile(95));				
		}
		return ""+getMin()+delimiter+getMax()+delimiter+getMean()+delimiter+getN()+delimiter+
				getStandardDeviation()+delimiter+getVariance()+delimiter+
						getPercentile(5)+delimiter+
						getPercentile(10)+delimiter+
						getPercentile(25)+delimiter+
						getPercentile(50)+delimiter+
						getPercentile(75)+delimiter+
						getPercentile(90)+delimiter+
						getPercentile(95);
		
	}
	

	@Override
	public String getCSVHeader(char delimiter) {
		return "Min"+delimiter+"Max"+delimiter+"Mean"+delimiter+"Count"+delimiter+"StandardDeviation"+delimiter+"variance"+delimiter+"percentile5"+delimiter+"percentile10"+delimiter+"percentile25"+delimiter +
				"percentile50"+delimiter+"percentile75"+delimiter+"percentile90"+delimiter+"percentile95";
	}
	
	@Override
	public IDescriptiveStatistics createInstance() {
		return new DescriptiveStatistics();
	}
	
}
