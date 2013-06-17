package de.uniol.inf.is.odysseus.sentimentdetection.physicaloperator;


import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

@SuppressWarnings({ "rawtypes" })
public class SentimentDetectionPO<T extends IMetaAttribute> extends AbstractPipe<Tuple <T>,Tuple <T>> {
	
	private static int ctr = 0;

	public SentimentDetectionPO() {
	}

	public SentimentDetectionPO(SentimentDetectionPO<T> senti) {
		super(senti);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void process_next(Tuple object, int port) {
		//get inputSize of the object	
		int inputSize = object.size();
		
		//create new output Tuple with size + 1 
		Tuple outputTuple = new Tuple( object.size()+1, false);
		
		//Copy object Attributes to the new outputTuple 
		System.arraycopy(object.getAttributes(), 0, outputTuple.getAttributes(), 0, inputSize);
	
		//text positive or negative
		String erg = detect(object.getAttribute(0).toString());
		
		//set the decision Attribute 
		outputTuple.setAttribute(object.size(), erg);
			
		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());
	
		//Test Start
		System.out.println("Alte Objekt ist: "+ object.toString());
		System.out.println("Neues Objekt ist: "+ outputTuple.toString());
		ctr++;
		//Test End
		
		//Transfer to the port 0
		transfer(outputTuple,0);
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
		
		System.out.println("Der Satz wird analysiert: " + inputText);
		
		String erg = "";
		
		if(ctr % 2 == 0 ){
			 erg = "positive";
		}else{
			 erg = "negative";
		}
	
		return erg;
	}

}
