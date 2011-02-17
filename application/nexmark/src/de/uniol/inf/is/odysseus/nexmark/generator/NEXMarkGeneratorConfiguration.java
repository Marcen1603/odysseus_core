/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.nexmark.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper for configuration of {@link NEXMarkGenerator}.
 */
public class NEXMarkGeneratorConfiguration {
	
	Logger logger = LoggerFactory.getLogger(NEXMarkGeneratorConfiguration.class);
	
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
	 * Reads configuration of generator from PROPERTIES_FILE.
	 * @throws IOException if property file cannot be read.
	 */
	public NEXMarkGeneratorConfiguration(String filename) throws IOException {
		Properties properties = readProperties(filename);

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
		logger.debug("Config Set: "+toString());
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
	private Properties readProperties(String filename) throws IOException {
		logger.debug("Reading Generator Properties from "+filename);
		InputStream is = getClass().getResourceAsStream(filename);
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
				+ "acceleration Factor: " + accelerationFactor +" " + burstConfig;
	}

}
