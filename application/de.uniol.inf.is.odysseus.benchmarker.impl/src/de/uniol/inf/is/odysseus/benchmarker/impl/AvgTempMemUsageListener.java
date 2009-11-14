package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;

/**
 * Collects memory usage for a given operator using SweepAreas.
 * @author Jan Steinke
 *
 */
public class AvgTempMemUsageListener  implements POEventListener{

	private double min = -1;
	private double max = -1;
	private double collected;
	private double called_times;
	
	@SuppressWarnings( "unchecked" )
	private IPhysicalOperator op;
	
	@SuppressWarnings("unchecked")
	public AvgTempMemUsageListener(IPhysicalOperator op) {
		this.op = op;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void poEventOccured(POEvent poEvent) {
		
		double tmp = 0;
		
		if(op instanceof JoinTIPO) {
			final ISweepArea[] areas = ((JoinTIPO)op).getAreas();

			for(ISweepArea each : areas) {
				tmp += each.size();
			}
		} else if(op instanceof IBuffer) {
			tmp = ((IBuffer)op).size();
		}
		
		if(min == -1 || tmp < min) {
			this.min = tmp;
		}
		
		if(max == -1 || tmp > max) {
			this.max = tmp;
		}
		
		collected += tmp;
		called_times++;
		
	}
	
	public double getAverage() {
		if(called_times == 0) {
			return -1;
		} else {
			return collected/called_times;
		}
	}
	
	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

}
