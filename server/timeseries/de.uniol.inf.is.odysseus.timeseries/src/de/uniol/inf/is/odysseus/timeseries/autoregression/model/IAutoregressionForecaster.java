package de.uniol.inf.is.odysseus.timeseries.autoregression.model;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * Represents a forcasting model like GARCH, ARCH or EWMA.
 * 
 * </br>
 * </br>
 * 
 * <b>T:</b> Type of the data in the time series. The time series is modeled as
 * a Linked List with elements of type T. The index can be seen as the time,
 * independent from the originally time domain.
 * 
 * @author Christoph Schröer
 * 
 */
public interface IAutoregressionForecaster<T> extends IClone {

	/**
	 * Forecasts variance of new time period (t+1). The stochastic process will
	 * not be recalculated. Forecasting is based on the lag variables.
	 * 
	 * @param lagResiduals
	 *            sorted last q residuals. First element is the oldest element.
	 * 
	 * @param lagVariances
	 *            sorted last p variances. First element is the oldest element.
	 * @return
	 */
	public Double forecast(final LinkedList<T> lagResiduals, final LinkedList<T> lagVariances);

	/**
	 * Forecasts variance of new time period (t+timehorizon). The stochastic
	 * process will not be recalculated.
	 * 
	 * @param lagResiduals
	 *            sorted last q residuals. First element is the oldest element.
	 * 
	 * @param lagVariances
	 *            sorted last p variances. First element is the oldest element.
	 * @param timeHorizon
	 *            forecasting for a specific time horizon.
	 * @return
	 */
	public Double forecast(final LinkedList<T> lagResiduals, final LinkedList<T> lagVariances,
			final Integer timeHorizon);

	/**
	 * Forecasts variance of new time period (t+1). Recalculating the stochastic
	 * process by the measured residuals.
	 * 
	 * @param sampleResiduals
	 *            time ordered list with residuals. First element is the oldest
	 *            element.
	 * 
	 * @return
	 */
	public Double forecast(final LinkedList<T> sampleResiduals);

	/**
	 * Forecasts variance of new time period (t+timehorizon). Recalculating the
	 * stochastic process by the measured residuals.
	 * 
	 * @param sampleResiduals
	 *            time ordered list with residuals. First element is the oldest
	 *            element.
	 * 
	 * @param timeHorizon
	 *            forecasting for a specific time horizon.
	 * @return
	 */
	public Double forecast(final LinkedList<T> sampleResiduals, final Integer timeHorizon);

	/**
	 * Number of residuals. also called number of autoregressive parameter
	 * 
	 * @return
	 */
	public Integer getQ();

	/**
	 * Number of GARCH-Parameter, the conditional variances, also called number
	 * of moving average-parameters
	 * 
	 * @return
	 */
	public Integer getP();

}
