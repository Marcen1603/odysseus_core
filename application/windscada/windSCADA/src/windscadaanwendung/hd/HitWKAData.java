package windscadaanwendung.hd;

/**
 * This class holds the historical data of a WKA. You have to set an instance of
 * this class in an WKA instance
 * 
 * @author MarkMilster
 * 
 */
public class HitWKAData {

	private double minWindSpeed;
	private double maxWindSpeed;
	private double minRotationalSpeed;
	private double maxRotationalSpeed;
	private double minPerformance;
	private double maxPerformance;

	/**
	 * @return the minWindSpeed
	 */
	public double getMinWindSpeed() {
		return minWindSpeed;
	}

	/**
	 * @param minWindSpeed
	 *            the minWindSpeed to set
	 */
	public void setMinWindSpeed(double minWindSpeed) {
		this.minWindSpeed = minWindSpeed;
	}

	/**
	 * @return the maxWindSpeed
	 */
	public double getMaxWindSpeed() {
		return maxWindSpeed;
	}

	/**
	 * @param maxWindSpeed
	 *            the maxWindSpeed to set
	 */
	public void setMaxWindSpeed(double maxWindSpeed) {
		this.maxWindSpeed = maxWindSpeed;
	}

	/**
	 * @return the minRotationalSpeed
	 */
	public double getMinRotationalSpeed() {
		return minRotationalSpeed;
	}

	/**
	 * @param minRotationalSpeed
	 *            the minRotationalSpeed to set
	 */
	public void setMinRotationalSpeed(double minRotationalSpeed) {
		this.minRotationalSpeed = minRotationalSpeed;
	}

	/**
	 * @return the maxRotationalSpeed
	 */
	public double getMaxRotationalSpeed() {
		return maxRotationalSpeed;
	}

	/**
	 * @param maxRotationalSpeed
	 *            the maxRotationalSpeed to set
	 */
	public void setMaxRotationalSpeed(double maxRotationalSpeed) {
		this.maxRotationalSpeed = maxRotationalSpeed;
	}

	/**
	 * @return the minPerformance
	 */
	public double getMinPerformance() {
		return minPerformance;
	}

	/**
	 * @param minPerformance
	 *            the minPerformance to set
	 */
	public void setMinPerformance(double minPerformance) {
		this.minPerformance = minPerformance;
	}

	/**
	 * @return the maxPerformance
	 */
	public double getMaxPerformance() {
		return maxPerformance;
	}

	/**
	 * @param maxPerformance
	 *            the maxPerformance to set
	 */
	public void setMaxPerformance(double maxPerformance) {
		this.maxPerformance = maxPerformance;
	}

	private double avgWindSpeed;

	/**
	 * @return the avgWindSpeed
	 */
	public double getAvgWindSpeed() {
		return avgWindSpeed;
	}

	/**
	 * @param avgWindSpeed
	 *            the avgWindSpeed to set
	 */
	public void setAvgWindSpeed(double avgWindSpeed) {
		this.avgWindSpeed = avgWindSpeed;
	}

	/**
	 * @return the avgWindDirection
	 */
	public double getAvgWindDirection() {
		return avgWindDirection;
	}

	/**
	 * @param avgWindDirection
	 *            the avgWindDirection to set
	 */
	public void setAvgWindDirection(double avgWindDirection) {
		this.avgWindDirection = avgWindDirection;
	}

	/**
	 * @return the avgRotationalSpeed
	 */
	public double getAvgRotationalSpeed() {
		return avgRotationalSpeed;
	}

	/**
	 * @param avgRotationalSpeed
	 *            the avgRotationalSpeed to set
	 */
	public void setAvgRotationalSpeed(double avgRotationalSpeed) {
		this.avgRotationalSpeed = avgRotationalSpeed;
	}

	/**
	 * @return the avgPerformance
	 */
	public double getAvgPerformance() {
		return avgPerformance;
	}

	/**
	 * @param avgPerformance
	 *            the avgPerformance to set
	 */
	public void setAvgPerformance(double avgPerformance) {
		this.avgPerformance = avgPerformance;
	}

	private double avgWindDirection;
	private double avgRotationalSpeed;
	private double avgPerformance;

}
