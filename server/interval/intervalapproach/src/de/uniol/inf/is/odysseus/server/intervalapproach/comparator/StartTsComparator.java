package de.uniol.inf.is.odysseus.server.intervalapproach.comparator;

import java.io.Serializable;
import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.metadata.IProvidesMetadata;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

public class StartTsComparator implements Comparator<IProvidesMetadata<? extends ITimeInterval>>, Serializable {

	private static final long serialVersionUID = 5321893137959979782L;

	@Override
	public int compare(IProvidesMetadata<? extends ITimeInterval> left, IProvidesMetadata<? extends ITimeInterval> right) {
		PointInTime l = left.getMetadata().getStart();
		PointInTime r = right.getMetadata().getStart();

		int c = l.compareTo(r);
		if (c == 0) {
			return Long.compare(l.tiBreaker, r.tiBreaker);
		}
		return c;
	}
}