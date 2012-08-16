package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticSelectPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> implements
        IHasPredicate {

    private final IPredicate<? super T>     predicate;
    private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();

    public ProbabilisticSelectPO(final IPredicate<? super T> predicate) {
        this.predicate = predicate.clone();
    }

    public ProbabilisticSelectPO(final ProbabilisticSelectPO<T> po) {
        this.predicate = po.predicate.clone();
        this.heartbeatGenerationStrategy = po.heartbeatGenerationStrategy.clone();
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.INPUT;
    }

    @Override
    protected void process_next(final Tuple<T> object, final int port) {
        // if (predicate.evaluate(object)) {
        // transfer(object);
        // } else {
        // // Send filtered data to output port 1
        // transfer(object, 1);
        // heartbeatGenerationStrategy.generateHeartbeat(object, this);
        // }
    }

    @Override
    public void process_open() throws OpenFailedException {
        this.predicate.init();
    }

    @Override
    public IPredicate<? super T> getPredicate() {
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

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {
        this.sendPunctuation(timestamp);
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
