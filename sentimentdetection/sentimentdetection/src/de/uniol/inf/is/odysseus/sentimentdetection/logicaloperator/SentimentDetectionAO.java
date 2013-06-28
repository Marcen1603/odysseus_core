package de.uniol.inf.is.odysseus.sentimentdetection.logicaloperator;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;



@LogicalOperator(name="SENTIMENTDETECTION", minInputPorts=2, maxInputPorts=2)
public class SentimentDetectionAO extends BinaryLogicalOp{

	private String classifier;
	private int minimumSize = 0;
	private int outputports = 1;
	private String domain;
	private int evaluateClassifier = 0;
	
	public SentimentDetectionAO(){
		super();
	}
	
	public SentimentDetectionAO(SentimentDetectionAO sentimentDetectionAO){
        super(sentimentDetectionAO);
        this.outputports = sentimentDetectionAO.outputports;
        this.classifier = sentimentDetectionAO.classifier;
        this.minimumSize = sentimentDetectionAO.minimumSize;
        this.domain = sentimentDetectionAO.domain;
        this.evaluateClassifier = sentimentDetectionAO.evaluateClassifier;
    }
	

	@Override
	public AbstractLogicalOperator clone() {
	     return new SentimentDetectionAO(this);
	}
	
	@Override 
	public SDFSchema getOutputSchemaIntern(int pos){
		
		SDFAttribute sentDetection = new SDFAttribute(null,"decision",SDFDatatype.STRING);
		
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		
		outputAttributes.addAll(getInputSchema(1).getAttributes());
		String name = getInputSchema(1).getURI();
		
		outputAttributes.add(sentDetection);
		
		setOutputSchema(new SDFSchema(name, outputAttributes));
		
		return getOutputSchema();
	}
	
	
	@Parameter(name = "domain", type=StringParameter.class, doc="")
	public void setDomain(String domain) {
		this.domain   = domain;
	}
	
	@Parameter(name = "minimumSize", type=IntegerParameter.class, doc="")
	public void setMinimumSize(int minimumSize) {
		this.minimumSize   = minimumSize;
	}

	@Parameter(name="outputports", type=IntegerParameter.class, optional = true, doc="")
	public void setOutputType(int outputports){
		this.outputports = outputports;
	}
	
	@Parameter(name="classifier", type=StringParameter.class, doc="")
	public void setClassifier(String classifier){
		this.classifier = classifier;
	}
	
	@Parameter(name="evaluateClassifier", type=IntegerParameter.class, optional = true)
	public void setEvaluateClassifier(int evaluateClassifier){
		this.evaluateClassifier = evaluateClassifier;
	}

	public int getOutputPorts(){
		return outputports;
	}
	
	public String getClassifier(){
		return classifier;
	}
	
	public int getMinimumSize(){
		return minimumSize;
	}
	
	public String getDomain(){
		return domain;
	}
	
	public int getEvaluateClassifier(){
		return evaluateClassifier;
	}
	

}
