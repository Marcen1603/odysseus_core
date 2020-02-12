package de.uniol.inf.is.odysseus.parser.pql.impl;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;

public class PlaceHolderAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 6899460703137885209L;
	String namedOP;
	private ILogicalOperator toReplace;
	
	public PlaceHolderAO() {
	}

	public PlaceHolderAO(PlaceHolderAO op) {
		super(op);
		this.namedOP = op.namedOP;
	}
	
	public void setNamedOP(String namedOP) {
		this.namedOP = namedOP;
	}
	
	public String getNamedOP() {
		return namedOP;
	}
	

	@Override
	public AbstractLogicalOperator clone() {
		return new PlaceHolderAO(this);
	}

	public void setLogicalOperator(ILogicalOperator toReplace) {
		this.toReplace = toReplace;
	}
	
	public ILogicalOperator getToReplace() {
		return toReplace;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (toReplace != null) {
			return toReplace.getOutputSchema(pos);
		}else {
			return null;
		}
	}

}
