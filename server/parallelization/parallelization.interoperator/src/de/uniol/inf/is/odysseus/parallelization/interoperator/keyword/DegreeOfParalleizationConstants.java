package de.uniol.inf.is.odysseus.parallelization.interoperator.keyword;

public enum DegreeOfParalleizationConstants {
	USERDEFINED, GLOBAL, AUTO;

	public static DegreeOfParalleizationConstants getConstantByName(
			String name) {
		for (DegreeOfParalleizationConstants parameter : DegreeOfParalleizationConstants
				.values()) {
			if (parameter.name().equalsIgnoreCase(name)) {
				return parameter;
			}
		}
		return null;
	}
}
