package de.uniol.inf.is.odysseus.multithreaded.keyword;

public enum ParallelizationTypes {
	INTER_OPERATOR,
	INTRA_OPERATOR;

	public static boolean contains(String parallelizationType) {
		for (ParallelizationTypes type : ParallelizationTypes.values()){
			if (type.name().equalsIgnoreCase(parallelizationType)){
				return true;
			}
		}
		return false;
	}

	public static ParallelizationTypes getTypeByName(String parallelizationType) {
		for (ParallelizationTypes type : ParallelizationTypes.values()){
			if (type.name().equalsIgnoreCase(parallelizationType)){
				return type;
			}
		}
		return null;
	}
}
