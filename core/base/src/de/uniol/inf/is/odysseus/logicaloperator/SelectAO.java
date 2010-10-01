/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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

	@Override
	public SelectAO clone() {
		return new SelectAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

}
