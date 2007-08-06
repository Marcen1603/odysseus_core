package mg.dynaquest.monitor.responseprediction.learner.weka;

import java.util.Map;

import mg.dynaquest.monitor.responseprediction.preprocessor.IMetadataPreprocessor;
import mg.dynaquest.monitor.responseprediction.preprocessor.IOfflineListener;
import mg.dynaquest.monitor.responseprediction.preprocessor.PlanOperator;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;

import weka.classifiers.trees.J48;

/**
 * A learner using the {@link J48} implementation of decisiontrees
 * using C4.5 algorithms. 
 * @author Jonas Jacobi
 */
public class DecisionTreeLearner extends AbstractWekaLearner<J48> implements
		IOfflineListener {
	private static final long serialVersionUID = -7629429032565516873L;
	
	public DecisionTreeLearner(IMetadataPreprocessor preprocessor) {
		super(preprocessor);
	}

	public void requestCountReached() {
		Map<PlanOperator, PreprocessedMetadata> data;
		try {
			data = getPreprocessor().getMetadata(getPreprocessor().getPlans());
			initLearner(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initLearner(Map<PlanOperator, PreprocessedMetadata> data) throws Exception {
		super.initLearner(data);
	}

	@Override
	protected J48 createClassifier() {
		J48 j48 = new J48();
		j48.setUnpruned(false);
		j48.setSubtreeRaising(true);
//		j48.setSeed(297175139);
//		j48.setUseLaplace(true);
		j48.setReducedErrorPruning(false);
//		j48.setBinarySplits(true);
		return j48;
	}
}
