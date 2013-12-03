package de.uniol.inf.is.odysseus.peer.distribute.partition.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartitioner;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartitionException;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.adv.CostQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.adv.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.Mailbox;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.PQLGeneratorService;

public class SurveyBasedPartitioner implements IQueryPartitioner {

	private static final long SURVEY_WAIT_TIME = 4000;

	private final Map<ID, Mailbox<CostResponseAdvertisement>> mailboxForPlanCosts = Maps.newHashMap();
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Collection<ILogicalQueryPart> partition(Collection<ILogicalOperator> operators, QueryBuildConfiguration config) throws QueryPartitionException {

//		List<CostResponseAdvertisement> advertisements = requestCostsForPlan(queryPlan, sharedQueryId, transCfg.getName());
//		Map<String, CostSummary> costsProOperator = calcAvgCostsProOperator(queryPlan, advertisements, transCfg.getName());
//
//		final List<Vote> votes = summariseVotes(advertisements);
//
//		List<SubPlan> subPlans = this.seperateLocalSubPlansLogical(queryPlan);
//		manipulator.insertDummyAOs(subPlans);
//		SubPlan planToDistribute = subPlans.get(0); // 1+ -> localPlans
//
//		List<SubPlan> result = _partition(planToDistribute, costsProOperator, new TargetSize() {
//			@Override
//			public double getNextSize(double totalAbsoluteCosts) {
//				if (votes.iterator().hasNext()) {
//					return totalAbsoluteCosts * votes.iterator().next().getPercentageOfBearableCosts();
//				}
//
//				return 0;
//			}
//		});
//
//		if (subPlans.size() > 1) {
//			result.addAll(subPlans.subList(1, subPlans.size()));
//		}

		return null;
	}

	private List<CostResponseAdvertisement> requestCostsForPlan(ILogicalOperator plan, ID sharedQueryId, String transCfgName) {
		final CostQueryAdvertisement adv = (CostQueryAdvertisement) AdvertisementFactory.newAdvertisement(CostQueryAdvertisement.getAdvertisementType());

		adv.setPqlStatement(PQLGeneratorService.get().generatePQLStatement(plan));
		adv.setSharedQueryID(sharedQueryId);
		adv.setTransCfgName(transCfgName);
		Future<List<CostResponseAdvertisement>> futureResult = askPeersForPlanCosts(adv);

		try {
			return futureResult.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Future<List<CostResponseAdvertisement>> askPeersForPlanCosts(final CostQueryAdvertisement adv) {
		synchronized (mailboxForPlanCosts) {
			Mailbox<CostResponseAdvertisement> mailbox = mailboxForPlanCosts.get(adv.getSharedQueryID());
			if (mailbox == null) {
				mailbox = new Mailbox<>(adv.getSharedQueryID());
				mailboxForPlanCosts.put(adv.getSharedQueryID(), mailbox);
			}
		}

		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setOwnerPeerId(P2PNetworkManagerService.get().getLocalPeerID());
		for (PeerID id : P2PDictionaryService.get().getRemotePeerIDs()) {
			if (!id.equals(P2PNetworkManagerService.get().getLocalPeerID())) {
				JxtaServicesProviderService.get().getDiscoveryService().remotePublish(id.toString(), adv, SURVEY_WAIT_TIME);
			}
		}
		return executorService.submit(new Callable<List<CostResponseAdvertisement>>() {
			@Override
			public List<CostResponseAdvertisement> call() throws Exception {
				Thread.sleep(SURVEY_WAIT_TIME);
				synchronized (mailboxForPlanCosts) {
					Mailbox<CostResponseAdvertisement> mailbox = mailboxForPlanCosts.remove(adv.getSharedQueryID());
					return mailbox.getMails();
				}
			}
		});
	}
}
