package de.uniol.inf.is.odysseus.p2p.ac.bid;

import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.costmodel.ICost;

public interface IP2PBidGenerator {

	public double generateBid( IAdmissionControl sender, ICost actSystemLoad, ICost queryCost, ICost maxCost );
}
