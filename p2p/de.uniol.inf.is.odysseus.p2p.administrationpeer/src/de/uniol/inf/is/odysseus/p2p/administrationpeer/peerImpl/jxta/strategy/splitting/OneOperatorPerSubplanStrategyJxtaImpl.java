package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.strategy.splitting;

import java.util.ArrayList;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.p2p.P2PSourceAO;
import de.uniol.inf.is.odysseus.p2p.P2PSinkAO;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.splitting.ISplittingStrategy;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class OneOperatorPerSubplanStrategyJxtaImpl implements
		ISplittingStrategy {

	private PeerGroup netPeerGroup;

	public PeerGroup getNetPeerGroup() {
		return netPeerGroup;
	}

	public void setNetPeerGroup(PeerGroup netPeerGroup) {
		this.netPeerGroup = netPeerGroup;
	}

	public OneOperatorPerSubplanStrategyJxtaImpl(PeerGroup netPeerGroup) {
		super();
		this.netPeerGroup = netPeerGroup;
	}

	@Override
	public ArrayList<AbstractLogicalOperator> splitPlan(AbstractLogicalOperator plan) {

		ArrayList<AbstractLogicalOperator> tempList = new ArrayList<AbstractLogicalOperator>();
//		if(plan instanceof AccessAO) {
//			tempList.add(plan);
//			return tempList;
//		}
		splitPlan(plan,
				createSocketAdvertisement().toString(), tempList);
		return tempList;
	}

	public PipeAdvertisement createSocketAdvertisement() {
		PipeID socketID = null;

		socketID = (PipeID) IDFactory.newPipeID(netPeerGroup.getPeerGroupID());
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(socketID);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("Socket tutorial");

		return advertisement;
	}

	private void splitPlan(ILogicalOperator iLogicalOperator, String current,
			ArrayList<AbstractLogicalOperator> list) {
		int inputCount = iLogicalOperator.getSubscribedTo().size();
		String temp = null;
		P2PSinkAO p2ppipe = new P2PSinkAO(current);
		String adv = createSocketAdvertisement().toString();
		P2PSourceAO p2paccess = new P2PSourceAO(adv);
		System.out.println("Der Übergebene LogicalOperator im split ist leer? "+(iLogicalOperator==null));
		AbstractLogicalOperator tempAO = (AbstractLogicalOperator) iLogicalOperator.clone();
		if (inputCount == 2) {
			// Wenn es sich um einen Operator mit 2 Eingabeports handelt dann
			// muss noch ein 2 P2PAccessAO rangehangen werden
			String adv2 = createSocketAdvertisement().toString();
			P2PSourceAO p2paccess2 = new P2PSourceAO(adv2);
			temp = adv2;
			
			if (iLogicalOperator.getSubscribedTo(0).getTarget() instanceof AccessAO) {
				SDFSource source = ((AccessAO) iLogicalOperator.getSubscribedTo(0).getTarget()).getSource();
				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
						.getSources().get(source.toString()).toString();
				p2paccess.setAdv(sourceAdv);
			}
			if (iLogicalOperator.getSubscribedTo(1).getTarget() instanceof AccessAO) {
				SDFSource source = ((AccessAO) iLogicalOperator.getSubscribedTo(1).getTarget()).getSource();
				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
						.getSources().get(source.toString()).toString();
				p2paccess2.setAdv(sourceAdv);
			}

			tempAO.subscribeTo(p2paccess,0,0);
			tempAO.subscribeTo(p2paccess2,1,0);
		} else if(inputCount == 1){
			// Wenn der naechste Operator ein AccessAO ist dann kann dieser
			// Operator uebersprungen werden
			if (iLogicalOperator.getSubscribedTo(0).getTarget() instanceof AccessAO) {
				System.out.println("Wir landen beim splitten hier");
				SDFSource source = ((AccessAO) iLogicalOperator.getSubscribedTo(0).getTarget()).getSource();
				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
						.getSources().get(source.toString()).toString();
				p2paccess.setAdv(sourceAdv);
				tempAO.subscribeTo(p2paccess,0,0);
				temp = adv;
			} else {
				System.out.println("oder etwa hier?");
				tempAO.subscribeTo(p2paccess,0,0);
				temp = adv;
			}
		} else if(inputCount == 0) {
			System.out.println("splitte hier den plan");
			//dann sind wir wohl accessao selbst
			SDFSource source = ((AccessAO) iLogicalOperator).getSource();
			String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
			.getSources().get(source.toString()).toString();
			p2paccess.setAdv(sourceAdv);
			tempAO = p2paccess;
		}
		p2ppipe.subscribeTo(tempAO,0,0, tempAO.getOutputSchema());
		System.out.println("das connection pipe für den Thin-Peer am Ende"+p2ppipe.getAdv().toString());

//		this.setIDSizes(p2ppipe);
		list.add(p2ppipe);

		for (int i = 0; i < inputCount; ++i) {
			if (i == 1) {
				adv = temp;
			}
			if (iLogicalOperator.getSubscribedTo(i).getTarget() instanceof AccessAO) {
				return;
			}
			splitPlan(iLogicalOperator.getSubscribedTo(i).getTarget(), adv, list);
		}
	}

	private void setIDSizes(ILogicalOperator plan) {
		// TODO: Klaeren wof�r die IDSizes notwendig sind, wird das �berhaupt irgendwo genutzt?
		
//		for (int i = 0; i < plan.getNumberOfInputs(); i++) {
//			setIDSizes(plan.getInputAO(i));
//		}
//
//		if (plan instanceof AccessAO) {
//			plan.setInputIDSize(0, 0);
//			plan.setOutputIDSize(0);
//		} else if (plan instanceof ProjectAO) {
//			plan.setInputIDSize(0, plan.getInputAO(0).getOutputIDSize());
//			plan.setOutputIDSize(plan.getInputIDSize(0));
//		} else if (plan instanceof MapAO) {
//			plan.setInputIDSize(0, plan.getInputAO(0).getOutputIDSize());
//			plan.setOutputIDSize(plan.getInputIDSize(0));
//		} else if (plan instanceof JoinAO) {
//			plan.setInputIDSize(0, plan.getInputAO(0).getOutputIDSize());
//			plan.setInputIDSize(1, plan.getInputAO(1).getOutputIDSize());
//			plan.setOutputIDSize(plan.getInputIDSize(0)
//					+ plan.getInputIDSize(1));
//		} else if (plan instanceof SelectAO) {
//			plan.setInputIDSize(0, plan.getInputAO(0).getOutputIDSize());
//			plan.setOutputIDSize(plan.getInputIDSize(0));
//		} else if (plan instanceof WindowAO) {
//			plan.setInputIDSize(0, plan.getInputAO(0).getOutputIDSize());
//			plan.setOutputIDSize(plan.getInputIDSize(0));
//		} else if (plan instanceof PriorityAO) {
//			plan.setInputIDSize(0, plan.getInputAO(0).getOutputIDSize());
//			plan.setOutputIDSize(plan.getInputIDSize(0));
//		}
	}

	@Override
	public String getName() {
		return "OneOperatorPerPeer";
	}

}
