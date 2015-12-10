/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AbstractTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticRelationalPredicate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.sweeparea.FastArrayList;
import de.uniol.inf.is.odysseus.sweeparea.IFastList;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticJoinTISweepArea<T extends ProbabilisticTuple<? extends IProbabilisticTimeInterval>> extends AbstractTISweepArea<T> {

	private static final long serialVersionUID = -7508914945473227317L;

	public ProbabilisticJoinTISweepArea() {
        super(new FastArrayList<T>());
    }

    public ProbabilisticJoinTISweepArea(final IFastList<T> list) {
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
    public ProbabilisticJoinTISweepArea(final ProbabilisticJoinTISweepArea<T> sweepArea) throws InstantiationException, IllegalAccessException {
        super(sweepArea);
    }

    @Override
    public void insert(final T s) {
        synchronized (this.getElements()) {
            this.setLatestTimeStamp(s);
            this.getElements().add(s);
        }
    }

    @Override
    public Iterator<T> queryCopy(final T element, final Order order, final boolean extract) {
        final LinkedList<T> result = new LinkedList<T>();
        Iterator<T> iter;
        synchronized (this.getElements()) {
            switch (order) {
                case LeftRight:
                    iter = this.getElements().iterator();
                    while (iter.hasNext()) {
                        final T next = iter.next();
                        if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
                            continue;
                        }
                        if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
                            break;
                        }
                        final ProbabilisticRelationalPredicate probabilisticPredicate = ((ProbabilisticRelationalPredicate) this.getQueryPredicate());
                        @SuppressWarnings("unchecked")
                        final T merge = (T) probabilisticPredicate.probabilisticEvaluate(element, next, next.getMetadata());
                        if (merge != null) {
                            result.add(merge);
                            if (extract) {
                                iter.remove();
                            }
                        }

                    }
                    break;
                case RightLeft:
                    iter = this.getElements().iterator();
                    while (iter.hasNext()) {
                        final T next = iter.next();
                        if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
                            continue;
                        }
                        if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
                            break;
                        }
                        final ProbabilisticRelationalPredicate probabilisticPredicate = ((ProbabilisticRelationalPredicate) this.getQueryPredicate());
                        @SuppressWarnings("unchecked")
                        final T merge = (T) probabilisticPredicate.probabilisticEvaluate(next, element, next.getMetadata());
                        if (merge != null) {
                            result.add(merge);
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

	@Override
	public ISweepArea<T> newInstance(OptionMap options) {
		return new ProbabilisticJoinTISweepArea<>();
	}

	@Override
	public ITimeIntervalSweepArea<T> clone() {
		try {
			return new ProbabilisticJoinTISweepArea<>(this);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
