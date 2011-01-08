package de.uniol.inf.is.odysseus.datamining.clustering;

import de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator.LeaderPO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class represents a cluster for the the leader algorithm implemented in
 * the {@link LeaderPO}.
 * 
 * 
 * @author Kolja Blohm
 * 
 */

public class LeaderCluster<T extends IMetaAttribute> extends AbstractCluster<T> {

	IClusteringObject<T> leader;

	/**
	 * Creates a new LeaderCluster.
	 * 
	 * @param numberOfAttributes
	 *            the number of attributes the inner ClusterinFeature can store.
	 */
	public LeaderCluster(int attributeCount) {
		super(attributeCount);

	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.AbstractCluster#getCenter()
	 */
	@Override
	public IClusteringObject<T> getCenter() {
		return leader;
	}


	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.clustering.AbstractCluster#addTuple(de.uniol.inf.is.odysseus.datamining.clustering.IClusteringObject)
	 */
	@Override
	public void addTuple(IClusteringObject<T> element) {
		super.addTuple(element);
		//sets the first element as this clusters leader
		if (leader == null) {
			leader = element;
		}
	}

}
