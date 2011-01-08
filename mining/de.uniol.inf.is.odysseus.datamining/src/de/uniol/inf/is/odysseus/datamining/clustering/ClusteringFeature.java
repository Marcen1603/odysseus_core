package de.uniol.inf.is.odysseus.datamining.clustering;

import java.util.Arrays;

/**
 * This class represents a clustering feature, summarizing information about a
 * cluster. It stores the number of data points in the cluster as well as the
 * linear and the square sum of these data points.
 * 
 * @author Kolja Blohm
 * 
 */
public class ClusteringFeature {
	private long size;
	private Double[] sum;
	private Double[] squareSum;
	private int numberOfAttributes;

	/**
	 * Copy constructor used in the clone() method.
	 * 
	 * @param copy
	 *            the original ClusterinFeature to copy.
	 */
	public ClusteringFeature(ClusteringFeature copy) {
		this.size = copy.size;
		this.sum = Arrays.copyOf(copy.sum, copy.sum.length);
		this.squareSum = Arrays.copyOf(copy.squareSum, copy.squareSum.length);
		this.numberOfAttributes = copy.numberOfAttributes;
	}

	/**
	 * Creates a new ClusteringFeature that can hold information about data
	 * points with a given number of attributes.
	 * 
	 * @param numberOfAttributes
	 *            the number of attributes.
	 */
	public ClusteringFeature(int numberOfAttributes) {
		this.numberOfAttributes = numberOfAttributes;
		sum = new Double[numberOfAttributes];
		squareSum = new Double[numberOfAttributes];
		Arrays.fill(sum, 0D);
		Arrays.fill(squareSum, 0D);
	}

	/**
	 * Returns the size of the ClusteringFeature.
	 * 
	 * @return the ClusteringFeatures size.
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Adds a data point represented by an Object-Array containing its attribute
	 * values to the ClusteringFeature.
	 * All the values have to be numeric.
	 * 
	 * @param add the data point to add.
	 */
	public void add(Object[] add) {
		size++;
		for (int i = 0; i < add.length; i++) {
			Double toAdd = Double.valueOf(add[i].toString());
			sum[i] += toAdd;
			squareSum[i] += Math.pow(toAdd, 2);

		}
	}

	/**
	 * Returns the linear sum of all the data points added to the ClusteringFeature.
	 * @return the linear sum.
	 */
	public Double[] getSum() {
		return sum;
	}
	/**
	 * Returns the square sum of all the data points added to the ClusteringFeature.
	 * @return the square sum.
	 */
	public Double[] getSquareSum() {
		return squareSum;
	}

	/**
	 * Calculates and returns the mean of all the data points added to the ClusteringFeature.
	 * 
	 * @return the mean.
	 */
	public Double[] getMean() {
		Double[] mean = new Double[numberOfAttributes];
		Arrays.fill(mean, 0d);
		if (size != 0) {

			for (int i = 0; i < numberOfAttributes; i++) {
				mean[i] = sum[i] / size;
			}
		}
		return mean;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected ClusteringFeature clone() {
		return new ClusteringFeature(this);
	}

	/**
	 * Merges a second ClusteringFeature into this ClusteringFeature.
	 * 
	 * @param clusteringFeature the second ClusteringFeature.
	 */
	public void add(ClusteringFeature clusteringFeature) {

		this.size += clusteringFeature.getSize();
		addToSum(clusteringFeature.getSum());
		addToSquareSum(clusteringFeature.getSquareSum());
	}

	
	/**
	 * Adds a Double-Array to the linear sum.
	 * 
	 * @param sum the Double-Array.
	 */
	private void addToSum(Double[] sum) {
		for (int i = 0; i < sum.length; i++) {
			this.sum[i] += sum[i];
		}
	}
	/**
	 * Adds a Double-Array to the square sum.
	 * 
	 * @param squareSum the Double-Array.
	 */
	private void addToSquareSum(Double[] squareSum) {
		for (int i = 0; i < squareSum.length; i++) {
			this.squareSum[i] += squareSum[i];
		}
	}
}
