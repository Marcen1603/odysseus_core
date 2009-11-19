package de.uniol.inf.is.odysseus.p2p.splitting.nosplitting;

import java.util.ArrayList;

import org.w3c.dom.views.AbstractView;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.p2p.P2PSourceAO;
import de.uniol.inf.is.odysseus.p2p.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.PeerGroupTool;

public class NoSplittingStrategy extends
		AbstractSplittingStrategy {

	
	private ArrayList<AbstractLogicalOperator> splitList;


	public NoSplittingStrategy() {
		super();
		this.splitList = new ArrayList<AbstractLogicalOperator>();
	}

	@Override
	public ArrayList<AbstractLogicalOperator> splitPlan(AbstractLogicalOperator plan) {

		P2PSinkAO p2psink = new P2PSinkAO(createSocketAdvertisement().toString());
		plan.subscribe(p2psink, 0, 0, plan.getOutputSchema());
		getSplitList().add(p2psink);
		return getSplitList();
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
	
	
	public ArrayList<AbstractLogicalOperator> getSplitList() {
		return splitList;
	}


	public void setSplitList(ArrayList<AbstractLogicalOperator> splitList) {
		this.splitList = splitList;
	}

}
