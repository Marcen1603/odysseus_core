package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * This operator checks, if the current tuple differs more than the tolerance
 * from the given base value.
 * 
 * @author Tobias Brandt
 *
 */
public class RelationalAbsoluteNumericBasevalueChangeDetectPO extends AbstractRelationalNumericChangeDetectPO {

	private double baseValue;

	public RelationalAbsoluteNumericBasevalueChangeDetectPO(int[] comparePositions, double tolerance, double baseValue) {
		super(comparePositions, tolerance);
		this.baseValue = baseValue;
	}

	public RelationalAbsoluteNumericBasevalueChangeDetectPO(RelationalAbsoluteNumericBasevalueChangeDetectPO po) {
		super(po);
		this.baseValue = po.getBaseValue();
	}

	public double getBaseValue() {
		return this.baseValue;
	}

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		for (int i : comparePositions) {
			Number a = object.getAttribute(i);
			// Compare to the base element instead to the last element
			Number b = baseValue;
			if (Math.abs(a.doubleValue() - b.doubleValue()) > tolerance) {
				return true;
			}
			return false;
		}
		return false;
	}

}
