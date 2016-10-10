package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import java.util.List;

import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * 
 * <b>T:</b> Type of data elements in estimation data.
 * 
 * @author Christoph Schröer
 */
public interface IAutoregressionEstimator<T> {

	public String getName();

	public void addEstimationData(T newEstimationObject);

	public void addEstimationData(List<T> newEstimationObjects);

	public void removeEstimationData(T oldEstimationObject);

	public void removeEstimationData(List<T> oldEstimationObjects);

	public void clear();

	public boolean isEstimated();

	public void estimateModel();

	public IAutoregressionForecaster<T> getModel();

	public IAutoregressionForecaster<T> getModel(boolean estimate);

}
