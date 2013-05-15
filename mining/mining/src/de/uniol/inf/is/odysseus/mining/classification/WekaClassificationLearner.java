package de.uniol.inf.is.odysseus.mining.classification;

import java.util.List;
import java.util.Map;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
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
				default:
					throw new IllegalArgumentException("There is no classifier model called " + modelSmall + "!");
				}
				String[] wekaoptions = weka.core.Utils.splitOptions(options.get("arguments"));
				wekaLearner.setOptions(wekaoptions);
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
			Instances instances = WekaConverter.convertToInstances(tuples, this.war);
			instances.setClassIndex(this.classIndex);
			wekaLearner.buildClassifier(instances);
			WekaClassifier<M> classifier = new WekaClassifier<>(war.getNominals(), AbstractClassifier.makeCopy(wekaLearner));
			classifier.init(this.recognizedSchema, classAttribute);
			return classifier;
		} catch (Exception e) {
			e.printStackTrace();
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

}
