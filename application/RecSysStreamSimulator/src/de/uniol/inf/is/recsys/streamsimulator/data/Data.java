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
public abstract class Data {

	protected abstract Properties getProperties();

	public synchronized String getByKey(String key) {
		return getProperties().getProperty(key);
	}

	public synchronized String getByKey(String key, String defaultValue) {
		String value = getByKey(key);
		if (value == null) {
			setByKey(key, defaultValue);
			return defaultValue;
		} else {
			return value;
		}
	}

	public synchronized int getByKeyAsInt(String key) {
		return Integer.parseInt(getByKey(key));
	}

	public synchronized int getByKeyAsInt(String key, int defaultValue) {
		return Integer.parseInt(getByKey(key, String.valueOf(defaultValue)));
	}

	public synchronized void setByKey(String key, String value) {
		getProperties().put(key, value);
	}

	public synchronized void setByKeyAsInt(String key, int value) {
		getProperties().put(key, String.valueOf(value));
	}

	/**
	 * @param key
	 * @return
	 */
	public int getMinValue(String key) {
		return 0;
	}

	/**
	 * @param key
	 * @return
	 */
	public int getMaxValue(String key) {
		return 0;
	}

}