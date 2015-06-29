package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.condition.datatypes.DeviationInformation;
import de.uniol.inf.is.odysseus.condition.enums.TrainingMode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationSequenceLearnAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

public class DeviationSequenceLearnPO<T extends Tuple<M>, M extends ITimeInterval> extends
		DeviationLearnPO<Tuple<ITimeInterval>, ITimeInterval> {

	public static final int DATA_OUT = 0;
	public static final int LEARN_OUT = 1;

	private Map<Long, DeviationInformation> deviationInfos;

	private int learnedSequence;
	private long lastSequenceCounter;
	private int sequencesToLearn;

	public DeviationSequenceLearnPO(DeviationSequenceLearnAO ao,
			IGroupProcessor<Tuple<ITimeInterval>, Tuple<ITimeInterval>> groupProcessor) {
		super(TrainingMode.ONLINE, "", groupProcessor);
		this.deviationInfos = new HashMap<Long, DeviationInformation>();
		this.sequencesToLearn = ao.getCurvesToLearn();
	}

	@Override
	protected void process_next(Tuple<ITimeInterval> tuple, int port) {
		// Get the counter from the curveCounter before this operator
		long sequenceTupleCounter = this.groupProcessor.getGroupID(tuple);

		if (sequenceTupleCounter < lastSequenceCounter
				&& (learnedSequence <= sequencesToLearn || sequencesToLearn == 0)) {
			// We are in the next curve
			learnedSequence++;
		}

		lastSequenceCounter = sequenceTupleCounter;

		if (learnedSequence <= sequencesToLearn || sequencesToLearn == 0) {
			// Sequence tuples
			DeviationInformation info = this.deviationInfos.get(sequenceTupleCounter);
			if (info == null) {
				info = new DeviationInformation();
				this.deviationInfos.put(sequenceTupleCounter, info);
			}

			double value = getValue(tuple);
			info = calcStandardDeviationOnline(value, info);

			// Transfer tuple with info about the updated standard deviation
			// (counter, mean, and std dev)
			Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(3, false);
			output.setMetadata(tuple.getMetadata());
			output.setAttribute(0, sequenceTupleCounter); // group
			output.setAttribute(1, info.mean);
			output.setAttribute(2, info.standardDeviation);
			transfer(output, LEARN_OUT);
		}

		// Give the next operator the data with our group id so that it's
		// possible to map to the learned group id
		Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(1, false);
		output.setMetadata(tuple.getMetadata());
		output.setAttribute(0, sequenceTupleCounter);
		output = output.appendList(Arrays.asList(tuple.getAttributes()), false);
		transfer(output, DATA_OUT);
	}

}
