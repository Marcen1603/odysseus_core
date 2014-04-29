package de.uniol.inf.is.odysseus.mining.classification;

import java.util.List;
import java.util.Map;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mining.weka.WekaAttributeResolver;
import de.uniol.inf.is.odysseus.mining.weka.WekaConverter;

public class WekaClassificationLearner<M extends ITimeInterval> implements IClassificationLearner<M> {

	private AbstractClassifier wekaLearner = new NaiveBayes();
	private WekaAttributeResolver war;
	private int classIndex;
	private SDFAttribute classAttribute;
	private SDFSchema recognizedSchema;
	
	public static String[] MODELS = {"J48", "NAIVEBAYES", "DECISIONTABLE", "LINEAR-REGRESSION", "LOGISTIC", "GAUSSIAN-PROCESSES","SMO", "MULTILAYER-PERCEPTRON", "SIMPLE-LOGISTIC", "SIMPLE_LINEAR-REGRESSION", "SMO-REGRESSION", "NAIVE-BAYES-TEXT"}; 

	@Override
	public void setOptions(Map<String, String> options) {
		try {

			String modelSmall = options.get("model");
			String model = modelSmall.toUpperCase();
			if (model != null) {
				switch (model) {
				case "J48":
					wekaLearner = new J48();
					break;
				case "NAIVEBAYES":
					wekaLearner = new NaiveBayes();
					break;
				case "DECISIONTABLE":
					wekaLearner = new DecisionTable();
					break;
				case "LINEAR-REGRESSION":
					wekaLearner = new LinearRegression();
					break;
				case "LOGISTIC":
					wekaLearner = new Logistic();
					break;
				case "GAUSSIAN-PROCESSES":
					wekaLearner = new GaussianProcesses();
					break;
				case "SMO":
					wekaLearner = new SMO();
					break;
				case "MULTILAYER-PERCEPTRON":
					wekaLearner = new MultilayerPerceptron();
					break;
				case "SIMPLE-LOGISTIC":
					wekaLearner = new SimpleLogistic();
					break;
				case "SIMPLE_LINEAR-REGRESSION":
					wekaLearner = new SimpleLinearRegression();
					break;
				case "SMO-REGRESSION":
					wekaLearner = new SMOreg();
					break;
				case "NAIVE-BAYES-TEXT":
					wekaLearner = new NaiveBayesMultinomialText();
					break;
				default:
					throw new IllegalArgumentException("There is no classifier model called " + modelSmall + "!");
				}
				if(options.containsKey("arguments")){
					String[] wekaoptions = weka.core.Utils.splitOptions(options.get("arguments"));
					wekaLearner.setOptions(wekaoptions);
				}
			} else {
				throw new IllegalArgumentException("Parameter \"model\" is not defined!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public IClassifier<M> createClassifier(List<Tuple<M>> tuples) {
		try {
			if(wekaLearner instanceof SMOreg && tuples.size()<=1){
				return null;
			}
			Instances instances = WekaConverter.convertToInstances(tuples, this.war);
			if(instances.size()<=0){
				return null;
			}
			instances.setClassIndex(this.classIndex);
			wekaLearner.buildClassifier(instances);
			WekaClassifier<M> classifier = new WekaClassifier<>(war.getNominals(), AbstractClassifier.makeCopy(wekaLearner));
			classifier.init(this.recognizedSchema, classAttribute);
			return classifier;
		} catch (Exception e) {
			if(e.getMessage().equalsIgnoreCase("All class values are the same. At least two class values should be different")){
				return null;
			}else{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void init(SDFSchema inputschema, SDFAttribute classAttribute, Map<SDFAttribute, List<String>> nominals) {
		this.war = new WekaAttributeResolver(inputschema, nominals);
		this.classIndex = inputschema.indexOf(classAttribute);
		this.classAttribute = classAttribute;
		SDFSchema recognizedSchema = inputschema.clone();
		recognizedSchema = SDFSchema.remove(recognizedSchema, classAttribute);
		this.recognizedSchema = recognizedSchema;
	}
	
	@Override
	public String getName() {
		return "weka";
	}

}
