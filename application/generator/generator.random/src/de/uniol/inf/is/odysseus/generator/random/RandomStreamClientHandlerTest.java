/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.generator.random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.generator.DataTuple;

public class RandomStreamClientHandlerTest {

	private static final int RANDOM_VALUE_TEST_COUNT = 1000;

	@Test
	public void testRandomTuple() throws Throwable {
		RandomStreamClientHandler handler = new RandomStreamClientHandler();
		
		List<DataTuple> tuples = handler.next();
		assertNotNull(tuples, "List of random tuples is null!");
		assertEquals(tuples.size(), 1, "List of random tuples must be exactly one!");
		
		DataTuple t = tuples.get(0);
		assertTrue( t.getAttribute(0) instanceof Long, "First attribute must be of type long!");
		assertTrue( t.getAttribute(1) instanceof Long, "First attribute must be of type long!");		
	}
	
	@Test(dependsOnMethods = "testRandomTuple", dataProvider = "randomValuesDataProvider")
	public void testRandomValues(int min, int max) throws Throwable {
		
		RandomStreamClientHandler handler = new RandomStreamClientHandler(min, max);
		
		for( int i = 0; i < RANDOM_VALUE_TEST_COUNT; i++ ) {
			List<DataTuple> tuples = handler.next();
			DataTuple tuple = tuples.get(0);
			
			assertTrue((long)tuple.getAttribute(1) <= max, "Value can be outside the max-limits!");
			assertTrue((long)tuple.getAttribute(1) >= min, "Value can be outside the min-limits!");
		}
		
	}
	
	@SuppressWarnings("unused")
	@DataProvider
	private static Object[][] randomValuesDataProvider() {
		return new Object[][] {
				{0, 100},	
				{-100, 0},	
				{-100, 100},	
				{30, 34},	
				{2, 2},	
		};
	}
	
}
