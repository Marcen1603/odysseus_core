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
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.MapAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.p2p.P2PAccessAO;
import de.uniol.inf.is.odysseus.p2p.P2PPipeAO;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.splitting.ISplittingStrategy;
import de.uniol.inf.is.odysseus.priority.PriorityAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;

public class OneOperatorPerSubplanStrategyJxtaImpl implements
		ISplittingStrategy {

	PeerGroup netPeerGroup;

	public OneOperatorPerSubplanStrategyJxtaImpl(PeerGroup netPeerGroup) {
		super();
		this.netPeerGroup = netPeerGroup;
	}

	@Override
	public ArrayList<AbstractLogicalOperator> splittPlan(AbstractLogicalOperator plan) {

		ArrayList<AbstractLogicalOperator> tempList = new ArrayList<AbstractLogicalOperator>();

		splittPlan(plan, 0, plan.getNumberOfInputs(),
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

	private void splittPlan(ILogicalOperator iLogicalOperator, int no, int sizeOld, String current,
			ArrayList<AbstractLogicalOperator> list) {
		int inputCount = iLogicalOperator.getNumberOfInputs();
		String temp;
		P2PPipeAO p2ppipe = new P2PPipeAO(current);
		String adv = createSocketAdvertisement().toString();
		P2PAccessAO p2paccess = new P2PAccessAO(adv);

		AbstractLogicalOperator tempAO = (AbstractLogicalOperator) iLogicalOperator.clone();
		if (inputCount == 2) {
			// Wenn es sich um einen Operator mit 2 Eingabeports handelt dann
			// muss noch ein 2 P2PAccessAO rangehangen werden
			String adv2 = createSocketAdvertisement().toString();
			P2PAccessAO p2paccess2 = new P2PAccessAO(adv2);
			temp = adv2;
			tempAO.setNoOfInputs(inputCount);

			if (iLogicalOperator.getInputAO(0) instanceof AccessAO) {
				SDFSource source = ((AccessAO) iLogicalOperator.getInputAO(0)).getSource();
				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
						.getSources().get(source.toString()).toString();
				p2paccess.setAdv(sourceAdv);
			}
			if (iLogicalOperator.getInputAO(1) instanceof AccessAO) {
				SDFSource source = ((AccessAO) iLogicalOperator.getInputAO(1)).getSource();
				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
						.getSources().get(source.toString()).toString();
				p2paccess2.setAdv(sourceAdv);
			}

			tempAO.setInputAO(0, p2paccess);
			tempAO.setInputAO(1, p2paccess2);
		} else {
			// Wenn der naechste Operator ein AccessAO ist dann kann dieser
			// Operator uebersprungen werden
			if (iLogicalOperator.getInputAO(0) instanceof AccessAO) {
				SDFSource source = ((AccessAO) iLogicalOperator.getInputAO(0)).getSource();
				String sourceAdv = AdministrationPeerJxtaImpl.getInstance()
						.getSources().get(source.toString()).toString();
				p2paccess.setAdv(sourceAdv);
				tempAO.setNoOfInputs(inputCount);
				tempAO.setInputAO(0, p2paccess);
				temp = adv;
			} else {
				tempAO.setNoOfInputs(inputCount);
				tempAO.setInputAO(0, p2paccess);
				temp = adv;
			}
		}
		p2ppipe.setNoOfInputs(1);
		p2ppipe.setInputAO(0, tempAO);

		this.setIDSizes(p2ppipe);
		list.add(p2ppipe);

		for (int i = 0; i < inputCount; ++i) {
			if (i == 1) {
				adv = temp;
			}
			if (iLogicalOperator.getInputAO(i) instanceof AccessAO) {
				return;
			}
			splittPlan(iLogicalOperator.getInputAO(i), i, inputCount, adv, list);
		}
	}

	private void setIDSizes(ILogicalOperator plan) {
		// TODO: Klaeren wofür die IDSizes notwendig sind, wird das überhaupt irgendwo genutzt?
		
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
