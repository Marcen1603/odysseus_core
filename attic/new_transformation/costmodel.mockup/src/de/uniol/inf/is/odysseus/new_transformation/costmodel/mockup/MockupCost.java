package de.uniol.inf.is.odysseus.new_transformation.costmodel.mockup;

import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.ICost;

public class MockupCost implements ICost {
	private final int cost;

	public MockupCost(int cost) {
		this.cost = cost;
	}

	@Override
	public boolean isBetterThan(ICost other) {
		MockupCost otherMockupCost = (MockupCost) other;
		return cost < otherMockupCost.cost;
	}

	@Override
	public String toString() {
		return "Cost of Operator: " + cost;
	}
}
