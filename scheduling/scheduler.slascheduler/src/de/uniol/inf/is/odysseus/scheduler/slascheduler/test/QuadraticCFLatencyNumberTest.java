package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.core.sla.SLA;
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
