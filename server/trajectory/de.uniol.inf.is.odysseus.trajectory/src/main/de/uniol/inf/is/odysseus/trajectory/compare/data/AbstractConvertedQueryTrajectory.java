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

package de.uniol.inf.is.odysseus.trajectory.compare.data;

import java.util.Map;

/**
 * Abstract base class for all <i>converted query trajectories</i>. This class encapsulated 
 * the <i>converted data</i> and the <i>the textual attributes</i>.
 * @author marcus
 *
 * @param <E> the type of the query trajectory data
 */
public abstract class AbstractConvertedQueryTrajectory<E> extends AbstractConvertedTrajectory<E, RawQueryTrajectory> implements IConvertedQueryTrajectory<E> {
	
	/** the textual attributes */
	private final Map<String, String> textualAttributes;
	
	/**
	 * Creates an instance of <tt>AbstractConvertedQueryTrajectory</tt>.
	 *  
	 * @param rawTrajectory the <tt>RawQueryTrajectory</tt> to encapsulate
	 * @param convertedData the converted data
	 * @param textualAttributes the textual attributes
	 * 
	 * @throws IllegalArgumentException if <tt>convertedData == null</tt> or <tt>rawTrajectory == null</tt> 
	 */
	protected AbstractConvertedQueryTrajectory(final RawQueryTrajectory rawTrajectory, final E convertedData, 
			final Map<String, String> textualAttributes) {
		super(rawTrajectory, convertedData);
		
		if(rawTrajectory == null) {
			throw new IllegalArgumentException("rawTrajectory is null");
		}
		this.textualAttributes = textualAttributes;
	}
	
	@Override
	public Map<String, String> getTextualAttributes() {
		return this.textualAttributes;
	}

	@Override
	public int numAttributes() {
		return this.textualAttributes.size();
	}
}
