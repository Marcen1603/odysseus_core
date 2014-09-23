package windscadaanwendung.hd;

public class HitWKAData {
	
	private double avgWindSpeed;
	/**
	 * @return the avgWindSpeed
	 */
	public double getAvgWindSpeed() {
		return avgWindSpeed;
	}

	/**
	 * @param avgWindSpeed the avgWindSpeed to set
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
	 * @param avgWindDirection the avgWindDirection to set
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
	 * @param avgRotationalSpeed the avgRotationalSpeed to set
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
	 * @param avgPerformance the avgPerformance to set
	 */
	public void setAvgPerformance(double avgPerformance) {
		this.avgPerformance = avgPerformance;
	}

	private double avgWindDirection;
	private double avgRotationalSpeed;
	private double avgPerformance;
	
	public HitWKAData() {
		
	}

}
