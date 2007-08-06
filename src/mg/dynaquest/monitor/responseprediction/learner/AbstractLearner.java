package mg.dynaquest.monitor.responseprediction.learner;

import java.util.HashMap;
import java.util.Map;

import mg.dynaquest.monitor.responseprediction.preprocessor.IMetadataPreprocessor;
import mg.dynaquest.monitor.responseprediction.preprocessor.PlanOperator;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;



/**
 * A partial implementation of the {@link ILearner} interface.
 * 
 * New learners subclassing this class only need to implement {@link #classify(String, Map)}
 * and {@link ILearner#initLearner(Map)}.
 * Be aware that {@link #classify(PlanOperator, Map)} just forwards 
 * {@link #classify(String, Map)} with the source being the source of the leftmost
 * access operator. If you want a different behaviour just override the method.
 * @author Jonas Jacobi
 */
public abstract class AbstractLearner implements ILearner {
	private Attribute classAttribute;
	private Attribute[] attributes;
	protected IMetadataPreprocessor preprocessor;

	/**
	 * Constructor
	 * @param preprocessor the learner gets the metadata from this preprocessor
	 */
	public AbstractLearner(IMetadataPreprocessor preprocessor) {
		this.preprocessor = preprocessor;
	}

	/* (non-Javadoc)
	 * @see jj.dynaquest.responsepredictor.learner.ILearner#setAttributes(jj.dynaquest.responsepredictor.learner.Attribute)
	 */
	public void setAttributes(Attribute... attributes) {
		this.attributes = attributes;
	}
	
	/* (non-Javadoc)
	 * @see jj.dynaquest.responsepredictor.learner.ILearner#getAttributes()
	 */
	public Attribute[] getAttributes() {
		return this.attributes;
	}
	
	/* (non-Javadoc)
	 * @see jj.dynaquest.responsepredictor.learner.ILearner#setClassAttribute(jj.dynaquest.responsepredictor.learner.Attribute)
	 */
	public void setClassAttribute(Attribute classAttribute) {
		this.classAttribute = classAttribute;
	}

	/* (non-Javadoc)
	 * @see jj.dynaquest.responsepredictor.learner.ILearner#getClassAttribute()
	 */
	public Attribute getClassAttribute() {
		return this.classAttribute;
	}
	
	public double[] classify(PlanOperator po, Map<Attribute, Object> values) throws Exception {
		while(po.hasSubOperators()) {
			po = po.getSubOperators().get(0);
		}
		return classify(po.getSource(), values);
	}
	
	/* (non-Javadoc)
	 * @see jj.dynaquest.responsepredictor.learner.ILearner#classify(java.lang.String, java.util.Map)
	 */
	public abstract double[] classify(String source, Map<Attribute, Object> values) throws Exception;

	/**
	 * This method is used to extract the access operators of a collection of plans and sort them
	 * by the source they access.
	 * @param plansBySource the sorted map
	 * @param curPlan the current planoperator 
	 * @param data all available metadata
	 */
	protected void addBySource(Map<String, Map<PlanOperator, PreprocessedMetadata>> plansBySource, PlanOperator curPlan, Map<PlanOperator, PreprocessedMetadata> data) {
		if (!curPlan.isAccessPlanOperator()) {
			for (PlanOperator sub : curPlan.getSubOperators()) {
				addBySource(plansBySource, sub, data);
			}
		} else {
			Map<PlanOperator, PreprocessedMetadata> sourcePlans = plansBySource.get(curPlan
					.getSource());
			if (sourcePlans == null) {
				sourcePlans = new HashMap<PlanOperator, PreprocessedMetadata>();
				plansBySource.put(curPlan.getSource(), sourcePlans);
			}
	
			sourcePlans.put(curPlan, data.get(curPlan));
		}
	}

	/**
	 * Extract and sort the metadata of access planoperators out of a collection of metadata for plans
	 * @param data all planoperators and the metadata for them
	 * @return a map with the metadata sorted by source
	 */
	protected Map<String, Map<PlanOperator, PreprocessedMetadata>> plansBySource(Map<PlanOperator, PreprocessedMetadata> data) {
		Map<String, Map<PlanOperator, PreprocessedMetadata>> plansBySource = new HashMap<String, Map<PlanOperator, PreprocessedMetadata>>();
		for (PlanOperator curPlan : data.keySet()) {
			addBySource(plansBySource, curPlan, data);
		}
		return plansBySource;
	}

	public IMetadataPreprocessor getPreprocessor() {
		return this.preprocessor;
	}

}