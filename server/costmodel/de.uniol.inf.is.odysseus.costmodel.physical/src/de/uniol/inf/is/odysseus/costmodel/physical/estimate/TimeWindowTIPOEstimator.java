package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.AbstractNonBlockingWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingTimeWindowTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.UnboundedWindowTIPO;

@SuppressWarnings("rawtypes")
public class TimeWindowTIPOEstimator extends StandardPhysicalOperatorEstimator<AbstractNonBlockingWindowTIPO>{

	@Override
	public double getWindowSize() {
		long windowAdvanceMillis = getOperator().getWindowAdvanceMillis();
		long windowSizeMillis = getOperator().getWindowSizeMillis();
		
		if( windowAdvanceMillis <= 1 ) {
			return windowSizeMillis;
		}
		
		return windowSizeMillis / 2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Class<? extends AbstractNonBlockingWindowTIPO>> getOperatorClasses() {
		return Lists.<Class<? extends AbstractNonBlockingWindowTIPO>>newArrayList(
					SlidingAdvanceTimeWindowTIPO.class,
					SlidingTimeWindowTIPO.class,
					UnboundedWindowTIPO.class
				);
	}
}
