package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

/**
 * This class represents the logical operator for the leader algorithm.
 * 
 * @author Kolja Blohm
 * 
 */
public class LeaderAO extends AbstractClusteringAO {

	private static final long serialVersionUID = -5809997584476169607L;
	private Double threshold;

	/**
	 * Returns the leader algorithms threshold
	 * 
	 * @return the threshold
	 */
	public Double getThreshold() {
		return threshold;
	}

	/**
	 * Creates a new LeaderAO.
	 */
	public LeaderAO() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param copy
	 *            the LeaderAO to copy.
	 */
	public LeaderAO(LeaderAO copy) {
		super(copy);
		this.threshold = copy.threshold;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public LeaderAO clone() {
		return new LeaderAO(this);
	}

	/**
	 * Sets the leader algorithms threshold.
	 * 
	 * @param threshold
	 *            the threshold.
	 */
	public void setThreshold(Double threshold) {

		this.threshold = threshold;
	}

}
