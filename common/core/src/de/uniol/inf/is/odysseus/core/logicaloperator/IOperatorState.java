/**
 * 
 */
package de.uniol.inf.is.odysseus.core.logicaloperator;

/**
 * @author Dennis Nowak
 *
 */
public interface IOperatorState {
	
	public enum OperatorStateType {
		
		UNKNOWN, STATELESS, PARTITIONED_STATE, ARBITRARY_STATE

	}
	
	public OperatorStateType getStateType();

}
