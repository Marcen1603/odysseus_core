package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * UnnestPO
 * 
 * @author Jendrik Poloczek
 * 
 */
public class UnnestPO<T extends IMetaAttribute> extends
		AbstractPipe<ObjectRelationalTuple<T>, ObjectRelationalTuple<T>> {

    private SDFAttributeList inputSchema;
    private SDFAttributeList outputSchema;
	private SDFAttribute nestingAttribute;
	
	/**
	 * @param toNestAttributes attributes to nest
	 * @param nestingAttribute nesting attribute
	 */
	public UnnestPO(
			SDFAttributeList inputSchema,
			SDFAttributeList outputSchema,
			SDFAttribute nestingAttribute
		) {
	    this.inputSchema = inputSchema;
	    this.outputSchema = outputSchema;
		this.nestingAttribute = nestingAttribute.clone();
	}

	/**
	 * @param relationalNestPO nesting plan operator to copy
	 */	
	public UnnestPO(UnnestPO<T> unnestPO) {
		super();
		this.inputSchema = unnestPO.getInputSchema();
		this.outputSchema = unnestPO.getOutputSchema();
		this.nestingAttribute = nestingAttribute.clone();
	}

	/*
	 * Getter and setter for copy constructor. 
	 */
	
    public SDFAttributeList getInputSchema() {
        return this.inputSchema;
    }
    
    public SDFAttributeList getOutputSchema() {
        return this.outputSchema;
    }
	
	public SDFAttribute getNestingAttribute() {
		return this.nestingAttribute;
	}	
	
	@Override
	public UnnestPO<T> clone() {
		return new UnnestPO<T>(this);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	final protected void process_next(
	    ObjectRelationalTuple<T> object,
	    int port
	) {
		try {
			System.out.println("RelationalTuple "+this+" "+object);
			// do nothing
			transfer(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Instead of transfering the objects to the subscriber, we return the 
	 * values to assert correctness in a test case.
	 * 
	 * @param object
	 * @param port
	 */
	
	final protected RelationalTuple<T> processNextTest
		(RelationalTuple<T> object, int port) {
	
		return object;
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
}
