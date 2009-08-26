package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.p2p.P2PPipeAO;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.AbstractOperatorPeer;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.ISourceHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.phyOp.jxta.IP2PInputPO;

import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.SourceAdvertisement;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.Subscription;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

public class SourceHandlerJxtaImpl implements ISourceHandler {

	private ArrayList<SourceAdvertisement> advList = new ArrayList<SourceAdvertisement>();

	private int LIFETIME = 30000;

	private AbstractOperatorPeer oPeer;

	public SourceHandlerJxtaImpl(AbstractOperatorPeer aPeer) {
		this.setoPeer(aPeer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		// init sources
		Set<Entry<String, ILogicalOperator>> views = DataDictionary.getInstance()
				.getViews();
		for (Entry<String, ILogicalOperator> v : views) {
			if (WrapperPlanFactory.getAccessPlan(v.getKey()) == null) {
				ISource<?> viewPlan = null;
//				try {
//					System.out.println(OperatorPeerJxtaImpl.getInstance().getTrafo().getClass().toString());
//					if (OperatorPeerJxtaImpl.getInstance().getTrafo().getClass() == RelationalPipesTransform.class) {
//						viewPlan = ((RelationalPipesTransform) OperatorPeerJxtaImpl
//								.getInstance().getTrafo()).transformPNID(v.getValue(),
//								OperatorPeerJxtaImpl
//										.getInstance().getPrioMode());
//					} else {
//						return;
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				try {
					viewPlan = (ISource<?>) OperatorPeerJxtaImpl.getInstance().getTrafo().transform(v.getValue(), new TransformationConfiguration("relational", "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"));
				} catch (TransformationException e) {
					e.printStackTrace();
				}
				WrapperPlanFactory.putAccessPlan(v.getKey(), viewPlan);
			}
		}

		Iterator<String> it = OperatorPeerJxtaImpl.getInstance()
				.getSourcesFromWrapperPlanFactory().keySet().iterator();

		while (it.hasNext()) {
			String s = it.next();
			if (!OperatorPeerJxtaImpl.getInstance().getSources().contains(s)) {
				continue;
			}

			SDFSource source = DataDictionary.getInstance().getSource(s);
			AccessAO ao = new AccessAO(source);
			SDFEntity entity = DataDictionary.getInstance().getEntity(s);
			ao.setOutputSchema(entity.getAttributes());

			PipeAdvertisement pipeAdv = createSocketAdvertisement();
			P2PPipeAO p2ppipe = new P2PPipeAO(pipeAdv.toString());
			p2ppipe.setNoOfInputs(1);
			p2ppipe.setInputAO(0, ao);
			IIterableSource<?> temp = null;
			try {
				temp = (IIterableSource<?>) getoPeer().getTrafo().transform(p2ppipe, new TransformationConfiguration("relational", "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"));
			} catch (TransformationException e1) {
				e1.printStackTrace();
			}
//			try {
//				if (OperatorPeerJxtaImpl.getTrafoMode().equals("TI")) {
//					temp = ((RelationalPipesTransform) OperatorPeerJxtaImpl
//							.getInstance().getTrafo()).transformTI(p2ppipe,
//							OperatorPeerJxtaImpl.getInstance().getPrioMode());
//				} else if (OperatorPeerJxtaImpl.getTrafoMode().equals("PNID")) {
//					temp = ((RelationalPipesTransform) OperatorPeerJxtaImpl
//							.getInstance().getTrafo()).transformPNID(p2ppipe,
//							OperatorPeerJxtaImpl.getInstance().getPrioMode());
//				}
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			Subscription<? extends ISource<?>> temp2 = (Subscription<? extends ISource<?>>) ((ISink) temp)
					.getSubscribedTo().get(0);

			while (true) {
				if (temp2.target instanceof IIterableSource) {
					break;
				}
				if (((ISink) temp2.target).getSubscribedTo().size() == 0) {
					break;
				}
				Subscription<? extends ISource<?>> temp3 = (Subscription<? extends ISource<?>>) ((ISink) temp2.target)
						.getSubscribedTo().get(0);
				if (temp3 == null)
					break;
				else
					temp2 = temp3;
			}

			if (!(temp2.target instanceof IIterableSource)) {
				continue;
			}

			((IP2PInputPO) temp2.target).setP2P(true);
			((IP2PInputPO) temp2.target).setConnectToPipe(false);

			SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory
					.newAdvertisement(SourceAdvertisement
							.getAdvertisementType());

			adv.setSourceName(s);
			adv.setSourceSocket(pipeAdv.toString());
			adv.setPeer(OperatorPeerJxtaImpl.getInstance().getNetPeerGroup()
					.getPeerAdvertisement().toString());
			adv.setSourceId(s);

			advList.add(adv);
			OperatorPeerJxtaImpl.getInstance().getScheduler().getSources().add(temp);
//			OperatorPeerJxtaImpl.getInstance().getScheduler().addPlan(temp,
//					Thread.NORM_PRIORITY);
			try {
				temp.open();
			} catch (OpenFailedException e) {
				temp = null;
				continue;
			}

		}

		OperatorPeerJxtaImpl.getInstance().getScheduler().startScheduling();

		while (true) {

			for (SourceAdvertisement adv : advList) {
				try {
					OperatorPeerJxtaImpl.getInstance().getDiscoveryService()
							.publish(adv, LIFETIME, LIFETIME);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				OperatorPeerJxtaImpl.getInstance().getDiscoveryService()
						.remotePublish(adv, 31000);
			}
			try {
				Thread.sleep(LIFETIME - 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public PipeAdvertisement createSocketAdvertisement() {
		PipeID socketID = null;
		socketID = (PipeID) IDFactory.newPipeID(OperatorPeerJxtaImpl
				.getInstance().getNetPeerGroup().getPeerGroupID());
		PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
				.newAdvertisement(PipeAdvertisement.getAdvertisementType());
		advertisement.setPipeID(socketID);
		advertisement.setType(PipeService.UnicastType);
		advertisement.setName("Socket tutorial");

		return advertisement;
	}

	public void setoPeer(AbstractOperatorPeer oPeer) {
		this.oPeer = oPeer;
	}

	public AbstractOperatorPeer getoPeer() {
		return oPeer;
	}

}
