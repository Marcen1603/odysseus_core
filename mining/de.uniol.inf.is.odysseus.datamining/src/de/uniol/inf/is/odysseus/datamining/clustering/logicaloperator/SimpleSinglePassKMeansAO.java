package de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;

public class SimpleSinglePassKMeansAO extends AbstractClusteringAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 288269790951499746L;
	private int clusterCount;
	private int bufferSize;

	public int getClusterCount() {
		return clusterCount;
	}

	protected SimpleSinglePassKMeansAO(SimpleSinglePassKMeansAO copy) {
		super(copy);
		this.clusterCount = copy.getClusterCount();
		this.bufferSize = copy.bufferSize;
	}

	public SimpleSinglePassKMeansAO() {
	}

	

	@Override
	public AbstractLogicalOperator clone() {

		return new SimpleSinglePassKMeansAO(this);
	}

	public void setClusterCount(int clusterCount) {
		this.clusterCount = clusterCount;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getBufferSize() {
		return bufferSize;
	}

}
