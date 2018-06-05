package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * This change detect operator checks for absolute change within a window.
 * 
 * @author Tobias Brandt
 *
 */
public class RelationalAbsoluteNumericWindowChangeDetectPO extends AbstractRelationalNumericWindowChangeDetectPO {

	public RelationalAbsoluteNumericWindowChangeDetectPO(int[] comparePositions, double tolerance) {
		super(comparePositions, tolerance);
	}

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		for (int i : comparePositions) {
			Number a = object.getAttribute(i);
			Number b = lastElement.getAttribute(i);
			if (Math.abs(a.doubleValue() - b.doubleValue()) > tolerance) {
				return true;
			}
			return false;
		}

		return false;
	}

}
