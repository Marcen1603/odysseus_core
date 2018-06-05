package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SPMap;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;

public class SAJoinPO<K extends ITimeInterval, T extends IStreamObject<K>> extends JoinTIPO<K, T> {

	// Contains a Map of the Objects with the SPs referring to them as a key
	SPMap<K, T> spMap;

	// Contains the SPs that were sent to the output recently
	List<ISecurityPunctuation> lastSentPunctuation;

	// Contains the currently active SPs
	List<SAOperatorDelegate<T>> saOpDelPo;

	// Collection of keys for the SPMap
	List<List<ISecurityPunctuation>> spList;

	// Attribute for validation of the tupleRange
	String tupleRangeAttribute;

	public SAJoinPO(IDataMergeFunction<T, K> dataMerge, IMetadataMergeFunction<K> metadataMerge,
			ITransferArea<T, T> transferFunction, String tupleRangeAttribute) {
		super(dataMerge, metadataMerge, transferFunction);
		this.tupleRangeAttribute = tupleRangeAttribute;
		initLists();

	}

	@SuppressWarnings("unchecked")
	public SAJoinPO(IMetadataMergeFunction<?> metaDataMerge, String tupleRangeAttribute) {
		super((IMetadataMergeFunction<K>) metaDataMerge);
		this.tupleRangeAttribute = tupleRangeAttribute;
		initLists();

	}

	@Override
	public synchronized void processPunctuation(IPunctuation inPunctuation, int port) {

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

		int otherport = port ^ 1;
		boolean send = false;

		// adds the incoming Object to the SPMap, with the current active SPs as
		// keys
		if (!this.saOpDelPo.get(port).getRecentSPs().isEmpty()) {
			for (ISecurityPunctuation sp : this.saOpDelPo.get(port).getRecentSPs()) {
				this.spMap.addValue(sp, object, port);
			}
		}

		// removes the sps that are not required anymore from the SPMap, based
		// on the timestamp of the incoming object
		List<ISecurityPunctuation> spsToDelete = new ArrayList<>();
		if (!this.spList.isEmpty() && !this.spList.get(otherport).isEmpty()) {
			for (ISecurityPunctuation sp : this.spList.get(otherport)) {
				spsToDelete.add(this.spMap.invalidate(sp, object.getMetadata().getStart(), otherport));
			}
		}
		// removes the SPs, that got deleted from the SPMap, from the spList,
		// which is functioning as a collection of keys for the SPMap
		this.spList.get(otherport).removeAll(spsToDelete);

		Iterator<T> qualifies;
		Order order = Order.fromOrdinal(port);
		qualifies = this.getSweepArea(otherport, DEFAULT_GROUPING_KEY).queryCopy(object, order, false);
		List<ISecurityPunctuation> intersectedSPs = new ArrayList<>();

		// extracts the SPs of the matching Tuples from the other port and
		// intersects them with the SPs from the incoming object
		boolean hit = qualifies.hasNext();
		while (qualifies.hasNext()) {
			ISecurityPunctuation intersectedSP;
			T next = qualifies.next();
			List<ISecurityPunctuation> matchingSPs = this.spMap.getMatchingSPs(next, otherport);
			if (!matchingSPs.isEmpty()) {
				for (ISecurityPunctuation matchingSP : matchingSPs) {
					ISecurityPunctuation temp = matchingSP;
					for (ISecurityPunctuation currentSP : this.saOpDelPo.get(port).getRecentSPs()) {
						if (port == 0) {
							// intersected SPs get the timestamp of the object-1
							// because it has to be smaller else the Security
							// Punctuation is transferred after the object
							intersectedSP = temp.intersect(currentSP, this.getInputSchema(port),
									this.getInputSchema(otherport),
									object.getMetadata().getStart().minus(new PointInTime(1L)));

						} else {
							intersectedSP = currentSP.intersect(temp, this.getInputSchema(otherport),
									this.getInputSchema(port),
									object.getMetadata().getStart().minus(new PointInTime(1L)));
						}
						// only adds the intersected sp, if it isn't already
						// contained in the list of intersected sps
						if (!intersectedSPs.contains(intersectedSP) && !intersectedSP.isEmpty()) {
							intersectedSPs.add(intersectedSP);
						}
					}
				}

			}
		}

		// if the intersected SPs are empty, the object and the intersected
		// Security Punctuations are discarded
		if (!intersectedSPs.isEmpty()) {
			send = true;
			if (!checkIfAlreadySent(intersectedSPs)) {

				lastSentPunctuation.clear();
				for (ISecurityPunctuation sp : intersectedSPs) {
					transferFunction.sendPunctuation(sp);
					lastSentPunctuation.add(sp);
				}
			}

		}
		// only inserts the element into the transferarea, if there is at least
		// one intersected SP, else the object is just inserted into the
		// sweeparea
		if (!send) {
			super.insertElement(object, port, hit);
		} else {

			super.process_next(object, port);
		}

	}

	// checks if the object fits to one of the current SPs in the
	// SAOperatorDelegate
	private boolean checkSP(T object, int port) {
		for (ISecurityPunctuation sp : this.saOpDelPo.get(port).getRecentSPs()) {
			if (sp.getDDP().match(object, this.getInputSchema(port), this.tupleRangeAttribute)) {
				return true;
			}
		}
		return false;

	}

	// checks if the last sent SPs are the same like the new intersected SPs, if
	// they are the same, they won't be sent again
	private boolean checkIfAlreadySent(List<ISecurityPunctuation> intersectedSPs) {
		if (lastSentPunctuation.isEmpty()) {
			return false;
		} else if (intersectedSPs.size() != lastSentPunctuation.size()) {
			return false;
		}

		else if (lastSentPunctuation.containsAll(intersectedSPs) && intersectedSPs.containsAll(lastSentPunctuation)) {
			return true;
		}
		return false;
	}

	// sets up the lists at the start of the operator
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

	public void setTupleRangeAttribute(String tupleRangeAttribute) {
		this.tupleRangeAttribute = tupleRangeAttribute;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof SAJoinPO)) {
			return false;
		}
		return super.process_isSemanticallyEqual(ipo);
	}

}
