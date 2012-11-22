package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

abstract public class AbstractPunctuation extends PointInTime implements
		IPunctuation {

	private static final long serialVersionUID = -1405097506573319434L;

	public AbstractPunctuation(long point) {
		super(point);
	}

	public AbstractPunctuation(PointInTime p) {
		super(p);
	}

}
