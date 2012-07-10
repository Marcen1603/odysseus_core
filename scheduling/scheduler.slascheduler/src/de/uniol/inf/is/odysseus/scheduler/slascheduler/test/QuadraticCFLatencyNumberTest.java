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
package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyNumber;

public class QuadraticCFLatencyNumberTest {
	
	public static final double DELTA = 0.000001;
	
	private SLA sla;
	private ICostFunction function;

	@Before
	public void setUp() throws Exception {
		this.sla = TestUtils.buildSLANumber();
		this.function = new QuadraticCFLatencyNumber();
	}

	@Test
	public void testOc() {
		assertEquals(3, this.function.oc(2, sla), DELTA);
		assertEquals(48, this.function.oc(8, sla), DELTA);
		
		assertEquals(3, this.function.oc(12, sla), DELTA);
		assertEquals(48, this.function.oc(18, sla), DELTA);

		assertEquals(300, this.function.oc(22, sla), DELTA);
		assertEquals(1200, this.function.oc(28, sla), DELTA);
	}

	@Test
	public void testMg() {
		assertEquals(0, this.function.mg(2, sla), DELTA);
		assertEquals(0, this.function.mg(8, sla), DELTA);
		assertEquals(0, this.function.mg(12, sla), DELTA);
		assertEquals(0, this.function.mg(18, sla), DELTA);
		assertEquals(0, this.function.mg(22, sla), DELTA);
		assertEquals(0, this.function.mg(28, sla), DELTA);
	}

}
