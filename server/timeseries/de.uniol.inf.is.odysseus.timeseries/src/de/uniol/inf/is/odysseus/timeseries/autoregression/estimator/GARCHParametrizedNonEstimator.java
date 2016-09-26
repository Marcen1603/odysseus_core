package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.machine_learning.model.Model;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * 
 * This is a GARCH, which does not learn. It has only a parameterized
 * model.
 * 
 * @author Christoph Schröer
 *
 */
public class GARCHParametrizedNonEstimator extends AbstractAutoregressionModelEstimator {

	IAutoregressionForecaster parametrizedGARCHModel;

	public GARCHParametrizedNonEstimator(IAutoregressionForecaster parametrizedGARCHModel) {
		super();
		this.parametrizedGARCHModel = parametrizedGARCHModel;
	}

	@Override
	public void trainModel() {
		this.isModelUpToDate = true;
	}

	@Override
	public Model<Tuple<ITimeInterval>, ITimeInterval, Double> getModel(boolean train) {
		// Default Model, no learning
		return this.parametrizedGARCHModel;
	}

	@Override
	public void addLearningData(Set<Tuple<ITimeInterval>> newLearningObjects) {
		// not required/necessary
	}

	@Override
	public void addLearningData(Tuple<ITimeInterval> newLearningObject) {
		// not required/necessary
	}

	@Override
	public void removeLearningData(Set<Tuple<ITimeInterval>> oldLearningObjects) {
		// not required/necessary
	}

	@Override
	public void removeLearningData(Tuple<ITimeInterval> oldLearningObject) {
		// not required/necessary
	}

}
