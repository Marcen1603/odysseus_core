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
import de.uniol.inf.is.odysseus.logicaloperator.base.RestructHelper;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PSourceAO;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.splitting.base.AbstractSplittingStrategy;
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
		System.out.println("Zeige Splits--------------------------");
		for (ILogicalOperator iLogicalOperator : splitList) {
			System.out.println(AbstractTreeWalker.prefixWalk(iLogicalOperator,
					new AlgebraPlanToStringVisitor(true)));
		}
		System.out.println("Zeige Splits Ende-------------------");
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
		int outputCount = iLogicalOperator.getSubscribedToSource().size();
		String temp = null;
		P2PSinkAO p2psink = new P2PSinkAO(current);
		String adv = createSocketAdvertisement().toString();
 		P2PSourceAO p2psource = new P2PSourceAO(p2psink.getAdv());
 		//für den ersten Operator
 		if(iLogicalOperator.getSubscriptions().isEmpty()) {
 			P2PSinkAO tempAO = new P2PSinkAO(createSocketAdvertisement().toString());
 			tempAO.subscribeToSource(iLogicalOperator, 0, 0, iLogicalOperator.getOutputSchema());
 			splitList.add(tempAO);
 		}
 		if(!(iLogicalOperator instanceof P2PAO)) {
			// Bei Operatoren mit zwei Eingabeports
			if (outputCount == 2) {
				
				System.out.println("Hier kommen wir auch rein, aber wir machen hier garnichts");
				String adv2 = createSocketAdvertisement().toString();
			//	P2PSourceAO p2paccess2 = new P2PSourceAO(adv2);
				temp = adv2;
				P2PSinkAO p2psink2 = new P2PSinkAO(temp);
				P2PSourceAO p2psource2 = new P2PSourceAO(p2psink2.getAdv());
				RestructHelper.insertOperator(p2psource, iLogicalOperator, 0, 0, 0);
				RestructHelper.insertOperator(p2psource2, iLogicalOperator, 1, 0, 0);
				RestructHelper.insertOperator(p2psink, p2psource,0,0,0);
				RestructHelper.insertOperator(p2psink2, p2psource2,0,0,0);
			} else if(outputCount == 1){
				RestructHelper.insertOperator(p2psource, iLogicalOperator, 0, 0, 0);
				RestructHelper.insertOperator(p2psink, p2psource,0,0,0);
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
			splitPlan(iLogicalOperator.getSubscribedToSource(i).getTarget(), adv, splitList);
			//Nach der Rekursion Verbindung zwischen P2PSource und P2PSinks kappen
			if(iLogicalOperator instanceof P2PSourceAO) {
				((P2PSourceAO)iLogicalOperator).unsubscribeFromSource(iLogicalOperator.getSubscribedToSource(0));
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
