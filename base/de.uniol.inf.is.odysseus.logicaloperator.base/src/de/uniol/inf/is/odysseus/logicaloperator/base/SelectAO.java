/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
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
	}

	public SelectAO(IPredicate<?> predicate) {
		setPredicate(predicate);
	}

	public SelectAO clone() {
		return new SelectAO(this);
	}

}
