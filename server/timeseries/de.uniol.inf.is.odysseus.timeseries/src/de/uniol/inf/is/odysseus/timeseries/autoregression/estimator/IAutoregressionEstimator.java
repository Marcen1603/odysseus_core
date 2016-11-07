package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import de.uniol.inf.is.odysseus.timeseries.IHasEstimationData;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * 
 * <b>T:</b> Type of data elements in estimation data.
 * 
 * @author Christoph Schröer
 */
public interface IAutoregressionEstimator<T> extends IHasEstimationData<T> {

	public String getName();

	public boolean isEstimated();

	public void estimateModel();

	public IAutoregressionForecaster<T> getModel();

	public IAutoregressionForecaster<T> getModel(boolean estimate);

}
