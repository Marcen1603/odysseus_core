package de.uniol.inf.is.odysseus.parallelization.autodetect;

public class PerformanceDetectionHelper {

	public static int getNumberOfCores(){
		int cores = Runtime.getRuntime().availableProcessors();
		return cores;
	}
	
}
