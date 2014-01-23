package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.FESortedPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

public class RelationalFastMapPO<T extends Comparable<T>>
		extends
		AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {

	IGroupProcessor<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> groupProcessor;

	Map<Long, List<FESortedPair<T, Tuple<? extends ITimeInterval>>>> elements = new HashMap<>();

	final private int medianAttrPos;

	public RelationalFastMapPO(int medianAttrPos) {
		this.medianAttrPos = medianAttrPos;
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
			groupList = new ArrayList<FESortedPair<T, Tuple<? extends ITimeInterval>>>();
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
		groupList.add(pos, p);

		// Create Median
		FESortedPair<T, Tuple<? extends ITimeInterval>> median = groupList
				.get(groupList.size() / 2);
		Tuple<? extends ITimeInterval> gr = groupProcessor
				.getGroupingPart(object);
		gr.append(median, false);
		// TODO what if element end is before "end" of groupList
		transfer(gr);
	}

	@Override
	public AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> clone() {
		throw new IllegalArgumentException("Not implemented");
	}

}
