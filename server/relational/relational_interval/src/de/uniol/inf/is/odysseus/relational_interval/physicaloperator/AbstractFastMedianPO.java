package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

abstract public class AbstractFastMedianPO<T extends Comparable<T>>
		extends
		AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {

	IGroupProcessor<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> groupProcessor;

	protected final int medianAttrPos;
	protected final boolean numericalMedian;

	final protected ITransferArea<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> transfer = new TITransferArea<>();

	private Map<Long, Tuple<? extends ITimeInterval>> lastCreatedElement = new HashMap<>();

	public AbstractFastMedianPO(int medianAttrPos, boolean numericalMedian) {
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
		transfer.init(this, 1);
	}

	@Override
	final protected void process_next(Tuple<? extends ITimeInterval> object,
			int port) {

		transfer.newElement(object, port);

		Long groupID = groupProcessor.getGroupID(object);

		process_next(object, port, groupID);
	}

	abstract protected void process_next(Tuple<? extends ITimeInterval> object,
			int port, Long groupID);

	public void createOutput(Long groupID, Tuple<? extends ITimeInterval> gr) {
		Tuple<? extends ITimeInterval> last_gr = lastCreatedElement
				.get(groupID);

		// TODO what if element end is before "end" of groupList

		if (last_gr != null) {
			if (last_gr.getMetadata().getStart()
					.before(gr.getMetadata().getStart())) {
				last_gr.getMetadata().setEnd(gr.getMetadata().getStart());
				transfer.transfer(last_gr);
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
		if (!(ipo instanceof AbstractFastMedianPO)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		AbstractFastMedianPO<T> po = (AbstractFastMedianPO<T>) ipo;

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

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		transfer.sendPunctuation(punctuation);
		transfer.newElement(punctuation, port);
	}
}
