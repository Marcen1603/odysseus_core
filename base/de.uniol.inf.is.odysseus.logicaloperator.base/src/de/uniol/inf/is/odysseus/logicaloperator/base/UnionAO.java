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
public class UnionAO extends BinaryLogicalOp{

	private static final long serialVersionUID = 2828756253165671692L;

	/**
     * @param unionPO
     */
    public UnionAO(UnionAO unionPO) {
        super(unionPO);
    }
    
    public UnionAO() {
        super();
    }

	public @Override
	UnionAO clone() {
		return new UnionAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(LEFT);
	}

}
