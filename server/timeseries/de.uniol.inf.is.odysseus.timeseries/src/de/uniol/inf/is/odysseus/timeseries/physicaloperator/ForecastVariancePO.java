package de.uniol.inf.is.odysseus.timeseries.physicaloperator;

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
public class ForecastVariancePO extends AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> {

	public enum ForecastVarianceTupleSchema {
		RESIDUAL, MODEL
	}

	private LinkedList<Tuple<ITimeInterval>> sampleResiduals;

	private LinkedList<Double> lagResiduals;

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

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

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
			Tuple<ITimeInterval> varianceForecastTuple = new Tuple<ITimeInterval>(1, false);
			varianceForecastTuple.setAttribute(0, 0.0);

			this.updateLagVariances(countOfVariances, 0.0);

			varianceForecastTuple.setMetadata((ITimeInterval) object.getMetadata().clone());

			transfer(varianceForecastTuple);

		} else {

			// if model not changed, laged can be used, else new recalculation
			double varianceForecast;
			if (hasModelChanged) {

				// recalulcation of whole stochastic process
				LinkedList<Double> sampleResidualsDouble = new LinkedList<Double>();
				for (Tuple<ITimeInterval> tuple : this.sampleResiduals) {
					final Double sampleResidual = this.tupleSchemaHelper.getAttributeValue(tuple,
							ForecastVarianceTupleSchema.RESIDUAL);
					sampleResidualsDouble.add(sampleResidual);
				}

				// new stochastic process with new model
				varianceForecast = model.forecast(sampleResidualsDouble);
			} else {
				varianceForecast = model.forecast(this.lagResiduals, this.lagVariances);
			}

			this.updateLagVariances(countOfVariances, varianceForecast);

			Tuple<ITimeInterval> varianceForecastTuple = new Tuple<ITimeInterval>(1, false);
			varianceForecastTuple.setAttribute(0, varianceForecast);

			varianceForecastTuple.setMetadata((ITimeInterval) object.getMetadata().clone());

			transfer(varianceForecastTuple);

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
