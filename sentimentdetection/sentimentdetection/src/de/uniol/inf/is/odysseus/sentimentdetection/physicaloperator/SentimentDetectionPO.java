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

@SuppressWarnings({ "rawtypes" })
public class SentimentDetectionPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	private static int ctr = 0;

	private int wrongdecision = 0;

	private int outputports;
	private String classifier;
	private int minimumSize;
	private boolean isTrained = false;

	private IClassifier<T> algo;

	private List<Tuple> buffer = new ArrayList<>();
	private Map<String, Integer> trainingset = new HashMap<String, Integer>();

	public SentimentDetectionPO() {
		super();
	}

	public SentimentDetectionPO(int outputports, String classifier,
			int minimumSize) {
		super();
		this.outputports = outputports;
		this.classifier = classifier;
		this.minimumSize = minimumSize;
	}

	public SentimentDetectionPO(SentimentDetectionPO<T> senti) {
		super(senti);
		this.outputports = senti.outputports;
		this.classifier = senti.classifier;
		this.minimumSize = senti.minimumSize;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		algo = (IClassifier<T>) ClassifierRegistry
				.getClassifierByName(classifier.toLowerCase());
		System.out.println("Classifier wird initialisiert....");

	}

	@Override
	protected void process_next(Tuple object, int port) {

		if (port == 0) {
			// add trainingsdata
			trainingset.put(object.getAttribute(0).toString(),
					Integer.parseInt(object.getAttribute(1).toString().trim()));

			if (trainingset.size() >= minimumSize) {
				// train classifier
				algo.trainClassifier(trainingset);		
				isTrained = true;
				trainingset.clear();
			}
		} else {
			if (isTrained) {
				if(buffer.size() >  0){
					for (Tuple buffered : this.buffer) {
						processSentimentDetection(buffered);
					}
					buffer.clear();
				}
				processSentimentDetection(object);
			} else {
				buffer.add(object);
			}

		}
	}

	@Override
	public SentimentDetectionPO<T> clone() {
		return new SentimentDetectionPO<T>(this);
	}

	@SuppressWarnings("unchecked")
	private void processSentimentDetection(Tuple object) {
		// System.out.println("outputType is: "+ outputtype);
		// get inputSize of the object
		int inputSize = object.size();

		// create new output Tuple with size + 1
		Tuple outputTuple = new Tuple(object.size() + 1, false);

		// Copy object Attributes to the new outputTuple
		System.arraycopy(object.getAttributes(), 0,
				outputTuple.getAttributes(), 0, inputSize);

		// text positive or negative
		// String erg = detect(object.getAttribute(0).toString());

		int decision = algo.startDetect(object.getAttribute(0).toString());

		// get OutputPort
		int outputPort = getOutPutPort(decision);

		// set the decision Attribute
		outputTuple.setAttribute(object.size(), decision);

		// calculate error
		String truedecision = outputTuple.getAttribute(object.size() - 1)
				.toString();

		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());

		System.out.println("record: " + object.getAttribute(0).toString());
		System.out.println("true decision: " + truedecision);
		System.out.println("decision: " + decision);
		System.out.println("wrong: " + wrongdecision);

		if (Integer.parseInt(truedecision.trim()) != decision) {
			wrongdecision++;
		}

		// System.out.println("Error:" + 1.0 * wrongdecision / 1062 );
		// System.out.println("Alte Objekt ist: "+ object.toString());
		// System.out.println("Neues Objekt ist: "+ outputTuple.toString());

		ctr++;

		transfer(outputTuple, outputPort);

	}

	/*
	 * Default port is 0 outputport is set to two transfer positive to port 0
	 * transfer negative to port 1
	 */
	private int getOutPutPort(int decision) {

		int outputPort = 0;

		if (outputports == 2) {
			if (decision == 1) {
				// System.out.println("Ausgabe an Port 0:"+ outputtype);
				outputPort = 0;
			} else {
				// System.out.println("Ausgabe an Port 1:" + outputtype);
				outputPort = 1;
			}
		}
		return outputPort;
	}
	
	@Override
	protected void process_close() {
		super.process_close();
		this.buffer.clear();
		this.isTrained = false;
	}

}
