package de.uniol.inf.is.odysseus.base;

import java.util.List;


/**
 * Describes an operator which is owned by an other object (e. g. a query can
 * own physical and logical operator). An operator could be owned by more then
 * one object (e. g. a global source which distributes data elements to more
 * then one query).
 * 
 * @author Wolf Bauer
 * 
 */
public interface IOwnedOperator {
	/**
	 * Adds a owner to this operator.
	 * 
	 * @param owner Owner which should be added.
	 */
	public void addOwner(IOperatorOwner owner);

	/**
	 * Removes a owner to this operator.
	 * 
	 * @param owner Owner which should be removed.
	 */
	public void removeOwner(IOperatorOwner owner);

	/**
	 * Checks if this operator is owned by a specific owner.
	 * 
	 * @param owner Owner which should be checked.
	 * @return TRUE: This operator is owned by the owner. FALSE: else
	 */
	public boolean isOwnedBy(IOperatorOwner owner);

	/**
	 * Indicatives if this operator has at least one owner.
	 * 
	 * @return TRUE: This operator has at least one owner. FALSE: else
	 */
	public boolean hasOwner();

	/**
	 * Returns all registered owner of this operator.
	 * 
	 * @return All registered owner of this operator.
	 */
	public List<IOperatorOwner> getOwner();
}
