package de.uniol.inf.is.odysseus.peer.distribute.postprocess.calclatency;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;
import de.uniol.inf.is.odysseus.peer.distribute.postprocess.AbstractOperatorInsertionPostProcessor;

/**
 * The {@link CalcLatencyPostProcessor} inserts a {@link CalcLatencyAO} for each sink within a query.
 * @author Michael Brand
 */
public class CalcLatencyPostProcessor extends AbstractOperatorInsertionPostProcessor {
	
	@Override
	public String getName() {
		
		return "calclatency";
		
	}
	
	@Override
	protected ILogicalOperator createOperator() {
		
		return new CalcLatencyAO();
		
	}

}