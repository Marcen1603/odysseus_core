/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticRelationalPredicate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.sweeparea.FastArrayList;
import de.uniol.inf.is.odysseus.sweeparea.IFastList;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticJoinTISweepArea<T extends ProbabilisticTuple<? extends IProbabilisticTimeInterval>> extends DefaultTISweepArea<T> {

    public ProbabilisticJoinTISweepArea() {
        super(new FastArrayList<T>());
    }

    public ProbabilisticJoinTISweepArea(IFastList<T> list) {
        super(list);
    }

    /**
     * Class constructor.
     * 
     * @param sweepArea
     *            The sweep area
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public ProbabilisticJoinTISweepArea(ProbabilisticJoinTISweepArea<T> sweepArea) throws InstantiationException, IllegalAccessException {
        super(sweepArea);
    }

    @Override
    public void insert(T s) {
        synchronized (this.getElements()) {
            setLatestTimeStamp(s);
            this.getElements().add(s);
        }
    }

    @Override
    public Iterator<T> queryCopy(T element, Order order, boolean extract) {
        LinkedList<T> result = new LinkedList<T>();
        Iterator<T> iter;
        synchronized (this.getElements()) {
            switch (order) {
                case LeftRight:
                    iter = this.getElements().iterator();
                    while (iter.hasNext()) {
                        T next = iter.next();
                        if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
                            continue;
                        }
                        if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
                            break;
                        }
                        ProbabilisticRelationalPredicate probabilisticPredicate = ((ProbabilisticRelationalPredicate) getQueryPredicate());
                        ProbabilisticBooleanResult probabilisticResult = probabilisticPredicate.probabilisticEvaluate(element, next);
                        if (probabilisticResult.getProbability() > 0.0) {
                            result.add(next);
                            if (extract) {
                                iter.remove();
                            }
                        }

                    }
                    break;
                case RightLeft:
                    iter = this.getElements().iterator();
                    while (iter.hasNext()) {
                        T next = iter.next();
                        if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
                            continue;
                        }
                        if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
                            break;
                        }
                        if (getQueryPredicate().evaluate(next, element)) {
                            result.add(next);
                            if (extract) {
                                iter.remove();
                            }
                        }
                    }
                    break;
            }
        }
        return result.iterator();
    }
}
