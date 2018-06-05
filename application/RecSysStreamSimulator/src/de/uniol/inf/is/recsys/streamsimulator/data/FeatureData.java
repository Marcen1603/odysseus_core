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
public class FeatureData extends Data {
	private static final FeatureData INSTANCE = new FeatureData();

	public static final String USER_KEY_PREFIX = "user ";
	public static final String ITEM_KEY_PREFIX = "item ";
	public static final String FEATURE_KEY_PREFIX = "feature ";
	public static final String KEY_DELIMITER = "__ ";

	private RandomHelper rh = RandomHelper.getInstance();

	protected Properties properties = new Properties();

	/**
	 * 
	 */
	private FeatureData() {
	}

	public static FeatureData getInstance() {
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

	/**
	 * @param userOrItemKey
	 * @param feature
	 * @return
	 */
	public int getFeature(String userOrItemKey, int feature) {
		return getByKeyAsInt(userOrItemKey + KEY_DELIMITER + FEATURE_KEY_PREFIX
				+ feature, rh.nextInt(getMinValue(FEATURE_KEY_PREFIX),
				getMaxValue(FEATURE_KEY_PREFIX) + 1));
	}

	public synchronized int getUserFeature(int user, int feature) {
		return getByKeyAsInt(getUserKey(user, feature), rh.nextInt(
				getMinValue(FEATURE_KEY_PREFIX),
				getMaxValue(FEATURE_KEY_PREFIX) + 1));
	}

	public synchronized void setUserFeature(int user, int feature, int value) {
		setByKeyAsInt(getUserKey(user, feature), value);
	}

	public synchronized int getItemFeature(int item, int feature) {
		return getByKeyAsInt(getItemKey(item, feature), rh.nextInt(
				getMinValue(FEATURE_KEY_PREFIX),
				getMaxValue(FEATURE_KEY_PREFIX) + 1));
	}

	public static synchronized String getUserKey(int user, int feature) {
		return USER_KEY_PREFIX + String.valueOf(user) + KEY_DELIMITER
				+ FEATURE_KEY_PREFIX + String.valueOf(feature);
	}

	public static synchronized String getItemKey(int item, int feature) {
		return ITEM_KEY_PREFIX + String.valueOf(item) + KEY_DELIMITER
				+ FEATURE_KEY_PREFIX + String.valueOf(feature);
	}

	@Override
	public synchronized int getMinValue(String key) {
		if (key == null) {
			return 0;
		}
		if (key.contains("feature") | key.contains(FEATURE_KEY_PREFIX)) {
			return 0;
		}
		return super.getMinValue(key);
	}

	@Override
	public synchronized int getMaxValue(String key) {
		if (key == null) {
			return 0;
		}
		if (key.contains("feature") | key.contains(FEATURE_KEY_PREFIX)) {
			return 120;
		}
		return super.getMaxValue(key);
	}

	/**
	 * @param f
	 */
	public synchronized void drift(float percentOfUsers) {
		RandomHelper rh = RandomHelper.getInstance();
		int noUsers = ConfigData.getInstance().getNoOfFeatures();
		int noFeatures = ConfigData.getInstance().getNoOfFeatures();
		int tenpercent = Math.round(noUsers * percentOfUsers);

		int wereSetToMinOrMax = 0;

		for (int i = 0; i < tenpercent; ++i) {
			int userId = rh.nextInt(0, noUsers);
			for (int j = 0; j < noFeatures; ++j) {
				int bias = (int) Math.round(rh.nextGaussian(0, ConfigData
						.getInstance().getStandardDeviationOfBias()));
				int feat = getUserFeature(userId, j);
				int oldFeat = feat;
				feat += bias;
				if (feat < getMinValue(FEATURE_KEY_PREFIX)) {
					feat = getMinValue(FEATURE_KEY_PREFIX);
					++wereSetToMinOrMax;
				} else if (feat > getMaxValue(FEATURE_KEY_PREFIX)) {
					feat = getMaxValue(FEATURE_KEY_PREFIX);
					++wereSetToMinOrMax;
				}
				System.out.println("Setting feature " + j + " of user "
						+ userId + " to: " + feat + " (was: " + oldFeat
						+ ", bias: " + bias + ")");
				setUserFeature(userId, j, feat);
			}
		}

		System.out
				.println(""
						+ wereSetToMinOrMax
						+ " of "
						+ (tenpercent * noFeatures)
						+ " features ("
						+ ((((double) wereSetToMinOrMax) / (tenpercent * noFeatures)) * 100)
						+ " %) were set to the min or max value (because the bias was to hight).");
	}
	
	public synchronized void resetAllFeatures() {
		properties.clear();
	}
}
