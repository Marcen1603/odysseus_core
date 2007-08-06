package mg.dynaquest.monitor.responseprediction.learner.weka;

import java.util.ArrayList;
import java.util.Collection;

import mg.dynaquest.monitor.responseprediction.preprocessor.IMetadataPreprocessor;
import mg.dynaquest.monitor.responseprediction.preprocessor.IOnlineListener;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instances;

/**
 * A learner utilizing the {@link NaiveBayesUpdateable} implementation of
 * a naive bayes classifier.
 * @author Jonas Jacobi
 */
public class NaiveBayesLerner extends AbstractWekaLearner<NaiveBayesUpdateable>
		implements IOnlineListener {
	private static final long serialVersionUID = 5472928969571926595L;

	public NaiveBayesLerner(IMetadataPreprocessor preprocessor) {
		super(preprocessor);
	}
	
	public void processedRequest(String source, PreprocessedMetadata request) {
		Collection<PreprocessedMetadata> list = new ArrayList<PreprocessedMetadata>(
				1);
		list.add(request);
		Instances instances = metadataToInstances(list);
		// TODO code duplication fuer online listener evtl. abstract
		// online/offline und neural net nicht beides
		NaiveBayesUpdateable nb = getClassifiers().get(source);
		try {
			if (nb == null) {
				nb = createClassifier();
				nb.buildClassifier(instances);
				getClassifiers().put(source, nb);
			} else {
				nb.updateClassifier(instances.firstInstance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected NaiveBayesUpdateable createClassifier() {
		return new NaiveBayesUpdateable();
	}

}
