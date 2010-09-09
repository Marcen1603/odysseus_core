package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.EqualsPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.OverlapsPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.DifferenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ExistenceAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;

///**
// * @author Jonas Jacobi
// */
public class AntiJoinTIPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>>
		extends AbstractPipe<T, T> {
	//
	private static final int LEFT = 0;
	//
	private static final int RIGHT = 1;
	//
	private final ISweepArea<T>[] sa;
	//
	private final PriorityQueue<T> returnBuffer;
	//
	private final PointInTime[] highestStart;

	//
	@SuppressWarnings("unchecked")
	public AntiJoinTIPO(ExistenceAO ao, ISweepArea<T> leftArea,
			ISweepArea<T> rightArea) {
		super();
		this.sa = new ISweepArea[] { leftArea, rightArea };
		this.returnBuffer = new PriorityQueue<T>(10,
				new MetadataComparator<ITimeInterval>());
		PointInTime startTime = PointInTime.getZeroTime();
		this.highestStart = new PointInTime[] { startTime, startTime };
	}

	//
	@SuppressWarnings("unchecked")
	public AntiJoinTIPO(DifferenceAO ao) {
		super();
		ISweepArea<T> leftSA = new DefaultTISweepArea<T>();
		ISweepArea<T> rightSA = new DefaultTISweepArea<T>();
		IPredicate<? super T> predicate = new AndPredicate<T>(OverlapsPredicate
				.getInstance(), EqualsPredicate.getInstance());
		leftSA.setQueryPredicate(predicate);
		rightSA.setQueryPredicate(predicate);
		this.sa = new ISweepArea[] { leftSA, rightSA };
		this.returnBuffer = new PriorityQueue<T>(10,
				new MetadataComparator<ITimeInterval>());
		PointInTime startTime = PointInTime.getZeroTime();
		this.highestStart = new PointInTime[] { startTime, startTime };
	}

	public AntiJoinTIPO(AntiJoinTIPO<K, T> antiJoinTIPO) {
        this.sa = (ISweepArea<T>[]) antiJoinTIPO.sa.clone();// TODO was ist hier gewollt
		this.returnBuffer = new PriorityQueue<T>(10,
				new MetadataComparator<ITimeInterval>());
		PointInTime startTime = PointInTime.getZeroTime();
		this.highestStart = new PointInTime[] { startTime, startTime };
	}

	//
	// /**
	// * TODO AUFRAEUMEN!!! evtl. abstraktere loesung fuer normalen antijoin
	// * implementieren
	// */
	// @SuppressWarnings("unchecked")
	// @Override
	// protected void process_next(T Object, int port) {
	// while (true) {
	// if (canReturn()) {
	// returnStatus.status = ReturnStatus.VALUE;
	// return returnBuffer.poll();
	// }
	//
	// T curInput = this.getInputNext(inputChannel, this, DEFAULT_TIMEOUT,
	// returnStatus);
	//
	// if (curInput == null) {
	// if (!returnStatus.isValue()) {
	// return null;
	// }
	//
	// this.done[this.inputChannel] = true;
	// if (this.done[1]) {
	// if (this.done[0]) {
	// continue;
	// } else {
	// for (T element : sa[LEFT]) {
	// this.returnBuffer.add(element);
	// }
	// this.sa[LEFT].clear();
	// this.inputChannel = LEFT;
	// this.order = ISweepArea.Order.LeftRight;
	// continue;
	// }
	// }
	// this.inputChannel = RIGHT;
	// this.order = ISweepArea.Order.RightLeft;
	// continue;
	// }
	//
	// // TODO in den sas aufraeumen
	// ITimeInterval curMetadata = curInput.getMetadata();
	// this.highestStart[this.inputChannel] = curMetadata.getStart();
	//
	// if (this.inputChannel == LEFT) {
	// sa[RIGHT].purgeElements(curInput, order);
	// if (!this.sa[RIGHT].isEmpty()
	// && TimeInterval.totallyBefore(curMetadata,
	// this.sa[RIGHT].top(false).getMetadata())) {
	// this.returnBuffer.add(curInput);
	// switchChannels();
	// continue;
	// }
	// Iterator<T> it = sa[RIGHT].query(curInput, order);
	// if (!it.hasNext()) {
	// sa[LEFT].insert(curInput);
	// switchChannels();
	// continue;
	// }
	// while (it.hasNext()) {
	// T next = it.next();
	// ITimeInterval nextMetadata = next.getMetadata();
	// if (TimeInterval.startsBefore(curMetadata, nextMetadata)) {
	// T newElement = (T) curInput.clone();
	// newElement.setMetadata(new TimeInterval(curMetadata
	// .getStart(), nextMetadata.getStart()));
	// this.returnBuffer.add(newElement);
	// }
	// if (curMetadata.getEnd().after(nextMetadata.getEnd())) {
	// curMetadata = new TimeInterval(nextMetadata.getEnd(),
	// curMetadata.getEnd());
	// curInput.setMetadata(curMetadata);
	// continue;
	// }
	// curInput = null;
	// break;
	// }
	// if (curInput != null) {
	// this.sa[LEFT].insert(curInput);
	// }
	// } else {
	// this.sa[RIGHT].insert(curInput);
	// Iterator<T> extractIT = this.sa[LEFT].extractElements(curInput,
	// order);
	// while (extractIT.hasNext()) {
	// this.returnBuffer.add(extractIT.next());
	// }
	//
	// LinkedList<T> newElements = new LinkedList<T>();
	// Iterator<T> it = this.sa[LEFT].query(curInput, order);
	// while (it.hasNext()) {
	// T next = it.next();
	// it.remove();
	// ITimeInterval nextMetadata = next.getMetadata();
	// if (TimeInterval.startsBefore(nextMetadata, curMetadata)) {
	// T newElement = (T) next.clone();
	// newElement.setMetadata(new TimeInterval(nextMetadata
	// .getStart(), curMetadata.getStart()));
	// this.returnBuffer.add(newElement);
	// }
	// if (nextMetadata.getEnd().after(curMetadata.getEnd())) {
	// TimeInterval newMetadata = new TimeInterval(curMetadata
	// .getEnd(), nextMetadata.getEnd());
	// next.setMetadata(newMetadata);
	// newElements.add(next);
	// continue;
	// }
	// }
	// this.sa[LEFT].insertAll(newElements);
	// }
	// switchChannels();
	// }
	// }
	//
	// private void switchChannels() {
	// if (!this.done[0] && !this.done[1]) {
	// this.inputChannel ^= 1;
	// this.order = this.order.inverse();
	// }
	// }
	//
	// private boolean canReturn() {
	// if (this.done[LEFT]) {
	// if (this.done[RIGHT]) {
	// return true;
	// } else {
	// if (this.sa[LEFT].isEmpty()) {
	// return true;
	// } else {
	// return !this.returnBuffer.isEmpty()
	// && this.returnBuffer.peek().getMetadata().getEnd()
	// .beforeOrEquals(
	// PointInTime.min(
	// this.highestStart[0],
	// this.highestStart[1]));
	// }
	// }
	// } else {
	// if (this.done[RIGHT]) {
	// return !this.returnBuffer.isEmpty();
	// } else {
	// return !this.returnBuffer.isEmpty()
	// && this.returnBuffer.peek().getMetadata().getEnd()
	// .beforeOrEquals(
	// PointInTime.min(this.highestStart[0],
	// this.highestStart[1]));
	// }
	// }
	// }
	//
	// @Override
	// public AntiJoinTIPO<T> clone() {
	// return new AntiJoinTIPO<T>(this);
	// }
	//
	// @Override
	// public
	// de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode
	// getOutputMode() {
	// return OutputMode.INPUT;
	// }
	//
	// @Override
	// public void processPunctuation(PointInTime timestamp, int port) {
	// }
	@Override
	public AntiJoinTIPO<K, T> clone() {
		return new AntiJoinTIPO<K, T>(this);
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	synchronized protected void process_next(T object, int port) {
		T curInput = object;
		ITimeInterval curMetadata = curInput.getMetadata();
		PointInTime curStart = curMetadata.getStart();
		this.highestStart[port] = curStart;

		if (port == LEFT) {
			sa[RIGHT].purgeElements(curInput, Order.LeftRight);
			if (!this.sa[RIGHT].isEmpty()
					&& (curMetadata.getEnd().before(this.sa[RIGHT].peek()
							.getMetadata().getStart()))) {
				this.returnBuffer.add(curInput);
			} else {
				Iterator<T> it = sa[RIGHT].query(curInput, Order.LeftRight);
				if (!it.hasNext()) {
					sa[LEFT].insert(curInput);
				} else {
					while (it.hasNext()) {
						T next = it.next();
						ITimeInterval nextMetadata = next.getMetadata();
						if (TimeInterval
								.startsBefore(curMetadata, nextMetadata)) {
							T newElement = (T) curInput.clone();
							newElement.getMetadata().setEnd(
									nextMetadata.getStart());
							this.returnBuffer.add(newElement);
						}
						if (curMetadata.getEnd().after(nextMetadata.getEnd())) {
							curMetadata = new TimeInterval(nextMetadata
									.getEnd(), curMetadata.getEnd());
							curInput.getMetadata().setStart(
									nextMetadata.getEnd());
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
		} else {
			this.sa[RIGHT].insert(curInput);
			Iterator<T> extractIT = this.sa[LEFT].extractElements(curInput,
					Order.RightLeft);
			while (extractIT.hasNext()) {
				this.returnBuffer.add(extractIT.next());
			}

			LinkedList<T> newElements = new LinkedList<T>();
			Iterator<T> it = this.sa[LEFT].query(curInput, Order.RightLeft);
			while (it.hasNext()) {
				T next = it.next();
				it.remove();
				ITimeInterval nextMetadata = next.getMetadata();
				if (TimeInterval.startsBefore(nextMetadata, curMetadata)) {
					T newElement = (T) next.clone();
					newElement.getMetadata().setStart(nextMetadata.getStart());
					newElement.getMetadata().setEnd(curStart);
					this.returnBuffer.add(newElement);
				}
				if (nextMetadata.getEnd().after(curMetadata.getEnd())) {
					TimeInterval newMetadata = new TimeInterval(curMetadata
							.getEnd(), nextMetadata.getEnd());
					next.getMetadata().setStart(curMetadata.getEnd());
					newElements.add(next);
					continue;
				}
			}
			this.sa[LEFT].insertAll(newElements);
		}
		PointInTime minStart = PointInTime.min(this.highestStart[LEFT],
				this.highestStart[RIGHT]);
		T tmpElement = returnBuffer.peek();
		while (tmpElement != null && tmpElement.getMetadata().getStart().beforeOrEquals(
				minStart)) {
			transfer(returnBuffer.poll());
			tmpElement = returnBuffer.peek();
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}
}