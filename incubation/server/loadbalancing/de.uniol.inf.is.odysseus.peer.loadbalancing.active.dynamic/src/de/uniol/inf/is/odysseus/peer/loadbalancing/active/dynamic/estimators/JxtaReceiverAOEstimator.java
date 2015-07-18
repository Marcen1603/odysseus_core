package de.uniol.inf.is.odysseus.peer.loadbalancing.active.dynamic.estimators;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.logical.StandardLogicalOperatorEstimator;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;

public class JxtaReceiverAOEstimator extends StandardLogicalOperatorEstimator<JxtaReceiverAO> {

	@Override
	protected Class<? extends JxtaReceiverAO> getOperatorClass() {
		return JxtaReceiverAO.class;
	}
	
	@Override
	//To estimate network bandwith guess, how many bytes need to be processed per second to not be a bottleneck
	public double getNetwork() {
		return getDatarate()*EstimatorHelper.sizeInBytes(getOperator().getOutputSchema());
	}
	
	//Taken from StreamAO
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
