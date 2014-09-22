package de.uniol.inf.is.odysseus.peer.distribute.modify.replication.costmodel;

import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.physicaloperator.ReplicationMergePO;

@SuppressWarnings("rawtypes")
public class ReplicationMergePOEstimator extends StandardPhysicalOperatorEstimator<ReplicationMergePO> {

	@Override
	protected Class<? extends ReplicationMergePO> getOperatorClass() {
		return ReplicationMergePO.class;
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
