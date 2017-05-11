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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.Cardinalities;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SPMap;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class SAJoinPO<K extends ITimeInterval, T extends IStreamObject<K>> extends JoinTIPO<K, T> {
	private static final Logger LOG = LoggerFactory.getLogger(SAJoinPO.class);
	SPMap<K, T> spMap;
	List<AbstractSecurityPunctuation> lastSentPunctuation;

	List<SAOperatorDelegate<T>> saOpDelPo;
	List<List<AbstractSecurityPunctuation>> spList = new ArrayList<List<AbstractSecurityPunctuation>>();

	public SAJoinPO(IDataMergeFunction<T, K> dataMerge, IMetadataMergeFunction<K> metadataMerge,
			ITransferArea<T, T> transferFunction, ITimeIntervalSweepArea<T>[] areas) {
		super(dataMerge, metadataMerge, transferFunction, areas);
		this.spMap = new SPMap<K, T>();
		this.saOpDelPo = new ArrayList<SAOperatorDelegate<T>>();

		this.spList = new ArrayList<List<AbstractSecurityPunctuation>>();
		this.spList.add(new ArrayList<AbstractSecurityPunctuation>());
		this.spList.add(new ArrayList<AbstractSecurityPunctuation>());
		this.saOpDelPo.add(new SAOperatorDelegate());
		this.saOpDelPo.add(new SAOperatorDelegate());
		lastSentPunctuation = new ArrayList<>();

	}

	@SuppressWarnings("unchecked")
	public SAJoinPO(IMetadataMergeFunction<?> metaDataMerge) {
		super((IMetadataMergeFunction<K>) metaDataMerge);
		this.spMap = new SPMap<K, T>();
		this.saOpDelPo = new ArrayList<SAOperatorDelegate<T>>();
		this.spList = new ArrayList<List<AbstractSecurityPunctuation>>();
		this.spList.add(new ArrayList<AbstractSecurityPunctuation>());
		this.spList.add(new ArrayList<AbstractSecurityPunctuation>());
		this.saOpDelPo.add(new SAOperatorDelegate());
		this.saOpDelPo.add(new SAOperatorDelegate());
		lastSentPunctuation = new ArrayList<>();

	}

	@Override
	public void processPunctuation(IPunctuation inPunctuation, int port) {

		if (inPunctuation instanceof AbstractSecurityPunctuation) {
			this.spList.get(port).add((AbstractSecurityPunctuation) inPunctuation);
			this.saOpDelPo.get(port).override((AbstractSecurityPunctuation) inPunctuation);
			this.spMap.addSP((AbstractSecurityPunctuation) inPunctuation, port);
		} else {
			super.processPunctuation(inPunctuation, port);
		}

	}

	@Override
	protected synchronized void process_next(T object, int port) {
		// adds the incoming Object to the SPMap, with the current active SPs as
		// key
		if (!this.saOpDelPo.get(port).getRecentSPs().isEmpty()) {
			for (AbstractSecurityPunctuation sp : this.saOpDelPo.get(port).getRecentSPs()) {
				this.spMap.addValue(sp, object, port);

			}
		}
		// removes the sps that are not required anymore from the SPMap, based
		// on the timestamp of the incoming object
		List<AbstractSecurityPunctuation> spsToDelete = new ArrayList<>();
		if (!this.spList.isEmpty() && !this.spList.get(port ^ 1).isEmpty()) {
			for (AbstractSecurityPunctuation sp : this.spList.get(port ^ 1)) {
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
		// Avoid removing elements while querying for potential hits
		// synchronized (this) {

		if (inOrder && object.isTimeProgressMarker()) {
			areas[otherport].purgeElements(object, order);
		}

		// status could change, if the other port was done and
		// its sweeparea is now empty after purging
		if (isDone()) {
			propagateDone();
			return;
		}

		// depending on card, delete hits from areas
		// deleting if port is ONE-side
		// cases for ONE_MANY, MANY_ONE:
		// ONE side element is earlier than MANY side elements, nothing
		// will
		// be found
		// and nothing will be removed
		// ONE side element is later than some MANY side elements, find
		// all
		// corresponding elements and remove them
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

		List<AbstractSecurityPunctuation> intersectedSPs = new ArrayList<>();

		// extracts the SPs of the matching Tuples from the other port and
		// intersects them with the SPs from the incoming object
		while (qualifies.hasNext()) {
			AbstractSecurityPunctuation intersectedSP;
			T next = qualifies.next();
			List<AbstractSecurityPunctuation> matchingSPs = this.spMap.getMatchingSPs(next, otherport);
			if (!matchingSPs.isEmpty()) {
				for (AbstractSecurityPunctuation matchingSP : matchingSPs) {
					AbstractSecurityPunctuation temp = matchingSP;
					for (AbstractSecurityPunctuation currentSP : this.saOpDelPo.get(port).getRecentSPs()) {
						if (port == 0) {
							intersectedSP = temp.intersectPunctuation(currentSP);
						} else {
							intersectedSP = currentSP.intersectPunctuation(temp);
						}
						if (!intersectedSPs.contains(intersectedSP)) {
							intersectedSPs.add(intersectedSP);
						}
					}
				}

			}
		}
		List<AbstractSecurityPunctuation> intersectedSPsCopy = new ArrayList<>(intersectedSPs);
		if (!(intersectedSPs.containsAll(lastSentPunctuation) && lastSentPunctuation.containsAll(intersectedSPs))) {

			// if the last sent punctuations are not the same like the new
			// intersected Punctuations and the new intersected Punctuations are
			// not empty, the last sent punctuations are cleared and the new
			// punctuations are added
			for (AbstractSecurityPunctuation sp : intersectedSPs) {
				if (!sp.isEmpty()) {
					lastSentPunctuation.clear();
				}
			}
			// if the intersected SPs are empty, the object and the intersected
			// SPs are discarded
			for (AbstractSecurityPunctuation sp : intersectedSPs) {
				if (!sp.isEmpty()) {
					sendPunctuation(sp);
					lastSentPunctuation.add(sp);
				} else {
					intersectedSPsCopy.remove(sp);
				}
			}
		}
		qualifies = areas[otherport].queryCopy(object, order, extract);
		boolean hit = qualifies.hasNext();
		while (qualifies.hasNext() && !intersectedSPsCopy.isEmpty()) {
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

}
