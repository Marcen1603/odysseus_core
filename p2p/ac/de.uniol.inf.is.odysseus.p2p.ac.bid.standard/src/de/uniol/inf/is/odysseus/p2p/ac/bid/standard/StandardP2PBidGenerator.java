package de.uniol.inf.is.odysseus.p2p.ac.bid.standard;

import java.lang.Runtime;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.costmodel.ICost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCost;
import de.uniol.inf.is.odysseus.p2p.ac.bid.IP2PBidGenerator;

public class StandardP2PBidGenerator implements IP2PBidGenerator {

	public double generateBid(IAdmissionControl sender, ICost actSystemLoad,
			ICost queryCost, ICost maxCost) {
		
		if( actSystemLoad instanceof OperatorCost ) {
			
			Runtime runtime = Runtime.getRuntime();
			
			// calculate potencial system load
			OperatorCost newSystemLoad = (OperatorCost)actSystemLoad.merge(queryCost);
			double cpu = newSystemLoad.getCpuCost();
			double memFactor = newSystemLoad.getMemCost() / (double)runtime.totalMemory();
			
			// higher cost --> higher bid
			return cpu + memFactor;
		} else {
			return 1;
		}
	}

}
