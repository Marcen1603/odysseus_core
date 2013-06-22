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
	
	private int wrongdecision=0;
	
	private String outputtype;
	private String classifier;
	private String trainingset;

	private IClassifier<T> algo;

	public SentimentDetectionPO() {
		super();
	}
	
	public SentimentDetectionPO(String outputtype, String classifier, String trainingset) {
		super();
		this.outputtype = outputtype;
		this.classifier = classifier;
		this.trainingset = trainingset;
	}

	public SentimentDetectionPO(SentimentDetectionPO<T> senti) {
		super(senti);
		this.outputtype = senti.outputtype;
		this.classifier = senti.classifier;
		this.trainingset = senti.trainingset;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		algo = (IClassifier<T>) ClassifierRegistry.getClassifierByName(classifier.toLowerCase());
		System.out.println("Classifier wird initialisiert....");
		algo.trainClassifier(trainingset);
	
	}
	

	@Override
	@SuppressWarnings("unchecked")
	protected void process_next(Tuple object, int port) {

		//System.out.println("outputType is: "+ outputtype);
		//get inputSize of the object	
		int inputSize = object.size();
		
		//create new output Tuple with size + 1 
		Tuple outputTuple = new Tuple( object.size()+1, false);
		
		//Copy object Attributes to the new outputTuple 
		System.arraycopy(object.getAttributes(), 0, outputTuple.getAttributes(), 0, inputSize);
	
		//text positive or negative
		//String erg = detect(object.getAttribute(0).toString());
		
		int decision  = algo.startDetect(object.getAttribute(0).toString());
	
		//get OutputPort 
		int outputPort = getOutPutPort(decision);
		
		//set the decision Attribute 
		outputTuple.setAttribute(object.size(), decision);
	
		//calculate error
		String truedecision = outputTuple.getAttribute(object.size()-1).toString();
	
		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());
	
		System.out.println("record: "+ object.getAttribute(0).toString());
		System.out.println("true decision: "+ truedecision);
		System.out.println("decision: "+ decision);
		System.out.println("wrong: "+ wrongdecision);
		
		if(Integer.parseInt(truedecision.trim()) != decision ){
			wrongdecision++;
		}
		
		//System.out.println("Error:" + 1.0 * wrongdecision / 1062 );
		//System.out.println("Alte Objekt ist: "+ object.toString());
		//System.out.println("Neues Objekt ist: "+ outputTuple.toString());
	
		ctr++;
		
		transfer(outputTuple,outputPort);
	
	}

	
	@Override
	public SentimentDetectionPO<T> clone() {
		return new SentimentDetectionPO<T>(this);
	}
	

	
	/*
	 * Default port is 0
	 * outputport is set to two 
	 * transfer positive to port 0
	 * transfer negative to port 1
	 */
	private int getOutPutPort(int decision){
		
		int outputPort = 0 ;
		
		if(outputtype.equals("two")){
			if(decision == 1){
				//System.out.println("Ausgabe an Port 0:"+ outputtype);
				outputPort = 0;
			}else{
				//System.out.println("Ausgabe an Port 1:" + outputtype);
				outputPort = 1;
			}
		}
		
		return outputPort;
	}

}
