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

package de.uniol.inf.is.odysseus.trajectory.compare;

import java.util.Locale;
import java.util.Map;

import de.uniol.inf.is.odysseus.trajectory.compare.owd.Owd;
import de.uniol.inf.is.odysseus.trajectory.compare.uots.Uots;
import de.uniol.inf.is.odysseus.trajectory.compare.util.QueryTrajectoryLoaderFactory;

/**
 * A Factory for creating instances of <tt>ITrajectoryCompareAlgorithm</tt>.
 * @author marcus
 *
 */
public class TrajectoryCompareAlgorithmFactory {
	
	/** the singleton instance */
	private final static TrajectoryCompareAlgorithmFactory INSTANCE = 
			new TrajectoryCompareAlgorithmFactory();
	
	/**
	 * Returns the <tt>TrajectoryCompareAlgorithmFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>TrajectoryCompareAlgorithmFactory</tt> as an eager singleton
	 */
	public static TrajectoryCompareAlgorithmFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private TrajectoryCompareAlgorithmFactory() {  }

	/**
	 * Creates a new <tt>ITrajectoryCompareAlgorithm</tt>.
	 * 
	 * @param name the name of the algorithm
	 * @param k the k-nearest trajectories to find
	 * @param queryTrajectoryPath the file path to the query trajectory
	 * @param textualAttributes textual attributes of the query trajectory
	 * @param utmZone the UTM zone of the trajectories
	 * @param lambda the importance between spatial and textual distance
	 * @param options the options for the algorithm
	 * @return a new <tt>ITrajectoryCompareAlgorithm</tt>
	 */
	public ITrajectoryCompareAlgorithm<?, ?> create(final String name, final int k, final String queryTrajectoryPath, final Map<String, String> textualAttributes,
			final int utmZone, final double lambda, final Map<String, String> options) {
		
		final String upperCaseName = name.toUpperCase(Locale.US);
		switch(upperCaseName) {
			case "OWD" : 
				return new Owd(options, 
						k, 
						QueryTrajectoryLoaderFactory.getInstance().load(queryTrajectoryPath, utmZone), 
						textualAttributes, 
						utmZone, 
						lambda);
			case "UOTS" : 
				return new Uots(options, 
						k, 
						QueryTrajectoryLoaderFactory.getInstance().load(queryTrajectoryPath, utmZone), 
						textualAttributes, 
						utmZone, 
						lambda);
		}
		throw new IllegalArgumentException("Algorithm not found: " + upperCaseName);
	}
}
