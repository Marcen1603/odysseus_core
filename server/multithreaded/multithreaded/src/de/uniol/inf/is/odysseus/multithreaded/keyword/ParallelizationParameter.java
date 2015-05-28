package de.uniol.inf.is.odysseus.multithreaded.keyword;

public enum ParallelizationParameter {
	DEGREE_OF_PARALLELIZATION;

	public static ParallelizationParameter getParameterByName(String parallelizationParameter) {
		for (ParallelizationParameter parameter : ParallelizationParameter.values()){
			if (parameter.name().equalsIgnoreCase(parallelizationParameter)){
				return parameter;
			}
		}
		return null;
	}
}