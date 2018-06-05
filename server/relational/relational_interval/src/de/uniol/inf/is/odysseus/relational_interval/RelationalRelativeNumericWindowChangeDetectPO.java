package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * This operator checks, if there is a relative change within that window.
 * Therefore, the oldest tuple in the window is compared to the new incoming
 * tuple.
 * 
 * @author Tobias Brandt
 *
 */
public class RelationalRelativeNumericWindowChangeDetectPO extends AbstractRelationalNumericWindowChangeDetectPO {


	public RelationalRelativeNumericWindowChangeDetectPO(int[] comparePositions, double tolerance) {
		super(comparePositions, tolerance);
	}

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		for (int i : comparePositions) {
			Number a = object.getAttribute(i);
			Number b = lastElement.getAttribute(i);
			if (a.doubleValue() != 0) {
				if (Math.abs(1 - b.doubleValue() / a.doubleValue()) > tolerance) {
					return true;
				}
			}else{
				// Need special handling if last value is 0 (0.1 * 0 is 0)
				if (b.doubleValue() != 0){
					return true;
				}
			}
			return false;
		}

		return false;
	}

}
