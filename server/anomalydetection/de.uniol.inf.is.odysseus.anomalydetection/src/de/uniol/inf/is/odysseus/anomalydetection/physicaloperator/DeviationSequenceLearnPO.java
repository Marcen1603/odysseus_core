package de.uniol.inf.is.odysseus.anomalydetection.physicaloperator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.anomalydetection.datatypes.DeviationInformation;
import de.uniol.inf.is.odysseus.anomalydetection.enums.TrainingMode;
import de.uniol.inf.is.odysseus.anomalydetection.logicaloperator.DeviationSequenceLearnAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

public class DeviationSequenceLearnPO<T extends Tuple<M>, M extends ITimeInterval>
		extends DeviationLearnPO<Tuple<ITimeInterval>, ITimeInterval> {

	public static final int DATA_OUT = 0;
	public static final int LEARN_OUT = 1;

	private Map<Object, DeviationInformation> deviationInfos;

	private int learnedSequence;
	private long lastSequenceCounter;
	private int sequencesToLearn;

	public DeviationSequenceLearnPO(DeviationSequenceLearnAO ao,
			IGroupProcessor<Tuple<ITimeInterval>, Tuple<ITimeInterval>> groupProcessor) {
		super(TrainingMode.ONLINE, "", groupProcessor);
		this.deviationInfos = new HashMap<>();
		this.sequencesToLearn = ao.getCurvesToLearn();
	}

	@Override
	protected void process_next(Tuple<ITimeInterval> tuple, int port) {
		// Get the counter from the curveCounter before this operator
		Long sequenceTupleCounter = this.groupProcessor.getAscendingGroupID(tuple);

		if (sequenceTupleCounter < lastSequenceCounter
				&& (learnedSequence <= sequencesToLearn || sequencesToLearn == 0)) {
			// We are in the next sequence
			learnedSequence++;
		}

		lastSequenceCounter = sequenceTupleCounter;

		// Sequence tuples
		DeviationInformation info = this.deviationInfos.get(sequenceTupleCounter);
		if (info == null) {
			info = new DeviationInformation();
			this.deviationInfos.put(sequenceTupleCounter, info);
		}

		if (learnedSequence <= sequencesToLearn || sequencesToLearn == 0) {
			double value = getValue(tuple);
			info = calcStandardDeviationOnline(value, info);
		}

		// Transfer tuple with info about the (maybe) updated standard deviation
		// (counter, mean, and std dev)
		Tuple<ITimeInterval> outputLearn = new Tuple<ITimeInterval>(3, false);
		outputLearn.setMetadata(tuple.getMetadata());
		outputLearn.setAttribute(0, sequenceTupleCounter); // group
		outputLearn.setAttribute(1, info.mean);
		outputLearn.setAttribute(2, info.standardDeviation);
		transfer(outputLearn, LEARN_OUT);

		// Give the next operator the data with our group id so that it's
		// possible to map to the learned group id
		Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(1, false);
		output.setMetadata(tuple.getMetadata());
		output.setAttribute(0, sequenceTupleCounter);
		output = output.appendList(Arrays.asList(tuple.getAttributes()), false);
		transfer(output, DATA_OUT);
	}

}
