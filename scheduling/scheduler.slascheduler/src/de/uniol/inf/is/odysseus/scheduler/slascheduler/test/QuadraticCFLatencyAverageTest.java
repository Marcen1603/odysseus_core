package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyAverage;

public class QuadraticCFLatencyAverageTest {
	
	private SLA sla;
	private ICostFunction function;

	@Before
	public void setUp() throws Exception {
		this.sla = TestUtils.buildSLAAverage();
		this.function = new QuadraticCFLatencyAverage();
	}

	@Test
	public void testOc() {
		assertEquals(6, this.function.oc(50, sla));
		assertEquals(56, this.function.oc(150, sla));
		
		assertEquals(6, this.function.oc(250, sla));
		assertEquals(56, this.function.oc(350, sla));
		
		assertEquals(0, this.function.oc(450, sla));
		assertEquals(0, this.function.oc(550, sla));
	}

	@Test
	public void testMg() {
		assertEquals(0, this.function.mg(50, sla));
		assertEquals(0, this.function.mg(150, sla));
		
		assertEquals(56, this.function.mg(250, sla));
		assertEquals(6, this.function.mg(350, sla));
		
		// expected value is 100 because the mg function is defined to infinty
		// maybe this should be changed in further optimization of formulas
		// to
		assertEquals(100, this.function.mg(450, sla));
		assertEquals(100, this.function.mg(550, sla));
		assertEquals(100, this.function.mg(222666650, sla));
	}

}
