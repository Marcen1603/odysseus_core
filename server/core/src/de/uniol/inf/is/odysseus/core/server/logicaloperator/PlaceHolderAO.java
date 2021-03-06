package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class PlaceHolderAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 6899460703137885209L;
	String namedOP;
	private ILogicalOperator replacemenrt;
	
	public PlaceHolderAO() {
	}

	public PlaceHolderAO(PlaceHolderAO op) {
		super(op);
		this.namedOP = op.namedOP;
		this.replacemenrt = op.replacemenrt;
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

	public void setLogicalOperator(ILogicalOperator replacement) {
		this.replacemenrt = replacement;
	}
	
	public ILogicalOperator getReplacement() {
		return replacemenrt;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (replacemenrt != null) {
			return replacemenrt.getOutputSchema(pos);
		}else {
			Collection<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			return SDFSchemaFactory.createNewTupleSchema("empty", attributes);
		}
	}

}
