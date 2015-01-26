package windscadaanwendung.hd;

/**
 * This class holds the historical data of a windFarm. You have to set an
 * instance of this class at the hitWindFarmData field in an windFarm instance.
 * 
 * @author MarkMilster
 * 
 */
public class HitWindFarmData {

	private double avgPervormance;

	/**
	 * Returns the average performance
	 * 
	 * @return the avgPervormance
	 */
	public double getAvgPervormance() {
		return avgPervormance;
	}

	/**
	 * Sets the average performace
	 * 
	 * @param avgPervormance
	 *            the avgPervormance to set
	 */
	public void setAvgPervormance(double avgPervormance) {
		this.avgPervormance = avgPervormance;
	}

}
