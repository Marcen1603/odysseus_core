package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class PunctuationPO<T extends IMetaAttributeContainer<? extends ITimeInterval>>
		extends AbstractPipe<T, T> {
	private final long ratio;
	private PointInTime punctuationTime;
	private long count;

	public PunctuationPO(final long ratio) {
		this.punctuationTime = PointInTime.getZeroTime();
		this.ratio = ratio;
		this.count = 0;
	}

	public PunctuationPO(final PunctuationPO<T> po) {
		this.punctuationTime = po.punctuationTime.clone();
		this.ratio = po.ratio;
		this.count = po.count;

	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T tuple, int port) {
		if (port == 0) {
			if (tuple.getMetadata().getStart().after(punctuationTime)) {
				punctuationTime = tuple.getMetadata().getStart();
				transfer(tuple);
				this.count = 0;
			}
		} else {
			this.count++;
			if (count % ratio == 0) {
				if (tuple.getMetadata().getStart().after(punctuationTime)) {
					punctuationTime = tuple.getMetadata().getStart();
				}
				sendPunctuation(punctuationTime);
			}
		}
		synchronized (this) {
			if (isDone()) {
				propagateDone();
				return;
			}
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (this != ipo) {
			return false;
		}
		if (getClass() != ipo.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		PunctuationPO<T> po = (PunctuationPO<T>) ipo;
		if (this.hasSameSources(po)) {
			return true;
		}
		return false;
	}

	@Override
	public PunctuationPO<T> clone() {
		return new PunctuationPO<T>(this);
	}

}
