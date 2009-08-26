package de.uniol.inf.is.odysseus.nexmark.generator;

import java.util.Random;

/**
 * Verwaltet Burststart und -ende fuer den {@link NEXMarkGenerator}.
 * 
 * @author Bernd Hochschulz
 * 
 */
public class NEXMarkBurstGenerator extends Thread {
	private NEXMarkBurstConfiguration burstConfig;
	private NEXMarkGenerator generator;

	private Random rnd;
	private long lastBurst = 0;

	/**
	 * Erstellt einen neuen BurstGenerator.
	 * 
	 * @param burstConfig
	 *            - Konfiguration anhand die Zeitabstaende bestimmt werden
	 * @param generator
	 *            - Generator der benachrichtigt werden soll
	 */
	public NEXMarkBurstGenerator(NEXMarkBurstConfiguration burstConfig, NEXMarkGenerator generator,
			boolean deterministic) {
		this.burstConfig = burstConfig;
		this.generator = generator;

		if (deterministic) {
			this.rnd = new Random(104132);
		} else {
			this.rnd = new Random();
		}
	}

	@Override
	public void run() {
		long timeUntilBurst;
		long timeUntilBurstEnd;

		while (!interrupted()) {
			// warte bis Burstanfang
			timeUntilBurst = getTimeUntilBurst();
			try {
				Thread.sleep(timeUntilBurst);
			} catch (InterruptedException e) {
				break;
			}

			// System.out.println("+++++++++++++++++++++++++++++++++++++++");
			// benachrichtige Generator
			lastBurst += timeUntilBurst;
			generator.startBurst(lastBurst);

			// warte bis Burstende
			timeUntilBurstEnd = getTimeUntilBurstEnd();
			try {
				Thread.sleep(timeUntilBurstEnd);
			} catch (InterruptedException e) {
				break;
			}
			// System.out.println("---------------------------------------");
			// benachrichtige Generator
			generator.stopBurst();

			lastBurst += timeUntilBurstEnd;
		}
	}

	/**
	 * Bestimmt Zeit zum naechsten Burst.
	 * 
	 * @return Zeit zum naechsten Burst
	 */
	private long getTimeUntilBurst() {
		long time;
		int interval = (burstConfig.maxTimeBetweenBursts - burstConfig.minTimeBetweenBursts);
		time = rnd.nextInt(interval + 1) + burstConfig.minTimeBetweenBursts;

		return time;
	}

	/**
	 * Bestimmt Zeit zum Ende des Bursts.
	 * 
	 * @return Zeit zum Ende des Bursts
	 */
	private long getTimeUntilBurstEnd() {
		long time;
		int interval = (burstConfig.maxBurstDuration - burstConfig.minBurstDuration);
		time = rnd.nextInt(interval + 1) + burstConfig.minBurstDuration;

		return time;
	}

	/**
	 * Soll der BurstGenerator benutzt werden oder nicht. Damit er benutzt
	 * werden soll muessen alle Zeit werte > 0 sein und die minimalen Werte >= 0.
	 * 
	 * @return ob der BurstGenerator benutzt werden soll
	 */
	public boolean shouldGenerateBursts() {
		return (burstConfig.minTimeBetweenBursts >= 0 && burstConfig.maxTimeBetweenBursts > 0)
				&& (burstConfig.minBurstDuration >= 0 && burstConfig.maxBurstDuration > 0)
				&& burstConfig.burstAccelerationFactor > 0;
	}
}