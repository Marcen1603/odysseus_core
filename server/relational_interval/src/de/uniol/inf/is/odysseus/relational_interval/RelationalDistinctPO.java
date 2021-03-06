package de.uniol.inf.is.odysseus.relational_interval;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class RelationalDistinctPO<R extends Tuple<? extends ITimeInterval>>
		extends AbstractPipe<R, R> {

	final ITimeIntervalSweepArea<R> sweepArea;
	final ITransferArea<R, R> transfer;

	public RelationalDistinctPO(ITimeIntervalSweepArea<R> sweepArea, ITransferArea<R,R> transfer) {
		this.sweepArea = sweepArea;
		this.transfer = transfer;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		transfer.newElement(object, port);
		Iterator<R> out = sweepArea.extractElementsBefore(object.getMetadata()
				.getStart());
		while (out.hasNext()) {
			transfer.transfer(out.next());
		}
		// Find temporal overlapping elements
		out = sweepArea.queryOverlaps(object.getMetadata());
		// Found some?
		if (!out.hasNext()) {
			// if not, insert element (all elements are at least younger)
			sweepArea.insert(object);
			transfer.transfer(object);
		} else {
			while (out.hasNext()) {
				// identify duplicates
				R elem = out.next();
				if (elem.equals(object)) {
					if (elem.getMetadata().getEnd()
							.before(object.getMetadata().getEnd())) {
						// insert part from old element end to new element end (= unchanged)
						object.getMetadata().setStart(
								elem.getMetadata().getEnd());
					}
				}
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation.isHeartbeat()) {
			sweepArea.purgeElementsBefore(punctuation.getTime());
		}
		transfer.newElement(punctuation, port);

	}

}
