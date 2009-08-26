package de.uniol.inf.is.odysseus.base;

import de.uniol.inf.is.odysseus.base.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IStatefulOperator;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */
public interface IPhysicalOperator extends IClone, IOwnedOperator,
		IStatefulOperator {

	boolean isSource();

	boolean isSink();
	
	boolean isPipe();
	
	public void open() throws OpenFailedException;

	/**
	 * stop the operator and free all its resources
	 */
	public void close();
}
