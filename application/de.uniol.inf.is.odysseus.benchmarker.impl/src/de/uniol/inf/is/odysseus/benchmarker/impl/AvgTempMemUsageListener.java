package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;

/**
 * Collects memory usage for a given operator using SweepAreas.
 * @author Jan Steinke
 *
 */
public class AvgTempMemUsageListener  implements POEventListener{

	double min = -1;
	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	double max = -1;
	double collected;
	double called_times;
	
	@SuppressWarnings( "unchecked" )
	private JoinTIPO op;
	
	@SuppressWarnings("unchecked")
	public AvgTempMemUsageListener(JoinTIPO op) {
		this.op = op;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void poEventOccured(POEvent poEvent) {
		
		final ISweepArea[] areas = op.getAreas();
		
		double tmp = 0;
		
		for(ISweepArea each : areas) {
			tmp += each.size();
		}
		
		if(min == -1 || tmp < min) {
			min = tmp;
		}
		
		if(max == -1 || tmp > max) {
			max = tmp;
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

}
