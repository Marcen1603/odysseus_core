package de.uniol.inf.is.odysseus.timeseries.physicaloperator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class ImputationPO extends AbstractPipe<Tuple<ITimeInterval>, Tuple<ITimeInterval>> {

	/**
	 * 
	 */
	private TimeValueItem imputationWindowSize;

	/**
	 * 
	 */
	private long imputationWindowSizeMillisec;

	private Tuple<ITimeInterval> lastElement;

	public ImputationPO(TimeValueItem imputationWindowSize) {
		this.imputationWindowSize = imputationWindowSize;

		long imputationWindowTime = this.imputationWindowSize.getTime();
		TimeUnit imputationWindowTimeUnit = this.imputationWindowSize.getUnit();
		this.imputationWindowSizeMillisec = TimeUnit.MILLISECONDS.convert(imputationWindowTime,
				imputationWindowTimeUnit);

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(Tuple<ITimeInterval> object, int port) {

		if (this.lastElement == null) {
			this.lastElement = object;
		} else {

			// there is a lastElement

			// Pruefung ob start neues Element = end altes Element

			if (this.lastElement.getMetadata().getEnd().equals(object.getMetadata().getStart())) {
				// no missing data
				this.lastElement = object;

			} else {
				Tuple<ITimeInterval> imputationElement = this.lastElement.clone();
				PointInTime lastElementEndTime = this.lastElement.getMetadata().getEnd();
				imputationElement.getMetadata().setStartAndEnd(lastElementEndTime,
						lastElementEndTime.plus(this.imputationWindowSizeMillisec));
				
				// transfer lastElement as Imputation
				transfer(imputationElement);
				this.lastElement = object;
			}
		}

		transfer(object);
	}

}
