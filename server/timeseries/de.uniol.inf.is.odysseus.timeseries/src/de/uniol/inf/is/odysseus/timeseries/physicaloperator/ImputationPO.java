package de.uniol.inf.is.odysseus.timeseries.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.timeseries.imputation.IImputation;

/**
 * 
 * This is the physical operator to deal with missing data and impute missing
 * data.
 * 
 * @author Christoph Schröer
 *
 */
public class ImputationPO extends AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> {

	/**
	 * Imputation stragey, how to deal with missing data.
	 */
	private IImputation<ITimeInterval> imputationStrategy;

	public ImputationPO(IImputation<ITimeInterval> imputationStrategy) {
		this.imputationStrategy = imputationStrategy;

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

		List<Tuple<ITimeInterval>> imputationDataList = this.imputationStrategy.getImputationData(object);

		if (!imputationDataList.isEmpty()) {
			for (Tuple<ITimeInterval> imputationData : imputationDataList) {
				transfer(imputationData);
			}
		}

		transfer(object);
	}

}
