/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.recsys.streamsimulator.data;

import java.util.Properties;

/**
 * @author Cornelius Ludmann
 *
 */
public class ConfigData extends Data {
	private static final ConfigData INSTANCE = new ConfigData();

	public static final String NO_USERS_KEY = "no_users";
	public static final String NO_ITEMS_KEY = "no_items";
	public static final String RATINGS_DELAY_KEY = "ratings_delay";
	public static final String RFR_DELAY_KEY = "rfr_delay";
	public static final String NO_FEATURES_KEY = "no_features";

	public static final String RATINGS_HOST_KEY = "ratings_host";
	public static final String RATINGS_PORT_KEY = "ratings_port";
	public static final String RFR_HOST_KEY = "rfr_host";
	public static final String RFR_PORT_KEY = "rfr_port";

	public static final String STANDARD_DEVIATION_OF_BIAS = "standard_deviation_of_bias";

	private static final String ADD_TIMESTAMP = "add_timstamp";

	protected Properties properties = new Properties();

	/**
	 * 
	 */
	private ConfigData() {
	}

	public static ConfigData getInstance() {
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.recsys.streamsimulator.data.Data#getProperties()
	 */
	@Override
	protected synchronized Properties getProperties() {
		return properties;
	}

	public synchronized int getNoOfUsers() {
		return getByKeyAsInt(NO_USERS_KEY, 100);
	}

	public synchronized int getNoOfItems() {
		return getByKeyAsInt(NO_ITEMS_KEY, 500);
	}

	public synchronized int getRatingsStreamDelay() {
		return getByKeyAsInt(RATINGS_DELAY_KEY, 500);
	}

	public synchronized int getRfrStreamDelay() {
		return getByKeyAsInt(RFR_DELAY_KEY, 1000);
	}

	public synchronized int getNoOfFeatures() {
		return getByKeyAsInt(NO_FEATURES_KEY, 10);
	}

	public synchronized void setNoOfUsers(int value) {
		setByKeyAsInt(NO_USERS_KEY, value);
	}

	public synchronized void setNoOfItems(int value) {
		setByKeyAsInt(NO_ITEMS_KEY, value);
	}

	public synchronized void setRatingsStreamDelay(int value) {
		setByKeyAsInt(RATINGS_DELAY_KEY, value);
	}

	public synchronized void setRfrStreamDelay(int value) {
		setByKeyAsInt(RFR_DELAY_KEY, value);
	}

	public synchronized void setNoOfFeatures(int value) {
		setByKeyAsInt(NO_FEATURES_KEY, value);
	}

	@Override
	public synchronized int getMinValue(String key) {
		if (NO_USERS_KEY.equals(key) || NO_ITEMS_KEY.equals(key)) {
			return 10;
		}
		if (RATINGS_DELAY_KEY.equals(key) || RFR_DELAY_KEY.equals(key)) {
			return 1;
		}
		if (NO_FEATURES_KEY.equals(key)) {
			return 2;
		}
		if (STANDARD_DEVIATION_OF_BIAS.equals(key)) {
			return 0;
		}
		return super.getMinValue(key);
	}

	@Override
	public synchronized int getMaxValue(String key) {
		if (NO_USERS_KEY.equals(key) || NO_ITEMS_KEY.equals(key)) {
			return 10000;
		}
		if (RATINGS_DELAY_KEY.equals(key) || RFR_DELAY_KEY.equals(key)) {
			return 10000;
		}
		if (NO_FEATURES_KEY.equals(key)) {
			return 200;
		}
		if (STANDARD_DEVIATION_OF_BIAS.equals(key)) {
			return 200;
		}
		return super.getMaxValue(key);
	}

	/**
	 * @return
	 */
	public synchronized String getRatingsStreamHost() {
		return getByKey(RATINGS_HOST_KEY, "127.0.0.1");
	}

	/**
	 * @return
	 */
	public synchronized String getRfrStreamHost() {
		return getByKey(RATINGS_HOST_KEY, "127.0.0.1");
	}

	/**
	 * @return
	 */
	public synchronized int getRatingsStreamPort() {
		return getByKeyAsInt(RATINGS_PORT_KEY, 8080);
	}

	/**
	 * @return
	 */
	public synchronized int getRfrStreamPort() {
		return getByKeyAsInt(RFR_PORT_KEY, 8081);
	}

	/**
	 * @return
	 */
	public synchronized int getStandardDeviationOfBias() {
		return getByKeyAsInt(STANDARD_DEVIATION_OF_BIAS, 30);
	}

	/**
	 * @return
	 */
	public synchronized boolean getAddTimestamp() {
		return Boolean.parseBoolean(getByKey(ADD_TIMESTAMP, "true"));
	}

	public synchronized void setAddTimestamp(boolean value) {
		setByKey(ADD_TIMESTAMP, String.valueOf(value));
	}
}
