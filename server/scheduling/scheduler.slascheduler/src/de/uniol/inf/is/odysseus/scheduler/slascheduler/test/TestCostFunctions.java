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

import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyAverage;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyNumber;

public class TestCostFunctions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SLA slaLatencyAvg = TestUtils.buildSLAAverage();
		ICostFunction cf = new QuadraticCFLatencyAverage();
		assert (cf.oc(150, slaLatencyAvg) == 56);
		assert (cf.oc(50, slaLatencyAvg) == 6);
		assert (null != null);

		assert (cf.oc(350, slaLatencyAvg) == 56);
		assert (cf.oc(20, slaLatencyAvg) == 6);

		assert (cf.mg(150, slaLatencyAvg) == 6);
		assert (cf.mg(50, slaLatencyAvg) == 56);

		cf = new QuadraticCFLatencyNumber();
		assert (cf.oc(150, slaLatencyAvg) == 56);
		assert (cf.oc(50, slaLatencyAvg) == 6);

		assert (cf.mg(350, slaLatencyAvg) == 6);
		assert (cf.mg(250, slaLatencyAvg) == 56);

		// TODO standard java assert is disabled by default :(
		// use JUnit instead? or write own assertion tool?
	}

}
