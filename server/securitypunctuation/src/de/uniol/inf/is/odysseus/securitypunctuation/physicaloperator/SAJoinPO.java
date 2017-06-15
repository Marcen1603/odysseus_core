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
	List<List<ISecurityPunctuation>> spList;

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
			if (!this.spList.get(port).contains(inPunctuation)) {
				this.spList.get(port).add((ISecurityPunctuation) inPunctuation);
			}
			this.saOpDelPo.get(port).override((ISecurityPunctuation) inPunctuation);
			this.spMap.addSP((ISecurityPunctuation) inPunctuation, port);
		} else {
			super.processPunctuation(inPunctuation, port);
		}

	}

	@Override
	protected synchronized void process_next(T object, int port) {
		// if no SP is referring to this object, it is discarded
		if (!checkSP(object, port)) {
			return;
		}

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
		// removes the SPs that got deleted from the SPMap from the spList,
		// which is functioning as a collection of keys for the SPMap
		this.spList.get(port ^ 1).removeAll(spsToDelete);

		Iterator<T> qualifies;
		Order order = Order.fromOrdinal(port);
		qualifies = areas[port ^ 1].queryCopy(object, order, false);
		List<ISecurityPunctuation> intersectedSPs = new ArrayList<>();

		// extracts the SPs of the matching Tuples from the other port and
		// intersects them with the SPs from the incoming object
		boolean hit = qualifies.hasNext();
		while (qualifies.hasNext()) {
			ISecurityPunctuation intersectedSP;
			T next = qualifies.next();
			List<ISecurityPunctuation> matchingSPs = this.spMap.getMatchingSPs(next, port ^ 1);
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
		
		// if the intersected SPs are empty, the object and the intersected
		// SPs are discarded
		if (!intersectedSPs.isEmpty()) {
			send = true;
			if (!checkIfAlreadySent(intersectedSPs)) {

				lastSentPunctuation.clear();
				for (ISecurityPunctuation sp : intersectedSPs) {

					sendPunctuation(sp);
					lastSentPunctuation.add(sp);
				}
			}
		}
		if (!send) {
			super.insertElement(object, port, hit);
		} else if (send) {
			super.process_next(object, port);
		}

	}

	// checks if the object fits to one of the current SPs in the
	// SAOperatorDelegate
	private boolean checkSP(T object, int port) {
		for (ISecurityPunctuation sp : this.saOpDelPo.get(port).getRecentSPs()) {
			if (sp.getDDP().match(object, this.getInputSchema(port))) {
				return true;
			}
		}
		return false;

	}
	//checks if the last sent SPs are the same like the new intersected SPs
	private boolean checkIfAlreadySent(List<ISecurityPunctuation> intersectedSPs) {
		if (lastSentPunctuation.isEmpty()) {
			return false;
		} else if (intersectedSPs.size() != this.lastSentPunctuation.size()) {
			return false;
		}
		for (ISecurityPunctuation sp : intersectedSPs) {
			if (!lastSentPunctuation.contains(sp)) {
				return false;
			}
		}
		return true;
	}

	private void initLists() {
		this.spMap = new SPMap<K, T>();
		this.saOpDelPo = new ArrayList<SAOperatorDelegate<T>>();
		lastSentPunctuation = new ArrayList<>();
		this.spList = new ArrayList<List<ISecurityPunctuation>>();
		for (int i = 0; i < 2; i++) {
			this.spList.add(new ArrayList<ISecurityPunctuation>());
			this.saOpDelPo.add(new SAOperatorDelegate<T>());
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
