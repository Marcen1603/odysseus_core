package de.uniol.inf.is.odysseus.p2p.splitting.nosplitting;

import java.util.ArrayList;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.jxta.SubplanJxtaImpl;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;

public class NoSplittingStrategy extends
		AbstractSplittingStrategy {

	@Override
	public ArrayList<ILogicalOperator> splitPlan(ILogicalOperator plan) {
		ArrayList<ILogicalOperator> splitList = new ArrayList<ILogicalOperator>();
		P2PSinkAO p2psink = new P2PSinkAO(createSocketAdvertisement().toString());
		plan.subscribeSink(p2psink, 0, 0, plan.getOutputSchema());
		splitList.add(p2psink);

		return splitList;
	}

	public PipeAdvertisement createSocketAdvertisement() {
		PipeID socketID = null;

		socketID = (PipeID) IDFactory.newPipeID(PeerGroupTool.getPeerGroup().getPeerGroupID());
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(socketID);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("P2PPipe Verbindung");

		return advertisement;
	}



	@Override
	public String getName() {
		return "NoSplitting";
	}

	@Override
	public void finalizeService() {
		
	}

	@Override
	public void initializeService() {
		IExecutionHandler<AbstractPeer, AbstractSplittingStrategy> handler = new NoSplittingExecutionHandler();
		handler.setPeer(getPeer());
		handler.setFunction(this);
		handler.setProvidedLifecycle(Lifecycle.SPLIT);

		getPeer().bindExecutionHandler(handler);
	}

	@Override
	public void startService() {
		
	}

}
