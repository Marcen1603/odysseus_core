package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.partitioner.internal;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;

public class SurveyBasedPartitionerTest {
	@Test
	public void testPartitioning() {
		// scheiß auf die abhängigkeiten, die zu testende methode hat keine
		SurveyBasedPartitioner partitioner = new SurveyBasedPartitioner();
		
		SubPlan plan = createPlan();
		Map<String, CostSummary> costs = calcCosts(plan.getLogicalPlan());
		List<Vote> votes = createVotes();
		List<SubPlan> subplans = partitioner._partition(plan, costs, votes);
		
		assertEquals(2, subplans.size());
		assertEquals(2, subplans.get(0).getOperators().size());
		assertEquals(4, subplans.get(1).getOperators().size());
	}
	
	private List<Vote> createVotes() {
		List<Vote> votes = Lists.newArrayList();
		votes.add(new Vote(1.6, 0.6, 2));
		votes.add(new Vote(0.4, 0.5, 1));
		return votes;
	}

	private SubPlan createPlan() {
		ILogicalOperator stream = new StreamAO();
		stream.getParameterInfos().put("ID", "'0'");
		
		ILogicalOperator window = new WindowAO();		
		window.getParameterInfos().put("ID", "'1'");
		
		ILogicalOperator select1 = new SelectAO();
		select1.getParameterInfos().put("ID", "'2'");
		
		ILogicalOperator select2 = new SelectAO();
		select2.getParameterInfos().put("ID", "'3'");
		
		ILogicalOperator union = new UnionAO();
		union.getParameterInfos().put("ID", "'4'");
		
		ILogicalOperator select3 = new SelectAO();
		select3.getParameterInfos().put("ID", "'6'");
		
		stream.subscribeSink(window, 0, 0, null);
		window.subscribeSink(select1, 0, 0, null);
		window.subscribeSink(select2, 0, 1, null);
		select1.subscribeSink(union, 0, 0, null);
		select2.subscribeSink(union, 1, 0, null);
		union.subscribeSink(select3, 0, 0, null);	
				
		return new SubPlan(null, stream, window, select1, select2, union);
	}
	
	private Map<String, CostSummary> calcCosts(ILogicalOperator plan) {
		Map<String, CostSummary> costs=Maps.newHashMap();
		double cpuCosts=0;		
		for(ILogicalOperator operator : DistributionHelper.collectOperators(plan)) {
			String id = operator.getParameterInfos().get("ID");
			cpuCosts += 0.001*100;
			costs.put(id, new CostSummary(id, cpuCosts/100, 0, plan));
		}
		return costs;
	}	
}
