package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.NestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper.NestTISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper.PartialNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.SimpleNestingFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * This test covers all overlapping possibilities in 
 * updating the sweep area. Fixture is internal, 
 * NestPOTestFixtureFactory only needed to fake parameters for nestPO.
 * The same two partials are used, but with different 
 * time intervals.
 * 
 * @author Jendrik Poloczek
 * 
 */
public class Update extends TestCase {

	private NestPO<TimeInterval> nestPO;
	private NestTISweepArea sa;
	private ObjectRelationalTuple<TimeInterval> first;
	private ObjectRelationalTuple<TimeInterval> second;
	
	private Method getGroupingValues;
	private Method update;

	public Update() {
	    this.setName(
	       "update"
	    );
	}
	
	@Before
	public void setUp() throws Exception {
		List<ObjectRelationalTuple<TimeInterval>> tuples;
		Factory fixtures;
		
		SDFAttributeList inputSchema;
		SDFAttributeList outputSchema;		
		SDFAttributeList groupingAttributes;
		SDFAttribute nestingAttribute;

		fixtures = new SimpleNestingFixture();
		inputSchema = fixtures.getInputSchema();
		outputSchema = fixtures.getOutputSchema();
		groupingAttributes = fixtures.getGroupingAttributes();
		nestingAttribute = fixtures.getNestingAttribute();
		tuples = fixtures.getInputTuples();
		
		this.nestPO = new NestPO<TimeInterval>(
				inputSchema, 
				outputSchema, 
				nestingAttribute, 
				groupingAttributes
		);
		
		this.getGroupingValues = 
			NestPO.class.getDeclaredMethod(
				"getGroupingValues", 
				ObjectRelationalTuple.class
			);
		
		this.getGroupingValues.setAccessible(true);
		
		this.update = 
			NestPO.class.getDeclaredMethod(
				"update", 
				NestTISweepArea.class,
				ObjectRelationalTuple.class
			);
		
		this.update.setAccessible(true);
		
		this.first = tuples.get(0);
		this.second = tuples.get(1);
		
		Object[] gv = 
			(Object[]) this.getGroupingValues.invoke(this.nestPO, first);
		
		this.sa = new NestTISweepArea(gv);		
	}
	
	/**
	 *
	 * test case one
	 * 
	 * incoming partial nest TI is contained in the partial nest TI
	 * 
	 * a1	a2	a3	a4
	 * 1	2	3	4 	[0,10) partial nest A	
	 * 1	5	6	7	[3,7) incoming partial nest B
	 * 
	 * result should be
	 * 
	 * [1	2	3	4]	[0,3)
	 * [1	2	3	4], [1	5	6	7] [3,7)
	 * [1	2	3	4]	[7,10)
	 *
	 */
	@Test 
	public void testIncomingTIContainedInPartialTI() {
		this.first.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		this.second.setMetadata(
			new TimeInterval(
				new PointInTime(3),
				new PointInTime(7)
			)
		);
		
		try {
			this.update.invoke(this.nestPO, sa, first);
			this.update.invoke(this.nestPO, sa, second);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Iterator<PartialNest<TimeInterval>> iter = sa.iterator();
		
		PartialNest<TimeInterval> first = iter.next();
		
		assertEquals(first.getMetadata().getStart(), new PointInTime(0));
		assertEquals(first.getMetadata().getEnd(), new PointInTime(3));
		
		List<ObjectRelationalTuple<TimeInterval>> firstPartialNest = 
			first.getNest();
		
		assertTrue(firstPartialNest.size() == 1);
		assertEquals(firstPartialNest.get(0), this.first);
		
		PartialNest<TimeInterval> second = iter.next();
		
		assertEquals(second.getMetadata().getStart(), new PointInTime(3));
		assertEquals(second.getMetadata().getEnd(), new PointInTime(7));
		
		List<ObjectRelationalTuple<TimeInterval>> secondPartialNest = 
			second.getNest();
		
		assertTrue(secondPartialNest.size() == 2);
		assertEquals(secondPartialNest.get(0), this.first);
		assertEquals(secondPartialNest.get(1), this.second);
		
		PartialNest<TimeInterval> third = iter.next();	
		
		assertEquals(third.getMetadata().getStart(), new PointInTime(7));
		assertEquals(third.getMetadata().getEnd(), new PointInTime(10));
		
		List<ObjectRelationalTuple<TimeInterval>> thirdPartialNest = 
			third.getNest();
		
		assertTrue(thirdPartialNest.size() == 1);
		assertEquals(thirdPartialNest.get(0), this.first);
	}
	
	/**
	 * 
	 * test case two
	 * 
	 * incoming partial nest TI is overlapping right of partial nest TI
	 * 
	 * a1	a2	a3	a4
	 * 1	2	3	4	[0,10) partial nest A
	 * 1	5	6	7	[5,15) incoming partial nest B
	 * 
	 * result should be
	 * 
	 * 1	2	3	4	[0,5) 
	 * [1	2	3	4], [1	5	6	7] [5,10)
	 * 1	5	6	7	[10, 15)
	 * 
	 */
	@Test 
	public void testIncomingTIOverlappingRightPartialTI() {
		this.first.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		this.second.setMetadata(
			new TimeInterval(
				new PointInTime(5),
				new PointInTime(15)
			)
		);
		
		try {
			this.update.invoke(this.nestPO, sa, first);
			this.update.invoke(this.nestPO, sa, second);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Iterator<PartialNest<TimeInterval>> iter = sa.iterator();
		
		PartialNest<TimeInterval> first = iter.next();
		
		assertEquals(first.getMetadata().getStart(), new PointInTime(0));
		assertEquals(first.getMetadata().getEnd(), new PointInTime(5));
		
		List<ObjectRelationalTuple<TimeInterval>> firstPartialNest = 
			first.getNest();
		
		assertTrue(firstPartialNest.size() == 1);
		assertEquals(firstPartialNest.get(0), this.first);
		
		PartialNest<TimeInterval> second = iter.next();
		
		assertEquals(second.getMetadata().getStart(), new PointInTime(5));
		assertEquals(second.getMetadata().getEnd(), new PointInTime(10));
		
		List<ObjectRelationalTuple<TimeInterval>> secondPartialNest = 
			second.getNest();
		
		assertTrue(secondPartialNest.size() == 2);
		assertEquals(secondPartialNest.get(0), this.first);
		assertEquals(secondPartialNest.get(1), this.second);
		
		PartialNest<TimeInterval> third = iter.next();	
		
		assertEquals(third.getMetadata().getStart(), new PointInTime(10));
		assertEquals(third.getMetadata().getEnd(), new PointInTime(15));
		
		List<ObjectRelationalTuple<TimeInterval>> thirdPartialNest = 
			third.getNest();
		
		assertTrue(thirdPartialNest.size() == 1);
		assertEquals(thirdPartialNest.get(0), this.second);
	}
	
	/**
	 * 
	 * test case three
	 * 
	 * incoming partial nest TI is equal to partial nest TI
	 * 
	 * a1	a2	a3	a4
	 * 1	2	3	4 	[0,10) partial nest A	
	 * 1	5	6	7	[0,10) incoming partial nest B
	 * 
	 * result should be
	 * 
	 * [1	2	3	4],[1	5	6	7] [0,10)
	 * 
	 */
	@Test 
	public void testIncomingTIEqualPartialTI() {
		this.first.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		this.second.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		
		try {
			this.update.invoke(this.nestPO, sa, first);
			this.update.invoke(this.nestPO, sa, second);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Iterator<PartialNest<TimeInterval>> iter = sa.iterator();
		
		PartialNest<TimeInterval> first = iter.next();
		
		assertEquals(first.getMetadata().getStart(), new PointInTime(0));
		assertEquals(first.getMetadata().getEnd(), new PointInTime(10));
		
		List<ObjectRelationalTuple<TimeInterval>> firstPartialNest = 
			first.getNest();
		
		assertTrue(firstPartialNest.size() == 2);
		assertEquals(firstPartialNest.get(0), this.first);
		assertEquals(firstPartialNest.get(1), this.second);
	}
	
	/**
	 * 
	 * test case four
	 * 
	 * incoming partial nest TI is overlapping left of partial nest TI
	 * 
	 * a1	a2	a3	a4
	 * 1	2	3	4 	[3,7) partial nest A	
	 * 1	5	6	7	[0,5) incoming partial nest B
	 * 
	 * result should be
	 * 
	 * 1	5	6	7	[0,3) 
	 * [1	2	3	4], [1	5	6	7] [3,5)
	 * 1	2	3	4	[5,7) 
	 * 
	 */
	@Test 
	public void testIncomingTIOverlappingLeftPartialTI() {
		this.first.setMetadata(
			new TimeInterval(
				new PointInTime(3),
				new PointInTime(7)
			)
		);
		this.second.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(5)
			)
		);
		
		try {
			this.update.invoke(this.nestPO, sa, first);
			this.update.invoke(this.nestPO, sa, second);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Iterator<PartialNest<TimeInterval>> iter = sa.iterator();
		
		PartialNest<TimeInterval> first = iter.next();
		
		assertEquals(first.getMetadata().getStart(), new PointInTime(0));
		assertEquals(first.getMetadata().getEnd(), new PointInTime(3));
		
		List<ObjectRelationalTuple<TimeInterval>> firstPartialNest = 
			first.getNest();
		
		assertTrue(firstPartialNest.size() == 1);
		assertEquals(firstPartialNest.get(0), this.second);
		
		PartialNest<TimeInterval> second = iter.next();
		
		assertEquals(second.getMetadata().getStart(), new PointInTime(3));
		assertEquals(second.getMetadata().getEnd(), new PointInTime(5));
		
		List<ObjectRelationalTuple<TimeInterval>> secondPartialNest = 
			second.getNest();
		
		assertTrue(secondPartialNest.size() == 2);
		assertEquals(secondPartialNest.get(0), this.first);
		assertEquals(secondPartialNest.get(1), this.second);
		
		PartialNest<TimeInterval> third = iter.next();	
		
		assertEquals(third.getMetadata().getStart(), new PointInTime(5));
		assertEquals(third.getMetadata().getEnd(), new PointInTime(7));
		
		List<ObjectRelationalTuple<TimeInterval>> thirdPartialNest = 
			third.getNest();
		
		assertTrue(thirdPartialNest.size() == 1);
		assertEquals(thirdPartialNest.get(0), this.first);
	}
	
	/**
	 * test case five
	 * 
	 * incoming partial nest TI contains whole partial nest TI
	 * 
	 * 1	2	3	4	[3,5)
	 * 1	5	6	7	[0,10)
	 * 
	 * result should be
	 * 
	 * 1	5	6	7	[0,3)
	 * [1	2	3	4], [1	5	6	7] [3,5)
	 * 1	5	6	7	[5, 10)
	 *
	 */
	@Test 
	public void testIncomingTIContainsPartialTI() {
		this.first.setMetadata(
			new TimeInterval(
				new PointInTime(3),
				new PointInTime(5)
			)
		);
		this.second.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		
		try {
			this.update.invoke(this.nestPO, sa, first);
			this.update.invoke(this.nestPO, sa, second);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Iterator<PartialNest<TimeInterval>> iter = sa.iterator();
		
		PartialNest<TimeInterval> first = iter.next();
		
		assertEquals(first.getMetadata().getStart(), new PointInTime(0));
		assertEquals(first.getMetadata().getEnd(), new PointInTime(3));
		
		List<ObjectRelationalTuple<TimeInterval>> firstPartialNest = 
			first.getNest();
		
		assertTrue(firstPartialNest.size() == 1);
		assertEquals(firstPartialNest.get(0), this.second);
		
		PartialNest<TimeInterval> second = iter.next();
		
		assertEquals(second.getMetadata().getStart(), new PointInTime(3));
		assertEquals(second.getMetadata().getEnd(), new PointInTime(5));
		
		List<ObjectRelationalTuple<TimeInterval>> secondPartialNest = 
			second.getNest();
		
		assertTrue(secondPartialNest.size() == 2);
		assertEquals(secondPartialNest.get(0), this.second);
		assertEquals(secondPartialNest.get(1), this.first);
		
		PartialNest<TimeInterval> third = iter.next();	
		
		assertEquals(third.getMetadata().getStart(), new PointInTime(5));
		assertEquals(third.getMetadata().getEnd(), new PointInTime(10));
		
		List<ObjectRelationalTuple<TimeInterval>> thirdPartialNest = 
			third.getNest();
		
		assertTrue(thirdPartialNest.size() == 1);
		assertEquals(thirdPartialNest.get(0), this.second);
	}
	/**
	 * test case six
	 * 
	 * this test case tests two existent partials with one incoming and 
	 * reducing the created incoming partials by union overlapping. 
	 * 
	 * 1	2	3	4	[3,5)
	 * 1	5	6	7	[0,10)
	 * 1	10	6	7	[0,10)
	 *
	 * 
	 * result should be
	 * 
	 * [1	10	6	7],	[1	5	6	7]	[0,3)
	 * [1	2	3	4], [1	5	6	7], [1	10	6	7] [3,5)
	 * [1	10	6	7],	[1	5	6	7]	[5, 10)
	 *
	 */
	@Test 
	public void testStep() {
		this.first.setMetadata(
			new TimeInterval(
				new PointInTime(3),
				new PointInTime(5)
			)
		);
		this.second.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		
		try {
			this.update.invoke(this.nestPO, sa, first);
			this.update.invoke(this.nestPO, sa, second);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		ObjectRelationalTuple<TimeInterval> next; 
		next = this.second.clone();
		next.setAttribute(1, new String("10"));
		
		next.setMetadata(
			new TimeInterval(
					new PointInTime(0),
					new PointInTime(10)
				)
			);
		
		try {
			this.update.invoke(this.nestPO, sa, next);
		} catch(Exception e) {
			e.printStackTrace();
		}	
		
		List<ObjectRelationalTuple<TimeInterval>> firstNest = sa.poll().getNest();
		List<ObjectRelationalTuple<TimeInterval>> secondNest = sa.poll().getNest();
		List<ObjectRelationalTuple<TimeInterval>> thirdNest = sa.poll().getNest();
		
		assertEquals(firstNest.size(), 2);
		assertEquals(firstNest.get(0), next);
		assertEquals(firstNest.get(1), this.second);
		
		assertEquals(secondNest.size(), 3);
		assertEquals(secondNest.get(0), this.second);
		assertEquals(secondNest.get(1), this.first);
		assertEquals(secondNest.get(2), next);
			
		assertEquals(thirdNest.size(), 2);
		assertEquals(thirdNest.get(0), this.second);
		assertEquals(thirdNest.get(1), next);		
	}
}
