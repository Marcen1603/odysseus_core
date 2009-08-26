/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;

/**
 * @author Marco Grawunder
 * 
 */
public class SelectAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 3215936185841514846L;

	public SelectAO() {
		super();
	}

	public SelectAO(SelectAO po) {
		super(po);
		setPOName(po.getPOName());
	}

	public SelectAO(IPredicate<?> predicate) {
		setPredicate(predicate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPredicate(IPredicate predicate) {
		super.setPredicate(predicate);
	}

	public SelectAO clone() {
		return new SelectAO(this);
	}

}
