/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



/**
 * @author Marco Grawunder
 *
 */
public class DifferenceAO extends BinaryLogicalOp{
	
	private static final long serialVersionUID = 4518770628909423647L;

    public DifferenceAO(DifferenceAO differencePO) {
        super(differencePO);
    }

    public DifferenceAO() {
        super();
    }

	public @Override
	DifferenceAO clone() {	
		return new DifferenceAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(LEFT);
	}

  

}
