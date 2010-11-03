package de.uniol.inf.is.odysseus.datamining.clustering;

import java.util.Arrays;

public class ClusteringFeature {
	private long count;
	private Double[] sum;
	private Double[] squareSum;
	private int size;

	public ClusteringFeature(int size) {
		this.size = size;
		sum = new Double[size];
		squareSum = new Double[size];
		Arrays.fill(sum, 0D);
		Arrays.fill(squareSum, 0D);
	}

	public long getCount() {
		return count;
	}

	public void add(Object[] add) {
		count++;
		for (int i = 0; i < add.length; i++) {
			Double toAdd = Double.valueOf(add[i].toString());
			sum[i] += toAdd;
			squareSum[i] += Math.pow(toAdd, 2);

		}
	}

	public Double[] getSum() {
		return sum;
	}

	public Double[] getSquareSum() {
		return squareSum;
	}

	public Double[] getMean() {
		Double[] mean = new Double[size];
		if (count != 0) {

			for (int i = 0; i < size; i++) {
				mean[i] = sum[i] / count;
			}
		}
		return mean;
	}
}
