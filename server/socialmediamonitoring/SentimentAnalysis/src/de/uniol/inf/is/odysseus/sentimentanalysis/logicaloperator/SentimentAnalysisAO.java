package de.uniol.inf.is.odysseus.sentimentanalysis.logicaloperator;


import java.util.ArrayList;
import java.util.List;






import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.sentimentanalysis.classifier.ClassifierRegistry;
import de.uniol.inf.is.odysseus.sentimentanalysis.stopwords.StopWordsRegistry;


@LogicalOperator(name="SENTIMENTANALYSIS", minInputPorts=2, maxInputPorts=2, category={LogicalOperatorCategory.MINING}, doc="Allows sentiment detection on input streams.")
public class SentimentAnalysisAO extends BinaryLogicalOp{

	private static final long serialVersionUID = -3965539158401817684L;
	private String classifier;
	//private String domain;
	private int maxBufferSize = 60000;

	private int totalInputports = 2;
	
	private SDFAttribute attributeTextToBeClassified;
	private SDFAttribute attributeTrainSetText;	
	private SDFAttribute attributeTrainSetTrueDecision;
	
	private String enrichAttribut = "classification";
		
	public SentimentAnalysisAO(){
		super();	
	}
	
	public SentimentAnalysisAO(SentimentAnalysisAO sentimentAnalysisAO){
        super(sentimentAnalysisAO);
        this.classifier = sentimentAnalysisAO.classifier;
        //this.domain = sentimentDetectionAO.domain;    
        this.maxBufferSize = sentimentAnalysisAO.maxBufferSize; 
        this.attributeTextToBeClassified = sentimentAnalysisAO.attributeTextToBeClassified; 
        this.totalInputports = sentimentAnalysisAO.totalInputports;
        this.attributeTrainSetText =  sentimentAnalysisAO.attributeTrainSetText;	
    	this.attributeTrainSetTrueDecision = sentimentAnalysisAO.attributeTrainSetTrueDecision;
    }
	

	@Override
	public AbstractLogicalOperator clone() {
	     return new SentimentAnalysisAO(this);
	}
	
	@Override 
	public SDFSchema getOutputSchemaIntern(int pos){	
		SDFSchema inSchema = getInputSchema(0);
		
		SDFAttribute sentDetection = new SDFAttribute(null, enrichAttribut ,SDFDatatype.STRING, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();		
		outputAttributes.addAll(inSchema.getAttributes());				
		outputAttributes.add(sentDetection);
		SDFSchema outSchema = new SDFSchema(inSchema, outputAttributes);
		setOutputSchema(outSchema);
		
		return getOutputSchema();
	}
		
/*	@Parameter(name = "domain", type=StringParameter.class, doc="")
	public void setDomain(String domain) {
		this.domain   = domain;
	}
*/		
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
		
	@Parameter(name = "maxBufferSize", type=IntegerParameter.class, optional= true, doc="")
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize   = maxBufferSize;
	}
	
	@Parameter(name = "enrichAttribut", type=StringParameter.class, optional= true, doc="")
	public void setEnrichAttribut(String enrichAttribut) {
		this.enrichAttribut   = enrichAttribut;
	}
	
	public SDFAttribute getAttributeTrainSetText() {
		return attributeTrainSetText;
	}

	public SDFAttribute getAttributeTrainSetTrueDecision() {
		return attributeTrainSetTrueDecision;
	}
	
	public int getMaxBufferSize(){
		return maxBufferSize;
	}

	public SDFAttribute getAttributeTextToBeClassified() {
		return attributeTextToBeClassified;
	}
	
	public String getClassifier(){
		return classifier;
	}
	
/*	public String getDomain(){
		return domain;
	}
*/		
	public String getEnrichAttribut(){
		return enrichAttribut;
	}
	
	public int getTotalInputports(){
		return totalInputports;
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
