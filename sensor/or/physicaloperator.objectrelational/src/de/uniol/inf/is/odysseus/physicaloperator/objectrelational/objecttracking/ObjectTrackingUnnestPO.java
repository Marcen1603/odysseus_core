package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Object Tracking UnnestPO, this one is for MVRelationalTuple processing
 * for testing the unnest po please use the test suite of 
 * 
 * /de.uniol.inf.is.odysseus.physicaloperator.objectrelational/src/de/uniol/
 * inf/is/odysseus/physicaloperator/objectrelational/objecttracking/test/
 * unnest/UnnestPOAllTests.java
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingUnnestPO
	<T extends ObjectTrackingMetadata<Object>> extends
	AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	/*
	 * For processing internally, only the super class SDFAttributeList
	 * is needed. The AO is dealing with SDFAttributeListExtended.  
	 */
	
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
    
    private DefaultTISweepArea<MVRelationalTuple<T>> q;
    	
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
		
		this.q = new DefaultTISweepArea<MVRelationalTuple<T>>();
		
		this.calcAttributePos();
	}

	/**
     * @param relationalNestPO nesting plan operator to copy
     */	
    public ObjectTrackingUnnestPO(ObjectTrackingUnnestPO<T> unnestPO) {
    	super(unnestPO);
    	this.inputSchema = unnestPO.getInputSchema();
    	this.outputSchema = unnestPO.getOutputSchema();
    	this.nestingAttribute = nestingAttribute.clone();
    	this.q = unnestPO.q.clone();
    }
    
    public void process_done() {
    	System.out.println(
    		"ObjectTrackingUnnestPO done: " + 
    		System.nanoTime()
    	);
    }
    
    protected void process_close() {
    	System.out.println(
    		"ObjectTrackingUnnestPO closed: " + 
    		System.nanoTime()
    	);
    }
    
    @SuppressWarnings("unchecked")
	protected void process_next(
    	RelationalTuple<T> inputrel, 
    	int port
    ) {    	    	
    	
    	MVRelationalTuple<T> input = new MVRelationalTuple(inputrel.getAttributes());
 
    	
        int index;
        Object[] outputValues;
        MVRelationalTuple<T> outputTuple;        
        
        index = 0;
        outputValues = new Object[this.outputAttributesCount];
        
        MVRelationalTuple<T>[] subTuples = 
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
            
            outputTuple = 
            	new MVRelationalTuple<T>(              
            			outputValues
            	);                  
            
            outputTuple.setMetadata(
                (T) subTuples[i].getMetadata().clone()
            );
            
            q.insert(outputTuple);                         
        }   
           
        /* 
         * The min-priority queue q is polling tuple with the smallest 
         * timestamp first. 
         */
            
        Iterator<MVRelationalTuple<T>> old = q.iterator();
        
        while(old.hasNext()) {
        	MVRelationalTuple<T> oldTuple = old.next();
        	old.remove();
        	this.transfer(oldTuple);        	
        }      
    } 
    
    /**
     * Instead of transferring the objects to the subscriber, we return the 
     * values to assert correctness in a test case.
     * 
     * @param object
     * @param port
     */
	final public void processNextTest(
    	MVRelationalTuple<T> input, 
    	int port
    ) {
    	this.process_next(input, port);
    }
    
    public MVRelationalTuple<T> deliver() {
        return this.q.poll();
    }
    
    public boolean isDone() {
       	return (this.q.size() == 0);
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
    private MVRelationalTuple<T>[] unnest(
        MVRelationalTuple<T> input
    ) {
        int setLength;
        SetEntry[] set;
        MVRelationalTuple[] output; 
        
        set = (SetEntry[]) input.getAttribute(this.nestingAttributePos);
        setLength = set.length;
        output = new MVRelationalTuple[set.length];
        
        for(int i = 0; i < setLength; i++) {
            output[i] = (MVRelationalTuple<ObjectTrackingMetadata<Object>>) set[i].getValue();
        }
        
        return output;
    }
}
