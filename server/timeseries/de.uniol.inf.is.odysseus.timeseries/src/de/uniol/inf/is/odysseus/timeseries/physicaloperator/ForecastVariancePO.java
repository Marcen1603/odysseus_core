package de.uniol.inf.is.odysseus.timeseries.physicaloperator;

import java.util.Date;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recommendation.util.TupleSchemaHelper;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * 
 * This is the physical operator to forecast variances with autoregressive
 * models. Model and data, which is required for forecasting, are in data
 * stream.
 * 
 * It is neccessary for changing models, that the last values of the
 * return/residuals have to be saved (e. g. window) to recalculate the
 * stochastic process by the new model.
 * 
 * @author Christoph Schröer
 *
 */
@SuppressWarnings("rawtypes")
@Deprecated
public class ForecastVariancePO extends AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> {

	public enum ForecastVarianceTupleSchema {
		RESIDUAL, MODEL
	}

	private Integer forecastHorizon = 1;

	private LinkedList<Tuple<ITimeInterval>> sampleResiduals;

	/**
	 * list with historical (lag) residuals
	 */
	private LinkedList<Double> lagResiduals;

	/**
	 * list with historical (lag) variances
	 */
	private LinkedList<Double> lagVariances;

	/**
	 * To detect, wether a model is changed.
	 */
	
	private IAutoregressionForecaster oldModel;

	/**
	 * QN: Wiederverwendung aus Recommendation.?
	 */
	private final TupleSchemaHelper<ITimeInterval, ForecastVarianceTupleSchema> tupleSchemaHelper;

	public ForecastVariancePO(TupleSchemaHelper<ITimeInterval, ForecastVarianceTupleSchema> tupleSchemaHelper) {
		this.tupleSchemaHelper = tupleSchemaHelper;
		this.sampleResiduals = new LinkedList<Tuple<ITimeInterval>>();
		this.lagResiduals = new LinkedList<Double>();
		this.lagVariances = new LinkedList<Double>();
		this.forecastHorizon = 1;
	}

	public ForecastVariancePO(TupleSchemaHelper<ITimeInterval, ForecastVarianceTupleSchema> tupleSchemaHelper,
			Integer forecastHorizon) {
		this.tupleSchemaHelper = tupleSchemaHelper;
		this.sampleResiduals = new LinkedList<Tuple<ITimeInterval>>();
		this.lagResiduals = new LinkedList<Double>();
		this.lagVariances = new LinkedList<Double>();
		this.forecastHorizon = forecastHorizon;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<ITimeInterval> object, int port) {

		// initialisation;
		IAutoregressionForecaster model = this.tupleSchemaHelper.getAttributeValue(object,
				ForecastVarianceTupleSchema.MODEL);

		// new model?
		boolean hasModelChanged = false;
		if (!(model.equals(this.oldModel))) {
			hasModelChanged = true;
		}
		this.oldModel = model;

		final Double residual = this.tupleSchemaHelper.getAttributeValue(object, ForecastVarianceTupleSchema.RESIDUAL);

		PointInTime currentStart = object.getMetadata().getStart();
		// between
		PointInTime between;

		try {
			Tuple<ITimeInterval> lastElement = this.sampleResiduals.getLast();
			between = object.getMetadata().getStart().minus(lastElement.getMetadata().getStart());
		} catch (Exception e) {
			between = object.getMetadata().getEnd().minus(object.getMetadata().getStart());
		}

		// add current object to sampleData
		this.sampleResiduals.add(object);

		// remove old sample data
		if (this.sampleResiduals.getFirst().getMetadata().getEnd().beforeOrEquals(currentStart)) {
			this.sampleResiduals.removeFirst();
		}

		// ----------------------------
		// init
		Integer countOfResiudals = model.getQ();
		Integer countOfVariances = model.getP();
		this.updateLagResiduals(countOfResiudals, residual);

		if (countOfResiudals > this.lagResiduals.size() || countOfVariances > this.lagVariances.size()) {
			// not enough data yet
			// transfer default variance

			// PointInTime between =
			// object.getMetadata().getEnd().minus(object.getMetadata().getStart());
			// PointInTime currentForecastStart = object.getMetadata().getEnd();
			PointInTime currentForecastStart = object.getMetadata().getStart().plus(between);
			new Date(currentForecastStart.getMainPoint());
			new Date(currentForecastStart.plus(between).getMainPoint());

			Tuple<ITimeInterval> varianceForecastTuple = new Tuple<ITimeInterval>(4, false);
			varianceForecastTuple.setAttribute(0, 0.0);
			varianceForecastTuple.setAttribute(1, 1);
			varianceForecastTuple.setAttribute(2, Long.toString(currentForecastStart.getMainPoint()));
			varianceForecastTuple.setAttribute(3, Long.toString(currentForecastStart.plus(between).getMainPoint()));

			this.updateLagVariances(countOfVariances, 0.0);

			varianceForecastTuple.setMetadata((ITimeInterval) object.getMetadata().clone());

			transfer(varianceForecastTuple);

		} else {

			// recalculation of whole stochastic process
			LinkedList<Double> sampleResidualsDouble = new LinkedList<Double>();
			if (hasModelChanged) {

				for (Tuple<ITimeInterval> tuple : this.sampleResiduals) {
					final Double sampleResidual = this.tupleSchemaHelper.getAttributeValue(tuple,
							ForecastVarianceTupleSchema.RESIDUAL);
					sampleResidualsDouble.add(sampleResidual);
				}
			}

			// PointInTime between =
			// object.getMetadata().getEnd().minus(object.getMetadata().getStart());
			// PointInTime currentForecastStart = object.getMetadata().getEnd();
			PointInTime currentForecastStart = object.getMetadata().getStart().plus(between);
			for (int currentForecastHorizon = 1; currentForecastHorizon <= this.forecastHorizon; currentForecastHorizon++, currentForecastStart = currentForecastStart
					.plus(between)) {

				// if model not changed, lagged data can be used, else new
				// recalculation
				double varianceForecast;
				if (hasModelChanged) {
					// new stochastic process with new model
					varianceForecast = model.forecast(sampleResidualsDouble, currentForecastHorizon);
				} else {
					varianceForecast = model.forecast(this.lagResiduals, this.lagVariances, currentForecastHorizon);
				}

				if (currentForecastHorizon == 1) {
					// only at the first time horizon the lags have to be
					// updated.
					this.updateLagVariances(countOfVariances, varianceForecast);
				}

				new Date(currentForecastStart.getMainPoint());
				new Date(currentForecastStart.plus(between).getMainPoint());

				Tuple<ITimeInterval> varianceForecastTuple = new Tuple<ITimeInterval>(4, false);
				varianceForecastTuple.setAttribute(0, varianceForecast);
				varianceForecastTuple.setAttribute(1, currentForecastHorizon);
//				varianceForecastTuple.setAttribute(2, currentForecastStart);
//				varianceForecastTuple.setAttribute(3, currentForecastStart.plus(between));
//				
				// XXX: String, because predicates with timestamps do   not function
				varianceForecastTuple.setAttribute(2, Long.toString(currentForecastStart.getMainPoint()));
				varianceForecastTuple.setAttribute(3, Long.toString(currentForecastStart.plus(between).getMainPoint()));
				
				// Tuple<ITimeInterval> oldTuple = object.clone();
				// oldTuple.setAttribute(oldTuple.getAttributes().length + 1,
				// varianceForecast);

				//
				//
				varianceForecastTuple.setMetadata((ITimeInterval) object.getMetadata().clone());

				transfer(varianceForecastTuple);

			}

		}

	}

	private void updateLagResiduals(Integer q, Double newResidual) {

		if (q > this.lagResiduals.size()) {
			this.lagResiduals.addLast(newResidual);
		} else if (q <= this.lagResiduals.size()) {
			this.lagResiduals.addLast(newResidual);
			this.lagResiduals.removeFirst();
		}
	}

	private void updateLagVariances(Integer p, Double newVariance) {

		if (p > this.lagVariances.size()) {
			this.lagVariances.addLast(newVariance);
		} else if (p <= this.lagVariances.size()) {
			this.lagVariances.addLast(newVariance);
			this.lagVariances.removeFirst();
		}
	}

}
