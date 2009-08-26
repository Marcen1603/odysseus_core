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
public class JoinAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 3710951139395164614L;

	public JoinAO() {
		super();
	}

	/**
	 * @param joinPredicate
	 */
	public JoinAO(IPredicate<?> joinPredicate) {
		super();
		this.setPredicate(joinPredicate);
	}

	/**
	 * @param po
	 */
	public JoinAO(JoinAO joinPO) {
		super(joinPO);
	}

	@SuppressWarnings("unchecked")
	public synchronized void setPredicate(IPredicate joinPredicate) {
		super.setPredicate(joinPredicate);
	}

	public @Override
	JoinAO clone() {
		return new JoinAO(this);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString());
		ret.append(" Predicate " + getPredicate());
		return ret.toString();
	}

}
