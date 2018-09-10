/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.opcua.common.utilities;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;

/**
 * The basic utilities.
 */
public final class BasicUtils {

	/**
	 * Instantiates a new basic utils.
	 */
	private BasicUtils() {
	}

	/**
	 * Get enumeration by name and type.
	 *
	 * @param <T>
	 *            the generic type
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 * @return the t
	 */
	public static <T> T toEnum(String name, Class<T> type) {
		for (T item : type.getEnumConstants())
			if (name.equalsIgnoreCase(item.toString()))
				return item;
		throw new UnsupportedOperationException(name);
	}

	/**
	 * Gets the bundle.
	 *
	 * @param type
	 *            the type
	 * @return the bundle
	 */
	public static Bundle getBundle(Class<?> type) {
		ClassLoader cl = type.getClassLoader();
		if (cl instanceof BundleReference)
			return ((BundleReference) cl).getBundle();
		return null;
	}
}