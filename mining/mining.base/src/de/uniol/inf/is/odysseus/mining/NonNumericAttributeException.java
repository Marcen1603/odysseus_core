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
package de.uniol.inf.is.odysseus.mining;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * This exception occurs if an attribute's data type is not numeric.
 * 
 * @author Kolja Blohm
 * 
 */
public class NonNumericAttributeException extends Exception {

	private static final long serialVersionUID = -5590493141116859811L;
	private SDFAttribute attribute;

	/**
	 * Creates a new instance of NonNumericAttributeException for the specified
	 * attribute
	 * 
	 * @param attribute
	 *            the non numeric attribute
	 */
	public NonNumericAttributeException(SDFAttribute attribute) {
		this.attribute = attribute;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {

		return "The data type of " + attribute.getAttributeName()
				+ " has to be numeric.";
	}
}
