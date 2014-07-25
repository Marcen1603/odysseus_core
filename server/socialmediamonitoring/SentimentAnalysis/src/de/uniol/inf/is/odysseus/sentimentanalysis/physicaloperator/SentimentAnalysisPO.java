package de.uniol.inf.is.odysseus.sentimentanalysis.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.MiningAlgorithmRegistry;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;

/**
 * physical Sentiment Detection Operator
 * 
 * @author Marc Preuschaft
 * 
 * @param <T>
 */
@SuppressWarnings({ "rawtypes", "deprecation" })
public class SentimentAnalysisPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {
	
	IClassificationLearner learner;
	de.uniol.inf.is.odysseus.mining.classification.IClassifier myClassifier;
	int safe = 0;
	
	// operator parameter
	private String userClassifier;
	//private String domain;
	private boolean debugClassifier = false;
	private int maxBufferSize;
	private int totalInputports = 2;

	private int attributeTextToBeClassifiedPos = -1;
	private SDFAttribute attributeTextToBeClassified;
	
	private SDFAttribute attributeTrainSetText;
	private int attributeTrainSetTextPosition = -1;
	
	private SDFAttribute attributeTrainSetTrueDecision;
	private int attributeTrainSetTrueDecisionPosition = -1;

	static Logger logger = LoggerFactory.getLogger(SentimentAnalysisPO.class);

	public SentimentAnalysisPO(String userClassifier, /*String domain,*/ int maxBufferSize, SDFAttribute attributeTextToBeClassified, int totalInputports, SDFAttribute attributeTrainSetText, SDFAttribute attributeTrainSetTrueDecision) {
		super();

		this.attributeTextToBeClassified = attributeTextToBeClassified;
		this.userClassifier = userClassifier;
		//this.domain = domain;
		this.totalInputports = totalInputports;
		this.maxBufferSize = maxBufferSize;
		this.attributeTrainSetText = attributeTrainSetText;
		this.attributeTrainSetTrueDecision = attributeTrainSetTrueDecision;
	}

	public SentimentAnalysisPO(SentimentAnalysisPO<M> senti) {
		super(senti);
		this.userClassifier = senti.userClassifier;
		//this.domain = senti.domain;
		this.debugClassifier = senti.debugClassifier;
		this.maxBufferSize = senti.maxBufferSize;
		this.attributeTextToBeClassified = senti.attributeTextToBeClassified;
		this.attributeTrainSetText = senti.attributeTrainSetText;
		this.attributeTrainSetTrueDecision = senti.attributeTrainSetTrueDecision;
	}

	@Override
	public SentimentAnalysisPO<M> clone() {
		return new SentimentAnalysisPO<M>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		SDFSchema dataSchema = getSubscribedToSource(0).getSchema();
		SDFSchema trainSchema = getSubscribedToSource(1).getSchema();
		
		this.attributeTrainSetTextPosition = trainSchema.indexOf(this.attributeTrainSetText);
		this.attributeTrainSetTrueDecisionPosition = trainSchema.indexOf(this.attributeTrainSetTrueDecision);
		
		this.attributeTextToBeClassifiedPos = dataSchema.indexOf(attributeTextToBeClassified);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("argument", "-W");
		
		//Erzeuge learner
		this.learner = MiningAlgorithmRegistry.getInstance().createClassificationLearner("weka");	
		this.learner.setOptions(map);
		this.learner.init(this.userClassifier, trainSchema, this.attributeTrainSetTrueDecision, null);	
	}
	
	@Override
	protected void process_next(Tuple<M> object, int port) {
		
		Object classy = null;
		
		if(port == 1)
		{
			List<Tuple<M>> list = new ArrayList<Tuple<M>>();
			list.add(object);
			this.myClassifier = learner.createClassifier(list);
			
			safe++;
		}
		else if(port == 0 && safe > 0)
		{
			classy = this.myClassifier.classify(object);
			System.out.println("Klasse: " + classy.toString());
		}
		
		Tuple outputTuple = new Tuple(object.size()+1, false);
		System.arraycopy(object.getAttributes(), 0, outputTuple.getAttributes(), 0, object.size());
		
		outputTuple.setAttribute(object.size(), classy.toString());
		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());
		
		transfer(outputTuple, 0);
	}
}
