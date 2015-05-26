package de.uniol.inf.is.odysseus.multithreaded.autodetect;

public class PerformanceDetectionHelper {

	public static int getNumberOfCores(){
		int cores = Runtime.getRuntime().availableProcessors();
		return cores;
	}
	
}
