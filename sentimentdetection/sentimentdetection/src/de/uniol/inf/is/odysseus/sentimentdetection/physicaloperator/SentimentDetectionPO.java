package de.uniol.inf.is.odysseus.sentimentdetection.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

import de.uniol.inf.is.odysseus.sentimentdetection.classifier.ClassifierRegistry;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.IClassifier;
import de.uniol.inf.is.odysseus.sentimentdetection.util.Metrics;

@SuppressWarnings({ "rawtypes" })
public class SentimentDetectionPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	//operator parameter
	private boolean splitDecision;
	private String classifier;
	private int trainSetMinSize;
	private String domain;
	private boolean debugClassifier = false;

	//variable for debug
	private int posCtr = 0;
	private int totalPosCtr = 0;
	private int totalExistPosCtr = 0;
	
	private int negCtr = 0;
	private int totalNegCtr = 0;
	private int totalExistNegCtr = 0;
	
	private int wrongdecision = 0;
	
	//help variable
	private static int ctr = 0;
	private boolean isTrained = false;
	
	//currend classifier
	private IClassifier<T> algo;
	
	//buffer 
	private List<Tuple> buffer = new ArrayList<>();
	private Map<String, Integer> trainingset = new HashMap<String, Integer>();
	
	
	//attribute positions
	private int attributeTrainSetTextPos = -1;
	private int attributeTrainSetTrueDecisionPos = -1;
	
	private int attributeTestSetTextPos = -1 ;
	private int attributeTestSetTrueDecisionPos = -1;


	public SentimentDetectionPO() {
		super();
	}

	public SentimentDetectionPO(boolean splitDecision, String classifier,
			int trainSetMinSize, 
			String domain, 
			boolean debugClassifier, 
			int attributeTrainSetTextPos,
			int attributeTrainSetTrueDecisionPos,
			int attributeTestSetTextPos,
			int attributeTestSetTrueDecisionPos) {
		
		super();
		
		this.splitDecision = splitDecision;
		this.classifier = classifier;
		this.trainSetMinSize = trainSetMinSize;
		this.domain = domain;
		this.debugClassifier = debugClassifier;
		
		this.attributeTrainSetTextPos = attributeTrainSetTextPos;
		this.attributeTrainSetTrueDecisionPos = attributeTrainSetTrueDecisionPos;
		
		this.attributeTestSetTextPos = attributeTestSetTextPos;
		this.attributeTestSetTrueDecisionPos = attributeTestSetTrueDecisionPos;
	}

	public SentimentDetectionPO(SentimentDetectionPO<T> senti) {
		super(senti);
		this.splitDecision = senti.splitDecision;
		this.classifier = senti.classifier;
		this.trainSetMinSize = senti.trainSetMinSize;
		this.domain = senti.domain;
		this.debugClassifier = senti.debugClassifier;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		algo = (IClassifier<T>) ClassifierRegistry
				.getClassifierByTypeAndDomain(classifier.toLowerCase(), domain);
	}

	@Override
	protected  void process_next(Tuple object, int port) {

		if (port == 0) {
			// add trainingsset
			trainingset.put(object.getAttribute(attributeTrainSetTextPos).toString(),
					Integer.parseInt(object.getAttribute(attributeTrainSetTrueDecisionPos).toString().trim()));

			if (trainingset.size() >= trainSetMinSize) {
				algo.trainClassifier(trainingset);
				isTrained = true;
				// synchronized for java.util.ConcurrentModificationException problems				
				synchronized (this.buffer) {
					// train classifier
					if (buffer.size() > 0) {
						for (Tuple buffered : this.buffer) {
							processSentimentDetection(buffered);
						}

						buffer.clear();
					}
					trainingset.clear();

				}

			}
		} else {
			if (isTrained) {
				// synchronized for java.util.ConcurrentModificationException problems		
				synchronized (this.buffer) {
					if (buffer.size() > 0) {
						for (Tuple buffered : this.buffer) {
							processSentimentDetection(buffered);
						}
						buffer.clear();
					}
				}
				processSentimentDetection(object);
			} else {
				// synchronized for java.util.ConcurrentModificationException problems		
				synchronized (this.buffer) {
					buffer.add(object);
				}
			}

		}
	}

	@Override
	public SentimentDetectionPO<T> clone() {
		return new SentimentDetectionPO<T>(this);
	}

	@SuppressWarnings("unchecked")
	private void processSentimentDetection(Tuple object) {
			// get inputSize of the object
			int inputSize = object.size();

			// create new output Tuple with size + 1
			Tuple outputTuple = new Tuple(object.size() + 1, false);

			// Copy object Attributes to the new outputTuple
			System.arraycopy(object.getAttributes(), 0,
					outputTuple.getAttributes(), 0, inputSize);

			// text positive or negative
			// String erg = detect(object.getAttribute(0).toString());

			int decision = algo.startDetect(object.getAttribute(attributeTestSetTextPos).toString());

			// get OutputPort
			int outputPort = getOutPutPort(decision);

			// set the decision Attribute
			outputTuple.setAttribute(object.size(), decision);
		
	
		
			outputTuple.setMetadata(object.getMetadata());
			outputTuple.setRequiresDeepClone(object.requiresDeepClone());

			
			if(debugClassifier){
				// calculate error
				String truedecision = outputTuple.getAttribute(attributeTestSetTrueDecisionPos).toString();
				
				
				if(Integer.parseInt(truedecision.trim()) == 1){
					totalExistPosCtr++;
				}else{
					totalExistNegCtr++;
				}
				
				
				if(decision == 1){
					totalPosCtr++;
				}else{
					totalNegCtr++;
				}
				
				if (Integer.parseInt(truedecision.trim()) != decision) {
					wrongdecision++;
					System.out.println("ERROR:-------------------------");
				}else{
					System.out.println("CORRECT:-----------------------");
					if(Integer.parseInt(truedecision.trim()) == 1){
						posCtr++;
					}else{
						negCtr++;
					}
				}
				
				System.out.println("record: " + object.getAttribute(attributeTestSetTextPos).toString());
				System.out.println("true decision: " + truedecision);
				System.out.println("decision: " + decision);
				System.out.println("total wrong: " + wrongdecision);			
			}
			ctr++;
			transfer(outputTuple, outputPort);
	}

	/*
	 * Default port is 0 outputport is set to two transfer positive to port 0
	 * transfer negative to port 1
	 */
	private int getOutPutPort(int decision) {
		int outputPort = 0;

		if (splitDecision) {
			if (decision == 1) {
				outputPort = 0;
			} else {
				outputPort = 1;
			}
		}
		return outputPort;
	}
	
	

	@Override
	protected void process_close() {
		
		if(debugClassifier){
			System.out.println("pos recall: " + Metrics.recall(posCtr, totalExistPosCtr));
			System.out.println("pos precision: " + Metrics.precision(posCtr, totalPosCtr));
			System.out.println("pos f-score: "+ Metrics.f_score(Metrics.recall(posCtr, totalExistPosCtr), Metrics.precision(posCtr, totalPosCtr)));
						
			System.out.println();
			System.out.println();
			
			System.out.println("neg recall: " + Metrics.recall(negCtr, totalExistNegCtr));
			System.out.println("neg precision: " + Metrics.precision(negCtr, totalNegCtr));
			System.out.println("neg f-score: "+ Metrics.f_score(Metrics.recall(negCtr, totalExistNegCtr), Metrics.precision(negCtr, totalNegCtr)));
		}
		
		super.process_close();
		
		this.buffer.clear();
		this.isTrained = false;
	
		if(algo != null){
			ClassifierRegistry.unregisterDomain(domain);
		}
		
	}

}
