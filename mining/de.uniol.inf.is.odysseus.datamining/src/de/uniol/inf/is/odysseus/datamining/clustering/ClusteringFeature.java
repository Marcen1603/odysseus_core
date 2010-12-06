package de.uniol.inf.is.odysseus.datamining.clustering;

import java.util.Arrays;

public class ClusteringFeature {
	private long count;
	private Double[] sum;
	private Double[] squareSum;
	private int size;

	
	public ClusteringFeature(ClusteringFeature copy) {
		this.count = copy.count;
		this.sum = Arrays.copyOf(copy.sum, copy.sum.length);
		this.squareSum = Arrays.copyOf(copy.squareSum, copy.squareSum.length);
		this.size = copy.size;
	}
	
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
	@Override
	protected ClusteringFeature clone(){
		return new ClusteringFeature(this);
	}

	public void add(ClusteringFeature clusteringFeature) {
		
		this.count += clusteringFeature.getCount();
		addToSum(clusteringFeature.getSum());
		addToSquareSum(clusteringFeature.getSquareSum());
	}
	
	private void addToSum(Double[] sum){
		for(int i = 0; i<sum.length ;i++){
			this.sum[i] += sum[i];
		}
	}
	
	private void addToSquareSum(Double[] squareSum){
		for(int i = 0; i<squareSum.length ;i++){
			this.squareSum[i] += squareSum[i];
		}
	}
}
