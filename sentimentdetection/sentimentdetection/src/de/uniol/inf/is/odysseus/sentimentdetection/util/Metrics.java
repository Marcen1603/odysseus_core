package de.uniol.inf.is.odysseus.sentimentdetection.util;

/**
 * @author Marc Preuschaft
 *
 */
public class Metrics {

	public static double recall(int ctr, int totalExistCtr) {
		double recall = 0.0;

		recall = ((ctr * 1.0) / totalExistCtr);

		return recall;

	}

	public static double precision(int ctr, int totalCtr) {
		double precision = 0.0;

		precision = ((ctr * 1.0) / totalCtr);

		return precision;
	}

	public static double f_score(double recall, double precision) {
		double fScore = 0.0;

		fScore = 2 * ((precision * recall) / (precision + recall));

		return fScore;
	}

}
