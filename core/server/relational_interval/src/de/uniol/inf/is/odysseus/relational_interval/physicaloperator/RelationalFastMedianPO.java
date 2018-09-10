package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.FESortedPair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public class RelationalFastMedianPO<T extends Comparable<T>> extends
		AbstractFastMedianPO<T> {

	Map<Object, List<FESortedPair<T, Tuple<? extends ITimeInterval>>>> elements = new HashMap<>();

	public RelationalFastMedianPO(int medianAttrPos, boolean numericalMedian) {
		super(medianAttrPos, numericalMedian);
	}

	
	@Override
	protected void process_open() throws OpenFailedException {
		elements.clear();
		super.process_open();
	}

	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object,
			int port, Object groupID) {

		List<FESortedPair<T, Tuple<? extends ITimeInterval>>> groupList = getOrCreateGroupList(groupID);

		@SuppressWarnings("unchecked")
		FESortedPair<T, Tuple<? extends ITimeInterval>> p = new FESortedPair<T, Tuple<? extends ITimeInterval>>(
				(T) object.getAttribute(medianAttrPos), object);

		cleanUpAndInsertNewValue(groupList, object.getMetadata().getStart(), p);

		Tuple<? extends ITimeInterval> gr = createMedian(object, groupList);

		createOutput(groupID, gr);
	}

	public void cleanUpAndInsertNewValue(
			List<FESortedPair<T, Tuple<? extends ITimeInterval>>> groupList,
			PointInTime eStart,
			FESortedPair<T, Tuple<? extends ITimeInterval>> p) {
		Iterator<FESortedPair<T, Tuple<? extends ITimeInterval>>> iter = groupList
				.iterator();
		int pos = 0;
		boolean found = false;
		// boolean odd = groupList.size() % 2 != 0;
		while (iter.hasNext()) {
			FESortedPair<T, Tuple<? extends ITimeInterval>> next = iter.next();
			if (next.getE2().getMetadata().getEnd().beforeOrEquals(eStart)) {
				iter.remove();
				if (!found) {
					pos--;
				}
			}
			if (!found) {
				int c = next.getE1().compareTo(p.getE1());
				if (c == 0) {
					found = true;
				} else if (c < 0) {
					pos++;
				} else {
					found = true;
					pos = ((pos+1) * -1);
				}
			}
		}
		insert(groupList, p, pos);
	}

	private List<FESortedPair<T, Tuple<? extends ITimeInterval>>> getOrCreateGroupList(
			Object groupID) {
		List<FESortedPair<T, Tuple<? extends ITimeInterval>>> groupList = elements
				.get(groupID);
		if (groupList == null) {
			groupList = new LinkedList<FESortedPair<T, Tuple<? extends ITimeInterval>>>();
			elements.put(groupID, groupList);
		}
		return groupList;
	}

	private void insert(
			List<FESortedPair<T, Tuple<? extends ITimeInterval>>> groupList,
			FESortedPair<T, Tuple<? extends ITimeInterval>> p, int pos) {
		if (pos < 0) { // Element not found in list
			int insert = (-1) * pos - 1;
			groupList.add(insert, p);
		} else {
			groupList.add(pos, p);
		}
		//assureOrder(groupList);
	}

	public Tuple<? extends ITimeInterval> createMedian(
			Tuple<? extends ITimeInterval> object,
			List<FESortedPair<T, Tuple<? extends ITimeInterval>>> groupList) {

		// DEBUG
		//assureOrder(groupList);

		Tuple<? extends ITimeInterval> gr = groupProcessor
				.getGroupingPart(object);
		FESortedPair<T, Tuple<? extends ITimeInterval>> median = null;
		int medianPos = 0;
		if (!numericalMedian) {
			if (groupList.size() > 1) {
				medianPos = groupList.size()/ 2 ;
			}
			median = groupList.get(medianPos);
			gr.append(median.getE1(), false);
		} else {
			Double num_median;
			if (groupList.size() > 1) {
				int middle = groupList.size() / 2 ;
				if (groupList.size() % 2 == 0) {
					num_median = (((Number) groupList.get(middle-1).getE1())
							.doubleValue() + ((Number) groupList
							.get(middle).getE1()).doubleValue()) / 2;
				} else {
					num_median = ((Number) groupList.get(middle).getE1())
							.doubleValue();
				}
			} else { // group list == 1
				num_median = ((Number) groupList.get(0).getE1()).doubleValue();
			}
			gr.append(num_median, false);
		}
		return gr;
	}

	
	@SuppressWarnings("unused")
	private void assureOrder(
			List<FESortedPair<T, Tuple<? extends ITimeInterval>>> groupList) {
		if (groupList.size() > 1) {
			Iterator<FESortedPair<T, Tuple<? extends ITimeInterval>>> i = groupList
					.iterator();
			FESortedPair<T, Tuple<? extends ITimeInterval>> last = i.next();
			while (i.hasNext()) {
				FESortedPair<T, Tuple<? extends ITimeInterval>> current = i.next();
				if (last.getE1().compareTo(current.getE1()) > 0){
					throw new IllegalArgumentException("WRONG ORDER IN GROUP_LIST "+last+" not before "+current);
				}
			}
		}
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalFastMedianPO)) {
			return false;
		}

		return super.isSemanticallyEqual(ipo);
	}

}
