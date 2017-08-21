package de.uniol.inf.is.odysseus.core.server.logicaloperator;

public interface IOutOfOrderHandler {

	/**
	 * Returns true, if this operator guarantees the right output oder or false, if
	 * the elements do not follow any order (null if order is not set)
	 * 
	 * @return
	 */
	Boolean isAssureOrder();

	/**
	 * Allow to set ordering guarantees (e.g. for operators than can reorder their
	 * output)
	 * 
	 * @param assureOrder
	 */
	void setAssureOrder(boolean assureOrder);

}
