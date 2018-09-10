package de.uniol.inf.is.odysseus.costmodel.logical.estimate;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;

public class StreamAOEstimator extends StandardLogicalOperatorEstimator<StreamAO> {
	

	@Override
	protected Class<? extends StreamAO> getOperatorClass() {
		return StreamAO.class;
	}
	
	@Override
	public double getDatarate() {
		Optional<Double> optDatarate = getCostModelKnowledge().getDatarate(getOperator().getName());
		return optDatarate.isPresent() ? optDatarate.get() : super.getDatarate();
	}
	
	@Override
	public double getMemory() {
		return super.getMemory();
	}
	
	@Override
	public double getCpu() {
		
		double cpuTime = 0;
		cpuTime = optionalAdd(cpuTime, getCostModelKnowledge().getCpuTime("StreamAO"), StandardLogicalOperatorEstimator.DEFAULT_CPU_COST);
		return cpuTime > 0 ? ( cpuTime * getDatarate() ) : super.getCpu();
	}

	private static double optionalAdd(double cpuTime, Optional<Double> opt, double other) {
		if( opt.isPresent() ) {
			return cpuTime + ( opt.get() / 1000000000 );
		}
		return cpuTime + other;
	}

}
