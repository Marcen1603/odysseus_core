package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;

public class ExtendedTISweepArea<T extends IStreamObject<? extends ITimeInterval>> extends DefaultTISweepArea<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3472731370495830967L;

	public List<T> queryAllElementsAsList() {
		List<T> result = new ArrayList<T>(getElements());
		return result;

	}

}
