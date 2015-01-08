package de.uniol.inf.is.odysseus.trajectory.physical;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class SubtrajectoryConstructStrategyTest {

	private SubtrajectoryConstructStrategy instance;
	
	private GeometryFactory gf = new GeometryFactory();
	
	@Before
	public void setUp() {
		this.instance = new SubtrajectoryConstructStrategy();
	}
	
	@After
	public void tearDown() {
		this.instance = null;
	}
	
	@Test
	public void testGetResultsToTransfer_60_20() {
		/* create Tuples for tumpling TimeWindows (Size=60s) (range=60s) */
		/*
		schema=[['Timestamp', 'Long'],
				['VehicleType', 'String'],
				['VehicleId', 'String'],
				['Position', 'SpatialGeometry'],
				['State', 'Integer']
		*/
		
		/* Start New Window */
		final Tuple<ITimeInterval> t_1_1 = this.create("1", 0, 0, 60); // new trajectory 1
		final Tuple<ITimeInterval> t_2_1 = this.create("2", 0, 3, 60); // new trajectory 2 (Case 3)
		final Tuple<ITimeInterval> t_1_2 = this.create("1", 1, 5, 60);
		final Tuple<ITimeInterval> t_2_2 = this.create("2", 1, 6, 60);
		final Tuple<ITimeInterval> t_1_3 = this.create("1", 2, 7, 60);
		final Tuple<ITimeInterval> t_2_3 = this.create("2", 2, 10, 60);
		final Tuple<ITimeInterval> t_1_4 = this.create("1", 3, 15, 60);	
		/* Start New Window */
		final Tuple<ITimeInterval> t_2_4 = this.create("2", -1, 20, 80); // trajectory 2 finished
		final Tuple<ITimeInterval> t_1_5 = this.create("1", 4, 25, 80);
		final Tuple<ITimeInterval> t_1_6 = this.create("1", 5, 30, 80);
		/* Start New Window */
		final Tuple<ITimeInterval> t_1_7 = this.create("1", 6, 40, 100);
		final Tuple<ITimeInterval> t_1_8 = this.create("1", 7, 50, 100);		
		final Tuple<ITimeInterval> t_1_9 = this.create("1", 8, 59, 100);
		/* Start New Window */
		final Tuple<ITimeInterval> t_1_10 = this.create("1", 9, 60, 120);
		final Tuple<ITimeInterval> t_1_11 = this.create("1", 10, 65, 120);
		final Tuple<ITimeInterval> t_3_1 = this.create("3", 0, 65, 120); // new trajectory 3
		final Tuple<ITimeInterval> t_1_12 = this.create("1", -1, 71, 120); // trajectory 1 finished
		final Tuple<ITimeInterval> t_3_2 = this.create("3", 1, 72, 120);
		final Tuple<ITimeInterval> t_3_3 = this.create("3", 2, 75, 120);
		final Tuple<ITimeInterval> t_4_1 = this.create("4", 0, 77, 120); // new trajectory 4
		/* Start New Window */
		final Tuple<ITimeInterval> t_3_4 = this.create("3", -1, 80, 140); // trajectory 3 finished
		final Tuple<ITimeInterval> t_4_2 = this.create("4", 1, 85, 140);
		final Tuple<ITimeInterval> t_3_5 = this.create("3", 0, 90, 140); // new trajectory 3
		final Tuple<ITimeInterval> t_3_6 = this.create("3", 1, 94, 140);
		/* Start New Window */
		final Tuple<ITimeInterval> t_3_7 = this.create("3", -1, 100, 160); // trajectory 3 finished
		final Tuple<ITimeInterval> t_4_3 = this.create("4", 2, 104, 160);
		/* Start New Window */
		final Tuple<ITimeInterval> t_4_4 = this.create("4", 3, 120, 180);
		
		assertThat(this.instance.getResultsToTransfer(t_1_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_4), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_4), 
				is(Arrays.asList(
						construct(t_2_1, t_2_2, t_2_3, t_2_4)
				)));
		assertThat(this.instance.getResultsToTransfer(t_1_5), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_6), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_7), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_8), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_9), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_10), 
				is(Arrays.asList(
						construct(t_1_1, t_1_2, t_1_3, t_1_4)
				)));
		assertThat(this.instance.getResultsToTransfer(t_1_11), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_12), 
				is(Arrays.asList(
						construct(t_1_5, t_1_6, t_1_7, t_1_8, t_1_9, t_1_10, t_1_11, t_1_12)
				)));
		assertThat(this.instance.getResultsToTransfer(t_3_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_4_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_4), 
				is(Arrays.asList(
						construct(t_3_1, t_3_2, t_3_3, t_3_4)
				)));
		assertThat(this.instance.getResultsToTransfer(t_4_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_5), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_6), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_7), 
				is(Arrays.asList(
						construct(t_3_5, t_3_6, t_3_7)
				)));
		assertThat(this.instance.getResultsToTransfer(t_4_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_4_4), 
				is(Arrays.asList(
						construct(t_4_1)
				)));
	}
	
	@Test
	public void testGetResultsToTransfer_60_60() {
		/* create Tuples for tumpling TimeWindows (Size=60s) (range=60s) */
		/*
		schema=[['Timestamp', 'Long'],
				['VehicleType', 'String'],
				['VehicleId', 'String'],
				['Position', 'SpatialGeometry'],
				['State', 'Integer']
		*/
		
		/* Start New Window */
		final Tuple<ITimeInterval> t_1_1 = this.create("1", 0, 0, 60); // new trajectory 1
		final Tuple<ITimeInterval> t_2_1 = this.create("2", 0, 3, 60); // new trajectory 2 (Case 3)
		final Tuple<ITimeInterval> t_1_2 = this.create("1", 1, 5, 60);
		final Tuple<ITimeInterval> t_2_2 = this.create("2", 1, 6, 60);
		final Tuple<ITimeInterval> t_1_3 = this.create("1", 2, 7, 60);
		final Tuple<ITimeInterval> t_2_3 = this.create("2", 2, 10, 60);
		final Tuple<ITimeInterval> t_1_4 = this.create("1", 3, 15, 60);		
		final Tuple<ITimeInterval> t_2_4 = this.create("2", -1, 20, 60); // trajectory 2 finished
		final Tuple<ITimeInterval> t_1_5 = this.create("1", 4, 25, 60);
		final Tuple<ITimeInterval> t_1_6 = this.create("1", 5, 30, 60);
		final Tuple<ITimeInterval> t_1_7 = this.create("1", 6, 40, 60);
		final Tuple<ITimeInterval> t_1_8 = this.create("1", 7, 50, 60);		
		final Tuple<ITimeInterval> t_1_9 = this.create("1", 8, 59, 60);
		/* Start New Window */
		final Tuple<ITimeInterval> t_1_10 = this.create("1", 9, 60, 120);
		final Tuple<ITimeInterval> t_1_11 = this.create("1", 10, 65, 120);
		final Tuple<ITimeInterval> t_3_1 = this.create("3", 0, 65, 120); // new trajectory 3
		final Tuple<ITimeInterval> t_1_12 = this.create("1", -1, 71, 120); // trajectory 1 finished
		final Tuple<ITimeInterval> t_3_2 = this.create("3", 1, 72, 120);
		final Tuple<ITimeInterval> t_3_3 = this.create("3", 2, 75, 120);
		final Tuple<ITimeInterval> t_4_1 = this.create("4", 0, 77, 120); // new trajectory 4
		final Tuple<ITimeInterval> t_3_4 = this.create("3", -1, 80, 120); // trajectory 3 finished
		final Tuple<ITimeInterval> t_4_2 = this.create("4", 1, 85, 120);
		final Tuple<ITimeInterval> t_3_5 = this.create("3", 0, 90, 120); // new trajectory 3
		final Tuple<ITimeInterval> t_3_6 = this.create("3", 1, 94, 120);
		final Tuple<ITimeInterval> t_3_7 = this.create("3", -1, 100, 120); // trajectory 3 finished
		final Tuple<ITimeInterval> t_4_3 = this.create("4", 2, 104, 120);
		/* Start New Window */
		final Tuple<ITimeInterval> t_4_4 = this.create("4", 3, 120, 180);
		
		assertThat(this.instance.getResultsToTransfer(t_1_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_4), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_4), 
				is(Arrays.asList(
						construct(t_2_1, t_2_2, t_2_3, t_2_4)
				)));
		assertThat(this.instance.getResultsToTransfer(t_1_5), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_6), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_7), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_8), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_9), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_10), 
				is(Arrays.asList(
						construct(t_1_1, t_1_2, t_1_3, t_1_4, t_1_5, t_1_6, t_1_7, t_1_8, t_1_9)
				)));
		assertThat(this.instance.getResultsToTransfer(t_1_11), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_12), 
				is(Arrays.asList(
						construct(t_1_10, t_1_11, t_1_12)
				)));
		assertThat(this.instance.getResultsToTransfer(t_3_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_4_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_4), 
				is(Arrays.asList(
						construct(t_3_1, t_3_2, t_3_3, t_3_4)
				)));
		assertThat(this.instance.getResultsToTransfer(t_4_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_5), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_6), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_7), 
				is(Arrays.asList(
						construct(t_3_5, t_3_6, t_3_7)
				)));
		assertThat(this.instance.getResultsToTransfer(t_4_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_4_4), 
				is(Arrays.asList(
						construct(t_4_1, t_4_2, t_4_3)
				)));
	}
		
	@Test
	public void testGetResultsToTransfer_Inf() {
		/* create Tuples for tumpling TimeWindows (Size=60s) (range=60s) */
		/*
		schema=[['Timestamp', 'Long'],
				['VehicleType', 'String'],
				['VehicleId', 'String'],
				['Position', 'SpatialGeometry'],
				['State', 'Integer']
		*/
		
		final Tuple<ITimeInterval> t_1_1 = this.create("1", 0, 0, Integer.MAX_VALUE); // new trajectory 1
		final Tuple<ITimeInterval> t_2_1 = this.create("2", 0, 3, Integer.MAX_VALUE); // new trajectory 2 (Case 3)
		final Tuple<ITimeInterval> t_1_2 = this.create("1", 1, 5, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_2_2 = this.create("2", 1, 6, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_1_3 = this.create("1", 2, 7, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_2_3 = this.create("2", 2, 10, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_1_4 = this.create("1", 3, 15, Integer.MAX_VALUE);		
		final Tuple<ITimeInterval> t_2_4 = this.create("2", -1, 20, Integer.MAX_VALUE); // trajectory 2 finished
		final Tuple<ITimeInterval> t_1_5 = this.create("1", 4, 25, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_1_6 = this.create("1", 5, 30, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_1_7 = this.create("1", 6, 40, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_1_8 = this.create("1", 7, 50, Integer.MAX_VALUE);		
		final Tuple<ITimeInterval> t_1_9 = this.create("1", 8, 59, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_1_10 = this.create("1", 9, 60, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_1_11 = this.create("1", 10, 65, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_3_1 = this.create("3", 0, 65, Integer.MAX_VALUE); // new trajectory 3
		final Tuple<ITimeInterval> t_1_12 = this.create("1", -1, 71, Integer.MAX_VALUE); // trajectory 1 finished
		final Tuple<ITimeInterval> t_3_2 = this.create("3", 1, 72, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_3_3 = this.create("3", 2, 75, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_4_1 = this.create("4", 0, 77, Integer.MAX_VALUE); // new trajectory 4
		final Tuple<ITimeInterval> t_3_4 = this.create("3", -1, 80, Integer.MAX_VALUE); // trajectory 3 finished
		final Tuple<ITimeInterval> t_4_2 = this.create("4", 1, 85, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_3_5 = this.create("3", 0, 90, Integer.MAX_VALUE); // new trajectory 3
		final Tuple<ITimeInterval> t_3_6 = this.create("3", 1, 94, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_3_7 = this.create("3", -1, 100, Integer.MAX_VALUE); // trajectory 3 finished
		final Tuple<ITimeInterval> t_4_3 = this.create("4", 2, 104, Integer.MAX_VALUE);
		final Tuple<ITimeInterval> t_4_4 = this.create("4", 3, 120, Integer.MAX_VALUE);
		
		assertThat(this.instance.getResultsToTransfer(t_1_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_4), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_2_4), 
				is(Arrays.asList(
						construct(t_2_1, t_2_2, t_2_3, t_2_4)
				)));
		assertThat(this.instance.getResultsToTransfer(t_1_5), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_6), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_7), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_8), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_9), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_10), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_11), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_1_12), 
				is(Arrays.asList(
						construct(t_1_1, t_1_2, t_1_3, t_1_4, t_1_5, t_1_6, t_1_7, t_1_8, t_1_9, t_1_10, t_1_11, t_1_12)
				)));
		assertThat(this.instance.getResultsToTransfer(t_3_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_4_1), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_4), 
				is(Arrays.asList(
						construct(t_3_1, t_3_2, t_3_3, t_3_4)
				)));
		assertThat(this.instance.getResultsToTransfer(t_4_2), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_5), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_6), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_3_7), 
				is(Arrays.asList(
						construct(t_3_5, t_3_6, t_3_7)
				)));
		assertThat(this.instance.getResultsToTransfer(t_4_3), is(Arrays.asList()));
		assertThat(this.instance.getResultsToTransfer(t_4_4), is(Arrays.asList()));
	}
	
	private int x,y = 0;
	private final Tuple<ITimeInterval> create(String id, int state, int start, int end) {
		final Tuple<ITimeInterval> result = new Tuple<ITimeInterval>(
				new Object[] {0L, "", id, gf.createPoint(new Coordinate(this.x++, this.y++)), state}, false);
		result.setMetadata(new TimeInterval(new PointInTime(start), new PointInTime(end)));
		return result;
	}
	
	@SafeVarargs
	private final RouteConstructResult construct(Tuple<ITimeInterval>... ts) {
		final RouteConstructResult result = new RouteConstructResult(ts[0].getAttribute(2));
		for(final Tuple<ITimeInterval> t : ts) {
			result.points.add(t.getAttribute(3));
		}
		return result;
	}

}
