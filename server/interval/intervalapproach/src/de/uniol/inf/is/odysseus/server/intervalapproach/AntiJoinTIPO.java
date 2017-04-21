/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.predicate.EqualsPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.OverlapsPredicate;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

///**
// * @author Jonas Jacobi
// */
public class AntiJoinTIPO<K extends ITimeInterval, T extends IStreamObject<K>> extends AbstractPipe<T, T> implements IStatefulOperator {
    //
    private static final int LEFT = 0;
    //
    private static final int RIGHT = 1;
    //
    private final ITimeIntervalSweepArea<T>[] sa;

    //
    final ITransferArea<T,T> transferArea;
    
    //
    private final PointInTime[] highestStart;

    //
    @SuppressWarnings("unchecked")
    public AntiJoinTIPO(ExistenceAO ao, ITimeIntervalSweepArea<T> leftArea, ITimeIntervalSweepArea<T> rightArea, ITransferArea<T, T> transferArea) {
        super();
        this.sa = new ITimeIntervalSweepArea[] { leftArea, rightArea };
        this.transferArea = transferArea;
        PointInTime startTime = PointInTime.getZeroTime();
        this.highestStart = new PointInTime[] { startTime, startTime };
    }

    //
    @SuppressWarnings("unchecked")
    public AntiJoinTIPO(DifferenceAO ao, ITimeIntervalSweepArea<T> leftArea, ITimeIntervalSweepArea<T> rightArea, ITransferArea<T, T> transferArea) {
        super();
        this.sa = new ITimeIntervalSweepArea[] { leftArea, rightArea };
        IPredicate<? super T> predicate = ComplexPredicateHelper.createAndPredicate(OverlapsPredicate.getInstance(), EqualsPredicate.getInstance());
        if (ao.getPredicate() != null) {
            predicate = ComplexPredicateHelper.createOrPredicate(predicate, ComplexPredicateHelper.createNotPredicate(ao.getPredicate()));
        }

        leftArea.setQueryPredicate(predicate);
        rightArea.setQueryPredicate(predicate);
        this.transferArea = transferArea;
        PointInTime startTime = PointInTime.getZeroTime();
        this.highestStart = new PointInTime[] { startTime, startTime };
        setOutputSchema(ao.getOutputSchema());
    }

    public ISweepArea<T>[] getAreas() {
        return sa;
    }

    @Override
    public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    @Override
    protected void process_open() throws OpenFailedException {
    	transferArea.init(this, 2);
    }

    @Override
    synchronized protected void process_next(T object, int port) {
    	transferArea.newElement(object, port);
        T curInput = object;
        ITimeInterval curMetadata = curInput.getMetadata();
        PointInTime curStart = curMetadata.getStart();
        this.highestStart[port] = curStart;

        if (port == LEFT) {
            sa[RIGHT].purgeElements(curInput, Order.LeftRight);
            if (!this.sa[RIGHT].isEmpty() && (curMetadata.getEnd().before(this.sa[RIGHT].peek().getMetadata().getStart()))) {
                this.transferArea.transfer(curInput);
            }
            else {
                Iterator<T> it = sa[RIGHT].query(curInput, Order.LeftRight);
                if (!it.hasNext()) {
                    sa[LEFT].insert(curInput);
                }
                else {
                    while (it.hasNext()) {
                        T next = it.next();
                        ITimeInterval nextMetadata = next.getMetadata();
                        if (TimeInterval.startsBefore(curMetadata, nextMetadata)) {
                            @SuppressWarnings("unchecked")
                            T newElement = (T) curInput.clone();
                            newElement.getMetadata().setEnd(nextMetadata.getStart());
                            this.transferArea.transfer(newElement);
                        }
                        if (curMetadata.getEnd().after(nextMetadata.getEnd())) {
                            curMetadata = new TimeInterval(nextMetadata.getEnd(), curMetadata.getEnd());
                            curInput.getMetadata().setStart(nextMetadata.getEnd());
                            continue;
                        }
                        curInput = null;
                        break;
                    }
                    if (curInput != null) {
                        this.sa[LEFT].insert(curInput);
                    }
                }
            }
        }
        else {
            this.sa[RIGHT].insert(curInput);
            Iterator<T> extractIT = this.sa[LEFT].extractElements(curInput, Order.RightLeft);
            while (extractIT.hasNext()) {
                this.transferArea.transfer(extractIT.next());
            }

            LinkedList<T> newElements = new LinkedList<>();
            Iterator<T> it = this.sa[LEFT].query(curInput, Order.RightLeft);
            while (it.hasNext()) {
                T next = it.next();
                it.remove();
                ITimeInterval nextMetadata = next.getMetadata();
                if (TimeInterval.startsBefore(nextMetadata, curMetadata)) {
                    @SuppressWarnings("unchecked")
                    T newElement = (T) next.clone();
                    newElement.getMetadata().setStart(nextMetadata.getStart());
                    newElement.getMetadata().setEnd(curStart);
                    this.transferArea.transfer(newElement);
                }
                if (nextMetadata.getEnd().after(curMetadata.getEnd())) {
                    // MG: Auskommentiert, da eh nicht verwendet
                    // TimeInterval newMetadata = new TimeInterval(curMetadata
                    // .getEnd(), nextMetadata.getEnd());
                    next.getMetadata().setStart(curMetadata.getEnd());
                    newElements.add(next);
                    continue;
                }
            }
            this.sa[LEFT].insertAll(newElements);
        }
        PointInTime minStart = PointInTime.min(this.highestStart[LEFT], this.highestStart[RIGHT]);
        transferArea.newHeartbeat(minStart, 0);
        transferArea.newHeartbeat(minStart, 1);
    }

    @SuppressWarnings("unchecked")
	@Override
    public PointInTime getLatestEndTimestamp() {
		PointInTime max = null;
		for (int i = 0; i < 2; i++) {
			synchronized (this.sa[i]) {
				PointInTime maxi = ((ITimeIntervalSweepArea<IStreamObject<? extends ITimeInterval>>) this.sa[i])
						.getMaxEndTs();
				if (max == null || (maxi != null && maxi.after(max))) {
					max = maxi;
				}
			}
		}
		return max;
    }

    @Override
    public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation.isHeartbeat()) {
			// synchronized (this) {
			this.sa[port ^ 1].purgeElementsBefore(punctuation.getTime());
			// }
		}
		this.transferArea.sendPunctuation(punctuation);
		this.transferArea.newElement(punctuation, port);
    }

    @Override
    public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
        if (ipo instanceof AntiJoinTIPO) {
            AntiJoinTIPO<?, ?> ajtipo = (AntiJoinTIPO<?, ?>) ipo;
            if (ajtipo.sa[0].getQueryPredicate().equals(this.sa[0].getQueryPredicate()) && ajtipo.sa[1].getQueryPredicate().equals(this.sa[1].getQueryPredicate())) {
                return true;
            }

        }
        return false;
    }
}