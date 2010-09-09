package de.uniol.inf.is.odysseus.nexmark.generator;

/**
 * Die NEXMarkBurstConfiguration kapselt die Parameter fuer einen Burst.
 * 
 * @see NEXMarkGeneratorConfiguration
 * @author Bernd Hochschulz
 * 
 */
public class NEXMarkBurstConfiguration {
	public int minTimeBetweenBursts;
	public int maxTimeBetweenBursts;

	public int minBurstDuration;
	public int maxBurstDuration;

	public int burstAccelerationFactor;

	public NEXMarkBurstConfiguration(int minTimeBetweenBursts, int maxTimeBetweenBursts,
			int minBurstDuration, int maxBurstDuration, int burstAccelerationFactor) {
		this.minTimeBetweenBursts = minTimeBetweenBursts;
		this.maxTimeBetweenBursts = maxTimeBetweenBursts;

		this.minBurstDuration = minBurstDuration;
		this.maxBurstDuration = maxBurstDuration;

		if (burstAccelerationFactor == 0) {
			this.burstAccelerationFactor = 1;
		} else {
			this.burstAccelerationFactor = burstAccelerationFactor;
		}
	}

	@Override
	public String toString() {
		return "burst [" + minTimeBetweenBursts + ", " + maxTimeBetweenBursts + "], ["
				+ minBurstDuration + ", " + maxBurstDuration + "], [" + burstAccelerationFactor
				+ "]";
	}
}
