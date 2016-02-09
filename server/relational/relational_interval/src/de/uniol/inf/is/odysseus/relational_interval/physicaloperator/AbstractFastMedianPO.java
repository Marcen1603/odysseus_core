package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
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

	private Map<Object, Tuple<? extends ITimeInterval>> lastCreatedElement = new HashMap<>();

	protected List<Double> percentiles;

	protected boolean appendGlobalMedian;

	private Tuple<? extends ITimeInterval> globalMedian;

	public AbstractFastMedianPO(int medianAttrPos, boolean numericalMedian) {
		this.medianAttrPos = medianAttrPos;
		this.numericalMedian = numericalMedian;
	}

	public void setGroupProcessor(
			IGroupProcessor<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	public void setPercentiles(List<Double> percentiles) {
		if (percentiles != null) {
			this.percentiles = new LinkedList<>(percentiles);
		}
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		groupProcessor.init();
		transfer.init(this, 1);
		lastCreatedElement.clear();;
	}

	@Override
	final protected void process_next(Tuple<? extends ITimeInterval> object,
			int port) {

		// transfer.newElement(object, port);

		if (appendGlobalMedian) {
			process_next(object, port, null);
		}

		Object groupID = groupProcessor.getGroupID(object);
		process_next(object, port, groupID);
	}
	
	@Override
	protected void process_done(int port) {
		transfer.done(port);
	}

	abstract protected void process_next(Tuple<? extends ITimeInterval> object,
			int port, Object groupID);

	public void createOutput(Object groupID, Tuple<? extends ITimeInterval> gr) {

		// just remember global median
		if (groupID == null) {
			globalMedian = gr;
		} else {
			Tuple<? extends ITimeInterval> last_gr = lastCreatedElement
					.get(groupID);

			if (appendGlobalMedian) { // append the global median
				gr = gr.append(
						globalMedian.getAttribute(globalMedian.size() - 1),
						true);
			}
			

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

		if (lastCreatedElement.size() > 0) {
			// Find min ts in groups
			PointInTime minTs = PointInTime.getInfinityTime();
			for (Tuple<? extends ITimeInterval> e : lastCreatedElement.values()) {
				if (e.getMetadata().getStart().before(minTs)) {
					minTs = e.getMetadata().getStart();
				}
			}
			transfer.newHeartbeat(minTs, 0);
		}

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

		if ((this.percentiles == null && po.percentiles == null)
				|| (percentiles != null && !(percentiles.equals(po.percentiles)))) {
			return false;
		}

		if (appendGlobalMedian != po.appendGlobalMedian) {
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

	public void appendGlobalMedian(boolean appendGlobalMedian) {
		this.appendGlobalMedian = appendGlobalMedian;
	}

}
