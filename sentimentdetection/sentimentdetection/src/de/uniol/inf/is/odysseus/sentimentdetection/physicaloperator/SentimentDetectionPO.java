package de.uniol.inf.is.odysseus.sentimentdetection.physicaloperator;


import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

import de.uniol.inf.is.odysseus.sentimentdetection.classifier.ClassifierRegistry;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.IClassifier;

@SuppressWarnings({ "rawtypes" })
public class SentimentDetectionPO<T extends IMetaAttribute> extends AbstractPipe<Tuple <T>,Tuple <T>> {
	
	private static int ctr = 0;
	
	private String outputtype;
	private String classifier;

	private IClassifier<T> algo;

	public SentimentDetectionPO() {
		super();
	}
	
	public SentimentDetectionPO(String outputtype, String classifier) {
		super();
		this.outputtype = outputtype;
		this.classifier = classifier;
	}

	public SentimentDetectionPO(SentimentDetectionPO<T> senti) {
		super(senti);
		this.outputtype = senti.outputtype;
		this.classifier = senti.classifier;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		algo = (IClassifier<T>) ClassifierRegistry.getClassifierByName(classifier.toLowerCase());
		
	
	}
	

	

	@Override
	@SuppressWarnings("unchecked")
	protected void process_next(Tuple object, int port) {
		
		
		
		System.out.println("Es wurde folgender OutputType gesetzt: "+ outputtype);
		//get inputSize of the object	
		int inputSize = object.size();
		
		//create new output Tuple with size + 1 
		Tuple outputTuple = new Tuple( object.size()+1, false);
		
		//Copy object Attributes to the new outputTuple 
		System.arraycopy(object.getAttributes(), 0, outputTuple.getAttributes(), 0, inputSize);
	
		//text positive or negative
		//String erg = detect(object.getAttribute(0).toString());
		
		String erg = algo.startDetect(object.getAttribute(0).toString());
	
		//get OutputPort 
		int outputPort = getOutPutPort(erg);
		
		//set the decision Attribute 
		outputTuple.setAttribute(object.size(), erg);
			
		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());
	
		//Test Start
		System.out.println("Alte Objekt ist: "+ object.toString());
		System.out.println("Neues Objekt ist: "+ outputTuple.toString());
		ctr++;
		//Test End
		
		transfer(outputTuple,outputPort);
	
	}

	
	@Override
	public SentimentDetectionPO<T> clone() {
		return new SentimentDetectionPO<T>(this);
	}
	
	/*
	 * SentimentDetection
	 * 
	 */
	private String detect(String inputText){
		
    String erg = "";
    
		if(classifier.equals("naivebayes")){
			
			System.out.println("Der Satz wird analysiert: " + inputText);
			
			if(ctr % 2 == 0 ){
				 erg = "positive";
			}else{
				 erg = "negative";
			}
		
		}
	
		return erg;
	}
	
	/*
	 * Default port is 0
	 * outputport is set to two 
	 * transfer positive to port 0
	 * transfer negative to port 1
	 */
	private int getOutPutPort(String erg){
		
		int outputPort = 0 ;
		
		if(outputtype.equals("two")){
			if(erg.equals("positive")){
				System.out.println("Ausgabe an Port 0:"+ outputtype);
				outputPort = 0;
			}else{
				System.out.println("Ausgabe an Port 1:" + outputtype);
				outputPort = 1;
			}
		}
		
		return outputPort;
	}

}
