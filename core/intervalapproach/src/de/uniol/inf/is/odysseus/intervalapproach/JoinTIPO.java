package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order;
import de.uniol.inf.is.odysseus.physicaloperator.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Der JoinOperator kann zwar von den Generics her gesehen unabhaengig von
 * Daten- und Metadatenmodell betrachtet werden. Die Logik des in dieser Klasse
 * implementierten Operators entspricht jedoch der Logik eines JoinOperators im
 * Intervallansatz.
 * 
 * @author Jonas Jacobi, abolles, jan steinke
 * 
 * @param <K>
 *            Metadatentyp
 * @param <T>
 *            Datentyp
 */
public class JoinTIPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>>
		extends AbstractPipe<T, T> implements IHasPredicate{
	private final POEvent processPunctuationDoneEvent;
	private static Logger _logger =  null;
	
	private static Logger getLogger(){
		if (_logger == null){
			_logger = LoggerFactory.getLogger(JoinTIPO.class);
		}
		return _logger;
	}
		
	private ITemporalSweepArea<T>[] areas;
	private IPredicate<? super T> joinPredicate;

	protected IDataMergeFunction<T> dataMerge;
	protected IMetadataMergeFunction<K> metadataMerge;
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

	public JoinTIPO(IDataMergeFunction<T> dataMerge,
			IMetadataMergeFunction<K> metadataMerge,
			ITransferArea<T,T> transferFunction, ITemporalSweepArea<T>[] areas) {
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
		this.transferFunction = transferFunction;
		this.areas = areas;
		this.processPunctuationDoneEvent  = new POEvent(this, POEventType.ProcessPunctuationDone);
	}

	public JoinTIPO() {
		this.processPunctuationDoneEvent  = new POEvent(this, POEventType.ProcessPunctuationDone);
	}

	public JoinTIPO(JoinTIPO<K, T> join) {
		super(join);
		this.processPunctuationDoneEvent  = new POEvent(this, POEventType.ProcessPunctuationDone);
		this.areas = (ITemporalSweepArea<T>[]) join.areas.clone();
		int i = 0;
		for (ITemporalSweepArea<T> ja : join.areas) {
			this.areas[i] = ja.clone();
			i++;
		}

		this.joinPredicate = join.joinPredicate.clone();
		this.dataMerge = join.dataMerge.clone();
		dataMerge.init();

		this.metadataMerge = join.metadataMerge.clone();
		metadataMerge.init();
		this.transferFunction = join.transferFunction.clone();
		this.transferFunction.init(this);
		this.outputSchema = join.outputSchema.clone();
		this.creationFunction = join.creationFunction.clone();

	}

	public IDataMergeFunction<T> getDataMerge() {
		return dataMerge;
	}

	public void setDataMerge(IDataMergeFunction<T> dataMerge) {
		this.dataMerge = dataMerge;
	}

	public IMetadataMergeFunction<K> getMetadataMerge() {
		return metadataMerge;
	}

	public void setMetadataMerge(IMetadataMergeFunction<K> metadataMerge) {
		this.metadataMerge = metadataMerge;
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
	
	@Override
	public IPredicate getPredicate() {
		return getJoinPredicate();
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
		Iterator<T> qualifies;
		synchronized (this.areas) {
			synchronized (this.areas[otherport]) {
				qualifies = areas[otherport].queryCopy(object, order);
			}
			transferFunction.newElement(object, port);
			synchronized (areas[port]) {
				areas[port].insert(object);
			}
		}

		while (qualifies.hasNext()) {
			T next = qualifies.next();
			T newElement = merge(object, next, order);
			transferFunction.transfer(newElement);
		}
	}

	protected T merge(T left, T right, Order order) {
		// if (logger.isTraceEnabled()) {
		// logger.trace("JoinTIPO (" + hashCode() + ") start merging: " + left
		// + " AND " + right);
		// }
		T mergedData;
		K mergedMetadata;
		if (order == Order.LeftRight) {
			mergedData = dataMerge.merge(left, right);
			mergedMetadata = metadataMerge.mergeMetadata(left.getMetadata(),
					right.getMetadata());
		} else {
			mergedData = dataMerge.merge(right, left);
			mergedMetadata = metadataMerge.mergeMetadata(right.getMetadata(),
					left.getMetadata());
		}
		mergedData.setMetadata(mergedMetadata);
		return mergedData;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		for (int i = 0; i < 2; ++i) {
			this.areas[i].clear();
			this.areas[i].init();
		}
		this.dataMerge.init();
		this.metadataMerge.init();
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
	public JoinTIPO<K, T> clone() {
		return new JoinTIPO<K, T>(this);
	}

	@Override
	public synchronized void processPunctuation(PointInTime timestamp, int port) {
		this.areas[port^1].purgeElementsBefore(timestamp);
		this.transferFunction.newHeartbeat(timestamp, port);
		fire(this.processPunctuationDoneEvent);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof JoinTIPO)) {
			return false;
		}
		JoinTIPO<? extends ITimeInterval, ? extends IMetaAttributeContainer<K>> jtipo = (JoinTIPO<? extends ITimeInterval, ? extends IMetaAttributeContainer<K>>) ipo;
		
		// Falls die Operatoren verschiedene Quellen haben, wird false zurück gegeben
		if(!this.hasSameSources(jtipo)) {
			return false;
		}
		
		// Vergleichen des Join-Prädikats und des Output-Schemas
		if(this.getJoinPredicate().equals(jtipo.getJoinPredicate()) &&
				this.getOutputSchema().compareTo(jtipo.getOutputSchema()) == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isContainedIn(IPipe<T,T> ip) {
		if(!(ip instanceof JoinTIPO)) {
			return false;
		}
		JoinTIPO<? extends ITimeInterval, ? extends IMetaAttributeContainer<K>> jtipo = (JoinTIPO<? extends ITimeInterval, ? extends IMetaAttributeContainer<K>>) ip;
		
		// Falls die Operatoren verschiedene Quellen haben, wird false zurück gegeben
		if(!this.hasSameSources(jtipo)) {
			return false;
		}
		
		// Vergleichen des Join-Prädikats
		if(this.getJoinPredicate().isContainedIn(jtipo.getJoinPredicate())) {
			return true;
		}
		return false;
	}

}
