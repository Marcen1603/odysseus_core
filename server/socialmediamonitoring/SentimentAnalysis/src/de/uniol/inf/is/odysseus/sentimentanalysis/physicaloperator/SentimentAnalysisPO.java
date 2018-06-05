package de.uniol.inf.is.odysseus.sentimentanalysis.physicaloperator;

//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.MiningAlgorithmRegistry;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;
import de.uniol.inf.is.odysseus.mining.weka.mapping.WekaAttributeResolver;
import de.uniol.inf.is.odysseus.mining.weka.mapping.WekaConverter;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * 
 * @author Christopher Licht
 * 
 */
@SuppressWarnings({ "deprecation" })
public class SentimentAnalysisPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {
	
	private boolean isTrained = false;
	private boolean isEvaluated = false;
	private final ITimeIntervalSweepArea<Tuple<M>> sweepArea;
	
	IClassificationLearner<ITimeInterval> learner;
	WekaAttributeResolver resolver;
		
	Instances trainData ;
	StringToWordVector filter;
	StringToNominal nomFilter;
	FilteredClassifier classifier;
	Classifier userDefinedClassifier;
	String classification;
	
	Instance instance;
	Instances instances;
		
	SDFSchema dataSchema;
	SDFSchema trainSchema;
	Map<SDFAttribute, List<String>> nominals;
	List<String>userNominals = new ArrayList<>();

	private String userClassifier;
	private int maxTrainSize;
	private int totalInputports = 2;
	private double thresholdValue;

	private int attributeTextToBeClassifiedPos = -1;
	private SDFAttribute attributeTextToBeClassified;
	
	private SDFAttribute attributeTrainSetText;
	private int attributeTrainSetTextPosition = -1;
	
	private SDFAttribute attributeTrainSetTrueDecision;
	private int attributeTrainSetTrueDecisionPosition = -1;

	static Logger logger = LoggerFactory.getLogger(SentimentAnalysisPO.class);

	public SentimentAnalysisPO(String userClassifier, SDFAttribute attributeTextToBeClassified, int totalInputports, SDFAttribute attributeTrainSetText, SDFAttribute attributeTrainSetTrueDecision, List<String>userNominals, int maxTrainSize, double thresholdValue, ITimeIntervalSweepArea<Tuple<M>> sweepArea) {
		super();

		this.attributeTextToBeClassified = attributeTextToBeClassified;
		this.userClassifier = userClassifier;
		this.setTotalInputports(totalInputports);
		this.maxTrainSize = maxTrainSize;
		this.attributeTrainSetText = attributeTrainSetText;
		this.attributeTrainSetTrueDecision = attributeTrainSetTrueDecision;
		this.userNominals = userNominals;
		this.thresholdValue = thresholdValue;
		this.sweepArea = sweepArea;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.dataSchema = getSubscribedToSource(0).getSchema();
		this.trainSchema = getSubscribedToSource(1).getSchema();
		
		this.setAttributeTrainSetTextPosition(this.trainSchema.indexOf(this.attributeTrainSetText));
		this.attributeTrainSetTrueDecisionPosition = this.trainSchema.indexOf(this.attributeTrainSetTrueDecision);
		
		this.attributeTextToBeClassifiedPos = this.dataSchema.indexOf(attributeTextToBeClassified);
		
		this.nominals = new HashMap<SDFAttribute, List<String>>();
		this.nominals.put(this.attributeTrainSetTrueDecision, this.userNominals);
		
		this.learner = MiningAlgorithmRegistry.getInstance().createClassificationLearner("weka");	
		this.learner.init(this.userClassifier, this.trainSchema, this.attributeTrainSetTrueDecision, this.nominals);
		setAlgorithm(this.userClassifier);
		this.resolver = new WekaAttributeResolver(this.trainSchema, this.nominals);
		
		try {
			instances = new Instances(this.resolver.getSchema().getURI(), this.resolver.getWekaSchema(), 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void classify(Instances instances) throws Exception {
		double pred = this.classifier.classifyInstance(instances.instance(0));
		
		if(pred > this.thresholdValue)
		{
			//System.out.println("Threshold fall below " + this.thresholdValue);
		}
		this.classification =  instances.classAttribute().value((int) pred);
	}
	
	/*private void getTrainData(Instances instances)
	{
		for(Instance instance : instances)
			this.trainData = instance.dataset();
	}*/
	
	private void evaluateClassifier()throws Exception
	{
		this.trainData.setClassIndex(attributeTrainSetTrueDecisionPosition);

		this.filter = new StringToWordVector();
		this.filter.setAttributeIndices("first");
		
		this.classifier = new FilteredClassifier();
		this.classifier.setFilter(this.filter);
		
		this.classifier.setClassifier(this.userDefinedClassifier);
		
		Evaluation evaluation = new Evaluation(this.trainData);		
		evaluation.crossValidateModel(this.classifier, this.trainData, 5, new Random(1));
		
		isEvaluated = true;
	}

	private void learnClassifier()
	{
		try {
				this.trainData.setClassIndex(this.attributeTrainSetTrueDecisionPosition);
				this.filter = new StringToWordVector();
				this.filter.setAttributeIndices("first");
				this.classifier = new FilteredClassifier();
				this.classifier.setFilter(this.filter);
				this.classifier.setClassifier(this.userDefinedClassifier);
				this.classifier.buildClassifier(this.trainData);
				
				this.isTrained = true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	private Instances createInstancesForClassifying(String textToClassify) 
	{
		List<String> list = this.nominals.get(this.attributeTrainSetTrueDecision);
		//FastVector fvNominalVal = new FastVector(list.size());
		List nominalValues = new ArrayList<>();
		
		for(int i=0; i < list.size(); i++){
			nominalValues.add(list.get(i));
		}
	
		/*for(int i=0; i < list.size(); i++){
			fvNominalVal.addElement(list.get(i));
		}*/
		
		Attribute textToClassifyAttribute = new Attribute(this.trainSchema.getAttribute(0).getAttributeName(), (FastVector)null);
		Attribute classifyAttribute = new Attribute(this.trainSchema.getAttribute(1).getAttributeName(), nominalValues);
		
		ArrayList<Attribute> wekaAttributes = new ArrayList<Attribute>();
		wekaAttributes.add(textToClassifyAttribute);
		wekaAttributes.add(classifyAttribute);
		//FastVector fvWekaAttributes = new FastVector(list.size());
		//fvWekaAttributes.addElement(textToClassifyAttribute);
		//fvWekaAttributes.addElement(classifyAttribute);
		
		Instances instances = new Instances("Test relation", wekaAttributes, 1);
		instances.setClassIndex(this.attributeTrainSetTrueDecisionPosition);
				
		DenseInstance instance = new DenseInstance(2); //list.size()
		instance.setValue(textToClassifyAttribute, textToClassify);
						
		instances.add(instance);
		
		return instances;
	}

	@Override
	protected void process_next(Tuple<M> object, int port) 
	{
		if(port == 1)
		{
			this.sweepArea.insert(object);
			
			@SuppressWarnings("unused")
			Iterator<Tuple<M>> oldElements = this.sweepArea.extractElementsBefore(object.getMetadata().getStart());
			
			List<Tuple<M>> list = this.sweepArea.queryOverlapsAsList(object.getMetadata());
			
			try {
				    instances.clear();
				
					for(Tuple<M> obj : list)
					{
						this.instance = WekaConverter.convertToNominalInstance(obj, this.resolver);
						instances.add(this.instance);
					}
					
					//this.instance = WekaConverter.convertToNominalInstance(object, this.resolver);
					//instances.add(this.instance);
				
					if(this.trainData == null)
						this.trainData = new Instances(this.instances);
					
					if((this.instances.size() >= this.maxTrainSize)) 
					{
						this.trainData = this.instances;
					}
			
					if((this.trainData != null) && (this.trainData.size() >= this.maxTrainSize))
					{
						evaluateClassifier();
						learnClassifier();
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
		}
		else 
		{
			if(/*(port == 0) &&*/ (isTrained) && (isEvaluated))
			{
				//if(this.trainData.size() >= this.maxTrainSize)		
				try 
				{
					Instances instances = createInstancesForClassifying(object.getAttribute(this.attributeTextToBeClassifiedPos).toString());
					classify(instances);
					prepareTupelForOutput(object);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	private void prepareTupelForOutput(Tuple<M> object) {
		Tuple<M> outputTuple = new Tuple<>(object.size()+1, false);
		System.arraycopy(object.getAttributes(), 0, outputTuple.getAttributes(), 0, object.size());
		
		outputTuple.setAttribute(object.size(), this.classification);
		outputTuple.setMetadata(object.getMetadata());
		outputTuple.setRequiresDeepClone(object.requiresDeepClone());
		
		transfer(outputTuple, 0);
	}
	
	/**
	 * Entnommen und angepasst von \de.uniol.inf.is.odysseus.mining.weka.classification.WekaClassificationLearner.java
	 * */
	public void setAlgorithm(String algorithm) 
	{
		algorithm = algorithm.toUpperCase();
		
		if (algorithm != null) 
		{
			switch (algorithm) 
			{
				case "J48":
					this.userDefinedClassifier = new J48();
					break;
				case "NAIVEBAYES":
					this.userDefinedClassifier = new NaiveBayes();
					break;
				case "DECISIONTABLE":
					this.userDefinedClassifier = new DecisionTable();
					break;
				case "SMO":
					this.userDefinedClassifier = new SMO();
					break;
				case "MULTILAYER-PERCEPTRON":
					this.userDefinedClassifier = new MultilayerPerceptron();
					break;
				case "SIMPLE-LOGISTIC":
					this.userDefinedClassifier = new SimpleLogistic();
					break;
				case "NAIVE-BAYES-TEXT":
					this.userDefinedClassifier = new NaiveBayesMultinomialText();
					break;
				default:
					throw new IllegalArgumentException(algorithm + "is not available. Choose another one!");
			}
		}	
	}

	public int getAttributeTrainSetTextPosition() {
		return attributeTrainSetTextPosition;
	}

	public void setAttributeTrainSetTextPosition(
			int attributeTrainSetTextPosition) {
		this.attributeTrainSetTextPosition = attributeTrainSetTextPosition;
	}

	public int getTotalInputports() {
		return totalInputports;
	}

	public void setTotalInputports(int totalInputports) {
		this.totalInputports = totalInputports;
	}

	@Override
	protected void process_close() {
		super.process_close();
		
		//isTrained = false;
		isEvaluated = false;
		//this.trainData.clear();
		this.instances.clear();
	}
}
