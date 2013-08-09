package de.uniol.inf.is.odysseus.sentimentdetection.physicaloperator;

import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

import de.uniol.inf.is.odysseus.sentimentdetection.classifier.ClassifierRegistry;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.IClassifier;
import de.uniol.inf.is.odysseus.sentimentdetection.stopwords.IStopWords;
import de.uniol.inf.is.odysseus.sentimentdetection.stopwords.StopWordsRegistry;
import de.uniol.inf.is.odysseus.sentimentdetection.util.Metrics;
import de.uniol.inf.is.odysseus.sentimentdetection.util.TrainSetEntry;

@SuppressWarnings({ "rawtypes" })
public class SentimentDetectionPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	// operator parameter
	private boolean splitDecision;
	private String classifier;
	private int trainSetMinSize;
	private String domain;
	private boolean debugClassifier = false;
	private int maxBufferSize;

	// classificator parameter
	private int ngram;
	private boolean ngramUpTo;
	private boolean removeStopWords;
	private boolean stemmWords;

	// variable for debug
	private int posCtr = 0;
	private int totalPosCtr = 0;
	private int totalExistPosCtr = 0;

	private int negCtr = 0;
	private int totalNegCtr = 0;
	private int totalExistNegCtr = 0;

	private int wrongdecision = 0;

	private long startTime;
	private long stopTime;
	private boolean isStarted = false;

	private long startTimeTrain;
	private long stopTimeTrain;

	// help variable
	private static int ctr = 0;
	private boolean isTrained = false;
	
	private int trainSetSize = 0;

	// currend classifier
	private IClassifier algo;
	// currend stopwords
	
	private IStopWords stopwordsSet;
	private String language;

	// buffer
	private List<Tuple> buffer = new ArrayList<>();

	// attribute positions
	private int attributeTrainSetTextPos = -1;
	private int attributeTrainSetTrueDecisionPos = -1;

	private int attributeTestSetTextPos = -1;
	private int attributeTestSetTrueDecisionPos = -1;
	
	static Logger logger = LoggerFactory.getLogger(SentimentDetectionPO.class);

	public SentimentDetectionPO(boolean splitDecision, String classifier,
			int trainSetMinSize, String domain, boolean debugClassifier,
			int attributeTrainSetTextPos, int attributeTrainSetTrueDecisionPos,
			int attributeTestSetTextPos, int attributeTestSetTrueDecisionPos,
			int ngram, boolean removeStopWords, boolean stemmWords,
			boolean ngramUpto, String language, int maxBufferSize) {
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

		this.ngram = ngram;
		this.ngramUpTo = ngramUpto;
		this.removeStopWords = removeStopWords;
		this.stemmWords = stemmWords;

		this.maxBufferSize = maxBufferSize;
		
		this.language = language;
	}

	public SentimentDetectionPO(SentimentDetectionPO<T> senti) {
		super(senti);
		this.splitDecision = senti.splitDecision;
		this.classifier = senti.classifier;
		this.trainSetMinSize = senti.trainSetMinSize;
		this.domain = senti.domain;
		this.debugClassifier = senti.debugClassifier;
		this.ngram = senti.ngram;
		this.maxBufferSize = senti.maxBufferSize;
	}

	@Override
	public SentimentDetectionPO<T> clone() {
		return new SentimentDetectionPO<T>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		algo = (IClassifier) ClassifierRegistry.getClassifierByTypeAndDomain(
				classifier.toLowerCase(), domain);
		algo.setNgram(ngram);
		algo.setRemoveStopWords(removeStopWords);
		algo.setStemmWords(stemmWords);
		if (ngramUpTo) {
			algo.setNgramUpTo(ngram);
		}
		stopwordsSet = (IStopWords) StopWordsRegistry.getStopWordsByLanguage(language);
	
	}

	@Override
	protected void process_next(Tuple object, int port) {
		if (!isStarted) {
			startTime = System.currentTimeMillis();
			isStarted = true;
		}
		if (port == 0) {
			logger.debug("trainingSetSize: " + trainSetSize);

			TrainSetEntry entry = new TrainSetEntry();
			entry.setTrueDecision(Integer.parseInt(object.getAttribute(attributeTrainSetTrueDecisionPos).toString().trim()));
			
			// remove stopwords
			if (algo.getRemoveStopWords()) {
				entry.setRecord(stopwordsSet.removeStopWords(object.getAttribute(attributeTrainSetTextPos).toString()));
			} else {
				entry.setRecord(object.getAttribute(attributeTrainSetTextPos).toString());
			}

			// stemm words
			if (algo.getStemmWords()) {
				entry.setRecord(stopwordsSet.stemmRecord(entry.getRecord()));
			}
		
			algo.trainClassifier(entry, isTrained);
			trainSetSize++;
			
			if (trainSetSize >= trainSetMinSize || isTrained) {
				isTrained = true;
				// synchronized for java.util.ConcurrentModificationException
				// problems
				synchronized (this.buffer) {
					// train classifier
					if (buffer.size() > 0) {
						for (Tuple buffered : this.buffer) {
							processSentimentDetection(buffered);
						}

						buffer.clear();
					}
				}

			}
		} else {
			if (isTrained) {
				// synchronized for java.util.ConcurrentModificationException
				// problems
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
				// synchronized for java.util.ConcurrentModificationException
				// problems
				synchronized (this.buffer) {
					if (buffer.size() >= maxBufferSize) {
						buffer.remove(0);
					}
					buffer.add(object);
				}
			}

		}
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
		int decision;
		String text = object.getAttribute(attributeTestSetTextPos).toString();

		// remove stopwords
		if (algo.getRemoveStopWords()) {
			text = 	stopwordsSet.removeStopWords(text);
			//text = StopWords.removeStopWords(text);
		}

		// stemm words
		if (algo.getStemmWords()) {
			text = stopwordsSet.stemmRecord(text);
			//text = StopWords.stemmRecord(text);
		}

		decision = algo.startDetect(text);

		// get OutputPort
		int outputPort = getOutPutPort(decision);

		// set the decision attribute
		outputTuple.setAttribute(object.size(), decision);
		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());

		if (debugClassifier) {
			// calculate error
			String truedecision = outputTuple.getAttribute(
					attributeTestSetTrueDecisionPos).toString();

			if (Integer.parseInt(truedecision.trim()) == 1) {
				totalExistPosCtr++;
			} else {
				totalExistNegCtr++;
			}

			if (decision == 1) {
				totalPosCtr++;
			} else {
				totalNegCtr++;
			}

			if (Integer.parseInt(truedecision.trim()) != decision) {
				wrongdecision++;
				System.out.println("ERROR:-------------------------");
			} else {
				System.out.println("CORRECT:-----------------------");
				if (Integer.parseInt(truedecision.trim()) == 1) {
					posCtr++;
				} else {
					negCtr++;
				}
			}

			System.out.println("record: "+ object.getAttribute(attributeTestSetTextPos).toString());
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

		if (debugClassifier) {
			System.out.println("Total counter: " + ctr);
			stopTime = System.currentTimeMillis();
			System.out.println("Total time used: " + (stopTime - startTime));
			System.out.println("Total train time used: "
					+ (stopTimeTrain - startTimeTrain));

			System.out.println("pos recall: "
					+ Metrics.recall(posCtr, totalExistPosCtr));
			System.out.println("pos precision: "
					+ Metrics.precision(posCtr, totalPosCtr));
			System.out.println("pos f-score: "
					+ Metrics.f_score(Metrics.recall(posCtr, totalExistPosCtr),
							Metrics.precision(posCtr, totalPosCtr)));

			System.out.println();
			System.out.println();

			System.out.println("neg recall: "
					+ Metrics.recall(negCtr, totalExistNegCtr));
			System.out.println("neg precision: "
					+ Metrics.precision(negCtr, totalNegCtr));
			System.out.println("neg f-score: "
					+ Metrics.f_score(Metrics.recall(negCtr, totalExistNegCtr),
							Metrics.precision(negCtr, totalNegCtr)));

		}

		super.process_close();

		this.buffer.clear();
		this.isTrained = false;

		if (algo != null) {
			ClassifierRegistry.unregisterDomain(domain);
		}

	}

}
