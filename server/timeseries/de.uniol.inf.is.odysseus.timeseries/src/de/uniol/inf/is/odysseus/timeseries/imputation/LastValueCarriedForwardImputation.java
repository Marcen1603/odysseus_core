package de.uniol.inf.is.odysseus.timeseries.imputation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * Time Series Imputation Strategy, can e.g. detect missing data by time
 * progress.
 * 
 * @author Christoph Schröer
 *
 */
public class LastValueCarriedForwardImputation implements IImputation<ITimeInterval> {

	/**
	 * Strategy-Name (also for eg. logical operator)
	 */
	public static final String NAME = "LastValueCarriedForward";

	/**
	 * window size, in which an element is expected. is needed to detect missing
	 * elements in this window.
	 */
	private TimeValueItem imputationWindowSize;

	private long imputationWindowSizeMillisec;

	private Tuple<ITimeInterval> lastElement;

	public LastValueCarriedForwardImputation(TimeValueItem imputationWindowSize) {
		this.imputationWindowSize = imputationWindowSize;

		long imputationWindowTime = this.imputationWindowSize.getTime();
		TimeUnit imputationWindowTimeUnit = this.imputationWindowSize.getUnit();
		this.imputationWindowSizeMillisec = TimeUnit.MILLISECONDS.convert(imputationWindowTime,
				imputationWindowTimeUnit);
	}

	@Override
	public List<Tuple<ITimeInterval>> getImputationData(Tuple<ITimeInterval> newElement) {
		List<Tuple<ITimeInterval>> imputingData = new LinkedList<Tuple<ITimeInterval>>();

		if (this.lastElement == null) {
			this.lastElement = newElement;
		} else {

			// there is a lastElement
			if (this.lastElement.getMetadata().getEnd().equals(newElement.getMetadata().getStart())) {

				// no missing data
				this.lastElement = newElement;

				// TODO: it could be, that in that element are missing
				// attribute-values, e.g. price is missing.

			} else {

				// missing data
				PointInTime imputedDataEndTime = this.lastElement.getMetadata().getEnd(); // first
																							// initialization
				do {

					PointInTime imputedDataStartTime = imputedDataEndTime;
					imputedDataEndTime = imputedDataStartTime.plus(this.imputationWindowSizeMillisec);
					Tuple<ITimeInterval> imputationElement = this.lastElement.clone();
					imputationElement.getMetadata().setStartAndEnd(imputedDataStartTime, imputedDataEndTime);

					// adding last element to imputation data
					imputingData.add(imputationElement);

				} while (!(imputedDataEndTime.equals(newElement.getMetadata().getStart())));

				// new element is now the last element
				this.lastElement = newElement;
			}
		}

		return imputingData;
	}

}
