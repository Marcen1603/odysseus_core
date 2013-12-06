package de.uniol.inf.is.odysseus.peer.distribute.partition.survey.partitioner.internal;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.internal.advertisement.CostQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.internal.advertisement.CostResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.partitioner.Partitioner;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.Communicator;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.DistributionHelper;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.distribute.partition.survey.util.calculator.CostCalculator;

public abstract class AbstractPartitioner implements Partitioner {
	
	protected CostCalculator costCalculator = new CostCalculator();

	public static interface TargetSize {
		public double getNextSize(double totalAbsoluteCosts);
	}

	public List<CostResponseAdvertisement> requestCostsForPlan(ILogicalOperator plan, ID sharedQueryId, String transCfgName) {
		final CostQueryAdvertisement adv = (CostQueryAdvertisement) AdvertisementFactory.newAdvertisement(CostQueryAdvertisement.getAdvertisementType());
		adv.setPqlStatement(PQLGeneratorService.get().generatePQLStatement(plan));
		adv.setSharedQueryID(sharedQueryId);
		adv.setTransCfgName(transCfgName);
		Future<List<CostResponseAdvertisement>> futureResult = Communicator.getInstance().askPeersForPlanCosts(adv);

		try {
			return futureResult.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<SubPlan> _partition(SubPlan planToDistribute, final Map<String, CostSummary> planCost, final TargetSize targetSize) {
		final ILogicalOperator startOperator = getSomeLeafOperator(planToDistribute);
		final List<SubPlan> subPlans = Lists.newArrayList();
		final List<ILogicalOperator> visitedOperators = Lists.newArrayList();

		final double targetAbsoluteCosts[] = new double[1];
		final double currentAbsoluteSubPlanCosts[] = new double[1];
		final double currentAbsoluteTotalCosts[] = new double[1];
		final double totalAbsoluteCosts = CostSummary.calcSum(planCost.values()).getCpuCost();
		final List<ILogicalOperator> currentSubPlanOperators = Lists.newArrayList();

		currentAbsoluteSubPlanCosts[0] = 0;
		targetAbsoluteCosts[0] = targetSize.getNextSize(totalAbsoluteCosts);

		Helper.iterateThroughPlan(startOperator, visitedOperators, new Function<ILogicalOperator, Void>() {
			@Override
			public Void apply(ILogicalOperator operator) {
				String id = Helper.getId(operator);
				double operatorAbsoluteCosts = (id != null) ? operatorAbsoluteCosts = planCost.get(id).getCpuCost() : 0;
				double newSubPlanCost = currentAbsoluteSubPlanCosts[0] + operatorAbsoluteCosts;

				currentAbsoluteTotalCosts[0] += operatorAbsoluteCosts;
				if (currentAbsoluteTotalCosts[0] < totalAbsoluteCosts) {
					if (newSubPlanCost <= targetAbsoluteCosts[0]) {
						currentSubPlanOperators.add(operator);
						currentAbsoluteSubPlanCosts[0] += operatorAbsoluteCosts;
					} else {
						subPlans.add(new SubPlan(currentSubPlanOperators.toArray(new ILogicalOperator[0])));
						// sollte kein fehler auftreten, da vorher schon
						// überprüft wurde,
						// ob der plan in seiner gänze zerlegt werden
						// kann
						targetAbsoluteCosts[0] = targetSize.getNextSize(totalAbsoluteCosts);
						currentAbsoluteSubPlanCosts[0] = operatorAbsoluteCosts;
						currentSubPlanOperators.clear();
						currentSubPlanOperators.add(operator);

						if (currentAbsoluteTotalCosts[0] + operatorAbsoluteCosts >= totalAbsoluteCosts) {
							subPlans.add(new SubPlan(currentSubPlanOperators.toArray(new ILogicalOperator[0])));
						}
					}
				} else {
					currentSubPlanOperators.add(operator);
					subPlans.add(new SubPlan(currentSubPlanOperators.toArray(new ILogicalOperator[0])));
					targetAbsoluteCosts[0] = targetSize.getNextSize(totalAbsoluteCosts);
					currentAbsoluteSubPlanCosts[0] = 0;
					currentSubPlanOperators.clear();
				}
				return null;
			}
		});
		// correct
		if (subPlans.isEmpty()) {
			subPlans.add(new SubPlan(currentSubPlanOperators.toArray(new ILogicalOperator[0])));
		}
		return subPlans;
	}

	protected List<SubPlan> seperateLocalSubPlansLogical(ILogicalOperator plan) {
		List<SubPlan> newSubPlans = Lists.newArrayList();

		SubPlan totalPlan = new SubPlan(DistributionHelper.collectOperators(plan).toArray(new ILogicalOperator[0]));

		// List<ILogicalOperator> localOperators =
		// totalPlan.findOperatorsByType(AccessAO.class, StreamAO.class,
		// TopAO.class);
		List<ILogicalOperator> localOperators = totalPlan.findOperatorsByType(AccessAO.class, TopAO.class);
		List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		if (!localOperators.isEmpty()) {
			// applicationtime und topao...

			for (ILogicalOperator localOperator : Lists.newArrayList(localOperators)) {
				if (localOperator instanceof TopAO) {
					for (LogicalSubscription sub : localOperator.getSubscribedToSource()) {
						ILogicalOperator src = sub.getTarget();
						if (!localOperators.contains(src)) {
							localOperators.add(src);
						}
					}
					localOperator.unsubscribeFromAllSources();
					operatorsToRemove.add(localOperator);
				}
			}

			for (ILogicalOperator localOperator : Lists.newArrayList(localOperators)) {
				for (LogicalSubscription sub : localOperator.getSubscriptions()) {
					if (sub.getTarget() instanceof TimestampAO) {
						localOperators.add(sub.getTarget());
						localOperators.addAll(Helper.allSubscriptions(sub.getTarget()));
					}
				}
			}

			totalPlan.removeOperators(localOperators.toArray(new ILogicalOperator[0]));
			totalPlan.removeOperators(operatorsToRemove.toArray(new ILogicalOperator[0]));
			localOperators.removeAll(operatorsToRemove);

			List<SubPlan> localPlans = defineLocalPlans(localOperators);
			newSubPlans.addAll(localPlans);
		}
		newSubPlans.add(totalPlan);
		return Lists.reverse(newSubPlans);
	}

	private List<SubPlan> defineLocalPlans(List<ILogicalOperator> localOperators) {
		List<SubPlan> subPlans = Lists.newArrayList();
		for (ILogicalOperator operator : Lists.newArrayList(localOperators)) {
			subPlans.add(new SubPlan("local", operator));
		}

		return subPlans;
	}

	protected Map<String, CostSummary> calcAvgCostsProOperator(final ILogicalOperator query, final List<CostResponseAdvertisement> advertisements, String transCfgName) {
		advertisements.add(calcCostsProOperator(query, transCfgName));
		Map<String, CostSummary> costs = Maps.newHashMap();
		for (CostResponseAdvertisement adv : advertisements) {
			for (String operatorId : adv.getCostSummary().keySet()) {
				CostSummary newCost = adv.getCostSummary().get(operatorId);
				CostSummary costTotal = costs.get(operatorId);
				if (costTotal == null) {
					costTotal = new CostSummary(operatorId, 0, 0, query);
				}
				costTotal = new CostSummary(operatorId, costTotal.getCpuCost() + newCost.getCpuCost(), costTotal.getMemCost() + newCost.getMemCost(), query);
				costs.put(operatorId, costTotal);
			}
		}

		costs = Maps.transformEntries(costs, new EntryTransformer<String, CostSummary, CostSummary>() {
			@Override
			public CostSummary transformEntry(String key, CostSummary value) {
				return new CostSummary(key, value.getCpuCost() / advertisements.size(), value.getMemCost() / advertisements.size(), query);
			}

		});

		costs = addCostsForIgnoredOperators(query, costs);

		return costs;
	}

	private ILogicalOperator getSomeLeafOperator(SubPlan planToDistribute) {
		try {
			if (!planToDistribute.getSources().isEmpty()) {
				return planToDistribute.getSources().get(0);
			}
			return planToDistribute.getDummySources().get(0);
		} catch (IndexOutOfBoundsException | NoSuchElementException e) {
			throw new RuntimeException("Could not find any source/leaf operator in the query plan");
		}
	}

	private CostResponseAdvertisement calcCostsProOperator(ILogicalOperator plan, String transCfgName) {

		Map<String, CostSummary> costsProOperator = costCalculator.calcCostsProOperator(Helper.wrapInLogicalQuery(plan, null, null), transCfgName, false);

		CostSummary sum = CostSummary.calcSum(costsProOperator.values());
		CostSummary relativeCosts = costCalculator.calcBearableCostsInPercentage(sum);
		double bid = costCalculator.calcBid(plan, sum);

		CostResponseAdvertisement costAdv = (CostResponseAdvertisement) AdvertisementFactory.newAdvertisement(CostResponseAdvertisement.getAdvertisementType());
		costAdv.setCostSummary(costsProOperator);
		costAdv.setOwnerPeerId(P2PNetworkManagerService.get().getLocalPeerID());
		costAdv.setPqlStatement(null);
		costAdv.setTransCfgName(null);
		costAdv.setSharedQueryID(null);
		costAdv.setID(null);
		costAdv.setPercentageOfBearableCpuCosts(relativeCosts.getCpuCost());
		costAdv.setPercentageOfBearableMemCosts(relativeCosts.getMemCost());
		costAdv.setBid(bid);

		return costAdv;
	}

	private Map<String, CostSummary> addCostsForIgnoredOperators(ILogicalOperator query, Map<String, CostSummary> costs) {
		costs = Maps.newHashMap(costs);
		for (ILogicalOperator op : DistributionHelper.collectOperators(query)) {
			String opId = Helper.getId(op);
			CostSummary cost = costs.get(opId);
			if (cost == null) {
				costs.put(opId, new CostSummary(opId, 0, 0, query));
			}
		}
		return costs;
	}
}
