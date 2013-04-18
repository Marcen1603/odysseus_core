package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.intervalapproach.IDummyDataCreationFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

public class DefaultProbabilisticTIDummyDataCreation implements IDummyDataCreationFunction<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> {

    public DefaultProbabilisticTIDummyDataCreation(DefaultProbabilisticTIDummyDataCreation defaultTIDummyDataCreation) {

    }

    public DefaultProbabilisticTIDummyDataCreation() {
    }

    @Override
    public ProbabilisticTuple<ITimeIntervalProbabilistic> createMetadata(ProbabilisticTuple<ITimeIntervalProbabilistic> source) {
        return (ProbabilisticTuple<ITimeIntervalProbabilistic>) source.clone();
    }

    @Override
    public boolean hasMetadata(ProbabilisticTuple<ITimeIntervalProbabilistic> source) {
        return true;
    }

    @Override
    public DefaultProbabilisticTIDummyDataCreation clone() {
        return new DefaultProbabilisticTIDummyDataCreation(this);
    }

}
