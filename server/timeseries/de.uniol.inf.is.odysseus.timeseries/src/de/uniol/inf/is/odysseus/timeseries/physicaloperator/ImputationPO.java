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
public class ImputationPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {

	/**
	 * Imputation stragey, how to deal with missing data.
	 */
	private IImputation<Tuple<M>, M> imputationStrategy;

	public ImputationPO(IImputation<Tuple<M>, M> imputationStrategy) {
		this.imputationStrategy = imputationStrategy;

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		
		List<Tuple<M>> imputationDataList = this.imputationStrategy.getImputationDataByPunctuation(punctuation);
		
		if (!imputationDataList.isEmpty()) {
			for (Tuple<M> imputationData : imputationDataList) {
				transfer(imputationData);
			}
		}
		
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<M> object, int port) {

		List<Tuple<M>> imputationDataList = this.imputationStrategy.getImputationData(object);

		if (!imputationDataList.isEmpty()) {
			for (Tuple<M> imputationData : imputationDataList) {
				transfer(imputationData);
			}
		}

		transfer(object);
	}

	@Override
	public String toString() {
		String imputationPOString = "ImputationPO, Imputation strategy: " + this.imputationStrategy.getName();
		return imputationPOString;
	}

}
