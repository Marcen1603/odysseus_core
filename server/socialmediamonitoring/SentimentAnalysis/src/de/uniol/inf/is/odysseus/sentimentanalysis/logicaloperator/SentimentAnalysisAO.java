package de.uniol.inf.is.odysseus.sentimentanalysis.logicaloperator;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;


@LogicalOperator(name="SENTIMENTANALYSIS", minInputPorts=2, maxInputPorts=2, category={LogicalOperatorCategory.MINING}, doc="Allows sentiment detection on input streams.")
public class SentimentAnalysisAO extends BinaryLogicalOp{

	private static final long serialVersionUID = -3965539158401817684L;
	private String classifier;
	private int maxTrainSize = 5;

	private int totalInputports = 2;
	
	private SDFAttribute attributeTextToBeClassified;
	private SDFAttribute attributeTrainSetText;	
	private SDFAttribute attributeTrainSetTrueDecision;
	
	private String enrichAttribut = "classification";
	private List<String> nominals = new ArrayList<String>();
	private double thresholdValue;
	
		
	public SentimentAnalysisAO(){
		super();	
	}
	
	public SentimentAnalysisAO(SentimentAnalysisAO sentimentAnalysisAO){
        super(sentimentAnalysisAO);
        this.classifier = sentimentAnalysisAO.classifier;
        this.attributeTextToBeClassified = sentimentAnalysisAO.attributeTextToBeClassified; 
        this.totalInputports = sentimentAnalysisAO.totalInputports;
        this.attributeTrainSetText =  sentimentAnalysisAO.attributeTrainSetText;	
    	this.attributeTrainSetTrueDecision = sentimentAnalysisAO.attributeTrainSetTrueDecision;
    	this.nominals = sentimentAnalysisAO.nominals;
    	this.maxTrainSize = sentimentAnalysisAO.maxTrainSize;
    }
	

	@Override
	public AbstractLogicalOperator clone() {
	     return new SentimentAnalysisAO(this);
	}
	
	@Override 
	public SDFSchema getOutputSchemaIntern(int pos){	
		SDFSchema inSchema = getInputSchema(0);
		
		SDFAttribute sentAnalysis = new SDFAttribute(null, enrichAttribut ,SDFDatatype.STRING, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();		
		outputAttributes.addAll(inSchema.getAttributes());				
		outputAttributes.add(sentAnalysis);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema);
		setOutputSchema(outSchema);
		
		return getOutputSchema();
	}
			
	@Parameter(name = "maxTrainSize", type=IntegerParameter.class,  doc="")
	public void setMaxTrainSize(int maxTrainSize) {
		this.maxTrainSize = maxTrainSize;
	}
	
	@Parameter(name = "thresholdValue", type=DoubleParameter.class,  doc="")
	public void setThresholdValue(double thresholdValue) {
		this.thresholdValue = thresholdValue;
		System.out.println("Threshold: " + this.thresholdValue);
	}
	
	@Parameter(name = "nominals", type=StringParameter.class, isList = true,  doc="")
	public void setNominals(List<String> nominals) {
		this.nominals   = nominals;
	}
	
	@Parameter(name="classifier", type=StringParameter.class, optional=true, doc="")
	public void setClassifier(String classifier){
		if(classifier == null)
		{
			this.classifier = "NaiveBayes";
		}
		else
		{
			this.classifier = classifier;
		}
	}
	
	@Parameter(name = "attributeTrainSetTrueDecision", type=ResolvedSDFAttributeParameter.class, doc="")
	public void setAttributeTrainSetTrueDecision(SDFAttribute attributeTrainSetTrueDecision) {
		this.attributeTrainSetTrueDecision   = attributeTrainSetTrueDecision;
	}
	
	@Parameter(name = "attributeTrainSetText", type=ResolvedSDFAttributeParameter.class, doc="")
	public void setAttributeTrainSetText(SDFAttribute attributeTrainSetText) {
		this.attributeTrainSetText   = attributeTrainSetText;
	}
	
	@Parameter(name = "textToBeClassified", type=ResolvedSDFAttributeParameter.class, doc="")
	public void setToClassifierText(SDFAttribute textToBeClassified) {
		this.attributeTextToBeClassified   = textToBeClassified;
	}
		
	public SDFAttribute getAttributeTrainSetText() {
		return attributeTrainSetText;
	}

	public SDFAttribute getAttributeTrainSetTrueDecision() {
		return attributeTrainSetTrueDecision;
	}

	public SDFAttribute getAttributeTextToBeClassified() {
		return attributeTextToBeClassified;
	}
	
	public String getClassifier(){
		return classifier;
	}
	
	public List<String> getNominals(){
		return this.nominals;
	}
	
	public int getTotalInputports(){
		return totalInputports;
	}
	
	public int getMaxTrainSize(){
		return this.maxTrainSize;
	}
	
	public double getThresholdValue()
	{
		return this.thresholdValue;
	}
	
	@Override
	public void initialize() {	
	}
	
	@Override
	public boolean isValid(){
		
		if(this.totalInputports != 2)
			return false;
		
		return true;
	}
}
