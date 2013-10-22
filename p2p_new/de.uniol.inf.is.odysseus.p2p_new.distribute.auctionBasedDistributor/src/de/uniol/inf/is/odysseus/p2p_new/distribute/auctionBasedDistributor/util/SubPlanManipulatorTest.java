package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.SubPlan;


public class SubPlanManipulatorTest {
	private static final Logger log = Logger.getLogger(SubPlanManipulatorTest.class);
	private SubPlanManipulator manipulator;
	
	@BeforeClass
	public static void setUp() {
		Properties p = new Properties();

		try {
		    p.load(SubPlanManipulatorTest.class.getResourceAsStream("/log4j.properties"));
		    PropertyConfigurator.configure(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void init() {
		manipulator = new SubPlanManipulator();
	}
	
	@Test
	public void testInsertingDummyAOs() {
		List<SubPlan> parts = createQueryParts2();
		int size = parts.size();
		manipulator.insertDummyAOs(parts);
		assertEquals(size, parts.size());
		for(SubPlan part : parts) {
			if(part.getDummySinks().isEmpty() &&
					part.getDummySources().isEmpty()) {
				assertTrue(false);
			}
		}
		assertTrue(true);
		assertEquals(10, countDummyOperators(parts));
	}
	
	@Test
	public void testMergeQueryParts() {
		List<SubPlan> parts = createQueryParts3();
		manipulator.insertDummyAOs(parts);
		parts = manipulator.mergeSubPlans(parts);
		assertEquals(1, parts.size());
		assertEquals(0, countDummyOperators(parts));
	}	
	
	@Test
	public void testInsertJxtaOperators() {
		List<SubPlan> parts = createQueryParts2();
		manipulator.insertDummyAOs(parts);
		manipulator.insertJxtaOperators(parts, "someQueryId", null);
		for(SubPlan part: parts) {
			assertTrue(part.getDummySinks().isEmpty()&&part.getDummySinks().isEmpty());
		}
		assertEquals(5, parts.size());
		assertEquals(10, countJxtaOperators(parts));
	}		
	
	@Test
	public void testMergeAndInsertJxtaOperators() {
		List<SubPlan> parts = createQueryParts2();
		manipulator.insertDummyAOs(parts);
		parts = manipulator.mergeSubPlans(parts);
		manipulator.insertJxtaOperators(parts, "someQueryId", null);
		assertEquals(3, parts.size());
		assertEquals(6, countJxtaOperators(parts));
		assertEquals(0, countDummyOperators(parts));
	}
	
	private int countJxtaOperators(List<SubPlan> parts) {
		int count = 0;
		for(SubPlan part : parts) {
			count+=part.getJxtaSinks().size() + part.getJxtaSources().size();
		}
		return count;
	}
	
	private int countDummyOperators(List<SubPlan> parts) {
		int count = 0;
		for(SubPlan part : parts) {
			count+=part.getDummySinks().size() + part.getDummySources().size();
		}
		return count;
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
	
	private List<SubPlan> createQueryParts3() {
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
		parts.add(new SubPlan("local", stream, window, select1));
		parts.add(new SubPlan("local", select2));
		parts.add(new SubPlan("local", union));
		parts.add(new SubPlan("local", select3));
				
		return parts;
	}		
}
