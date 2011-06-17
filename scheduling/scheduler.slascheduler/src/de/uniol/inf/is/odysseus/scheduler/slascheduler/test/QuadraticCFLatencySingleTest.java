package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencySingle;

public class QuadraticCFLatencySingleTest {

	private SLA sla;
	private ICostFunction function;

	@Before
	public void setUp() throws Exception {
		this.sla = TestUtils.buildSLASingle();
		this.function = new QuadraticCFLatencySingle();
	}

	@Test
	public void testOc() {
		assertEquals(106, this.function.oc(50, sla));
		assertEquals(156, this.function.oc(150, sla));
		
		assertEquals(132, this.function.oc(250, sla));
		assertEquals(195, this.function.oc(350, sla));
		
		assertEquals(101250, this.function.oc(450, sla));
		assertEquals(123750, this.function.oc(550, sla));
	}

	@Test
	public void testMg() {
		assertEquals(0, this.function.mg(2, sla));
		assertEquals(0, this.function.mg(8, sla));
		assertEquals(0, this.function.mg(12, sla));
		assertEquals(0, this.function.mg(18, sla));
		assertEquals(0, this.function.mg(22, sla));
		assertEquals(0, this.function.mg(28, sla));
	}

}
