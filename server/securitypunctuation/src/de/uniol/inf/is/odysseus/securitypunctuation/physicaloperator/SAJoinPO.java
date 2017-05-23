package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SPMap;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class SAJoinPO<K extends ITimeInterval, T extends IStreamObject<K>> extends JoinTIPO<K, T> {
	private static final Logger LOG = LoggerFactory.getLogger(SAJoinPO.class);
	SPMap<K, T> spMap;
	List<ISecurityPunctuation> lastSentPunctuation;

	List<SAOperatorDelegate<T>> saOpDelPo;
	List<List<ISecurityPunctuation>> spList = new ArrayList<List<ISecurityPunctuation>>();

	public SAJoinPO(IDataMergeFunction<T, K> dataMerge, IMetadataMergeFunction<K> metadataMerge,
			ITransferArea<T, T> transferFunction, ITimeIntervalSweepArea<T>[] areas) {
		super(dataMerge, metadataMerge, transferFunction, areas);
		initLists();

	}

	@SuppressWarnings("unchecked")
	public SAJoinPO(IMetadataMergeFunction<?> metaDataMerge) {
		super((IMetadataMergeFunction<K>) metaDataMerge);
		initLists();

	}

	@Override
	public void processPunctuation(IPunctuation inPunctuation, int port) {

		if (inPunctuation instanceof ISecurityPunctuation) {
			this.spList.get(port).add((ISecurityPunctuation) inPunctuation);
			this.saOpDelPo.get(port).override((ISecurityPunctuation) inPunctuation);
			this.spMap.addSP((ISecurityPunctuation) inPunctuation, port);
		} else {
			super.processPunctuation(inPunctuation, port);
		}

	}

	@Override
	protected synchronized void process_next(T object, int port) {
		boolean send = false;
		// adds the incoming Object to the SPMap, with the current active SPs as
		// key
		if (!this.saOpDelPo.get(port).getRecentSPs().isEmpty()) {
			for (ISecurityPunctuation sp : this.saOpDelPo.get(port).getRecentSPs()) {
				this.spMap.addValue(sp, object, port);

			}
		}
		// removes the sps that are not required anymore from the SPMap, based
		// on the timestamp of the incoming object
		List<ISecurityPunctuation> spsToDelete = new ArrayList<>();
		if (!this.spList.isEmpty() && !this.spList.get(port ^ 1).isEmpty()) {
			for (ISecurityPunctuation sp : this.spList.get(port ^ 1)) {
				spsToDelete.add(this.spMap.invalidate(sp, object.getMetadata().getStart(), port ^ 1));
			}
		}

		this.spList.removeAll(spsToDelete);

		transferFunction.newElement(object, port);

		if (isDone()) {
			return;
		}

		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		Iterator<T> qualifies;

		if (inOrder && object.isTimeProgressMarker()) {
			areas[otherport].purgeElements(object, order);
		}

		if (isDone()) {
			propagateDone();
			return;
		}

		boolean extract = false;
		if (card != null) {
			switch (card) {
			case ONE_ONE:
				extract = true;
				break;
			case MANY_ONE:
				extract = port == 1;
				break;
			case ONE_MANY:
				extract = port == 0;
				break;
			default:
				break;
			}
		}

		qualifies = areas[otherport].queryCopy(object, order, extract);

		List<ISecurityPunctuation> intersectedSPs = new ArrayList<>();

		// extracts the SPs of the matching Tuples from the other port and
		// intersects them with the SPs from the incoming object
		while (qualifies.hasNext()) {
			ISecurityPunctuation intersectedSP;
			T next = qualifies.next();
			List<ISecurityPunctuation> matchingSPs = this.spMap.getMatchingSPs(next, otherport);
			LOG.info("matching SPs größe:" + matchingSPs.size());
			if (!matchingSPs.isEmpty()) {
				for (ISecurityPunctuation matchingSP : matchingSPs) {
					ISecurityPunctuation temp = matchingSP;
					for (ISecurityPunctuation currentSP : this.saOpDelPo.get(port).getRecentSPs()) {
						if (port == 0) {
							intersectedSP = temp.intersect(currentSP);
						} else {
							intersectedSP = currentSP.intersect(temp);
						}
						if (!intersectedSPs.contains(intersectedSP) && !intersectedSP.isEmpty()) {
							intersectedSPs.add(intersectedSP);
						}
					}
				}

			}
		}

		LOG.info("intersectedSPS Größe" + intersectedSPs.size());
		LOG.info("lastsentsps Größe" + lastSentPunctuation.size());

		// if the intersected SPs are empty, the object and the intersected
		// SPs are discarded
		if (!intersectedSPs.isEmpty()) {
			send = true;
			if (!(intersectedSPs.containsAll(lastSentPunctuation) && lastSentPunctuation.containsAll(intersectedSPs))) {
				lastSentPunctuation.clear();
				for (ISecurityPunctuation sp : intersectedSPs) {

					sendPunctuation(sp);
					lastSentPunctuation.add(sp);
				}
			}
		}

		qualifies = areas[otherport].queryCopy(object, order, extract);
		boolean hit = qualifies.hasNext();
		while (qualifies.hasNext() && send) {
			T next = qualifies.next();
			T newElement = dataMerge.merge(object, next, metadataMerge, order);
			transferFunction.transfer(newElement);

		}

		// Depending on card insert elements into sweep area
		if (card == null || card == Cardinalities.MANY_MANY) {
			areas[port].insert(object);
		} else {
			switch (card) {
			case ONE_ONE:
				// If one to one case, a hit cannot be produce another
				// hit
				if (!hit) {
					areas[port].insert(object);
				}
				break;
			case ONE_MANY:
				// If from left insert
				// if from right and no hit, insert (corresponding left
				// element not found now)
				if (port == 0 || (port == 1 && !hit)) {
					areas[port].insert(object);
				}
				break;
			case MANY_ONE:
				// If from rightt insert
				// if from left and no hit, insert (corresponding right
				// element not found now)
				if (port == 1 || (port == 0 && !hit)) {
					areas[port].insert(object);
				}
				break;
			default:
				areas[port].insert(object);
				break;
			}
		}
		PointInTime a = areas[port].getMinStartTs();
		PointInTime b = areas[otherport].getMinStartTs();
		PointInTime heartbeat = PointInTime.max(a, b);
		if (heartbeat != null) {
			transferFunction.newHeartbeat(heartbeat, port);
			transferFunction.newHeartbeat(heartbeat, otherport);
		}

	}

	private void initLists() {
		this.spMap = new SPMap<K, T>();
		this.saOpDelPo = new ArrayList<SAOperatorDelegate<T>>();
		lastSentPunctuation = new ArrayList<>();
		this.spList = new ArrayList<List<ISecurityPunctuation>>();
		for (int i = 0; i < 2; i++) {
			this.spList.add(new ArrayList<ISecurityPunctuation>());
			this.saOpDelPo.add(new SAOperatorDelegate());
		}

	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof SAJoinPO)) {
			return false;
		}
		return super.process_isSemanticallyEqual(ipo);
	}

}
