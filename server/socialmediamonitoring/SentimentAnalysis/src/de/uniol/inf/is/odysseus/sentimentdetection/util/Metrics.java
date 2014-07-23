package de.uniol.inf.is.odysseus.sentimentdetection.util;

/**
 * util class to calculate recall, precision and f-score
 * @author Marc Preuschaft
 *
 */
public class Metrics {

	/**
	 * calculated the recall 
	 * @param ctr
	 * @param totalExistCtr
	 * @return
	 */
	public static double recall(int ctr, int totalExistCtr) {
		double recall = 0.0;

		recall = ((ctr * 1.0) / totalExistCtr);

		return recall;

	}

	/**
	 * calculated the precision
	 * @param ctr
	 * @param totalCtr
	 * @return
	 */
	public static double precision(int ctr, int totalCtr) {
		double precision = 0.0;

		precision = ((ctr * 1.0) / totalCtr);

		return precision;
	}

	/**
	 * calculated the f-score
	 * @param recall
	 * @param precision
	 * @return
	 */
	public static double f_score(double recall, double precision) {
		double fScore = 0.0;

		fScore = 2 * ((precision * recall) / (precision + recall));

		return fScore;
	}

}
