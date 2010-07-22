package de.uniol.inf.is.odysseus.filtering.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class GainFunctionAO <M extends IProbability> extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String functionID;
	
	// TODO Weitere benötigte Attribute hier einfügen
	
	public GainFunctionAO(String functionID) {
		super();
		this.functionID = functionID;
	}
	
	public GainFunctionAO(GainFunctionAO<M> copy) {
		super(copy);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new GainFunctionAO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	
}
