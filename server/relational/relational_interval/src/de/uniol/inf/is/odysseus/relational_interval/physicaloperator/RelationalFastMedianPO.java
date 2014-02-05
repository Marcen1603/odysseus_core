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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
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

	private Map<Long, Tuple<? extends ITimeInterval>> lastCreatedElement = new HashMap<>();

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
//		if (groupProcessor instanceof RelationalNoGroupProcessor) {
//			System.err.println("New element " + object);
//		}

		Iterator<FESortedPair<T, Tuple<? extends ITimeInterval>>> iter = groupList
				.iterator();
		while (iter.hasNext()) {
			FESortedPair<T, Tuple<? extends ITimeInterval>> next = iter.next();
			if (next.getE2().getMetadata().getEnd().beforeOrEquals(eStart)) {
				iter.remove();
			}
		}

		// System.err.println("Cleaning done "+groupList);

		// Add new value sorted
		int pos = Collections.binarySearch(groupList, p);
//		if (groupProcessor instanceof RelationalNoGroupProcessor) {
//			System.err.println(pos + " for " + p + " in List " + groupList);
//		}
		if (pos < 0) { // Element not found in list
			int insert = (-1) * pos - 1;
			groupList.add(insert, p);
		} else {
			groupList.add(pos, p);
		}

//		if (groupProcessor instanceof RelationalNoGroupProcessor) {
//			System.err.println("After insert: " + groupList);
//		}
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
			if (groupList.size() > 1) {
				int middle = groupList.size() / 2;
				if (groupList.size() % 2 == 0) {
					num_median = (((Number) groupList.get(middle - 1).getE1())
							.doubleValue() + ((Number) groupList.get(middle)
							.getE1()).doubleValue()) / 2;
				} else {
					num_median = ((Number) groupList.get(middle).getE1())
							.doubleValue();
				}
			} else {
				num_median = ((Number) groupList.get(0).getE1()).doubleValue();
			}
			gr.append(num_median, false);
		}

//		if (groupProcessor instanceof RelationalNoGroupProcessor){
//			System.err.println("Found median "+gr+" in list "+groupList);
//		}
		
		// TODO what if element end is before "end" of groupList

		// Element can be written, if next element is created (starttimestamp of
		// next element is needed)

		Tuple<? extends ITimeInterval> last_gr = lastCreatedElement
				.get(groupID);
		if (last_gr != null) {
			if (last_gr.getMetadata().getStart()
					.before(gr.getMetadata().getStart())) {
				last_gr.getMetadata().setEnd(gr.getMetadata().getStart());
				transfer(last_gr);
				lastCreatedElement.put(groupID, gr);
			}
		} else {
			lastCreatedElement.put(groupID, gr);
		}
	}

	@Override
	public AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> clone() {
		throw new IllegalArgumentException("Not implemented");
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalFastMedianPO)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		RelationalFastMedianPO<T> po = (RelationalFastMedianPO<T>) ipo;

		if (medianAttrPos != po.medianAttrPos
				|| numericalMedian != po.numericalMedian) {
			return false;
		}

		if (this.groupProcessor == null && po.groupProcessor == null) {
			return true;
		}

		if (this.groupProcessor != null && po.groupProcessor != null) {
			return groupProcessor.equals(po.groupProcessor);
		}

		return false;
	}
}
