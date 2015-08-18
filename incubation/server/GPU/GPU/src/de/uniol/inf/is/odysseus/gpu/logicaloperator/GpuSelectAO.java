/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author Alexander
 *
 */
@LogicalOperator(name="GpuSelect", minInputPorts=1, maxInputPorts=1, doc ="Selection on GPU", category = { "Operator" })
public class GpuSelectAO extends SelectAO {

	public GpuSelectAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GpuSelectAO(IPredicate<?> predicate) {
		super(predicate);
		// TODO Auto-generated constructor stub
	}

	public GpuSelectAO(SelectAO po) {
		super(po);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setHeartbeatRate(int rate) {
		// TODO Auto-generated method stub
		super.setHeartbeatRate(rate);
	}

	@Override
	public int getHeartbeatRate() {
		// TODO Auto-generated method stub
		return super.getHeartbeatRate();
	}

	@Override
	public void setPredicate(IPredicate predicate) {
		// TODO Auto-generated method stub
		super.setPredicate(predicate);
	}

	@Override
	public IPredicate<?> getPredicate() {
		// TODO Auto-generated method stub
		return super.getPredicate();
	}

	@Override
	public SelectAO clone() {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	

}
