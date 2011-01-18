package de.uniol.inf.is.odysseus.p2p.splitting.base;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;

public interface ISplittingStrategy {
	public void initializeService();
	public void startService();
	public void finalizeService();
	public ArrayList<ILogicalOperator> splitPlan(ILogicalOperator ao);
	public String getName();
	public void setPeer(IOdysseusPeer peer);
}
