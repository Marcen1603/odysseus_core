package mg.dynaquest.monitor.responseprediction;

import java.util.Map;

import mg.dynaquest.monitor.responseprediction.learner.Attribute;
import mg.dynaquest.monitor.responseprediction.learner.ILearnerFactory;

public interface IPredictorManager {
	public void createLearner(ILearnerFactory factory, Attribute classAttribute, Attribute... attributes);
	public double[] getPrediction(Attribute classAttribute, String source, Map<Attribute, Object> attributeValues);
}
