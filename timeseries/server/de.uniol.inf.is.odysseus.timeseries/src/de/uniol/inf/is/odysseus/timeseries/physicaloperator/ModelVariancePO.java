package de.uniol.inf.is.odysseus.timeseries.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.timeseries.autoregression.estimator.IAutoregressionEstimator;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * 
 * This is the physical operator to create models for autoregressions on data
 * streams.
 * 
 * @author Christoph Schröer
 *
 */
@Deprecated
public class ModelVariancePO extends AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> {

	/**
	 * TODO abstraction
	 */
	private IAutoregressionEstimator<Tuple<ITimeInterval>> autoregressionLearner;

	public ModelVariancePO(IAutoregressionEstimator<Tuple<ITimeInterval>> autoregressionLearner) {
		this.autoregressionLearner = autoregressionLearner;

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void process_next(Tuple<ITimeInterval> object, int port) {

		// TODO: add object to learning data...
		// TODO: handle also, when no learning is required.

		// newLearningObject
		// this.autoregressionLearner.addLearningData(newLearningObject);

		IAutoregressionForecaster model = this.autoregressionLearner.getModel();
		Tuple<ITimeInterval> modelTuple = new Tuple<ITimeInterval>(1, false);
		modelTuple.setAttribute(0, model);

		modelTuple.setMetadata((ITimeInterval) object.getMetadata().clone());

		transfer(modelTuple);
	}

}
