package de.uniol.inf.is.odysseus.p2p.splitting.nosplitting;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.AdvertisementTools;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;
import de.uniol.inf.is.odysseus.p2p.splitting.base.SplittingExecutionHandler;

public class NoSplitting extends
		AbstractSplittingStrategy {

	@Override
	public ArrayList<ILogicalOperator> splitPlan(ILogicalOperator plan) {
		ArrayList<ILogicalOperator> splitList = new ArrayList<ILogicalOperator>();
		P2PSinkAO p2psink = new P2PSinkAO(AdvertisementTools.createSocketAdvertisement().toString());
		plan.subscribeSink(p2psink, 0, 0, plan.getOutputSchema());
		splitList.add(p2psink);

		return splitList;
	}

	@Override
	public String getName() {
		return "NoSplitting";
	}

	@Override
	public void initializeService() {
		IExecutionHandler<NoSplitting> handler = new SplittingExecutionHandler<NoSplitting>();
		handler.setPeer(getPeer());
		handler.setFunction(this);
		getPeer().bindExecutionHandler(handler);
	}

}
