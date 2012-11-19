package de.uniol.inf.is.odysseus.core;

public enum Order {
	LeftRight, RightLeft;
	public Order inverse() {
		if (this.ordinal() == LeftRight.ordinal()) {
			return RightLeft;
		}
        return LeftRight;
	}

	public static Order fromOrdinal(int i) {
		switch (i) {
		case 0:
			return LeftRight;
		case 1:
			return RightLeft;
		default:
			throw new IllegalArgumentException(
					"illegal ordinal value for Order");
		}
	}
}