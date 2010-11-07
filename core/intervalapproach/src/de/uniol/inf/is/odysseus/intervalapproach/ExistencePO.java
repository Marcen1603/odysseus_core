package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Works the same way as JoinTIPO, but only the left elements are passed
 * to the output stream. Furthermore, no duplicates will be produced. That means,
 * for each element on the left, only one element will be put into the result,
 * no matter if there is more than one element on the right.
 * 
 * The semantic: If on the right there exist an element, so that the join
 * predicate is fullfilled, the corresponding left element will be put into
 * the result.
 * 
 * @author Jonas Jacobi, abolles, jan steinke
 * 
 * @param <K>
 *            Metadatentyp
 * @param <T>
 *            Datentyp
 */
public class ExistencePO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>>
		extends AbstractPipe<T, T> {
	private static Logger _logger =  null;
	
	private static Logger getLogger(){
		if (_logger == null){
			_logger = LoggerFactory.getLogger(JoinTIPO.class);
		}
		return _logger;
	}
	
	//
	private static final int LEFT = 0;
	//
	private static final int RIGHT = 1;
	
	private ITemporalSweepArea<T>[] areas;
	private IPredicate<? super T> joinPredicate;

	protected ITransferArea<T,T> transferFunction;
	
	protected SDFAttributeList outputSchema;
	protected IDummyDataCreationFunction<K, T> creationFunction;

	private int otherport = 0;

	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema;
	}

	public ExistencePO(ITemporalSweepArea<T> leftArea, ITemporalSweepArea<T> rightArea) {
		this.areas = new ITemporalSweepArea[2];
		this.areas[0] = leftArea;
		this.areas[1] = rightArea;
	}

	public ExistencePO() {

	}

	public ExistencePO(ExistencePO<K, T> join) {
		super(join);
		this.areas = (ITemporalSweepArea<T>[]) join.areas.clone();
		int i = 0;
		for (ITemporalSweepArea<T> ja : join.areas) {
			this.areas[i] = ja.clone();
			i++;
		}

		this.joinPredicate = join.joinPredicate.clone();
		this.transferFunction = join.transferFunction.clone();
		this.transferFunction.init(this);
		this.outputSchema = join.outputSchema.clone();
		this.creationFunction = join.creationFunction.clone();

	}

	public void setAreas(ITemporalSweepArea<T>[] areas) {
		this.areas = areas;
		if (this.joinPredicate != null) {
			areas[0].setQueryPredicate(this.joinPredicate);
			areas[1].setQueryPredicate(this.joinPredicate);
		}
	}

	public IPredicate<? super T> getJoinPredicate() {
		return joinPredicate;
	}

	public void setJoinPredicate(IPredicate<? super T> joinPredicate) {
		this.joinPredicate = joinPredicate;
		if (this.areas != null && this.joinPredicate != null) {
			areas[0].setQueryPredicate(this.joinPredicate);
			areas[1].setQueryPredicate(this.joinPredicate);
		}
	}

	public void setTransferFunction(ITransferArea<T,T> transferFunction) {
		this.transferFunction = transferFunction;
		transferFunction.init(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T object, int port) {

		if (isDone()) {
			// TODO bei den sources abmelden ?? MG: Warum??
			// propagateDone gemeint?
			// JJ: weil man schon fertig sein
			// kann, wenn ein strom keine elemente liefert, der
			// andere aber noch, dann muss man von dem anderen keine
			// eingaben mehr verarbeiten, was dazu fuehren kann,
			// dass ein kompletter teilplan nicht mehr ausgefuehrt
			// werden muss, man also ressourcen spart
			return;
		}
		if (!isOpen()) {
			getLogger().error("process next called on non open operator "+this);
			return;
		}
		otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		synchronized (this.areas[otherport]) {
			areas[otherport].purgeElements(object, order);
		}

		synchronized (this) {
			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (isDone()) {
				propagateDone();
				return;
			}
		}
		
		// differentiate between left and right
		if(port == LEFT){
			Iterator<T> qualifies = areas[otherport].queryCopy(object, order);
			K curMetadata = object.getMetadata();
			boolean elementFull = false;
			while(qualifies.hasNext()){
				T next = qualifies.next();
				ITimeInterval newInterval = TimeInterval.intersection(curMetadata, next.getMetadata());
				// the merged interval can be null, if the following happens
				// right stream:
				// 1.       |---------------------|
				// 2.           |--------|
				// left stream:
				// a.     |----------------------------|
				// results:
				// (1,a)    |---------------------|
				// rest of validity of a:
				//                                |----|
				// then (2,a) will be null, since it is covered
				// be (1,a), one element per left input!
				//              |--------|
				//                                |----|
				if(newInterval != null){
					T clonedInput = (T)object.clone();
					clonedInput.getMetadata().setStart(newInterval.getStart());
					clonedInput.getMetadata().setEnd(newInterval.getEnd());
					transferFunction.transfer(clonedInput);
					if(newInterval.getEnd().before(curMetadata.getEnd())){
						curMetadata.setStart(newInterval.getEnd());
					}
					else{
						elementFull = true;
						break;
					}
				}
			}
			
			// the element has not been processed for the whole
			// validity. Remember: each element on the left
			// will be processed only once for each point in time
			// of its validity.
			if(!elementFull){
				object.getMetadata().setStart(curMetadata.getStart());
				object.getMetadata().setEnd(curMetadata.getEnd());
				areas[port].insert(object);
			}
		}
		else if(port == RIGHT){
			Iterator<T> qualifies = areas[otherport].queryCopy(object, order);
			LinkedList<T> modifiedElemsOnLeft = new LinkedList<T>();
			while(qualifies.hasNext()){
				T next = qualifies.next();
				K curMetadata = next.getMetadata();
				ITimeInterval newInterval = TimeInterval.intersection(curMetadata, object.getMetadata());
				// here the interval cannot be null, because
				// otherwise the element from the left would
				// not be in qualifies.
				
				T clonedLeft = (T)next.clone();
				clonedLeft.getMetadata().setStart(newInterval.getStart());
				clonedLeft.getMetadata().setEnd(newInterval.getEnd());
				transferFunction.transfer(clonedLeft);
				if(newInterval.getEnd().before(curMetadata.getEnd())){
					areas[otherport].remove(next);
					curMetadata.setStart(newInterval.getEnd());
					next.getMetadata().setStart(curMetadata.getStart());
					next.getMetadata().setEnd(curMetadata.getEnd());
					areas[otherport].insert(next);
				}
			}
			
			this.areas[port].insert(object);
		}
		else{
			throw new RuntimeException("Invalid port '" + port + "' in ExistencePO.");
		}
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		for (int i = 0; i < 2; ++i) {
			this.areas[i].clear();
			this.areas[i].init();
		}
		this.transferFunction.init(this);
	}

	@Override
	protected void process_done() {
		transferFunction.done();
		areas[0].clear();
		areas[1].clear();
	}

	@Override
	protected boolean isDone() {
		try {
			if (getSubscribedToSource(0).isDone()) {
				return getSubscribedToSource(1).isDone() || areas[0].isEmpty();
			} else {
				return getSubscribedToSource(0).isDone() && areas[1].isEmpty();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// Can happen if sources are unsubscribed while asking for done
			// Ignore
			return true;
		}
	}

	public ITemporalSweepArea<T>[] getAreas() {
		return areas;
	}

	public ITransferArea<T, T> getTransferFunction() {
		return transferFunction;
	}

	public IDummyDataCreationFunction<K, T> getCreationFunction() {
		return creationFunction;
	}

	public void setCreationFunction(
			IDummyDataCreationFunction<K, T> creationFunction) {
		this.creationFunction = creationFunction;
	}

	@Override
	public ExistencePO<K, T> clone() {
		return new ExistencePO<K, T>(this);
	}

	@Override
	public synchronized void processPunctuation(PointInTime timestamp, int port) {
		this.areas[port^1].purgeElementsBefore(timestamp);
		this.transferFunction.newHeartbeat(timestamp, port);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof ExistencePO)) {
			return false;
		}
		ExistencePO epo = (ExistencePO) ipo;
		if(this.hasSameSources(epo) && this.joinPredicate.equals(epo.joinPredicate)) {
			return true;
		}
		return false;
	}

}
