package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;

public class SubPlanTest {
	@Test
	public void testSubPlan() {
		List<SubPlan> subPlans = createQueryParts2();
		assertEquals(1, subPlans.get(0).getSinks().size());
		assertEquals(1, subPlans.get(0).getSources().size());
		assertTrue(subPlans.get(0).getSinks().get(0)!=subPlans.get(0).getSources().get(0));
	}
	
	private List<SubPlan> createQueryParts2() {
		ILogicalOperator stream = new StreamAO();
		ILogicalOperator window = new WindowAO();
		ILogicalOperator select1 = new SelectAO();
		ILogicalOperator select2 = new SelectAO();
		ILogicalOperator union = new UnionAO();
		ILogicalOperator select3 = new SelectAO();
		
		stream.subscribeSink(window, 0, 0, null);
		window.subscribeSink(select1, 0, 0, null);
		window.subscribeSink(select2, 0, 1, null);
		select1.subscribeSink(union, 0, 0, null);
		select2.subscribeSink(union, 1, 0, null);
		union.subscribeSink(select3, 0, 0, null);
		
		List<SubPlan> parts = Lists.newArrayList();
		parts.add(new SubPlan("local", stream, window));
		parts.add(new SubPlan("remotePeer2", select1));
		parts.add(new SubPlan("remotePeer2", select2));
		parts.add(new SubPlan("remotePeer2", union));
		parts.add(new SubPlan("local", select3));
				
		return parts;
	}	
}
