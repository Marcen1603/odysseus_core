package mg.dynaquest.monitor.responseprediction.learner.weka;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mg.dynaquest.monitor.responseprediction.learner.AbstractLearner;
import mg.dynaquest.monitor.responseprediction.learner.Attribute;
import mg.dynaquest.monitor.responseprediction.learner.ILearner;
import mg.dynaquest.monitor.responseprediction.preprocessor.IMetadataPreprocessor;
import mg.dynaquest.monitor.responseprediction.preprocessor.PlanOperator;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;

import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * A partial implementation of an {@link ILearner} used as basis for learners
 * using weka algorithms for classification.
 * 
 * Usually you only have to override the {@link #createClassifier()} method,
 * if you want to implement a learner based on weka algorithms
 * @author Jonas Jacobi
 * 
 * @param <T> the type of the classifier used by the subclass 
 */
public abstract class AbstractWekaLearner<T extends Classifier> extends
		AbstractLearner {

	private Map<String, T> classifiers;

	FastVector attributeInfos;

	public AbstractWekaLearner(IMetadataPreprocessor preprocessor) {
		super(preprocessor);

		this.classifiers = new HashMap<String, T>();
	}

	/**
	 * Get a {@link FastVector} containing the weka
	 * representation of all attributes.
	 * @return the weka representation of all attributes
	 */
	protected FastVector getAttributeInfos() {
		if (this.attributeInfos == null) {
			this.attributeInfos = new FastVector();
			Attribute[] attributes = getAttributes();
			for (Attribute curAttribute : attributes) {
				this.attributeInfos
						.addElement(((WekaAttribute<?>) curAttribute)
								.getAttribute());
			}

			this.attributeInfos
					.addElement(((WekaAttribute<?>) getClassAttribute())
							.getAttribute());
		}
		return this.attributeInfos;
	}

	/**
	 * Convert metadata to the weka input representation ({@link Instances})
	 * @param name the metadata to convert
	 * @return the weka representation
	 */
	protected Instances metadataToInstances(
			Collection<PreprocessedMetadata> name) {
		FastVector attributeInfo = getAttributeInfos();
		Instances instances = new Instances("instances", attributeInfo, name
				.size());
		instances.setClass(((WekaAttribute<?>) getClassAttribute())
				.getAttribute());
		for (PreprocessedMetadata curData : name) {
			Instance curInstance = new Instance(attributeInfo.size());
			curInstance.setDataset(instances);
			Attribute[] attributes = getAttributes();
			for (int i = 0; i < attributes.length; ++i) {
				curInstance.setValue(i, (String) attributes[i]
						.classify(curData));
			}

			curInstance.setClassValue(((WekaAttribute<?>) getClassAttribute())
					.classify(curData));
			instances.add(curInstance);
		}
		return instances;
	}

	/**
	 * Create a {@link FastVector}  containing multiple strings
	 * @param objs the strings
	 * @return the {@link FastVector} containing objs
	 */
	protected FastVector createFastVector(String... objs) {
		FastVector v = new FastVector();
		if (objs == null) {
			return v;
		}
		for (String o : objs) {
			v.addElement(o);
		}
		return v;
	}

	public void initLearner(Map<PlanOperator, PreprocessedMetadata> data)
			throws Exception {
		Map<String, Map<PlanOperator, PreprocessedMetadata>> plansBySource = new HashMap<String, Map<PlanOperator, PreprocessedMetadata>>();
		for (PlanOperator curPlan : data.keySet()) {
			addBySource(plansBySource, curPlan, data);
		}
		for (Entry<String, Map<PlanOperator, PreprocessedMetadata>> testPlans : plansBySource
				.entrySet()) {
			//	
			// List<PlanOperator> plans = this.preprocessor.getPlans();
			//
			// Map<String, List<PlanOperator>> plansBySource = new
			// HashMap<String, List<PlanOperator>>();
			// for (PlanOperator curPlan : plans) {
			// addBySource(plansBySource, curPlan);
			// }
			// for (Entry<String, List<PlanOperator>> testPlans : plansBySource
			// .entrySet()) {
			// try {
			// Map<PlanOperator, PreprocessedMetadata> metadata =
			// this.preprocessor
			// .getMetadata(testPlans.getValue());

			Instances instances = metadataToInstances(testPlans.getValue()
					.values());
			T classifier = createClassifier();
			classifier.buildClassifier(instances);

			this.classifiers.put(testPlans.getKey(), classifier);

		}
	}

	/**
	 * print out the status of the classifiers by 
	 * calling toString() on all classifiers
	 */
	public void printClassifiers() {
		for (Entry<String, T> curEntry : getClassifiers().entrySet()) {
			System.out.println(curEntry.getKey());
			System.out.println("------------------------------");
			System.out.println(curEntry.getValue().toString());
		}
	}

	/**
	 * Get all classifiers sorted by source
	 * @return all classifiers sorted by source
	 */
	protected Map<String, T> getClassifiers() {
		return this.classifiers;
	}

	/**
	 * Create a new classifier
	 * @return a new classifier
	 */
	abstract protected T createClassifier();

	@Override
	public double[] classify(String source, Map<Attribute, Object> values)
			throws Exception {
		Instance instance = createInstance(values);
		return getClassifiers().get(source).distributionForInstance(instance);
	}

	/**
	 * Create a single {@link Instance} out of the given values
	 * for the attributes.
	 * @param values values of the attributes
	 * @return the weka representation of the input as {@link Instance} instance
	 */
	private Instance createInstance(Map<Attribute, Object> values) {
		FastVector attributeInfo = getAttributeInfos();
		Instances instances = new Instances("instances", attributeInfo, 1);
		instances.setClass(((WekaAttribute<?>) getClassAttribute())
				.getAttribute());

		Instance instance = new Instance(attributeInfo.size());
		instance.setDataset(instances);
		instance.setClassMissing();
		for (Entry<Attribute, Object> curEntry : values.entrySet()) {
			WekaAttribute<?> attrib = (WekaAttribute<?>) curEntry.getKey();
			instance.setValue(attrib.getAttribute(), curEntry.getValue()
					.toString());
		}

		return instance;
	}
}
