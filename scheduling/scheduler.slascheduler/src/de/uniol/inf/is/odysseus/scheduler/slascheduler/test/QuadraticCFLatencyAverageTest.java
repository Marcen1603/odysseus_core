package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyAverage;
import de.uniol.inf.is.odysseus.slamodel.SLA;

public class QuadraticCFLatencyAverageTest {
	
	public static final double DELTA = 0.000001;
	
	private SLA sla;
	private ICostFunction function;

	@Before
	public void setUp() throws Exception {
		this.sla = TestUtils.buildSLAAverage();
		this.function = new QuadraticCFLatencyAverage();
	}

	@Test
	public void testOc() {
		assertEquals(6.25, this.function.oc(50, sla), DELTA);
		assertEquals(56.25, this.function.oc(150, sla), DELTA);
		
		assertEquals(6.25, this.function.oc(250, sla), DELTA);
		assertEquals(56.25, this.function.oc(350, sla), DELTA);
		
		assertEquals(0, this.function.oc(450, sla), DELTA);
		assertEquals(0, this.function.oc(550, sla), DELTA);
	}

	@Test
	public void testMg() {
		assertEquals(0, this.function.mg(50, sla), DELTA);
		assertEquals(0, this.function.mg(150, sla), DELTA);
		
		assertEquals(56.25, this.function.mg(250, sla), DELTA);
		assertEquals(6.25, this.function.mg(350, sla), DELTA);
		
		// expected value is 100 because the mg function is defined to infinty
		// maybe this should be changed in further optimization of formulas
		// to
		assertEquals(100, this.function.mg(450, sla), DELTA);
		assertEquals(100, this.function.mg(550, sla), DELTA);
		assertEquals(100, this.function.mg(222666650, sla), DELTA);
	}

}
