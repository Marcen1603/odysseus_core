package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class AccessAOEstimator extends StandardLogicalOperatorEstimator<AbstractAccessAO> {
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Class<? extends AbstractAccessAO>> getOperatorClasses() {
		return Lists.newArrayList(AbstractAccessAO.class, AccessAO.class);
	}
	
	@Override
	public double getDatarate() {
		Optional<Double> optDatarate = getCostModelKnowledge().getDatarate(getOperator().getName());
		return optDatarate.isPresent() ? optDatarate.get() : super.getDatarate();
	}
	
	@Override
	public double getMemory() {
		return super.getMemory() + 20; // MetadataUpdatePO and MetadataCreationPO
	}
	
	@Override
	public double getCpu() {
		Optional<Double> optCpuTime1 = getCostModelKnowledge().getCpuTime("Receiver");
		Optional<Double> optCpuTime2 = getCostModelKnowledge().getCpuTime("MetadataCreation");
		Optional<Double> optCpuTime3 = getCostModelKnowledge().getCpuTime("MetadataUpdate");
		
		double cpuTime = 0;
		cpuTime = optionalAdd(cpuTime, optCpuTime1, StandardLogicalOperatorEstimator.DEFAULT_CPU_COST);
		cpuTime = optionalAdd(cpuTime, optCpuTime2, StandardLogicalOperatorEstimator.DEFAULT_CPU_COST);
		cpuTime = optionalAdd(cpuTime, optCpuTime3, StandardLogicalOperatorEstimator.DEFAULT_CPU_COST);
		
		return cpuTime > 0 ? ( cpuTime * getDatarate() ) : super.getCpu();
	}

	private static double optionalAdd(double cpuTime, Optional<Double> opt, double other) {
		if( opt.isPresent() ) {
			return cpuTime + ( opt.get() / 1000000000 );
		}
		return cpuTime + other;
	}
}
