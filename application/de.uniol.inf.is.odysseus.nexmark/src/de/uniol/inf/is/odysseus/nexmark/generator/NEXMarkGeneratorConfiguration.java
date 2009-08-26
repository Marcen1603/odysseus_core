package de.uniol.inf.is.odysseus.nexmark.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Kapselt die Konfigurationsdaten fuer den {@link NEXMarkGenerator}.
 */
public class NEXMarkGeneratorConfiguration {
	private static final String PROPERTIES_FILE = "/de/uniol/inf/is/odysseus/nexmark/generator/NEXMarkGeneratorConfiguration.properties";

	public int minDistBetweenPersons;
	public int maxDistBetweenPersons;

	public int minDistBetweenAuctions;
	public int maxDistBetweenAuctions;

	public int minDistBetweenBids;
	public int maxDistBetweenBids;

	public long accelerationFactor;

	public int minAuctionDuration;
	public int maxAuctionDuration;

	public NEXMarkBurstConfiguration burstConfig;

	/**
	 * Liest die Konfiguration fuer den Generator aus dem PROPERTIES_FILE ein.
	 * @throws IOException wenn Properties file nicht gelesen werden kann
	 */
	public NEXMarkGeneratorConfiguration() throws IOException {
		Properties properties = readProperties();

		burstConfig = new NEXMarkBurstConfiguration(Integer.parseInt(properties
				.getProperty("minTimeBetweenBursts")), Integer.parseInt(properties
				.getProperty("maxTimeBetweenBursts")), Integer.parseInt(properties
				.getProperty("minBurstDuration")), Integer.parseInt(properties
				.getProperty("maxBurstDuration")), Integer.parseInt(properties
				.getProperty("burstAccelerationFactor")));

		this.minDistBetweenPersons = Integer.parseInt(properties
				.getProperty("minDistBetweenPersons"));
		this.maxDistBetweenPersons = Integer.parseInt(properties
				.getProperty("maxDistBetweenPersons"));

		this.minDistBetweenAuctions = Integer.parseInt(properties
				.getProperty("minDistBetweenAuctions"));
		this.maxDistBetweenAuctions = Integer.parseInt(properties
				.getProperty("maxDistBetweenAuctions"));

		this.minDistBetweenBids = Integer.parseInt(properties.getProperty("minDistBetweenBids"));
		this.maxDistBetweenBids = Integer.parseInt(properties.getProperty("maxDistBetweenBids"));

		this.accelerationFactor = Long.parseLong(properties.getProperty("accelerationFactor"));
		if (accelerationFactor == 0) {
			this.accelerationFactor = 1;
		}

		this.minAuctionDuration = Integer.parseInt(properties.getProperty("minAuctionDuration"));
		this.maxAuctionDuration = Integer.parseInt(properties.getProperty("maxAuctionDuration"));
	}

	public NEXMarkGeneratorConfiguration(int minDistBetweenPersons, int maxDistBetweenPersons,
			int minDistBetweenAuctions, int maxDistBetweenAuctions, int minDistBetweenBids,
			int maxDistBetweenBids, long accelerationFactor, int minAuctionDuration,
			int maxAuctionDuration, NEXMarkBurstConfiguration burstConfig) {
		this.minDistBetweenPersons = minDistBetweenPersons;
		this.maxDistBetweenPersons = maxDistBetweenPersons;

		this.minDistBetweenAuctions = minDistBetweenAuctions;
		this.maxDistBetweenAuctions = maxDistBetweenAuctions;

		this.minDistBetweenBids = minDistBetweenBids;
		this.maxDistBetweenBids = maxDistBetweenBids;

		if (accelerationFactor == 0) {
			this.accelerationFactor = 1;
		} else {
			this.accelerationFactor = accelerationFactor;
		}

		this.minAuctionDuration = minAuctionDuration;
		this.maxAuctionDuration = maxAuctionDuration;

		this.burstConfig = burstConfig;
	}

	/**
	 * Liest die Properties ein.
	 * 
	 * @return die Properties
	 * @throws IOException wenn Properties file nicht gelesen werden kann
	 */
	private Properties readProperties() throws IOException {
		InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE);
		Properties properties = new Properties();
		
		try {
			properties.loadFromXML(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	@Override
	public String toString() {
		return "person: [" + minDistBetweenPersons + ", " + maxDistBetweenPersons + "] - "
				+ "auction: [" + minDistBetweenAuctions + ", " + maxDistBetweenAuctions + "] - "
				+ "bid: [" + minDistBetweenBids + ", " + maxDistBetweenBids + "] - "
				+ "acceleration Factor: " + accelerationFactor + burstConfig;
	}

}
