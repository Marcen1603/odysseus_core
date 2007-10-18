package mg.dynaquest.monitor.responseprediction.learner;

public interface ILearnerFactory {
	public ILearner createLearner(Attribute classAttribute, Attribute... attribute);
}
