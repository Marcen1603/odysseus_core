package de.uniol.inf.is.odysseus.base.planmanagement;

/**
 * Describes an object which could own an operator.
 * 
 * @author Wolf Bauer
 *
 */
public interface IOperatorOwner {
	/**
	 * ID which identifies an owner. This ID should be unique.
	 * 
	 * @return ID which identifies an owner. This ID should be unique.
	 */
	public int getID();
}
