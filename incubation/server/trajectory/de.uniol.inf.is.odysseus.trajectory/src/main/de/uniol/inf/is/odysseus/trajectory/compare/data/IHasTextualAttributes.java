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
 * An object that has set of textual attributes. Textual Attributes are 
 * stored in a <tt>Map</tt> where the <i>key</i> of an entry is a 
 * <tt>String</tt> and represents the attribute's <i>name</i>. The values
 * of the entries store the value for the associated attribute names.
 * 
 * @author marcus
 *
 */
public interface IHasTextualAttributes {

	/**
	 * Returns the the <tt>Map</tt> which stores the attribute names and
	 * their respective values.
	 * 
	 * @return the the <tt>Map</tt> which stores the attribute names and
	 *         their respective values
	 */
	public Map<String, String> getTextualAttributes();
	
	/**
	 * Returns the number of textual attributes stored in this 
	 * <tt>object</TT>.
	 * 
	 * @return the number of textual attributes
	 */
	public int numAttributes();
}
