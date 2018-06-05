package de.uniol.inf.is.odysseus.sentimentdetection.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.ClassifierRegistry;
import de.uniol.inf.is.odysseus.sentimentdetection.classifier.IClassifier;
import de.uniol.inf.is.odysseus.sentimentdetection.stopwords.IStopWords;
import de.uniol.inf.is.odysseus.sentimentdetection.stopwords.StopWordsRegistry;
import de.uniol.inf.is.odysseus.sentimentdetection.util.Metrics;
import de.uniol.inf.is.odysseus.sentimentdetection.util.TrainSetEntry;

/**
 * physical Sentiment Detection Operator
 * 
 * @author Marc Preuschaft
 * 
 * @param <T>
 */
@SuppressWarnings({ "rawtypes" })
public class SentimentDetectionPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	// operator parameter
	private boolean splitDecision;
	private String classifier;
	private int trainSetMinSize;
	private String domain;
	private boolean debugClassifier = false;
	private int maxBufferSize;
	private int totalInputports = 2;

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
	private long trainTimeTotal;

	// help variable
	private int ctr = 0;
	private boolean isTrained = false;

	private int trainSetSize = 0;

	// currend classifier
	private IClassifier algo;

	// currend stopwords
	private IStopWords stopwordsSet;
	private String language;

	// buffer
	private List<Tuple> buffer = new ArrayList<>();
	private List<Tuple> testDataBuffer = new ArrayList<>();

	// attribute positions
	private int attributeTrainSetTextPos = -1;
	private int attributeTrainSetTrueDecisionPos = -1;

	private int attributeTestSetTextPos = -1;
	private int attributeTestSetTrueDecisionPos = -1;

	private int attributeTextToBeClassifiedPos = -1;
	private SDFAttribute attributeTrainSetText;
	private SDFAttribute attributeTrainSetTrueDecision;
	private SDFAttribute attributeTestSetText;
	private SDFAttribute attributeTestSetTrueDecision;
	private SDFAttribute attributeTextToBeClassified;

	static Logger logger = LoggerFactory.getLogger(SentimentDetectionPO.class);

	public SentimentDetectionPO(boolean splitDecision, String classifier, int trainSetMinSize, String domain, boolean debugClassifier, SDFAttribute attributeTrainSetText, SDFAttribute attributeTrainSetTrueDecision, SDFAttribute attributeTestSetText, SDFAttribute attributeTestSetTrueDecision,
			int ngram, boolean removeStopWords, boolean stemmWords, boolean ngramUpto, String language, int maxBufferSize, SDFAttribute attributeTextToBeClassified, int totalInputports) {
		super();

		// Determine Attribute positions!
		this.attributeTrainSetText = attributeTrainSetText;
		this.attributeTrainSetTrueDecision = attributeTrainSetTrueDecision;

		this.attributeTestSetText = attributeTestSetText;
		this.attributeTestSetTrueDecision = attributeTestSetTrueDecision;

		this.attributeTextToBeClassified = attributeTextToBeClassified;

		this.splitDecision = splitDecision;
		this.classifier = classifier;
		this.trainSetMinSize = trainSetMinSize;
		this.domain = domain;
		this.debugClassifier = debugClassifier;

		this.totalInputports = totalInputports;

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
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		SDFSchema trainSchema = getSubscribedToSource(0).getSchema();
		SDFSchema dataSchema = getSubscribedToSource(1).getSchema();

		this.attributeTrainSetTextPos = trainSchema.indexOf(attributeTrainSetText);
		this.attributeTrainSetTrueDecisionPos = trainSchema.indexOf(attributeTrainSetTrueDecision);
		if (totalInputports == 3) {
			this.attributeTestSetTextPos = getOutputSchema().indexOf(attributeTestSetText);
			this.attributeTestSetTrueDecisionPos = getOutputSchema().indexOf(attributeTestSetTrueDecision);
		}
		this.attributeTextToBeClassifiedPos = dataSchema.indexOf(attributeTextToBeClassified);

		algo = ClassifierRegistry.getClassifierByTypeAndDomain(classifier.toLowerCase(), domain);
		algo.setNgram(ngram);
		algo.setRemoveStopWords(removeStopWords);
		algo.setStemmWords(stemmWords);
		algo.setDebugModus(debugClassifier);

		if (ngramUpTo) {
			algo.setNgramUpTo(ngram);
		}
		stopwordsSet = StopWordsRegistry.getStopWordsByLanguage(language);
	}

	@Override
	protected void process_next(Tuple object, int port) {
		if (!isStarted) {
			startTime = System.currentTimeMillis();
			isStarted = true;
		}

		if (debugClassifier) {
			/*
			 * Debug-Mouds two modi 1. traindata and testdata 2. traindata, testdata and text to be classified
			 */
			// in debug-modus port 0 only trainingdata
			if (port == 0) {
				addTrainData(object);

				if (trainSetSize >= trainSetMinSize || isTrained) {
					isTrained = true;
					// synchronized for
					// java.util.ConcurrentModificationException
					// problems
					synchronized (this.testDataBuffer) {
						if (testDataBuffer.size() > 0) {
							for (Tuple buffered : this.testDataBuffer) {
								processSentimentDetection(buffered, true);
							}
							testDataBuffer.clear();
						}
					}

					synchronized (this.buffer) {
						if (buffer.size() > 0) {
							for (Tuple buffered : this.buffer) {
								processSentimentDetection(buffered, false);
							}
							buffer.clear();
						}
					}

				}

			} else {
				// in debug-modus port 1 only testdata
				if (port == 1) {

					if (isTrained) {
						// synchronized for
						// java.util.ConcurrentModificationException
						// problems
						synchronized (this.testDataBuffer) {
							if (testDataBuffer.size() > 0) {
								for (Tuple buffered : this.testDataBuffer) {
									processSentimentDetection(buffered, true);
								}
								testDataBuffer.clear();
							}
						}
						processSentimentDetection(object, true);
					} else {
						// synchronized for
						// java.util.ConcurrentModificationException
						// problems
						synchronized (this.testDataBuffer) {
							testDataBuffer.add(object);
						}
					}
					// in debug-modus on port 2 text to classified
				} else if (port == 2) {

					if (isTrained) {
						// classifier is trained send text direct to the
						// classifier
						processSentimentDetection(object, false);
					} else {
						// classifier is not trained, add text to a buffer
						buffer.add(object);
					}

				}

			}

		} else {
			/*
			 * normalmodus only one modus 1. traindata and text to be classified
			 */

			// port 0 only traindata
			if (port == 0) {
				addTrainData(object);

				if (trainSetSize >= trainSetMinSize || isTrained) {
					if (!isTrained) {
						System.out.println("Sentiment detection is trained");
						isTrained = true;
					}

					synchronized (this.buffer) {
						// train classifier
						if (buffer.size() > 0) {
							for (Tuple buffered : this.buffer) {
								processSentimentDetection(buffered, false);
							}

							buffer.clear();
						}
					}

				}

			} else {
				// text to be classified

				// if classifiere trained?
				if (isTrained) {
					// synchronized for
					// java.util.ConcurrentModificationException
					// problems
					synchronized (this.buffer) {
						if (buffer.size() > 0) {
							for (Tuple buffered : this.buffer) {
								processSentimentDetection(buffered, false);
							}
							buffer.clear();
						}
					}
					processSentimentDetection(object, false);
				} else {
					/*
					 * classifier is not trained add text to the buffer synchronized for java.util.ConcurrentModificationException problems
					 */
					synchronized (this.buffer) {
						if (buffer.size() >= maxBufferSize) {
							buffer.remove(0);
						}
						buffer.add(object);
					}
				}
			}

		}

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@SuppressWarnings("unchecked")
	private void processSentimentDetection(Tuple object, boolean debug) {

		// get inputSize of the object
		int inputSize = object.size();

		// create new output Tuple with size + 1
		Tuple outputTuple = new Tuple(object.size() + 1, false);

		// Copy object Attributes to the new outputTuple
		System.arraycopy(object.getAttributes(), 0, outputTuple.getAttributes(), 0, inputSize);
		String text;
		int decision;
		if (debug) {
			text = object.getAttribute(attributeTestSetTextPos).toString();
		} else {
			text = object.getAttribute(attributeTextToBeClassifiedPos).toString();
		}

		// remove stopwords / stemm words
		text = cleanSentence(text);

		// start sentiment detection
		decision = algo.startDetect(text);

		// set the decision attribute
		outputTuple.setAttribute(object.size(), decision);
		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());

		if (debug) {
			// calculate error
			String truedecision = outputTuple.getAttribute(attributeTestSetTrueDecisionPos).toString();

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

			System.out.println("sentence: " + object.getAttribute(attributeTestSetTextPos).toString());
			System.out.println("true decision: " + truedecision);
			System.out.println("decision: " + decision);
			System.out.println("total wrong: " + wrongdecision);
		}
		ctr++;

		if (debug) {
			if (totalInputports == 3) {
				transfer(outputTuple, 2);
			} else {
				transfer(outputTuple, 0);
			}
		} else {
			transfer(outputTuple, getOutPutPort(decision));
		}

		// add debug-infos
		if (debug) {
			addDebugInfosToOperatorInfo();
		}
	}

	/**
	 * add a Tuple object to the trainingsset
	 * 
	 * @param object
	 */
	private void addTrainData(Tuple object) {

		TrainSetEntry entry = new TrainSetEntry();
		entry.setTrueDecision(Integer.parseInt(object.getAttribute(attributeTrainSetTrueDecisionPos).toString().trim()));
		entry.setSentence(cleanSentence(object.getAttribute(attributeTrainSetTextPos).toString()));

		startTimeTrain = System.currentTimeMillis();
		algo.trainClassifier(entry, isTrained);
		stopTimeTrain = System.currentTimeMillis();

		trainTimeTotal += stopTimeTrain - startTimeTrain;

		trainSetSize++;

		// add current trainsetsize
		addParameterInfo("TRAINSET-SIZE", trainSetSize);
	}

	/*
	 * Default port is 0 outputport is set to two transfer positive to port 0 transfer negative to port 1
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

	/**
	 * cleanup the a sentence stopwords/stemming
	 * 
	 * @param text
	 * @return
	 */
	private String cleanSentence(String text) {
		// remove stopwords
		if (algo.getRemoveStopWords()) {
			text = stopwordsSet.removeStopWords(text);
		}

		// stemm words
		if (algo.getStemmWords()) {
			text = stopwordsSet.stemmSentence(text);
		}

		return text;
	}

	@Override
	protected void process_close() {

		if (debugClassifier) {
			System.out.println("Total counter: " + ctr);
			stopTime = System.currentTimeMillis();
			System.out.println("Total time used: " + (stopTime - startTime));
			System.out.println("Total train time used: " + trainTimeTotal);

			System.out.println("pos recall: " + Metrics.recall(posCtr, totalExistPosCtr));
			System.out.println("pos precision: " + Metrics.precision(posCtr, totalPosCtr));
			System.out.println("pos f-score: " + Metrics.f_score(Metrics.recall(posCtr, totalExistPosCtr), Metrics.precision(posCtr, totalPosCtr)));
			System.out.println();
			System.out.println();

			System.out.println("neg recall: " + Metrics.recall(negCtr, totalExistNegCtr));
			System.out.println("neg precision: " + Metrics.precision(negCtr, totalNegCtr));
			System.out.println("neg f-score: " + Metrics.f_score(Metrics.recall(negCtr, totalExistNegCtr), Metrics.precision(negCtr, totalNegCtr)));

		}

		super.process_close();

		this.buffer.clear();
		this.isTrained = false;
		this.trainSetSize = 0;
		this.ctr = 0;

		if (algo != null) {
			ClassifierRegistry.unregisterDomain(domain);
		}

	}

	/*
	 * add Debug-Infos to the operator detail infos
	 */
	private void addDebugInfosToOperatorInfo() {
		addParameterInfo("TOTAL WRONG", wrongdecision);
		addParameterInfo("POS RECALL", Metrics.recall(posCtr, totalExistPosCtr));
		addParameterInfo("POS PRECISION", Metrics.precision(posCtr, totalPosCtr));
		addParameterInfo("POS F-SCORE", Metrics.f_score(Metrics.recall(posCtr, totalExistPosCtr), Metrics.precision(posCtr, totalPosCtr)));

		addParameterInfo("NEG RECALL", Metrics.recall(negCtr, totalExistNegCtr));
		addParameterInfo("NEG PRECISION", Metrics.precision(negCtr, totalNegCtr));
		addParameterInfo("NEG F-SCORE", Metrics.f_score(Metrics.recall(negCtr, totalExistNegCtr), Metrics.precision(negCtr, totalNegCtr)));
	}

}
