/*
 *Copyright 2015 Marcus Behrendt
 * 
 \* Licensed under the Apache License, Version 2.0 (the "License");
 \* you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/package de.uniol.inf.is.odysseus.trajectory.physical.construct;


public class FulltrajectoryConstructStrategyTest {

	
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Test
//	public void testGetResultsToTransfer_60_20() {
//		/* create Tuples for tumpling TimeWindows (Size=60s) (range=20s) */
//		/*
//		schema=[['Timestamp', 'Long'],
//				['VehicleType', 'String'],
//				['VehicleId', 'String'],
//				['Position', 'SpatialGeometry'],
//				['State', 'Integer']
//		*/
//		
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_1_1 = this.create("1", 0, 1, 60); // new trajectory 1
//		final Tuple<ITimeInterval> t_2_1 = this.create("2", 0, 3, 60); // new trajectory 2 (Case 3)
//		final Tuple<ITimeInterval> t_1_2 = this.create("1", 1, 5, 60);
//		final Tuple<ITimeInterval> t_2_2 = this.create("2", 1, 6, 60);
//		final Tuple<ITimeInterval> t_1_3 = this.create("1", 2, 7, 60);
//		final Tuple<ITimeInterval> t_2_3 = this.create("2", 2, 10, 60);
//		final Tuple<ITimeInterval> t_1_4 = this.create("1", 3, 15, 60);	
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_2_4 = this.create("2", -1, 22, 80); // trajectory 2 finished
//		final Tuple<ITimeInterval> t_1_5 = this.create("1", 4, 25, 80);
//		final Tuple<ITimeInterval> t_1_6 = this.create("1", 5, 30, 80);
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_1_7 = this.create("1", 6, 40, 100);
//		final Tuple<ITimeInterval> t_1_8 = this.create("1", 7, 50, 100);		
//		final Tuple<ITimeInterval> t_1_9 = this.create("1", 8, 59, 100);
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_1_10 = this.create("1", 9, 60, 120);
//		final Tuple<ITimeInterval> t_1_11 = this.create("1", 10, 65, 120);
//		final Tuple<ITimeInterval> t_3_1 = this.create("3", 0, 65, 120); // new trajectory 3
//		final Tuple<ITimeInterval> t_1_12 = this.create("1", -1, 71, 120); // trajectory 1 finished
//		final Tuple<ITimeInterval> t_3_2 = this.create("3", 1, 72, 120);
//		final Tuple<ITimeInterval> t_3_3 = this.create("3", 2, 75, 120);
//		final Tuple<ITimeInterval> t_4_1 = this.create("4", 0, 77, 120); // new trajectory 4
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_3_4 = this.create("3", -1, 80, 140); // trajectory 3 finished
//		final Tuple<ITimeInterval> t_4_2 = this.create("4", 1, 85, 140);
//		final Tuple<ITimeInterval> t_3_5 = this.create("3", 0, 90, 140); // new trajectory 3
//		final Tuple<ITimeInterval> t_3_6 = this.create("3", 1, 94, 140);
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_3_7 = this.create("3", -1, 100, 160); // trajectory 3 finished
//		final Tuple<ITimeInterval> t_4_3 = this.create("4", 2, 104, 160);
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_4_4 = this.create("4", -1, 120, 180); // trajectory 4 finished
//		
//		List<List<Tuple<ITimeInterval>>> r;
//		
//		assertTrue(this.instance.getResultsToTransfer(t_1_1).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_1).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_4).isEmpty());
//		/* Start New Window */
//		r = (List)this.instance.getResultsToTransfer(t_2_4);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_2_1, t_2_2, t_2_3, t_2_4)
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_1_5).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_6).isEmpty());
//		/* Start New Window */
//		assertTrue(this.instance.getResultsToTransfer(t_1_7).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_8).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_9).isEmpty());
//		/* Start New Window */
//		assertTrue(this.instance.getResultsToTransfer(t_1_10).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_11).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_1).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_12).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_4_1).isEmpty());
//		/* Start New Window */
//		r = (List)this.instance.getResultsToTransfer(t_3_4);
//		assertThat(r,
//				hasItems(new List[] {
//							Arrays.asList(t_3_1, t_3_2, t_3_3, t_3_4)
//						}
//						
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_4_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_5).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_6).isEmpty());
//		/* Start New Window */
//		r = (List)this.instance.getResultsToTransfer(t_3_7);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_3_5, t_3_6, t_3_7),
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_4_3).isEmpty());
//		/* Start New Window */
//		assertTrue(this.instance.getResultsToTransfer(t_4_4).isEmpty());
//	}
//	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Test
//	public void testGetResultsToTransfer_60_60() {
//		/* create Tuples for tumpling TimeWindows (Size=60s) (range=60s) */
//		/*
//		schema=[['Timestamp', 'Long'],
//				['VehicleType', 'String'],
//				['VehicleId', 'String'],
//				['Position', 'SpatialGeometry'],
//				['State', 'Integer']
//		*/
//		
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_1_1 = this.create("1", 0, 1, 60); // new trajectory 1
//		final Tuple<ITimeInterval> t_2_1 = this.create("2", 0, 3, 60); // new trajectory 2 (Case 3)
//		final Tuple<ITimeInterval> t_1_2 = this.create("1", 1, 5, 60);
//		final Tuple<ITimeInterval> t_2_2 = this.create("2", 1, 6, 60);
//		final Tuple<ITimeInterval> t_1_3 = this.create("1", 2, 7, 60);
//		final Tuple<ITimeInterval> t_2_3 = this.create("2", 2, 10, 60);
//		final Tuple<ITimeInterval> t_1_4 = this.create("1", 3, 15, 60);		
//		final Tuple<ITimeInterval> t_2_4 = this.create("2", -1, 20, 60); // trajectory 2 finished
//		final Tuple<ITimeInterval> t_1_5 = this.create("1", 4, 25, 60);
//		final Tuple<ITimeInterval> t_1_6 = this.create("1", 5, 30, 60);
//		final Tuple<ITimeInterval> t_1_7 = this.create("1", 6, 40, 60);
//		final Tuple<ITimeInterval> t_1_8 = this.create("1", 7, 50, 60);		
//		final Tuple<ITimeInterval> t_1_9 = this.create("1", 8, 59, 60);
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_1_10 = this.create("1", 9, 60, 120);
//		final Tuple<ITimeInterval> t_1_11 = this.create("1", 10, 65, 120);
//		final Tuple<ITimeInterval> t_3_1 = this.create("3", 0, 65, 120); // new trajectory 3
//		final Tuple<ITimeInterval> t_1_12 = this.create("1", -1, 71, 120); // trajectory 1 finished
//		final Tuple<ITimeInterval> t_3_2 = this.create("3", 1, 72, 120);
//		final Tuple<ITimeInterval> t_3_3 = this.create("3", 2, 75, 120);
//		final Tuple<ITimeInterval> t_4_1 = this.create("4", 0, 77, 120); // new trajectory 4
//		final Tuple<ITimeInterval> t_3_4 = this.create("3", -1, 80, 120); // trajectory 3 finished
//		final Tuple<ITimeInterval> t_4_2 = this.create("4", 1, 85, 120);
//		final Tuple<ITimeInterval> t_3_5 = this.create("3", 0, 90, 120); // new trajectory 3
//		final Tuple<ITimeInterval> t_3_6 = this.create("3", 1, 94, 120);
//		final Tuple<ITimeInterval> t_3_7 = this.create("3", -1, 100, 120); // trajectory 3 finished
//		final Tuple<ITimeInterval> t_4_3 = this.create("4", 2, 104, 120);
//		/* Start New Window */
//		final Tuple<ITimeInterval> t_4_4 = this.create("4", -1, 120, 180); // trajectory 4
//		
//		List<List<Tuple<ITimeInterval>>> r;
//		
//		assertTrue(this.instance.getResultsToTransfer(t_1_1).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_1).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_4).isEmpty());
//		r = (List)this.instance.getResultsToTransfer(t_2_4);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_2_1, t_2_2, t_2_3, t_2_4)
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_1_5).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_6).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_7).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_8).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_9).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_10).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_11).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_1).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_12).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_4_1).isEmpty());
//		r = (List)this.instance.getResultsToTransfer(t_3_4);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_3_1, t_3_2, t_3_3, t_3_4)
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_4_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_5).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_6).isEmpty());
//		r = (List)this.instance.getResultsToTransfer(t_3_7);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_3_5, t_3_6, t_3_7) 
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_4_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_4_4).isEmpty());
//	}
//		
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Test
//	public void testGetResultsToTransfer_Inf() {
//		/* create Tuples for tumpling TimeWindows (Size=60s) (range=60s) */
//		/*
//		schema=[['Timestamp', 'Long'],
//				['VehicleType', 'String'],
//				['VehicleId', 'String'],
//				['Position', 'SpatialGeometry'],
//				['State', 'Integer']
//		*/
//		
//		final Tuple<ITimeInterval> t_1_1 = this.create("1", 0, 0, Integer.MAX_VALUE); // new trajectory 1
//		final Tuple<ITimeInterval> t_2_1 = this.create("2", 0, 3, Integer.MAX_VALUE); // new trajectory 2 (Case 3)
//		final Tuple<ITimeInterval> t_1_2 = this.create("1", 1, 5, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_2_2 = this.create("2", 1, 6, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_1_3 = this.create("1", 2, 7, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_2_3 = this.create("2", 2, 10, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_1_4 = this.create("1", 3, 15, Integer.MAX_VALUE);		
//		final Tuple<ITimeInterval> t_2_4 = this.create("2", -1, 20, Integer.MAX_VALUE); // trajectory 2 finished
//		final Tuple<ITimeInterval> t_1_5 = this.create("1", 4, 25, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_1_6 = this.create("1", 5, 30, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_1_7 = this.create("1", 6, 40, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_1_8 = this.create("1", 7, 50, Integer.MAX_VALUE);		
//		final Tuple<ITimeInterval> t_1_9 = this.create("1", 8, 59, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_1_10 = this.create("1", 9, 60, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_1_11 = this.create("1", 10, 65, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_3_1 = this.create("3", 0, 65, Integer.MAX_VALUE); // new trajectory 3
//		final Tuple<ITimeInterval> t_1_12 = this.create("1", -1, 71, Integer.MAX_VALUE); // trajectory 1 finished
//		final Tuple<ITimeInterval> t_3_2 = this.create("3", 1, 72, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_3_3 = this.create("3", 2, 75, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_4_1 = this.create("4", 0, 77, Integer.MAX_VALUE); // new trajectory 4
//		final Tuple<ITimeInterval> t_3_4 = this.create("3", -1, 80, Integer.MAX_VALUE); // trajectory 3 finished
//		final Tuple<ITimeInterval> t_4_2 = this.create("4", 1, 85, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_3_5 = this.create("3", 0, 90, Integer.MAX_VALUE); // new trajectory 3
//		final Tuple<ITimeInterval> t_3_6 = this.create("3", 1, 94, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_3_7 = this.create("3", -1, 100, Integer.MAX_VALUE); // trajectory 3 finished
//		final Tuple<ITimeInterval> t_4_3 = this.create("4", 2, 104, Integer.MAX_VALUE);
//		final Tuple<ITimeInterval> t_4_4 = this.create("4", -1, 120, Integer.MAX_VALUE); // trajectory 4 finished
//		
//		List<List<Tuple<ITimeInterval>>> r;
//		
//		assertTrue(this.instance.getResultsToTransfer(t_1_1).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_1).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_2_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_4).isEmpty());
//		r = (List)this.instance.getResultsToTransfer(t_2_4);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_2_1, t_2_2, t_2_3, t_2_4)
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_1_5).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_6).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_7).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_8).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_9).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_10).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_1_11).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_1).isEmpty());
//		r = (List)this.instance.getResultsToTransfer(t_1_12);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_1_1, t_1_2, t_1_3, t_1_4, t_1_5, t_1_6, t_1_7, t_1_8, t_1_9, t_1_10, t_1_11, t_1_12)
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_3_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_3).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_4_1).isEmpty());
//		r = (List)this.instance.getResultsToTransfer(t_3_4);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_3_1, t_3_2, t_3_3, t_3_4)
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_4_2).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_5).isEmpty());
//		assertTrue(this.instance.getResultsToTransfer(t_3_6).isEmpty());
//		r = (List)this.instance.getResultsToTransfer(t_3_7);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_3_5, t_3_6, t_3_7)
//						}
//				));
//		assertSame(r.size(), 1);
//		assertTrue(this.instance.getResultsToTransfer(t_4_3).isEmpty());
//		r = (List)this.instance.getResultsToTransfer(t_4_4);
//		assertThat(r, 
//				hasItems(new List[] {
//							Arrays.asList(t_4_1, t_4_2, t_4_3, t_4_4)
//						}
//				));
//		assertSame(r.size(), 1);
//	}
//	

}
