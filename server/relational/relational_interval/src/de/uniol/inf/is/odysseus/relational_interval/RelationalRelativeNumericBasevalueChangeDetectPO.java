package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * This operator checks, if the current tuple differs more than the tolerance
 * from the given base value.
 * 
 * @author Tobias Brandt
 *
 */
public class RelationalRelativeNumericBasevalueChangeDetectPO extends AbstractRelationalNumericChangeDetectPO {

	private double baseValue;

	public RelationalRelativeNumericBasevalueChangeDetectPO(int[] comparePositions, double tolerance, double baseValue) {
		super(comparePositions, tolerance);
		this.baseValue = baseValue;
	}

	public RelationalRelativeNumericBasevalueChangeDetectPO(RelationalRelativeNumericBasevalueChangeDetectPO po) {
		super(po);
		this.baseValue = po.getBaseValue();
	}

	public double getBaseValue() {
		return baseValue;
	}

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		for (int i : comparePositions) {
			Number a = object.getAttribute(i);
			// Compare to the base element instead to the last element
			Number b = baseValue;
			if (a.doubleValue() != 0) {
				if (Math.abs(1 - b.doubleValue() / a.doubleValue()) > tolerance) {
					return true;
				}
			} else {
				// Need special handling if last value is 0 (0.1 * 0 is 0)
				if (b.doubleValue() != 0) {
					return true;
				}
			}
			return false;
		}

		return false;
	}

}
