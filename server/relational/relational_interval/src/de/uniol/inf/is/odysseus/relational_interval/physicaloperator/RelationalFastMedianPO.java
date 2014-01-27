package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.FESortedPair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

public class RelationalFastMedianPO<T extends Comparable<T>>
		extends
		AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {

	IGroupProcessor<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> groupProcessor;

	Map<Long, List<FESortedPair<T, Tuple<? extends ITimeInterval>>>> elements = new HashMap<>();

	final private int medianAttrPos;
	final private boolean numericalMedian;

	public RelationalFastMedianPO(int medianAttrPos, boolean numericalMedian) {
		this.medianAttrPos = medianAttrPos;
		this.numericalMedian = numericalMedian;
	}

	public void setGroupProcessor(
			IGroupProcessor<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		groupProcessor.init();
	}

	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
		Long groupID = groupProcessor.getGroupID(object);
		List<FESortedPair<T, Tuple<? extends ITimeInterval>>> groupList = elements
				.get(groupID);
		if (groupList == null) {
			groupList = new LinkedList<FESortedPair<T, Tuple<? extends ITimeInterval>>>();
			elements.put(groupID, groupList);
		}
		T n = object.getAttribute(medianAttrPos);
		FESortedPair<T, Tuple<? extends ITimeInterval>> p = new FESortedPair<T, Tuple<? extends ITimeInterval>>(
				n, object);

		PointInTime eStart = object.getMetadata().getStart();
		// PointInTime eEnd = object.getMetadata().getEnd();

		// Cleanup

		Iterator<FESortedPair<T, Tuple<? extends ITimeInterval>>> iter = groupList
				.iterator();
		while (iter.hasNext()) {
			FESortedPair<T, Tuple<? extends ITimeInterval>> next = iter.next();
			if (next.getE2().getMetadata().getEnd().before(eStart)) {
				iter.remove();
			}
		}

		// Add new value sorted
		int pos = Collections.binarySearch(groupList, p);
		// System.err.println(pos + " for " + p + " in List " + groupList);
		if (pos < 0) { // Element not found in list
			int insert = (-1) * pos - 1;
			groupList.add(insert, p);
		} else {
			groupList.add(pos, p);
		}

		// System.err.println("After insert: " + groupList);

		// Create Median
		Tuple<? extends ITimeInterval> gr = groupProcessor
				.getGroupingPart(object);
		FESortedPair<T, Tuple<? extends ITimeInterval>> median = null;
		int medianPos = 0;
		if (!numericalMedian) {
			if (groupList.size() > 1) {
				if (groupList.size() % 2 == 0) {
					medianPos = (groupList.size() / 2) - 1;
				} else {
					medianPos = (groupList.size() / 2);
				}
			}
			median = groupList.get(medianPos);
			gr.append(median.getE1(), false);
		} else {
			Double num_median;
			if (groupList.size() > 1){
				int middle = groupList.size() / 2;
				if (groupList.size() % 2 == 0) {					
					num_median = (((Number) groupList.get(middle).getE1()).doubleValue() + ((Number) groupList.get(middle+1).getE1()).doubleValue())/2;
				} else {
					num_median = ((Number) groupList.get(middle).getE1()).doubleValue();
				}								
			}else{
				num_median = ((Number) groupList.get(0).getE1()).doubleValue();
			}
			gr.append(num_median, false);
		}

		// TODO what if element end is before "end" of groupList
		transfer(gr);
	}

	@Override
	public AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> clone() {
		throw new IllegalArgumentException("Not implemented");
	}

}
