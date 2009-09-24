package de.uniol.inf.is.odysseus.monitoring;

import java.util.Arrays;

public class MyStats {
	private int[] values = null;
	private int count = 0;
	boolean notcalculated = true;
	private double standardDeviation;
	private double variance;
	private double sum = 0;
	private int offset=0;

	public MyStats(){
		reset();	
	}

	public void reset() {
		this.values = new int[10000]; // MG:Ihh ... 			
		this.count = 0;
		this.notcalculated = true;
		this.standardDeviation = 0;
		this.variance = 0;
		this.sum = 0;
		this.offset = 0;
	}
	
	public MyStats(MyStats stats) {
		this.values = new int[10000]; // MG:Ihh ... 
		System.arraycopy(stats.values, 0, this.values, 0, stats.values.length);			
		this.count = stats.count;
		this.notcalculated = stats.notcalculated;
		this.standardDeviation = stats.standardDeviation;
		this.variance = stats.variance;
		this.sum = stats.sum;
		this.offset = stats.offset;

	}


	public int getN() {
		return this.count;
	}

	public void addValue(int latency) {
		if (!notcalculated) {//TODO variablenname
			throw new RuntimeException("geht nicht mehr, array schon sortiert ;)");
		}
		if (count + 1 == values.length) {
			int[] tmp = values;
			values = new int[tmp.length * 3 / 2 + 1];
			System.arraycopy(tmp, 0, values, 0, tmp.length);
		}
		values[count++] = latency;
		sum += latency;
	}

	public double getMean() {
		return this.sum / this.count;
	}

	public double getStandardDeviation() {
		prepareStats();
		return this.standardDeviation;
	}

	private void prepareStats() {
		if (notcalculated == false) {
			return;
		}
		notcalculated = false;
		double accum = 0.0;
		double dev = 0.0;
		double accum2 = 0.0;
		double mean = getMean();
		Arrays.sort(this.values);
		for (int i = offset; i < this.values.length; ++i){
			dev = this.values[i] - mean;
			accum += dev * dev;
			accum2 += dev;
		}
		this.variance = (accum - (accum2 * accum2 / count)) / count;
		this.standardDeviation = Math.sqrt(this.variance);
		this.offset = this.values.length - this.count;
	}

	private double getVariance() {
		prepareStats();
		return this.variance;
	}

	public int getMin() {
		prepareStats();
		return this.values[offset];
	}

	public int getMax() {
		prepareStats();
		return this.values[this.values.length - 1];
	}

	public int getPercentile(int i) {
		prepareStats();
		int pos = offset + count *  i / 100;
		if (this.values.length % 2 == 0) {
			//TODO nicht ganz genau wegen int div, sollte double returnen
			return (this.values[pos] + this.values[pos - 1]) / 2;
		} else {
			return this.values[pos];
		}
	}

}