package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;


public class LeaderAO extends AbstractClusteringAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5809997584476169607L;
	private Double threshold;

	public Double getThreshold() {
		return threshold;
	}

	public LeaderAO() {
		super();
	}

	public LeaderAO(LeaderAO o) {
		super(o);
		this.threshold = o.threshold;
	}

	

	

	@Override
	public LeaderAO clone() {
		return new LeaderAO(this);
	}

	public void setThreshold(Double threshold) {

		this.threshold = threshold;
	}

}
