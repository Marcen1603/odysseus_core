package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.datatypes.DeviationInformation;
import de.uniol.inf.is.odysseus.condition.enums.TrainingMode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationSequenceLearnAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class DeviationSequenceLearnPO<T extends Tuple<M>, M extends ITimeInterval> extends
		DeviationLearnPO<Tuple<ITimeInterval>, ITimeInterval> {

	private List<DeviationInformation> deviationInfo;
	private String counterAttributeName = "counter";

	private int learnedCurves;
	private int lastCurveCounter;
	private int curvesToLearn;

	public DeviationSequenceLearnPO(DeviationSequenceLearnAO ao) {
		super(TrainingMode.ONLINE, "");
		this.deviationInfo = new ArrayList<DeviationInformation>();
		this.curvesToLearn = ao.getCurvesToLearn();
	}

	@Override
	protected void process_next(Tuple<ITimeInterval> tuple, int port) {
		// Get the counter from the curveCounter before this operator
		int curveTupleCounter = getCounter(tuple);

		if (curveTupleCounter < lastCurveCounter && learnedCurves <= curvesToLearn) {
			// We are in the next curve
			learnedCurves++;
		}

		lastCurveCounter = curveTupleCounter;

		if (learnedCurves <= curvesToLearn) {
			// Curve tuples
			DeviationInformation info = null;
			if (curveTupleCounter >= deviationInfo.size()) {
				info = new DeviationInformation();
				deviationInfo.add(curveTupleCounter, info);
			} else {
				info = deviationInfo.get(curveTupleCounter);
				if (info == null) {
					info = new DeviationInformation();
					deviationInfo.add(info);
				}
			}

			double value = getValue(tuple);
			info = calcStandardDeviationOnline(value, info);

			// Transfer tuple with info about the updated standard deviation
			// (counter, mean, and std dev)
			Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(3, false);
			output.setMetadata(tuple.getMetadata());
			output.setAttribute(0, curveTupleCounter);
			output.setAttribute(1, info.mean);
			output.setAttribute(2, info.standardDeviation);
			transfer(output);
		}
	}

	private int getCounter(Tuple<ITimeInterval> tuple) {
		int counterIndex = getInputSchema(0).findAttributeIndex(counterAttributeName);
		if (counterIndex >= 0) {
			int counter = tuple.getAttribute(counterIndex);
			return counter;
		}
		return 0;
	}

}
