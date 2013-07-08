package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.intervalapproach.IDummyDataCreationFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

public class DefaultProbabilisticTIDummyDataCreation implements IDummyDataCreationFunction<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> {

	public DefaultProbabilisticTIDummyDataCreation(final DefaultProbabilisticTIDummyDataCreation defaultTIDummyDataCreation) {

	}

	public DefaultProbabilisticTIDummyDataCreation() {
	}

	@Override
	public ProbabilisticTuple<ITimeIntervalProbabilistic> createMetadata(final ProbabilisticTuple<ITimeIntervalProbabilistic> source) {
		return source.clone();
	}

	@Override
	public boolean hasMetadata(final ProbabilisticTuple<ITimeIntervalProbabilistic> source) {
		return true;
	}

	@Override
	public DefaultProbabilisticTIDummyDataCreation clone() {
		return new DefaultProbabilisticTIDummyDataCreation(this);
	}

}
