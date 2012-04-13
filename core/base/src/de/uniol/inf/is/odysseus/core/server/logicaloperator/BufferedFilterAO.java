package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

/**
 * This class represents an operator that buffers elements for
 * a distinct time. If the predicate is positive evaluated, element are
 * send to the next operator for some time. 
 * 
 * @author Marco Grawunder
 *
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="BUFFEREDFILTER")
public class BufferedFilterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 5312945034141719894L;
	private long bufferTime;
	private long deliverTime;
		
	public BufferedFilterAO() {
		super();
	}

	public BufferedFilterAO(BufferedFilterAO po) {
		super(po);
		this.bufferTime = po.bufferTime;
		this.deliverTime = po.deliverTime;
	}

	@Override
	public BufferedFilterAO clone() {
		return new BufferedFilterAO(this);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@Parameter(type=PredicateParameter.class)
	public void setPredicate(IPredicate predicate) {
			super.setPredicate(predicate);
	}

	public long getBufferTime() {
		return bufferTime;
	}

	@Parameter(type=LongParameter.class, name = "BUFFERTIME")
	public void setBufferTime(long bufferTime) {
		this.bufferTime = bufferTime;
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	@Parameter(type=LongParameter.class, name = "DELIVERTIME")
	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
	}
	
	

}
