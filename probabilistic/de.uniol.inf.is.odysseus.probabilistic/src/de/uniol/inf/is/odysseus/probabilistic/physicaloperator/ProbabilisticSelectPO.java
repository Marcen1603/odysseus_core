package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.IProbabilisticPredicate;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticSelectPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

    private final IProbabilisticPredicate<? super T> predicate;
    private IHeartbeatGenerationStrategy<T>          heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();

    public ProbabilisticSelectPO(final IProbabilisticPredicate<? super T> predicate) {
        this.predicate = predicate.clone();
    }

    public ProbabilisticSelectPO(final ProbabilisticSelectPO<T> po) {
        this.predicate = po.predicate.clone();
        this.heartbeatGenerationStrategy = po.heartbeatGenerationStrategy.clone();
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    @Override
    protected void process_next(final T object, final int port) {
        predicate.getAttributes();
        double prob = predicate.evaluate(object);
        ((IProbabilistic) object.getMetadata()).setExistence(prob);
        transfer(object);
    }

    @Override
    public void process_open() throws OpenFailedException {
        this.predicate.init();
    }

    public IProbabilisticPredicate<? super T> getPredicate() {
        return this.predicate;
    }

    @Override
    public ProbabilisticSelectPO<T> clone() {
        return new ProbabilisticSelectPO<T>(this);
    }

    @Override
    public String toString() {
        return super.toString() + " predicate: " + this.getPredicate().toString();
    }

    public IHeartbeatGenerationStrategy<T> getHeartbeatGenerationStrategy() {
        return this.heartbeatGenerationStrategy;
    }

    public void setHeartbeatGenerationStrategy(final IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy) {
        this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
    }

    @Override
    public boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
        if (!(ipo instanceof SelectPO<?>)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final ProbabilisticSelectPO<T> spo = (ProbabilisticSelectPO<T>) ipo;
        // Different sources
        if (!this.hasSameSources(spo)) {
            return false;
        }
        // Predicates match
        if (this.predicate.equals(spo.getPredicate())
                || (this.predicate.isContainedIn(spo.getPredicate()) && spo.getPredicate()
                        .isContainedIn(this.predicate))) {
            return true;
        }

        return false;
    }

}
