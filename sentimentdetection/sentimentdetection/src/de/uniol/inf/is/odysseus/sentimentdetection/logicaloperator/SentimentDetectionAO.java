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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;



@LogicalOperator(name="SENTIMENTDETECTION", minInputPorts=2, maxInputPorts=2)
public class SentimentDetectionAO extends BinaryLogicalOp{

	private String classifier;
	private int minimumSize = 0;
	private int outputports = 1;
	private String domain;
	private int evaluateClassifier = 0;
	
	private String attributeTrainSetText;
	private String attributeTrainSetTrueDecision;
	
	private String attributeTestSetText;
	private String attributeTestSetTrueDecision;
	
	
	//Attribute positions
	private int attributeTrainSetTextPos = -1;
	private int attributeTrainSetTrueDecisionPos = -1;
	
	private int attributeTestSetTextPos = -1 ;
	private int attributeTestSetTrueDecisionPos = -1;
	
	
	
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
 
        this.attributeTrainSetText = sentimentDetectionAO.attributeTrainSetText;
        this.attributeTrainSetTrueDecision = sentimentDetectionAO.attributeTrainSetTrueDecision;
        
        this.attributeTestSetText = sentimentDetectionAO.attributeTestSetText;
        this.attributeTestSetTrueDecision = sentimentDetectionAO.attributeTestSetTrueDecision;
        
        this.attributeTestSetTextPos = sentimentDetectionAO.attributeTrainSetTextPos;
        this.attributeTestSetTrueDecisionPos = sentimentDetectionAO.attributeTestSetTrueDecisionPos;
        
        this.attributeTrainSetTextPos = sentimentDetectionAO.attributeTrainSetTextPos;
        this.attributeTrainSetTrueDecisionPos = sentimentDetectionAO.attributeTrainSetTrueDecisionPos;
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

	@Parameter(name="attributeTrainSetText", type=StringParameter.class, doc="")
	public void setAttributeTrainSetText(String attributeTrainSetText){
		this.attributeTrainSetText = attributeTrainSetText;
	}
	
	@Parameter(name="attributeTrainSetTrueDecision", type=StringParameter.class, doc="")
	public void setAttributeTrainSetTrueDecision(String attributeTrainSetTrueDecision){
		this.attributeTrainSetTrueDecision = attributeTrainSetTrueDecision;
	}
	
	@Parameter(name="attributeTestSetTrueDecision", type=StringParameter.class, optional= true ,doc="")
	public void setAttributeTestSetTrueDecision(String attributeTestSetTrueDecision){
		this.attributeTestSetTrueDecision = attributeTestSetTrueDecision;
	}
	
	@Parameter(name="attributeTestSetText", type=StringParameter.class, doc="")
	public void setAttributeTestSetText(String attributeTestSetText){
		this.attributeTestSetText = attributeTestSetText;
	}
	
	public String getAttributeTestSetTrueDecision(){
		return attributeTestSetTrueDecision;
	}
	
	public String getAttributeTestSetText(){
		return attributeTestSetText;
	}

	public String getAttributeTrainSetTrueDecision(){
		return attributeTrainSetTrueDecision;
	}
	
	public String getAttributeTrainSetText(){
		return attributeTrainSetText;
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
	
	
	public int getAttributeTrainSetTextPos(){
		return attributeTrainSetTextPos;
	}
	
	public int getAttributeTrainSetTrueDecisionPos(){
		return attributeTrainSetTrueDecisionPos;
	}
	
	
	public int getAttributeTestSetTextPos(){
		return attributeTestSetTextPos;
	}
	
	public int getAttributeTestSetTrueDecisionPos(){
		return attributeTestSetTrueDecisionPos;
	}
	
	@Override
	public boolean isValid(){
		
		if(getAttributePos(this.getInputSchema(0),attributeTrainSetText) != -1){
			this.attributeTrainSetTextPos = getAttributePos(this.getInputSchema(0),attributeTrainSetText);
		}else{
			addError(new IllegalParameterException(
					"Attribute: "+ attributeTrainSetText +" could not found in the TrainingSet!"));
			return false;
		}
		
		if(getAttributePos(this.getInputSchema(0),attributeTrainSetTrueDecision) != -1){
			this.attributeTrainSetTrueDecisionPos = getAttributePos(this.getInputSchema(0),attributeTrainSetTrueDecision);
		}else{
			addError(new IllegalParameterException(
					"Attribute: "+ attributeTrainSetTrueDecision +" could not found in the TrainingSet!"));
			return false;
		}
		
		
		if(getAttributePos(this.getInputSchema(1),attributeTestSetText) != -1){
			this.attributeTestSetTextPos = getAttributePos(this.getInputSchema(1),attributeTestSetText);
		}else{
			addError(new IllegalParameterException(
					"Attribute: "+ attributeTestSetText +" could not found in the TestSet!"));
			return false;
		}
		
		
		if(evaluateClassifier == 1){
			if(attributeTestSetTrueDecision == null){
				addError(new IllegalParameterException(
						"For debugging, the attribute attribueTestSetTrueDecision must be specified!"));
				return false;
			}
			
			if(getAttributePos(this.getInputSchema(1),attributeTestSetTrueDecision) != -1){
				this.attributeTestSetTrueDecisionPos = getAttributePos(this.getInputSchema(1),attributeTestSetTrueDecision);
			}else{
				addError(new IllegalParameterException(
						"Attribute: "+ attributeTestSetTrueDecision +" could not found in the TestSet!"));
				return false;
			}
		}

		return true;
	}
	
	public int getAttributePos(SDFSchema schema, String attribute){
		int pos = -1;
		int i = 0;
		for (SDFAttribute a : schema) {
			//System.out.println(a.getAttributeName());
			if(a.getAttributeName().equals(attribute)){
				pos = i;
			}
			i++;
		}
		
		return pos;
	}

}
