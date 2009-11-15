package de.uniol.inf.is.odysseus.p2p.splitting.oneplanperoperator;

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

public class OnePlanPerOperator extends
		AbstractSplittingStrategy {

	
	private ArrayList<AbstractLogicalOperator> splitList;


	public OnePlanPerOperator() {
		super();
		this.splitList = new ArrayList<AbstractLogicalOperator>();
	}

	@Override
	public ArrayList<AbstractLogicalOperator> splitPlan(AbstractLogicalOperator plan) {

		splitPlan(plan,
				createSocketAdvertisement().toString());
		if(getRefinement()!=null) {
			setSplitList(getRefinement().refinePlan(this.splitList));
		}
		
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

	private void splitPlan(ILogicalOperator iLogicalOperator, String current) {
		int outputCount = iLogicalOperator.getSubscribedTo().size();
		String temp = null;
		P2PSinkAO p2ppipe = new P2PSinkAO(current);
		String adv = createSocketAdvertisement().toString();
		P2PSourceAO p2paccess = new P2PSourceAO(adv);
		AbstractLogicalOperator tempAO = (AbstractLogicalOperator) iLogicalOperator.clone();
		if(!tempAO.getSubscribedTo().isEmpty()) {
			for(LogicalSubscription ls : tempAO.getSubscribedTo()) {
				tempAO.unsubscribeTo(ls);
			}
		}
		if(!tempAO.getSubscriptions().isEmpty()) {
			for(LogicalSubscription ls :tempAO.getSubscriptions()) {
				tempAO.unsubscribe(ls);
			}
		}
		// Bei Operatoren mit zwei Eingabeports
		if (outputCount == 2) {
			String adv2 = createSocketAdvertisement().toString();
			P2PSourceAO p2paccess2 = new P2PSourceAO(adv2);
			temp = adv2;
			
//			if (iLogicalOperator.getSubscribedTo(0).getTarget() instanceof AccessAO) {
//				SDFSource source = ((AccessAO) iLogicalOperator.getSubscribedTo(0).getTarget()).getSource();
//				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
//						.getSources().get(source.toString()).toString();
//				p2paccess.setAdv(sourceAdv);
//			}
//			if (iLogicalOperator.getSubscribedTo(1).getTarget() instanceof AccessAO) {
//				SDFSource source = ((AccessAO) iLogicalOperator.getSubscribedTo(1).getTarget()).getSource();
//				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
//						.getSources().get(source.toString()).toString();
//				p2paccess2.setAdv(sourceAdv);
//			}

			tempAO.subscribeTo(p2paccess,0,0, tempAO.getOutputSchema());
			tempAO.subscribeTo(p2paccess2,1,0, tempAO.getOutputSchema());
		} else if(outputCount == 1){
			// Wenn der naechste Operator ein AccessAO ist dann kann dieser
			// Operator uebersprungen werden
//			if (iLogicalOperator.getSubscribedTo(0).getTarget() instanceof AccessAO) {
//				SDFSource source = ((AccessAO) iLogicalOperator.getSubscribedTo(0).getTarget()).getSource();
//				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
//						.getSources().get(source.toString()).toString();
//				p2paccess.setAdv(sourceAdv);
//				tempAO.subscribeTo(p2paccess,0,0);
//				temp = adv;
//			} else {
				tempAO.subscribeTo(p2paccess,0,0, tempAO.getOutputSchema());
				temp = adv;
//			}
		  // Wir befinden uns bei der Source. Zur Sicherheit nochmal geprüft
		} 
//		else if(outputCount == 0 && (iLogicalOperator instanceof AccessAO)) {
//			SDFSource source = ((AccessAO) iLogicalOperator).getSource();
//			String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
//			.getSources().get(source.toString()).toString();
//			p2paccess.setAdv(sourceAdv);
//			tempAO = (AbstractLogicalOperator) iLogicalOperator;
//			return;
//		}
		p2ppipe.subscribeTo(tempAO,0,0, tempAO.getOutputSchema());

		getSplitList().add(p2ppipe);

		for (int i = 0; i < outputCount; ++i) {
			if (i == 1) {
				adv = temp;
			}
			if (iLogicalOperator instanceof AccessAO) {
				return;
			}
			splitPlan(iLogicalOperator.getSubscribedTo(i).getTarget(), adv);
		}
	}

	@Override
	public String getName() {
		return "OneOperatorPerPeer";
	}
	
	
	public ArrayList<AbstractLogicalOperator> getSplitList() {
		return splitList;
	}


	public void setSplitList(ArrayList<AbstractLogicalOperator> splitList) {
		this.splitList = splitList;
	}

}
