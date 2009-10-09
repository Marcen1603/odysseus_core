/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;



/**
 * @author Marco Grawunder
 *
 */
public class DifferenceAO extends BinaryLogicalOp{

    /**
     * @param differencePO
     */
    public DifferenceAO(DifferenceAO differencePO) {
        super(differencePO);
        setName("DifferencePO");
    }

    /**
     * 
     */
    public DifferenceAO() {
        super();
        setName("DifferencePO");
    }

	public @Override
	DifferenceAO clone() {	
		return new DifferenceAO(this);
	}



  

}
