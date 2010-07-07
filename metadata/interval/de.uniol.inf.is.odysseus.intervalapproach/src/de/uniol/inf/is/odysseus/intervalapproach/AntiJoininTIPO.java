//package de.uniol.inf.is.odysseus.intervalapproach;
//
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.PriorityQueue;
//import java.util.concurrent.TimeoutException;
//
//import de.uniol.inf.is.odysseus.base.PointInTime;
//import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
//import de.uniol.inf.is.odysseus.base.predicate.EqualsPredicate;
//import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
//import de.uniol.inf.is.odysseus.intervalapproach.predicate.OverlapsPredicate;
//import de.uniol.inf.is.odysseus.logicaloperator.base.DifferenceAO;
//import de.uniol.inf.is.odysseus.logicaloperator.base.ExistenceAO;
//import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
//import de.uniol.inf.is.odysseus.metadata.base.MetadataComparator;
//import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
//import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
//import de.uniol.inf.is.odysseus.physicaloperator.base.event.POException;
//
///**
// * @author Jonas Jacobi
// */
//public class NonExistenceTIPO<T extends IMetaAttributeContainers<ITimeInterval>> extends
//		BinaryPlanOperator<T, T> {
//	private static final long DEFAULT_TIMEOUT = -1;
//
//	private static final int LEFT = 0;
//
//	private static final int RIGHT = 1;
//
//	private ISweepArea<T>[] sa;
//
//	private PriorityQueue<T> returnBuffer;
//
//	private int inputChannel;
//
//	private Order order;
//
//	private PointInTime[] highestStart;
//
//	private boolean[] done;
//
//	@SuppressWarnings("unchecked")
//	public NonExistenceTIPO(ExistenceAO ao, ISweepArea<T> leftArea,
//			ISweepArea<T> rightArea) {
//		super(ao);
//		this.sa = (ISweepArea<T>[]) new ISweepArea[] { leftArea, rightArea };
//		this.returnBuffer = new PriorityQueue<T>(10,
//				new MetadataComparator<ITimeInterval>());
//		this.inputChannel = InputPort.LEFT.ordinal();
//		this.order = Order.LeftRight;
//		PointInTime startTime = new PointInTime(0, 0);
//		this.highestStart = new PointInTime[] { startTime, startTime };
//	}
//
//	@SuppressWarnings("unchecked")
//	public NonExistenceTIPO(DifferenceAO ao) {
//		super(ao);
//		SweepArea<T> leftSA = new DefaultTISweepArea<T>();
//		SweepArea<T> rightSA = new DefaultTISweepArea<T>();
//		IPredicate<? super T> predicate = new AndPredicate<T>(OverlapsPredicate
//				.getInstance(), EqualsPredicate.getInstance());
//		leftSA.setQueryPredicate(predicate);
//		rightSA.setQueryPredicate(predicate);
//		this.sa = new ISweepArea[] { leftSA, rightSA };
//		this.returnBuffer = new PriorityQueue<T>(10,
//				new MetadataComparator<ITimeInterval>());
//		this.inputChannel = InputPort.LEFT.ordinal();
//		this.order = Order.LeftRight;
//		PointInTime startTime = new PointInTime(0, 0);
//		this.highestStart = new PointInTime[] { startTime, startTime };
//	}
//
//	@Override
//	public synchronized void open(SimplePlanOperator<T, ?> caller) {
//		super.open(caller);
//		this.done = new boolean[] { false, false };
//		this.highestStart = new PointInTime[2];
//	}
//
//	/**
//	 * TODO AUFRAEUMEN!!! evtl. abstraktere loesung fuer normalen antijoin
//	 * implementieren
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	protected T process_next(ReturnStatusStore returnStatus)
//			throws POException, TimeoutException {
//		while (true) {
//			if (canReturn()) {
//				returnStatus.status = ReturnStatus.VALUE;
//				return returnBuffer.poll();
//			}
//
//			T curInput = this.getInputNext(inputChannel, this, DEFAULT_TIMEOUT,
//					returnStatus);
//
//			if (curInput == null) {
//				if (!returnStatus.isValue()) {
//					return null;
//				}
//
//				this.done[this.inputChannel] = true;
//				if (this.done[1]) {
//					if (this.done[0]) {
//						continue;
//					} else {
//						for (T element : sa[LEFT]) {
//							this.returnBuffer.add(element);
//						}
//						this.sa[LEFT].clear();
//						this.inputChannel = LEFT;
//						this.order = ISweepArea.Order.LeftRight;
//						continue;
//					}
//				}
//				this.inputChannel = RIGHT;
//				this.order = ISweepArea.Order.RightLeft;
//				continue;
//			}
//
//			// TODO in den sas aufraeumen
//			ITimeInterval curMetadata = curInput.getMetadata();
//			this.highestStart[this.inputChannel] = curMetadata.getStart();
//
//			if (this.inputChannel == LEFT) {
//				sa[RIGHT].purgeElements(curInput, order);
//				if (!this.sa[RIGHT].isEmpty()
//						&& TimeInterval.totallyBefore(curMetadata,
//								this.sa[RIGHT].top(false).getMetadata())) {
//					this.returnBuffer.add(curInput);
//					switchChannels();
//					continue;
//				}
//				Iterator<T> it = sa[RIGHT].query(curInput, order);
//				if (!it.hasNext()) {
//					sa[LEFT].insert(curInput);
//					switchChannels();
//					continue;
//				}
//				while (it.hasNext()) {
//					T next = it.next();
//					ITimeInterval nextMetadata = next.getMetadata();
//					if (TimeInterval.startsBefore(curMetadata, nextMetadata)) {
//						T newElement = (T) curInput.clone();
//						newElement.setMetadata(new TimeInterval(curMetadata
//								.getStart(), nextMetadata.getStart()));
//						this.returnBuffer.add(newElement);
//					}
//					if (curMetadata.getEnd().after(nextMetadata.getEnd())) {
//						curMetadata = new TimeInterval(nextMetadata.getEnd(),
//								curMetadata.getEnd());
//						curInput.setMetadata(curMetadata);
//						continue;
//					}
//					curInput = null;
//					break;
//				}
//				if (curInput != null) {
//					this.sa[LEFT].insert(curInput);
//				}
//			} else {
//				this.sa[RIGHT].insert(curInput);
//				Iterator<T> extractIT = this.sa[LEFT].extractElements(curInput,
//						order);
//				while (extractIT.hasNext()) {
//					this.returnBuffer.add(extractIT.next());
//				}
//
//				LinkedList<T> newElements = new LinkedList<T>();
//				Iterator<T> it = this.sa[LEFT].query(curInput, order);
//				while (it.hasNext()) {
//					T next = it.next();
//					it.remove();
//					ITimeInterval nextMetadata = next.getMetadata();
//					if (TimeInterval.startsBefore(nextMetadata, curMetadata)) {
//						T newElement = (T) next.clone();
//						newElement.setMetadata(new TimeInterval(nextMetadata
//								.getStart(), curMetadata.getStart()));
//						this.returnBuffer.add(newElement);
//					}
//					if (nextMetadata.getEnd().after(curMetadata.getEnd())) {
//						TimeInterval newMetadata = new TimeInterval(curMetadata
//								.getEnd(), nextMetadata.getEnd());
//						next.setMetadata(newMetadata);
//						newElements.add(next);
//						continue;
//					}
//				}
//				this.sa[LEFT].insertAll(newElements);
//			}
//			switchChannels();
//		}
//	}
//
//	private void switchChannels() {
//		if (!this.done[0] && !this.done[1]) {
//			this.inputChannel ^= 1;
//			this.order = this.order.inverse();
//		}
//	}
//
//	private boolean canReturn() {
//		if (this.done[LEFT]) {
//			if (this.done[RIGHT]) {
//				return true;
//			} else {
//				if (this.sa[LEFT].isEmpty()) {
//					return true;
//				} else {
//					return !this.returnBuffer.isEmpty()
//							&& this.returnBuffer.peek().getMetadata().getEnd()
//									.beforeOrEquals(
//											PointInTime.min(
//													this.highestStart[0],
//													this.highestStart[1]));
//				}
//			}
//		} else {
//			if (this.done[RIGHT]) {
//				return !this.returnBuffer.isEmpty();
//			} else {
//				return !this.returnBuffer.isEmpty()
//						&& this.returnBuffer.peek().getMetadata().getEnd()
//								.beforeOrEquals(
//										PointInTime.min(this.highestStart[0],
//												this.highestStart[1]));
//			}
//		}
//	}
//}o