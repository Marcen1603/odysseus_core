package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Penalty;
import de.uniol.inf.is.odysseus.scheduler.slamodel.SLA;
import de.uniol.inf.is.odysseus.scheduler.slamodel.Scope;
import de.uniol.inf.is.odysseus.scheduler.slamodel.ServiceLevel;
import de.uniol.inf.is.odysseus.scheduler.slamodel.metric.Latency;
import de.uniol.inf.is.odysseus.scheduler.slamodel.penalty.AbsolutePenalty;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Average;
import de.uniol.inf.is.odysseus.scheduler.slamodel.scope.Number;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.TimeUnit;

public class TestUtils {
	
	public static SLA buildSLAAverage() {
		SLA sla = new SLA();
		sla.setName("Test SLA Latency Average");
		
		Latency latency = new Latency(new Integer(0), TimeUnit.milliseconds);
		sla.setMetric(latency);
		
		sla.setPrice(1000);
		
		Scope scope = new Average();
		sla.setScope(scope);
		
		List<ServiceLevel<?>> serviceLevels = new ArrayList<ServiceLevel<?>>();
		
		serviceLevels.add(buildServiceLevel(sla, 100, 200));
		serviceLevels.add(buildServiceLevel(sla, 200, 400));
		sla.setServiceLevel(serviceLevels);
		
		return sla;
	}
	
	public static SLA buildSLANumber() {
		SLA sla = new SLA();
		sla.setName("Test SLA Latency Number");
		
		Latency latency = new Latency(new Integer(500), TimeUnit.milliseconds);
		sla.setMetric(latency);
		
		sla.setPrice(1000);
		
		Scope scope = new Number();
		sla.setScope(scope);
		
		List<ServiceLevel<?>> serviceLevels = new ArrayList<ServiceLevel<?>>();
		
		serviceLevels.add(buildServiceLevel(sla, 75, 10));
		serviceLevels.add(buildServiceLevel(sla, 150, 20));
		sla.setServiceLevel(serviceLevels);
		
		return sla;
	}
	
	public static SLA buildSLASingle() {
		SLA sla = new SLA();
		sla.setName("Test SLA Latency Single");
		
		Latency latency = new Latency(new Integer(0), TimeUnit.milliseconds);
		sla.setMetric(latency);
		
		sla.setPrice(1000);
		
		Scope scope = new Average();
		sla.setScope(scope);
		
		List<ServiceLevel<?>> serviceLevels = new ArrayList<ServiceLevel<?>>();
		
		serviceLevels.add(buildServiceLevel(sla, 100, 200));
		serviceLevels.add(buildServiceLevel(sla, 225, 400));
		sla.setServiceLevel(serviceLevels);
		
		return sla;
	}
	
	private static ServiceLevel<?> buildServiceLevel(SLA sla, int penaltyCost, int threshold) {
		ServiceLevel<Integer> sl = new ServiceLevel<Integer>();
		Penalty penalty = new AbsolutePenalty(penaltyCost);
		sl.setPenalty(penalty);
		sl.setSla(sla);
		sl.setThreshold(threshold);
		return sl;
	}
	
}
