/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;

/**
 * @author Alexander
 *
 */
public class GpuJoinAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	/**
	 * @param op
	 */
	public GpuJoinAO(AbstractLogicalOperator op) {
		super(op);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public GpuJoinAO() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
