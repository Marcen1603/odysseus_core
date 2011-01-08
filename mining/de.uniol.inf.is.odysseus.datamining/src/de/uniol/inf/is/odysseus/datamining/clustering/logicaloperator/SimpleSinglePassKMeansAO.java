package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;

/**
 * This class represents the logical operator for the simple single pass k-means
 * algorithm.
 * 
 * @author Kolja Blohm
 * 
 */
public class SimpleSinglePassKMeansAO extends AbstractClusteringAO {

	private static final long serialVersionUID = 288269790951499746L;
	private int clusterCount;
	private int bufferSize;

	/**
	 * Returns how many clusters the algorithm should find.
	 * 
	 * @return the number of clusters.
	 */
	public int getClusterCount() {
		return clusterCount;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            the SimpleSinglePassKMeansAO to copy.
	 */
	protected SimpleSinglePassKMeansAO(SimpleSinglePassKMeansAO copy) {
		super(copy);
		this.clusterCount = copy.getClusterCount();
		this.bufferSize = copy.bufferSize;
	}

	/**
	 * Creates a new SimpleSinglePassKMeansAO.
	 */
	public SimpleSinglePassKMeansAO() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {

		return new SimpleSinglePassKMeansAO(this);
	}

	/**
	 * Sets the number of clusters the algorithm has to find.
	 * 
	 * @param clusterCount
	 *            the number of clusters.
	 */
	public void setClusterCount(int clusterCount) {
		this.clusterCount = clusterCount;
	}

	/**
	 * Sets the size of the buffer. The size is measured in the number of data
	 * points the buffer can contain.
	 * 
	 * @param bufferSize
	 *            the buffer size.
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * Returns the number of data points the buffer can contain.
	 * 
	 * @return the buffer size.
	 */
	public int getBufferSize() {
		return bufferSize;
	}

}
