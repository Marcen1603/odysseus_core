package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencySingle;

public class QuadraticCFLatencySingleTest {
	
	public static final double DELTA = 0.000001;

	private SLA sla;
	private ICostFunction function;

	@Before
	public void setUp() throws Exception {
		this.sla = TestUtils.buildSLASingle();
		this.function = new QuadraticCFLatencySingle();
	}

	@Test
	public void testOc() {
		assertEquals(106.25, this.function.oc(50, sla), DELTA);
		assertEquals(156.25, this.function.oc(150, sla), DELTA);
		
		assertEquals(132.8125, this.function.oc(250, sla), DELTA);
		assertEquals(195.3125, this.function.oc(350, sla), DELTA);
		
		assertEquals(101250, this.function.oc(450, sla), DELTA);
		assertEquals(123750, this.function.oc(550, sla), DELTA);
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
