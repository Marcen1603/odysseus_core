/*
 * Copyright 2015 Marcus Behrendt
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
 *
**/

package de.uniol.inf.is.odysseus.trajectory.compare.util;

import java.util.Locale;

import de.uniol.inf.is.odysseus.trajectory.compare.data.RawQueryTrajectory;
import de.uniol.inf.is.odysseus.trajectory.util.AbstractObjectLoaderFactory;

/**
 * Implementation of <tt>AbstractObjectLoaderFactory</tt> that holds different 
 * <tt>IQueryTrajectoryLoaders</tt> for different <i>file extensions</i>.
 * 
 * @author marcus
 *
 */
public class QueryTrajectoryLoaderFactory extends AbstractObjectLoaderFactory<IQueryTrajectoryLoader, RawQueryTrajectory, String, Integer>{

	/** the singleton instance */
	private final static QueryTrajectoryLoaderFactory INSTANCE = new QueryTrajectoryLoaderFactory();
	
	/**
	 * Returns the <tt>QueryTrajectoryLoaderFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>QueryTrajectoryLoaderFactory</tt> as an eager singleton
	 */
	public static QueryTrajectoryLoaderFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private QueryTrajectoryLoaderFactory() { }
	
	
	@Override
	protected String convertKey(final String key) {
		return key.substring(key.lastIndexOf('.') + 1).toUpperCase(Locale.US);
	}

	@Override
	protected IQueryTrajectoryLoader createLoader(final String convertedKey) {
		switch(convertedKey) {
			case "CSV" : return new CsvQueryTrajectoryLoader();
		}
		throw new IllegalArgumentException("No IQueryTrajectoryLoader found for file extension: " + convertedKey);
	}

}
