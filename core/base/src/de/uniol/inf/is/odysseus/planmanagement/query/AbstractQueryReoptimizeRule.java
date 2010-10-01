package de.uniol.inf.is.odysseus.planmanagement.query;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule;

/**
 * This class is a base object for creating reoptimization rules for queries. It
 * provides methods to store queries and to send reoptimize
 * request to these queries if this rule is valid.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractQueryReoptimizeRule implements
		IReoptimizeRule<IQuery> {

	/**
	 * List of queries which are informed if this rule is valid.
	 */
	protected ArrayList<IQuery> reoptimizable;
	
	/**
	 * Informs all registered queries that this rule is valid.
	 */
	protected void fireReoptimizeEvent() {
		for (IQuery reoptimizableType : this.reoptimizable) {
			reoptimizableType.reoptimize();
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule#addReoptimieRequester(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester)
	 */
	@Override
	public void addReoptimieRequester(IQuery reoptimizable) {
		this.reoptimizable.add(reoptimizable);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule#removeReoptimieRequester(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester)
	 */
	@Override
	public void removeReoptimieRequester(IQuery reoptimizable) {
		this.reoptimizable.remove(reoptimizable);
	}
}
