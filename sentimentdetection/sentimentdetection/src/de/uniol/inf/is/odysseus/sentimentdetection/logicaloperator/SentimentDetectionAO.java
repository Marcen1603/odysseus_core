package de.uniol.inf.is.odysseus.sentimentdetection.logicaloperator;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;



@LogicalOperator(name="SENTIMENTDETECTION", minInputPorts=1, maxInputPorts=1)
public class SentimentDetectionAO extends UnaryLogicalOp{

	private String outputtype;
	private String classifier;
	
	
	public SentimentDetectionAO(){
		super();
		
	}
	
	public SentimentDetectionAO(SentimentDetectionAO sentimentDetectionAO){
        super(sentimentDetectionAO);
        this.outputtype = sentimentDetectionAO.outputtype;
        this.classifier = sentimentDetectionAO.classifier;
    }
	

	@Override
	public AbstractLogicalOperator clone() {
	     return new SentimentDetectionAO(this);
	}
	
	@Override 
	public SDFSchema getOutputSchemaIntern(int pos){
		
		SDFAttribute sentDetection = new SDFAttribute(null,"decision",SDFDatatype.STRING);
		
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		
		outputAttributes.addAll(getInputSchema(0).getAttributes());
		String name = getInputSchema(0).getURI();
		
		outputAttributes.add(sentDetection);
		
		setOutputSchema(new SDFSchema(name, outputAttributes));
		
		return getOutputSchema();
	}
	
	@Parameter(name="outputtype", type=StringParameter.class)
	public void setOutputType(String outputtype){
		this.outputtype = outputtype;
	}
	
	@Parameter(name="classifier", type=StringParameter.class)
	public void setClassifier(String classifier){
		this.classifier = classifier;
	}
	
	
	public String getOutputType(){
		return outputtype;
	}
	
	public String getClassifier(){
		return classifier;
	}
	
	

}
