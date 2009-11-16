package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.handler.ISourceHandler;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.advertisements.SourceAdvertisement;

/**
 * Alle Quellen werden hier fuer die Verwendung im P2P Netzwerk vorbereitet und in regelmaessigen Abstaenden ausgeschrieben.
 * 
 * @author Mart Köhler
 *
 */
public class SourceHandlerJxtaImpl implements ISourceHandler {

	private ArrayList<SourceAdvertisement> advList = new ArrayList<SourceAdvertisement>();

	private int LIFETIME = 30000;

	private OperatorPeerJxtaImpl oPeer = null;

	public SourceHandlerJxtaImpl(OperatorPeerJxtaImpl aPeer) {
		this.setoPeer(aPeer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		// Wollen jede View bzw. Quelle ausschreiben
		for (Entry<String, ILogicalOperator> v : DataDictionary.getInstance()
				.getViews()) {
//			P2PSinkAO p2ppipe = null;
//			PipeAdvertisement pipeAdv = null;
			if(v.getValue() instanceof AccessAO) {
//				pipeAdv = createSocketAdvertisement();
//			    p2ppipe = new P2PSinkAO(pipeAdv.toString());
//				AccessAO ao = new AccessAO();
//				ao.setSource(((AccessAO)v.getValue()).getSource());
//				//TODO: Auf NIO umstellen
//				 ((AccessAO)v.getValue()).getSource().setSourceType("RelationalInputStreamAccessPO");
//				 try {
//
//					OperatorPeerJxtaImpl.getInstance().getExecutor().addQuery(v.getValue(), new ParameterPriority(2));
//					OperatorPeerJxtaImpl.getInstance().getExecutor().startExecution();
//				} catch (PlanManagementException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				ao.subscribe(p2ppipe, 0, 0,((AccessAO)v.getValue()).getOutputSchema());
//				//Überschreibe den alten Wert der Quelle im DD
//				DataDictionary.getInstance().setView(v.getKey(), p2ppipe);
				
				// Erzeuge Advertisement zur Quelle und füge in die advList für das spätere publishen
				SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory
				.newAdvertisement(SourceAdvertisement
						.getAdvertisementType());

				adv.setSourceName(v.getKey());
//				adv.setSourceSocket(pipeAdv.toString());
				adv.setPeer(OperatorPeerJxtaImpl.getInstance().getNetPeerGroup()
						.getPeerAdvertisement().toString());
				adv.setSourceId(v.getKey());
				adv.setSourceScheme(OperatorPeerJxtaImpl.getInstance().getSources().get(v.getKey()));
				advList.add(adv);
				
			}
			
//			if (WrapperPlanFactory.getAccessPlan(v.getKey()) == null) {
//				P2PSinkAO p2ppipe = null;
//				PipeAdvertisement pipeAdv = null;
//				try {
//					pipeAdv = createSocketAdvertisement();
//				    p2ppipe = new P2PSinkAO(pipeAdv.toString());
//					
//					AccessAO ao = new AccessAO();
//					ao.setSource(((AccessAO)v.getValue()).getSource());
//					ao.getSource().setSourceType("RelationalInputStreamAccessPO");
//					ao.subscribe(p2ppipe, 0, 0,((AccessAO)v.getValue()).getOutputSchema());
//					OperatorPeerJxtaImpl.getInstance().getExecutor().addQuery(p2ppipe, new ParameterPriority(2));
////					OperatorPeerJxtaImpl.getInstance().getExecutor().startExecution();
//					System.out.println("Setze Advertisement");
//					SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory
//					.newAdvertisement(SourceAdvertisement
//							.getAdvertisementType());
//
//					adv.setSourceName(v.getKey());
//					adv.setSourceSocket(pipeAdv.toString());
//					adv.setPeer(OperatorPeerJxtaImpl.getInstance().getNetPeerGroup()
//							.getPeerAdvertisement().toString());
//					adv.setSourceId(v.getKey());
//					adv.setSourceScheme(OperatorPeerJxtaImpl.getInstance().getSources().get(v.getKey()));
//					advList.add(adv);
//				} catch (PlanManagementException e) {
//					e.printStackTrace();
//				}
//			WrapperPlanFactory.putAccessPlan(v.getKey(), source);
//			
//			
//			}
		}

		
//		Iterator<String> it = OperatorPeerJxtaImpl.getInstance()
//				.getSourcesFromWrapperPlanFactory().keySet().iterator();
//
//		while (it.hasNext()) {
//			String s = it.next();
//			if (!OperatorPeerJxtaImpl.getInstance().getSources().containsKey(s)) {
//				continue;
//			}
//
//			SDFSource source = DataDictionary.getInstance().getSource(s);
//			
//			AccessAO ao = new AccessAO(source);
//			SDFEntity entity = DataDictionary.getInstance().getEntity(s);
//			ao.setOutputSchema(entity.getAttributes());

//			PipeAdvertisement pipeAdv = createSocketAdvertisement();
//			P2PSinkAO p2ppipe = new P2PSinkAO(pipeAdv.toString());
////			p2ppipe.setPhysSubscriptionTo(viewPlan, 0, 0);
////			p2ppipe.subscribeTo(ao, 0, 0);
////			AbstractPipe p2pipePO = null;
//			AbstractPipe phys = null;
			
//			try {
//				System.out.println("einmal initialisieren");
//				phys = (AbstractPipe) OperatorPeerJxtaImpl.getInstance().getTrafo().transform(p2ppipe, new TransformationConfiguration("relational", "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"));
//				System.out.println("ist viewplan null?"+(viewPlan==null));
//				phys.subscribeTo(viewPlan, 0, 0);
//				try {
//					OperatorPeerJxtaImpl.getInstance().getExecutor().addQuery(viewPlan, new ParameterPriority(2));
//				} catch (PlanManagementException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			} catch (TransformationException e1) {
//				e1.printStackTrace();
//			}
			
//			WrapperPlanFactory.getAccessPlan(s).subscribe( p2pipePO, 0, 0);
			
//			System.out.println("Analyse nach dem übersetzen");
//			System.out.println("Subscriptions empty?"+WrapperPlanFactory.getAccessPlan(s).getSubscriptions().isEmpty()+ " "+WrapperPlanFactory.getAccessPlan(s).getSubscriptions().toString());
			
//			try {
//				getoPeer().getExecutor().stopExecution();
//				System.out.println("nochmal initialisieren");
//				getoPeer().getExecutor().addQuery(p2ppipe, new ParameterPriority(2));
//				getoPeer().getExecutor().startExecution();
//			} catch (PlanManagementException e) {
//				e.printStackTrace();
//			}
//			IIterableSource<?> temp = null;
//			AbstractSource physOp = null;
//			try {
//				temp = (IIterableSource<?>) getoPeer().getTrafo().transform(p2ppipe, new TransformationConfiguration("relational", "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"));
//				physOp = (AbstractSource) getoPeer().getTrafo().transform(p2ppipe, new TransformationConfiguration("relational", "de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval"));
				
//			} catch (TransformationException e1) {
//				e1.printStackTrace();
//			}
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


			
//			PhysicalSubscription<? extends ISource<?>> temp2 = (PhysicalSubscription<? extends ISource<?>>) ((ISink) temp)
//					.getSubscribedTo(0);
//
//			while (true) {
//				if (temp2.getTarget() instanceof IIterableSource) {
//					break;
//				}
//				if (((ISink) temp2.getTarget()).getSubscribedTo().size() == 0) {
//					break;
//				}
//				PhysicalSubscription<? extends ISource<?>> temp3 = (PhysicalSubscription<? extends ISource<?>>) ((ISink) temp2.getTarget())
//						.getSubscribedTo(0);
//				if (temp3 == null)
//					break;
//				else
//					temp2 = temp3;
//			}
//
//			if (!(temp2.getTarget() instanceof IIterableSource)) {
//				continue;
//			}
//
//			((IP2PInputPO) temp2.getTarget()).setP2P(true);
//			((IP2PInputPO) temp2.getTarget()).setConnectedToPipe(false);
//
//			

//		OperatorPeerJxtaImpl.getInstance().getScheduler().startScheduling();


//		SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory
//				.newAdvertisement(SourceAdvertisement
//						.getAdvertisementType());
//
//		adv.setSourceName(s);
//		adv.setSourceSocket(pipeAdv.toString());
//		adv.setPeer(OperatorPeerJxtaImpl.getInstance().getNetPeerGroup()
//				.getPeerAdvertisement().toString());
//		adv.setSourceId(s);
//		adv.setSourceScheme(OperatorPeerJxtaImpl.getInstance().getSources().get(s));
//		advList.add(adv);

//		try {
//			
//
//			OperatorPeerJxtaImpl.getInstance().getExecutor().addQuery(ao, null);
//			OperatorPeerJxtaImpl.getInstance().getExecutor().startExecution();
//		} catch (PlanManagementException e1) {
//			e1.printStackTrace();
//		}
//		OperatorPeerJxtaImpl.getInstance().getScheduler().addPlan(physOp,
//				Thread.NORM_PRIORITY);
//		try {
//			physOp.open();
//		} catch (OpenFailedException e) {
//			physOp = null;
//			continue;
//		}
//	}
	
//	try {
//		System.out.println("Starte Executor");
//		OperatorPeerJxtaImpl.getInstance().getExecutor().addQuery(viewPlan, new ParameterPriority(2));
//		OperatorPeerJxtaImpl.getInstance().getExecutor().startExecution();
//	} catch (PlanManagementException e1) {
//		e1.printStackTrace();
//	}
//	OperatorPeerJxtaImpl.getInstance().getScheduler().startScheduling();
	
		
		
		
		
		//Publishe alle Quellen
		while (true) {
			for (SourceAdvertisement adv : advList) {
				try {
					System.out.println("Publishe Quelle "+adv.getSourceName());
					OperatorPeerJxtaImpl.getInstance().getDiscoveryService()
							.publish(adv, LIFETIME, LIFETIME);
				} catch (IOException e) {
					e.printStackTrace();
				}
				OperatorPeerJxtaImpl.getInstance().getDiscoveryService()
						.remotePublish(adv, 31000);
			}
			try {
				Thread.sleep(LIFETIME - 1);
			} catch (InterruptedException e) {
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
		advertisement.setName("Source Distribution Socket");

		return advertisement;
	}

	public void setoPeer(OperatorPeerJxtaImpl oPeer) {
		this.oPeer = oPeer;
	}

	public OperatorPeerJxtaImpl getoPeer() {
		return oPeer;
	}
	


}
