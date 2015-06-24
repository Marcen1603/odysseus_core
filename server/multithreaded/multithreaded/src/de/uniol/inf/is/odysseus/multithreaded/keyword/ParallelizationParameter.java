package de.uniol.inf.is.odysseus.multithreaded.keyword;

public enum ParallelizationParameter {
	GLOBAL_DEGREE_OF_PARALLELIZATION, GLOBAL_BUFFERSIZE, GLOBAL_OPTIMIZATION;

	public static ParallelizationParameter getParameterByName(String parallelizationParameter) {
		for (ParallelizationParameter parameter : ParallelizationParameter.values()){
			if (parameter.name().equalsIgnoreCase(parallelizationParameter)){
				return parameter;
			}
		}
		return null;
	}
}