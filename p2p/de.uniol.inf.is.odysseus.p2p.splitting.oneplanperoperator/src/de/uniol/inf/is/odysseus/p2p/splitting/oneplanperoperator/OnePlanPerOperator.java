package de.uniol.inf.is.odysseus.p2p.splitting.oneplanperoperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSourceAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.rewrite.drools.RestructHelper;

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
		P2PSinkAO p2psink = new P2PSinkAO(current);
		String adv = createSocketAdvertisement().toString();
 		P2PSourceAO p2paccess = new P2PSourceAO(p2psink.getAdv());
 		//für den ersten Operator
 		if(iLogicalOperator.getSubscriptions().isEmpty()) {
 			P2PSinkAO tempAO = new P2PSinkAO(createSocketAdvertisement().toString());
 			tempAO.subscribeTo(iLogicalOperator, 0, 0, iLogicalOperator.getOutputSchema());
 			getSplitList().add(tempAO);
 		}
//		AbstractLogicalOperator tempAO = (AbstractLogicalOperator) iLogicalOperator.clone();
 		if(!(iLogicalOperator instanceof P2PAO)) {
		// Bei Operatoren mit zwei Eingabeports
		if (outputCount == 2) {
			String adv2 = createSocketAdvertisement().toString();
		//	P2PSourceAO p2paccess2 = new P2PSourceAO(adv2);
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

//			tempAO.subscribeTo(p2paccess,0,0, tempAO.getOutputSchema());
//			tempAO.subscribeTo(p2paccess2,1,0, tempAO.getOutputSchema());
//			p2psink.subscribeTo(tempAO,0,0, tempAO.getOutputSchema());
//			if(!tempAO.getSubscribedTo().isEmpty()) {
//				
//				for(LogicalSubscription ls : tempAO.getSubscribedTo()) {
//					if(!ls.getTarget().equals(p2paccess) || !ls.getTarget().equals(p2paccess2) || !ls.getTarget().equals(p2psink)) {
//						tempAO.unsubscribeTo(ls);
//					}
//				}
//			}
//			if(!tempAO.getSubscriptions().isEmpty()) {
//				for(LogicalSubscription ls :tempAO.getSubscriptions()) {
//					if(!ls.getTarget().equals(p2paccess) || !ls.getTarget().equals(p2paccess2) || !ls.getTarget().equals(p2psink)) {
//						tempAO.unsubscribe(ls);
//					}
//				}
//			}
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
//			} else {6
//			p2psink.subscribeTo(p2paccess, 0, 0, tempAO.getOutputSchema());
			
//			for(LogicalSubscription subscription : tempAO.getSubscribedTo()) {
//				tempAO.unsubscribeTo(subscription);
//			}
			
			
			RestructHelper.insertOperator(p2paccess, iLogicalOperator, 0, 0, 0);
//			p2paccess.setOutputSchema(p2paccess.getSubscribedTo().iterator().next().getTarget().getOutputSchema());
			RestructHelper.insertOperator(p2psink, p2paccess,0,0,0);
			p2paccess.setOutputSchema(p2psink.getOutputSchema());
			Collection<LogicalSubscription> col = p2paccess.getSubscribedTo();
			for(LogicalSubscription elem : col) {
				if(elem.getTarget().equals(p2psink)) {
					elem.setInputSchema(elem.getTarget().getOutputSchema());
				}
			}
//			Collection<ILogicalOperator> sw = RestructHelper.switchOperatorInternal(p2psink, p2paccess);
//			p2psink.unsubscribeSubscriptionTo(p2paccess, 0, 0);
//			Collection<ILogicalOperator> ins = RestructHelper.insertOperator(p2psink, iLogicalOperator, 0, 0, 0);
//			Collection<ILogicalOperator> sw = RestructHelper.switchOperatorInternal(p2psink,(UnaryLogicalOp) iLogicalOperator);
			
//			RestructHelper.insertOperator(p2paccess, iLogicalOperator, 0, 0, 0);
//			p2paccess.subscribe(tempAO, 0, 0, attributeList);
//			for(LogicalSubscription subscription : tempAO.getSubscriptions()) {
//				tempAO.unsubscribe(subscription);
//			}
//			tempAO.subscribe(p2psink, 0, 0, attributeList);
//			tempAO.subscribeTo(p2paccess,0,0,tempAO.getOutputSchema());
//			tempAO.subscribe(p2psink, 0, 0, tempAO.getOutputSchema());
//			p2psink.subscribeTo(tempAO, 0, 0, tempAO.getOutputSchema());
//				tempAO.subscribeTo(p2paccess,0,0, tempAO.getOutputSchema());
//				p2psink.subscribeTo(tempAO,0,0, tempAO.getOutputSchema());
				temp = adv;
//				if(!tempAO.getSubscribedTo().isEmpty()) {
//					
//					for(LogicalSubscription ls : tempAO.getSubscribedTo()) {
//						if(!ls.getTarget().equals(p2paccess)) {
//							tempAO.unsubscribeTo(ls);
//						}
//					}
//				}
//				if(!tempAO.getSubscriptions().isEmpty()) {
//					for(LogicalSubscription ls :tempAO.getSubscriptions()) {
//						if(!ls.getTarget().equals(p2psink)) {
//							tempAO.unsubscribe(ls);
//						}
//					}
//				}
//			}
		  // Wir befinden uns bei der Source. Zur Sicherheit nochmal geprüft
		} 
//		else if(outputCount == 0 && (iLogicalOperator instanceof AccessAO)) {
////			SDFSource source = ((AccessAO) iLogicalOperator).getSource();
////			String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
////			.getSources().get(source.toString()).toString();
////			p2paccess.setAdv(sourceAdv);
////			tempAO = (AbstractLogicalOperator) iLogicalOperator;
////			return;
//			for(LogicalSubscription subscription : tempAO.getSubscriptions()) {
//				tempAO.unsubscribe(subscription);
//			}
//			tempAO.subscribe(p2psink,0,0,attributeList);
////			p2psink.subscribeTo(tempAO,0,0, tempAO.getOutputSchema());
////			if(!tempAO.getSubscriptions().isEmpty()) {
////				for(LogicalSubscription ls :tempAO.getSubscriptions()) {
////					if(!ls.getTarget().equals(p2psink)) {
////						tempAO.unsubscribe(ls);
////					}
////				}
////			}
//			p2psink.subscribeTo(iLogicalOperator, 0, 0, iLogicalOperator.getOutputSchema());
//		}
//		p2psink.subscribeTo(p2paccess, 0, 0, tempAO.getOutputSchema());
//		p2psink.subscribeTo(tempAO,0,0, tempAO.getOutputSchema());
 		
//		getSplitList().add(p2psink);
//		getSplitList().add(p2paccess);
 		}
 		else if(iLogicalOperator instanceof P2PSinkAO) {
 			getSplitList().add((AbstractLogicalOperator) iLogicalOperator);
 		}
 		if(iLogicalOperator instanceof ProjectAO) {
 			Iterator<LogicalSubscription> iter = iLogicalOperator.getSubscribedTo().iterator();
 			while(iter.hasNext()) {
 				LogicalSubscription subscr = iter.next();
 				subscr.setInputSchema(subscr.getTarget().getOutputSchema());
 			}
 		}
		for (int i = 0; i < outputCount; ++i) {
			if (i == 1) {
				adv = temp;
			}
			if (iLogicalOperator instanceof AccessAO) {
				return;
			}
			splitPlan(iLogicalOperator.getSubscribedTo(i).getTarget(), adv);
			//Nach der Rekursion Verbindung zwischen P2PSource und P2PSinks kappen
			if(iLogicalOperator instanceof P2PSourceAO) {
				((P2PSourceAO)iLogicalOperator).unsubscribeTo(iLogicalOperator.getSubscribedTo(0));
			}
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
