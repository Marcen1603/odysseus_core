package de.uniol.inf.is.odysseus.pnapproach.base.sweeparea;

import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;

/**
 * Die IPNAwareSweepArea bietet zusaetzlich zu den Methoden der {@link ISweepArea}
 * eine Moeglichkeit zu erfahren, wieviele positive bzw negative Elemente in der
 * SweepArea vorhanden sind.
 */
public interface IPNAwareSweepArea<T> extends ISweepArea<T> {
	public int getPositiveElementCount();

	public int getNegativeElementCount();
}
