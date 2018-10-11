package de.uniol.inf.is.odysseus.rcp.viewer.stream.interval;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public final class IntervalLine {

	private final long start;
	private final long end;

	private final boolean isStartInfinite;
	private final boolean isEndInfinite;

	private final IStreamObject<? extends ITimeInterval> element;

	public IntervalLine(IStreamObject<? extends ITimeInterval> element) {
		// Preconditions.checkNotNull(element, "element must not be null!");

		this.element = element;
		ITimeInterval metadata = element.getMetadata();

		if (metadata.getStart().isInfinite()) {
			isStartInfinite = true;
			start = -1;
		} else {
			isStartInfinite = false;
			start = metadata.getStart().getMainPoint();
		}

		if (metadata.getEnd().isInfinite()) {
			isEndInfinite = true;
			end = -1;
		} else {
			isEndInfinite = false;
			end = metadata.getEnd().getMainPoint();
		}
	}

	public boolean isEndInfinite() {
		return isEndInfinite;
	}

	public boolean isStartInfinite() {
		return isStartInfinite;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public IStreamObject<? extends ITimeInterval> getElement() {
		return element;
	}
}
