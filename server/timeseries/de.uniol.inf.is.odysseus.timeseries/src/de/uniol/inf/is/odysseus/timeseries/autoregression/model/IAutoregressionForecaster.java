package de.uniol.inf.is.odysseus.timeseries.autoregression.model;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.machine_learning.model.Model;

/**
 * Represents a forcasting model like GARCH, ARCH or EWMA.
 * 
 * @author Christoph Schröer
 * 
 */
public interface IAutoregressionForecaster extends Model<Tuple<ITimeInterval>, ITimeInterval, Double> {

	/**
	 * Forecasts variance of new time period (t+1). The stochastic process will
	 * not be recalculated.
	 * 
	 * @param lagResiduals
	 *            sorted last q residuals. First element is the oldest element.
	 * 
	 * @param lagVariances
	 *            sorted last p variances. First element is the oldest element.
	 * @return
	 */
	public Double forecast(final LinkedList<Double> lagResiduals, final LinkedList<Double> lagVariances);

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
	public Double forecast(final LinkedList<Double> lagResiduals, final LinkedList<Double> lagVariances,
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
	public Double forecast(final LinkedList<Double> sampleResiduals);

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
	public Double forecast(final LinkedList<Double> sampleResiduals, final Integer timeHorizon);

	public Integer getQ();

	public Integer getP();

}
