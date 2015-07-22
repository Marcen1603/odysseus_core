package de.uniol.inf.is.odysseus.parallelization.interoperator.constants;

public enum BufferSizeConstants {
	USERDEFINED, GLOBAL, AUTO;

	public static BufferSizeConstants getConstantByName(String name) {
		for (BufferSizeConstants parameter : BufferSizeConstants.values()) {
			if (parameter.name().equalsIgnoreCase(name)) {
				return parameter;
			}
		}
		return null;
	}
}
