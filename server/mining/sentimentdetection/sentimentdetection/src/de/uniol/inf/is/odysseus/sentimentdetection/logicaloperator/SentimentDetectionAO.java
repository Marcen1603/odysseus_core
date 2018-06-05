package de.uniol.inf.is.odysseus.sentimentdetection.logicaloperator;


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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.ClassifierRegistry;
import de.uniol.inf.is.odysseus.sentimentdetection.stopwords.StopWordsRegistry;


@LogicalOperator(name="SENTIMENTDETECTION", minInputPorts=2, maxInputPorts=3, category={LogicalOperatorCategory.MINING}, doc="Allows sentiment detection on input streams.")
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
	
	private SDFAttribute attributeTrainSet;
	private SDFAttribute attributeTrainSetTrueDecision;
	
	private SDFAttribute attributeTestSet;
	private SDFAttribute attributeTestSetTrueDecision;
	
	private SDFAttribute attributeTextToBeClassified;
	
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
 
        this.attributeTrainSet = sentimentDetectionAO.attributeTrainSet;
        this.attributeTrainSetTrueDecision = sentimentDetectionAO.attributeTrainSetTrueDecision;
        
        this.attributeTestSet = sentimentDetectionAO.attributeTestSet;
        this.attributeTestSetTrueDecision = sentimentDetectionAO.attributeTestSetTrueDecision;
        
        this.attributeTextToBeClassified = sentimentDetectionAO.attributeTextToBeClassified;
        
        this.totalInputports = sentimentDetectionAO.totalInputports;
   
    }
	

	@Override
	public AbstractLogicalOperator clone() {
	     return new SentimentDetectionAO(this);
	}
	
	@Override 
	public SDFSchema getOutputSchemaIntern(int pos){
		// add detected sentiment to schema 		
		SDFSchema inSchema = getInputSchema(outputSchemaPort);
		SDFAttribute sentDetection = new SDFAttribute(null, enrichAttribut ,SDFDatatype.STRING, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();		
		outputAttributes.addAll(inSchema.getAttributes());				
		outputAttributes.add(sentDetection);
		SDFSchema outSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inSchema); 
		setOutputSchema(outSchema);
		
		if(totalInputports == 3){
			List<SDFAttribute> outputAttributesneu = new ArrayList<SDFAttribute>();
			outputAttributesneu.addAll(getInputSchema(1).getAttributes());
			outputAttributesneu.add(sentDetection);

			setOutputSchema(2, SDFSchemaFactory.createNewWithAttributes(outputAttributesneu, getInputSchema(1)));
		}
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

	@Parameter(name="trainSetText", type=ResolvedSDFAttributeParameter.class, doc="")
	public void setAttributeTrainSetText(SDFAttribute attributeTrainSetText){
		this.attributeTrainSet = attributeTrainSetText;
	}
	
	@Parameter(name="trainSetTrueDecision", type=ResolvedSDFAttributeParameter.class, doc="")
	public void setAttributeTrainSetTrueDecision(SDFAttribute attributeTrainSetTrueDecision){
		this.attributeTrainSetTrueDecision = attributeTrainSetTrueDecision;
	}
	
	@Parameter(name="testSetTrueDecision", type=ResolvedSDFAttributeParameter.class, optional= true ,doc="")
	public void setAttributeTestSetTrueDecision(SDFAttribute attributeTestSetTrueDecision){
		this.attributeTestSetTrueDecision = attributeTestSetTrueDecision;
	}
	
	@Parameter(name="testSetText", type=ResolvedSDFAttributeParameter.class, optional= true, doc="")
	public void setAttributeTestSetText(SDFAttribute attributeTestSetText){
		this.attributeTestSet = attributeTestSetText;
	}
	
	@Parameter(name = "textToBeClassified", type=ResolvedSDFAttributeParameter.class, optional= true, doc="")
	public void setToClassifierText(SDFAttribute textToBeClassified) {
		this.attributeTextToBeClassified   = textToBeClassified;
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
	
	public SDFAttribute getAttributeTestSetTrueDecision(){
		return attributeTestSetTrueDecision;
	}
	
	public SDFAttribute getAttributeTestSetText(){
		return attributeTestSet;
	}

	public SDFAttribute getAttributeTrainSetTrueDecision(){
		return attributeTrainSetTrueDecision;
	}
	
	public SDFAttribute getAttributeTrainSetText(){
		return attributeTrainSet;
	}
	
	public SDFAttribute getAttributeTextToBeClassified() {
		return attributeTextToBeClassified;
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
		
		List<String> validClassifier = ClassifierRegistry.getValidClassifier();
		List<String> validLanguage = StopWordsRegistry.getValidLanguage();
		
		if(!validClassifier.contains(classifier.toLowerCase())){
			addError(
			"The classifier "+ classifier.toLowerCase()+" could not found.");	
			return false;
		}
		
		
		//NEU
		if(debugClassifier){
			
			// 2. inputs 
		
			
			this.totalInputports = 2;
			this.outputSchemaPort = 1;
			
			
			// 3 inputs ?

		
			SDFSchema toClassifierSchema = this.getInputSchema(2);
			
			if(toClassifierSchema == null && attributeTextToBeClassified != null ){
				addError(
						"Please define all inputports");
				return false;
			}
			
			if(toClassifierSchema != null){
				
				
			this.totalInputports = 3;
			this.outputSchemaPort = 2;
			
			}
			
			
			
			
		}else{
			
			
			
			this.totalInputports = 2;
			this.outputSchemaPort = 1;
			
			
		}
		
		
		if(!validLanguage.contains(language.toLowerCase())){
			addError(
					"Language: "+ language +" for stopwords not found!");
			return false;
			
		}
	 

		return true;
	}
	
	

	
	
}
