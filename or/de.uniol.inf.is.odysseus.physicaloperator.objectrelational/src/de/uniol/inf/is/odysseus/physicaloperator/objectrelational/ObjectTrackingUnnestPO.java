package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Object Tracking UnnestPO, this one is for MVRelationalTuple processing
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingUnnestPO<T extends IMetaAttribute> extends
		AbstractPipe<ObjectRelationalTuple<T>, ObjectRelationalTuple<T>> {

    private SDFAttributeList inputSchema;
    private SDFAttributeList outputSchema;
	private SDFAttribute nestingAttribute;
		
	private int nestingAttributePos;
	private int nonNestAttributesPos[];
	private int inputAttributesCount;
	private int nestedAttributesCount;
	private int nonNestAttributesCount;
	private int outputAttributesCount;

	/*
     * q is a default min-priority queue for relational tuples according 
     * to their time stamps.
     */
    
    private DefaultTISweepArea<ObjectRelationalTuple<TimeInterval>> q;
    	
	/**
	 * @param toNestAttributes attributes to nest
	 * @param nestingAttribute nesting attribute
	 */
	public ObjectTrackingUnnestPO(
			SDFAttributeList inputSchema,
			SDFAttributeList outputSchema,
			SDFAttribute nestingAttribute
		) {
	    this.inputSchema = inputSchema;
	    this.outputSchema = outputSchema;
		this.nestingAttribute = nestingAttribute.clone();
		
		this.inputAttributesCount = this.inputSchema.getAttributeCount();
		this.outputAttributesCount = this.outputSchema.getAttributeCount();  
		this.nonNestAttributesCount = this.inputAttributesCount - 1;
		
		this.nestedAttributesCount = 
		    this.nestingAttribute.getAmountOfSubattributes();
		
		this.nonNestAttributesPos = new int[this.nonNestAttributesCount]; 
		this.q = new DefaultTISweepArea<ObjectRelationalTuple<TimeInterval>>();
		
		this.calcAttributePos();
	}

	/**
     * @param relationalNestPO nesting plan operator to copy
     */	
    public ObjectTrackingUnnestPO(ObjectTrackingUnnestPO<T> unnestPO) {
    	super();
    	this.inputSchema = unnestPO.getInputSchema();
    	this.outputSchema = unnestPO.getOutputSchema();
    	this.nestingAttribute = nestingAttribute.clone();
    	this.q = unnestPO.q.clone();
    }

    /**
     * Instead of transferring the objects to the subscriber, we return the 
     * values to assert correctness in a test case.
     * 
     * @param object
     * @param port
     */
    
    final public void processNextTest
    	(ObjectRelationalTuple<TimeInterval> input, int port) {

        int index;
        Object[] outputValues;
        ObjectRelationalTuple<TimeInterval> outputTuple;        
        
        index = 0;
        outputValues = new Object[this.outputAttributesCount];
        
        ObjectRelationalTuple<TimeInterval>[] subTuples = 
            this.unnest(input);
        
        for(int i = 0; i < subTuples.length; i++) {
            for(index = 0; index < this.nonNestAttributesCount; index++) {               
                outputValues[index] = 
                    input.getAttribute(this.nonNestAttributesPos[index]);
            }
            for(int k = 0; k < this.nestedAttributesCount; k++) {
                outputValues[index] = subTuples[i].getAttribute(k);
                index++;
            }
            outputTuple = new ObjectRelationalTuple<TimeInterval>(
                this.outputSchema,
                outputValues
            );
            outputTuple.setMetadata(
                subTuples[i].getMetadata().clone()
            );
            this.q.insert(outputTuple.clone());
        }
    }
    
    public ObjectRelationalTuple<TimeInterval> deliver() {
        return this.q.poll();
    }
    
    public boolean isDone() {
        return this.q.size() == 0;
    }
    
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
    public ObjectTrackingUnnestPO<T> clone() {
    	return new ObjectTrackingUnnestPO<T>(this);
    }

    @Override
    public OutputMode getOutputMode() {
    	return OutputMode.MODIFIED_INPUT;
    }

    @Override
    public void processPunctuation(PointInTime timestamp, int port) {
    	sendPunctuation(timestamp);
    }

    @Override
    final protected void process_next(
        ObjectRelationalTuple<T> tuple,
        int port
    ) {
    	try {			
    		// do nothing
    		transfer(tuple);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    private void calcAttributePos() {
        int index;
	    String nestingAttrName;
	    
	    index = 0;
	    nestingAttrName = this.nestingAttribute.getAttributeName();	    
	    
        for(int i = 0; i < this.inputAttributesCount; i++) {
            if(nestingAttrName == this.inputSchema.get(i).getAttributeName()) 
                this.nestingAttributePos = i;
            else {
                this.nonNestAttributesPos[index] = i;
                index++;
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private ObjectRelationalTuple<TimeInterval>[] unnest(
        ObjectRelationalTuple<TimeInterval> input
    ) {
        int setLength;
        SetEntry[] set;
        ObjectRelationalTuple[] output; 
        
        set = (SetEntry[]) input.getAttribute(this.nestingAttributePos);
        setLength = set.length;
        output = new ObjectRelationalTuple[set.length];
        
        for(int i = 0; i < setLength; i++) {
            output[i] = (ObjectRelationalTuple<TimeInterval>) set[i].getValue();
        }
        
        return output;
    }
}
