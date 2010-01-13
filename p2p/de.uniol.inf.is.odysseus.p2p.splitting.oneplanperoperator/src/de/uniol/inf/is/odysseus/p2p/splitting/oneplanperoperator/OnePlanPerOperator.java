package de.uniol.inf.is.odysseus.p2p.splitting.oneplanperoperator;

import java.util.ArrayList;
import java.util.List;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSourceAO;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;
import de.uniol.inf.is.odysseus.rewrite.drools.RestructHelper;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

public class OnePlanPerOperator extends
		AbstractSplittingStrategy {

	

	@Override
	public ArrayList<ILogicalOperator> splitPlan(ILogicalOperator plan) {
		ArrayList<ILogicalOperator> splitList = new ArrayList<ILogicalOperator>();
		System.out.println(AbstractTreeWalker.prefixWalk(plan,
							new AlgebraPlanToStringVisitor(true)));
		splitPlan(plan, createSocketAdvertisement().toString(), splitList);
		if(getRefinement()!=null) {
			return getRefinement().refinePlan(splitList);
		}
			
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

	private void splitPlan(ILogicalOperator iLogicalOperator, String current, List<ILogicalOperator> splitList) {
		int outputCount = iLogicalOperator.getSubscribedTo().size();
		String temp = null;
		P2PSinkAO p2psink = new P2PSinkAO(current);
		String adv = createSocketAdvertisement().toString();
 		P2PSourceAO p2psource = new P2PSourceAO(p2psink.getAdv());
 		//für den ersten Operator
 		if(iLogicalOperator.getSubscriptions().isEmpty()) {
 			P2PSinkAO tempAO = new P2PSinkAO(createSocketAdvertisement().toString());
 			tempAO.subscribeTo(iLogicalOperator, 0, 0, iLogicalOperator.getOutputSchema());
 			splitList.add(tempAO);
 		}
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
				RestructHelper.insertOperator(p2psource, iLogicalOperator, 0, 0, 0);
				System.out.println(AbstractTreeWalker.prefixWalk(iLogicalOperator,
						new AlgebraPlanToStringVisitor(true)));
				RestructHelper.insertOperator(p2psink, p2psource,0,0,0);
				System.out.println(AbstractTreeWalker.prefixWalk(iLogicalOperator,
						new AlgebraPlanToStringVisitor(true)));
				temp = adv;
			} 
 		}
 		else if(iLogicalOperator instanceof P2PSinkAO) {
 			splitList.add((AbstractLogicalOperator) iLogicalOperator);
 		}
		for (int i = 0; i < outputCount; ++i) {
			if (i == 1) {
				adv = temp;
			}
			if (iLogicalOperator instanceof AccessAO) {
				return;
			}
			splitPlan(iLogicalOperator.getSubscribedTo(i).getTarget(), adv, splitList);
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

	@Override
	public void finalizeService() {
		
	}

	@Override
	public void initializeService() {
		IExecutionHandler<AbstractPeer, AbstractSplittingStrategy> handler = new OnePlanPerOperatorExecutionHandler<AbstractPeer, AbstractSplittingStrategy>();
		handler.setPeer(getPeer());
		handler.setFunction(this);
		handler.setProvidedLifecycle(Lifecycle.SPLIT);
		getPeer().bindExecutionHandler(handler);
		
	}

	@Override
	public void startService() {
		
	}
	

}
