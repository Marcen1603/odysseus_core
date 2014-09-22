package de.uniol.inf.is.odysseus.peer.distribute.modify.replication.costmodel;

import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.logicaloperator.ReplicationMergeAO;

public class ReplicationMergeAOEstimator extends StandardLogicalOperatorEstimator<ReplicationMergeAO> {

	@Override
	protected Class<? extends ReplicationMergeAO> getOperatorClass() {
		return ReplicationMergeAO.class;
	}
	
	@Override
	public double getDatarate() {
		double maxDatarate = Double.MIN_VALUE;
		for(DetailCost detailCost : getPrevCostMap().values() ) {
			if( detailCost.getDatarate() > maxDatarate ) {
				maxDatarate = detailCost.getDatarate();
			}
		}
		return maxDatarate;
	}
}
