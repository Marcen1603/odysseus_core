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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.ClassifierRegistry;
import de.uniol.inf.is.odysseus.sentimentdetection.stopwords.StopWordsRegistry;


@LogicalOperator(name="SENTIMENTDETECTION", minInputPorts=2, maxInputPorts=3)
public class SentimentDetectionAO extends BinaryLogicalOp{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3965539158401817684L;
	private String classifier;
	private int trainSetMinSize = 0;
	private boolean splitDecision = false;
	private String domain;
	private boolean debugClassifier = false;
	private int maxBufferSize = 60000;
	
	
	private int ngram = 1;
	private int outputSchemaPort = 1;
	private int totalInputports = 2;
	
	private boolean removeStopWords = false;
	private boolean stemmWords = false;
	private boolean ngramUpTo = false;
	
	private String attributeTrainSetText;
	private String attributeTrainSetTrueDecision;
	
	private String attributeTestSetText;
	private String attributeTestSetTrueDecision;
	
	private String attributeTextToBeClassifiedText;
	
	
	//Attribute positions
	private int attributeTrainSetTextPos = -1;
	private int attributeTrainSetTrueDecisionPos = -1;
	
	private int attributeTestSetTextPos = -1 ;
	private int attributeTestSetTrueDecisionPos = -1;
	
	private int attributeTextToBeClassifiedPos = -1;
	

	
	
	private String enrichAttribut = "decision";
	private String language = "english";
	
	
	public SentimentDetectionAO(){
		super();
	}
	
	public SentimentDetectionAO(SentimentDetectionAO sentimentDetectionAO){
        super(sentimentDetectionAO);
        this.classifier = sentimentDetectionAO.classifier;
        this.trainSetMinSize = sentimentDetectionAO.trainSetMinSize;
        this.domain = sentimentDetectionAO.domain;
        this.splitDecision = sentimentDetectionAO.splitDecision;
        this.debugClassifier = sentimentDetectionAO.debugClassifier;
        this.removeStopWords = sentimentDetectionAO.removeStopWords;
        this.ngram = sentimentDetectionAO.ngram;
        this.ngramUpTo = sentimentDetectionAO.ngramUpTo;
        this.stemmWords = sentimentDetectionAO.stemmWords;
        this.language = sentimentDetectionAO.language;
     
        this.maxBufferSize = sentimentDetectionAO.maxBufferSize;
 
        this.attributeTrainSetText = sentimentDetectionAO.attributeTrainSetText;
        this.attributeTrainSetTrueDecision = sentimentDetectionAO.attributeTrainSetTrueDecision;
        
        this.attributeTestSetText = sentimentDetectionAO.attributeTestSetText;
        this.attributeTestSetTrueDecision = sentimentDetectionAO.attributeTestSetTrueDecision;
        
        this.attributeTestSetTextPos = sentimentDetectionAO.attributeTestSetTextPos;
        this.attributeTestSetTrueDecisionPos = sentimentDetectionAO.attributeTestSetTrueDecisionPos;
        
        this.attributeTrainSetTextPos = sentimentDetectionAO.attributeTrainSetTextPos;
        this.attributeTrainSetTrueDecisionPos = sentimentDetectionAO.attributeTrainSetTrueDecisionPos;
        
        this.attributeTextToBeClassifiedPos = sentimentDetectionAO.attributeTextToBeClassifiedPos;
        
        this.totalInputports = sentimentDetectionAO.totalInputports;
        
    }
	

	@Override
	public AbstractLogicalOperator clone() {
	     return new SentimentDetectionAO(this);
	}
	
	@Override 
	public SDFSchema getOutputSchemaIntern(int pos){
		
		SDFAttribute sentDetection = new SDFAttribute(null, enrichAttribut ,SDFDatatype.STRING);
		
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		
		outputAttributes.addAll(getInputSchema(outputSchemaPort).getAttributes());
		String name = getInputSchema(outputSchemaPort).getURI();
		
		outputAttributes.add(sentDetection);
		
		setOutputSchema(new SDFSchema(name, getInputSchema(outputSchemaPort).getType(), outputAttributes));
		
		return getOutputSchema();
	}
	
	@Parameter(name = "domain", type=StringParameter.class, doc="")
	public void setDomain(String domain) {
		this.domain   = domain;
	}
	
	@Parameter(name = "trainSetMinSize", type=IntegerParameter.class, doc="")
	public void setMinimumSize(int minimumSize) {
		this.trainSetMinSize   = minimumSize;
	}

	@Parameter(name="splitDecision", type=BooleanParameter.class, optional = true, doc="")
	public void setOutputType(boolean splitDecision){
		this.splitDecision = splitDecision;
	}
	
	@Parameter(name="classifier", type=StringParameter.class, doc="")
	public void setClassifier(String classifier){
		this.classifier = classifier;
	}
	
	@Parameter(name="debugClassifier", type=BooleanParameter.class, optional = true)
	public void setEvaluateClassifier(boolean debugClassifier){
		this.debugClassifier = debugClassifier;
	}

	@Parameter(name="trainSetText", type=StringParameter.class, doc="")
	public void setAttributeTrainSetText(String attributeTrainSetText){
		this.attributeTrainSetText = attributeTrainSetText;
	}
	
	@Parameter(name="trainSetTrueDecision", type=StringParameter.class, doc="")
	public void setAttributeTrainSetTrueDecision(String attributeTrainSetTrueDecision){
		this.attributeTrainSetTrueDecision = attributeTrainSetTrueDecision;
	}
	
	@Parameter(name="testSetTrueDecision", type=StringParameter.class, optional= true ,doc="")
	public void setAttributeTestSetTrueDecision(String attributeTestSetTrueDecision){
		this.attributeTestSetTrueDecision = attributeTestSetTrueDecision;
	}
	
	@Parameter(name="testSetText", type=StringParameter.class, optional= true, doc="")
	public void setAttributeTestSetText(String attributeTestSetText){
		this.attributeTestSetText = attributeTestSetText;
	}
	
	@Parameter(name = "textToBeClassified", type=StringParameter.class, optional= true, doc="")
	public void setToClassifierText(String textToBeClassified) {
		this.attributeTextToBeClassifiedText   = textToBeClassified;
	}
	
	@Parameter(name="language", type=StringParameter.class, optional= true, doc="")
	public void setSanguage(String language){
		this.language = language;
	}
	
	
	@Parameter(name = "ngram", type=IntegerParameter.class, optional= true, doc="")
	public void setNgram(int ngram) {
		this.ngram   = ngram;
	}
	
	
	@Parameter(name = "ngramUpTo", type=BooleanParameter.class, optional= true, doc="")
	public void setNgramUpTo(boolean ngramupto) {
		this.ngramUpTo   = ngramupto;
	}
	
	
	@Parameter(name = "removeStopWords", type=BooleanParameter.class, optional= true, doc="")
	public void setRemoveStopWords(boolean removeStopWords) {
		this.removeStopWords   = removeStopWords;
	}
	
	
	@Parameter(name = "stemmWords", type=BooleanParameter.class, optional= true, doc="")
	public void setStemmWords(boolean stemmWords) {
		this.stemmWords   = stemmWords;
	}
	
	
	@Parameter(name = "maxBufferSize", type=IntegerParameter.class, optional= true, doc="")
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize   = maxBufferSize;
	}
	
	
	@Parameter(name = "enrichAttribut", type=StringParameter.class, optional= true, doc="")
	public void setEnrichAttribut(String enrichAttribut) {
		this.enrichAttribut   = enrichAttribut;
	}
	
	
	
	
	
	
	public String getLanguage(){
		return language;
	}

	public int getMaxBufferSize(){
		return maxBufferSize;
	}
	
	public int getNgram(){
		return ngram;
	}
	
	public boolean getNgramUpTo(){
		return ngramUpTo;
	}
	public boolean getStemmWords(){
		return stemmWords;
	}
	
	public boolean getRemoveStopWords(){
		return removeStopWords;
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
	public boolean getOutputPorts(){
		return splitDecision;
	}
	
	public String getClassifier(){
		return classifier;
	}
	
	public int getMinimumSize(){
		return trainSetMinSize;
	}
	
	public String getDomain(){
		return domain;
	}
	
	public boolean getEvaluateClassifier(){
		return debugClassifier;
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
	
	public String getEnrichAttribut(){
		return enrichAttribut;
	}
	
	public int getAttributeTextToBeClassifiedPos(){
		return attributeTextToBeClassifiedPos;
	}
	
	public int getTotalInputports(){
		return totalInputports;
	}
	
	@Override
	public boolean isValid(){
		
		List<String> validClassifier = ClassifierRegistry.getValidClassifier();
		List<String> validLanguage = StopWordsRegistry.getValidLanguage();
		
		if(!validClassifier.contains(classifier.toLowerCase())){
			addError(new IllegalParameterException(
			"The classifier "+ classifier.toLowerCase()+" could not found."));	
			return false;
		}
		
		
		//NEU
		if(debugClassifier){
			
			// 2. inputs 
			
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
				this.outputSchemaPort = 2;
				this.totalInputports = 3;
				this.attributeTestSetTextPos = getAttributePos(this.getInputSchema(1),attributeTestSetText);
			}else{
				addError(new IllegalParameterException(
						"Attribute: "+ attributeTestSetText +" could not found in the TestSet!"));
				return false;
			}
			
			
			if(getAttributePos(this.getInputSchema(1),attributeTestSetTrueDecision) != -1){
				this.attributeTestSetTrueDecisionPos = getAttributePos(this.getInputSchema(1),attributeTestSetTrueDecision);
			}else{
				addError(new IllegalParameterException(
						"Attribute: "+ attributeTestSetTrueDecision +" could not found in the TestSet!"));
				return false;
			}
			
			
			this.totalInputports = 2;
			this.outputSchemaPort = 1;
			
			
			// 3 inputs ?

		
			SDFSchema toClassifierSchema = this.getInputSchema(2);
			
			if(toClassifierSchema == null && attributeTextToBeClassifiedText != null ){
				addError(new IllegalParameterException(
						"Please define all inputports"));
				return false;
			}
			
			if(toClassifierSchema != null){
				
				if(getAttributePos(toClassifierSchema, attributeTextToBeClassifiedText) != -1){
					
					this.attributeTextToBeClassifiedPos = getAttributePos(toClassifierSchema,attributeTextToBeClassifiedText);
				}else{
					addError(new IllegalParameterException(
							"Attribute: "+ attributeTextToBeClassifiedText +" could not found in the data that are to classifiered!"));
					return false;
				}
				
			this.totalInputports = 3;
			this.outputSchemaPort = 2;
			
			}
			
			
			
			
		}else{
			
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
			
			
			SDFSchema toClassifierSchema = this.getInputSchema(1);
			
			
			if(getAttributePos(toClassifierSchema, attributeTextToBeClassifiedText) != -1){	
				this.attributeTextToBeClassifiedPos = getAttributePos(toClassifierSchema,attributeTextToBeClassifiedText);
			}else{
				addError(new IllegalParameterException(
						"Attribute: "+ attributeTextToBeClassifiedText +" could not found in the data that are to classifiered!"));
				return false;
			}
			
			this.totalInputports = 2;
			this.outputSchemaPort = 1;
			
			
		}
		
		
		if(!validLanguage.contains(language.toLowerCase())){
			addError(new IllegalParameterException(
					"Language: "+ language +" for stopwords not found!"));
			return false;
			
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
