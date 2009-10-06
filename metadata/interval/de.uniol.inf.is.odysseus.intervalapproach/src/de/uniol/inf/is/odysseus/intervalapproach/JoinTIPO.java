package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITransferFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Der JoinOperator kann zwar von den Generics her gesehen unabhaengig von
 * Daten- und Metadatenmodell betrachtet werden. Die Logik des in dieser Klasse
 * implementierten Operators entspricht jedoch der Logik eines JoinOperators im
 * Intervallansatz.
 * 
 * @author Jonas Jacobi, abolles
 * 
 * @param <K>
 *            Metadatentyp
 * @param <T>
 *            Datentyp
 */
public class JoinTIPO<K extends ITimeInterval, T extends IMetaAttributeContainer<K>>
		extends AbstractPunctuationPipe<T, T> {
	private static final Logger logger = LoggerFactory
			.getLogger(JoinTIPO.class);
	private ISweepArea<T>[] areas;
	private IPredicate<? super T> joinPredicate;

	protected IDataMergeFunction<T> dataMerge;
	protected IMetadataMergeFunction<K> metadataMerge;
	protected ITransferFunction<T> transferFunction;
	protected SDFAttributeList outputSchema;

	private int otherport = 0;
	private int port = 0;

	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema;
	}

	public JoinTIPO(IDataMergeFunction<T> dataMerge,
			IMetadataMergeFunction<K> metadataMerge,
			ITransferFunction<T> transferFunction, ISweepArea<T>[] areas) {
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
		this.transferFunction = transferFunction;
		this.areas = areas;
	}

	public JoinTIPO() {

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

	public void setAreas(ISweepArea<T>[] areas) {
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

	public void setTransferFunction(ITransferFunction<T> transferFunction) {
		this.transferFunction = transferFunction;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T object, int port) {
		currentPort = port;
		if (isDone()) { // TODO bei den sources abmelden ?? MG: Warum??
			// propagateDone gemeint?
			// JJ: weil man schon fertig sein
			// kann, wenn ein strom keine elemente liefert, der
			// andere aber noch, dann muss man von dem anderen keine
			// eingaben mehr verarbeiten, was dazu fuehren kann,
			// dass ein kompletter teilplan nicht mehr ausgefuehrt
			// werden muss, man also ressourcen spart
			return;
		}
		this.port = port;
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

	private T merge(T left, T right, Order order) {
		if (logger.isTraceEnabled()) {
			logger.trace("JoinTIPO (" + hashCode() + ") start merging: " + left
					+ " AND " + right);
		}
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

	protected boolean isDone() {
		if (getSubscribedTo(0).done) {
			return getSubscribedTo(1).done || areas[0].isEmpty();
		} else {
			return getSubscribedTo(0).done && areas[1].isEmpty();
		}
	}

	public ISweepArea<T>[] getAreas() {
		return areas;
	}

	public ITransferFunction<T> getTransferFunction() {
		return transferFunction;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void cleanInternalStates(PointInTime punctuation,
			IMetaAttributeContainer<?> current) {

		//TODO noch in der Entwicklung...
		/*logger.info("Cleaning JoinTIPO SweepAreas...");
		IMetaAttributeContainer<?> purgeInterval = (IMetaAttributeContainer<?>) current.clone();
		ITimeInterval punctuationInterval = (ITimeInterval) purgeInterval.getMetadata();
		punctuationInterval.setStart(punctuation);

		Order order = Order.fromOrdinal(port);
		areas[otherport].purgeElements((T)purgeInterval, order);*/

	}

}
