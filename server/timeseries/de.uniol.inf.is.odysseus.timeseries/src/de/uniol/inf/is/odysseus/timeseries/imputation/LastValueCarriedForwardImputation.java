package de.uniol.inf.is.odysseus.timeseries.imputation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

/**
 * Time Series Imputation Strategy, can e.g. detect missing data by time
 * progress.
 * 
 * @author Christoph Schröer
 *
 */
public class LastValueCarriedForwardImputation extends AbstractTupleBasedImputation {

	/**
	 * Strategy-Name (also for eg. logical operator)
	 */
	public static final String NAME = "LastValueCarriedForward";

	private Tuple<ITimeInterval> lastElement;

	public LastValueCarriedForwardImputation() {
	}

	@Override
	public List<Tuple<ITimeInterval>> getImputationData(Tuple<ITimeInterval> newElement) {
		List<Tuple<ITimeInterval>> imputingData = new LinkedList<Tuple<ITimeInterval>>();

		if (this.lastElement == null) {
			this.lastElement = newElement;
		} else {

			// the imputationWindowTime to detect missing data is based on the
			// difference
			// between end and start timestamp of the last element.
			PointInTime lastStart = this.lastElement.getMetadata().getStart();
			PointInTime lastEnd = this.lastElement.getMetadata().getEnd();
			PointInTime betweenEndAndStart = lastEnd.minus(lastStart);
			long imputationWindowTime = betweenEndAndStart.getMainPoint();

			// there is a lastElement
			if (this.lastElement.getMetadata().getEnd().equals(newElement.getMetadata().getStart())) {

				// no missing data
				this.lastElement = newElement;

			} else {

				// there are missing data
				// first initialization
				PointInTime imputedDataEndTime = this.lastElement.getMetadata().getEnd();

				do {

					PointInTime imputedDataStartTime = imputedDataEndTime;
					imputedDataEndTime = imputedDataStartTime.plus(imputationWindowTime);
					Tuple<ITimeInterval> imputationElement = this.lastElement.clone();
					imputationElement.getMetadata().setStartAndEnd(imputedDataStartTime, imputedDataEndTime);

					// adding last element to imputation data
					imputingData.add(imputationElement);

				} while (!(imputedDataEndTime.equals(newElement.getMetadata().getStart())));

				// new element is the last element now
				this.lastElement = newElement;
			}
		}

		return imputingData;
	}

	@Override
	public String getName() {
		return LastValueCarriedForwardImputation.NAME;
	}

	@Override
	public IImputation<Tuple<ITimeInterval>, ITimeInterval> createInstance(Map<String, String> optionsMap) {
		return new LastValueCarriedForwardImputation();
	}

	@Override
	public List<Tuple<ITimeInterval>> getImputationDataByPunctuation(IPunctuation punctuation) {
		LinkedList<Tuple<ITimeInterval>> imputingData = new LinkedList<Tuple<ITimeInterval>>();
		if(this.lastElement == null){
			return imputingData;
		}
		// the imputationWindowTime to detect missing data is based on the
		// difference
		// between end and start timestamp of the last element.
		PointInTime lastStart = this.lastElement.getMetadata().getStart();
		PointInTime lastEnd = this.lastElement.getMetadata().getEnd();
		PointInTime betweenEndAndStart = lastEnd.minus(lastStart);
		long imputationWindowTime = betweenEndAndStart.getMainPoint();

		// there is a lastElement
		if (this.lastElement.getMetadata().getEnd().equals(punctuation.getTime())) {

			// no missing data

		} else {
			
			if(this.lastElement.getMetadata().getEnd().before(punctuation.getTime())){
				
				// there are missing data
				// first initialization
				PointInTime imputedDataEndTime = this.lastElement.getMetadata().getEnd();

				do {

					PointInTime imputedDataStartTime = imputedDataEndTime;
					imputedDataEndTime = imputedDataStartTime.plus(imputationWindowTime);
					Tuple<ITimeInterval> imputationElement = this.lastElement.clone();
					imputationElement.getMetadata().setStartAndEnd(imputedDataStartTime, imputedDataEndTime);

					// adding last element to imputation data
					imputingData.add(imputationElement);

				} while (!(imputedDataEndTime.equals(punctuation.getTime())));

				// new element is the last element now
				this.lastElement = imputingData.getLast();
				
				
				
			}

		}
		
		return imputingData;
	}

}
