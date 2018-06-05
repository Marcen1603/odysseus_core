package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.machine_learning.learner.AbstractLearner;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * 
 * This is an abstract learner for autoregression models like GARCH
 * 
 * Idea of implementation {@link AbstractLearner}.
 * 
 * @author Christoph Schröer
 *
 */
abstract public class AbstractAutoregressionModelEstimator<T> implements IAutoregressionEstimator<T> {

	protected LinkedList<T> estimationData = new LinkedList<T>();
	protected boolean isModelUpToDate = false;

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public void addEstimationData(T newEstimationObject) {
		if (this.estimationData.add(newEstimationObject)) {
			this.isModelUpToDate = false;
		}
	}

	@Override
	public void removeEstimationData(T oldEstimationObject) {
		this.estimationData.removeFirst();
		this.isModelUpToDate = false;
	}

	@Override
	public void clear() {
		this.estimationData.clear();
		this.isModelUpToDate = false;
	}

	@Override
	public boolean isEstimated() {
		return this.isModelUpToDate;
	}

	@Override
	public IAutoregressionForecaster<T> getModel() {
		return this.getModel(true);
	}
}
