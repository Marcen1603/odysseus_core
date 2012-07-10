/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

/**
 * This exception occurs if an attribute's value is out of it's allowed range.
 * 
 * @author Kolja Blohm
 * 
 */
public class AttributeOutOfRangeException extends Exception {

	private static final long serialVersionUID = 3632365610334662268L;
	private String attributeName;
	private String errorMessage;

	/**
	 * Creates a new instance of AttributeOutOfRangeException with an error
	 * message and the name of the attribute.
	 * 
	 * @param attributeName
	 *            the name of the attribute
	 * @param errorMessage
	 *            the error message
	 */
	public AttributeOutOfRangeException(String attributeName,
			String errorMessage) {
		this.attributeName = attributeName;
		this.errorMessage = errorMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {

		return "The value of " + attributeName + " " + errorMessage;
	}

}
