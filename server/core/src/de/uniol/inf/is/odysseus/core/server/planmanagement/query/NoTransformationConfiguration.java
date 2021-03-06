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
/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.query;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * NoTransformationConfiguration is an {@link Exception} which occurs during
 * transformation of an query if no {@link TransformationConfiguration} is
 * established.
 * 
 * @author Wolf Bauer
 * 
 */
public class NoTransformationConfiguration extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1842137531605713157L;

	/**
	 * Creates a new NoTransformationConfiguration. The Exception Message will
	 * be "No Transformation Configuration specified.".
	 */
	public NoTransformationConfiguration() {
		super("No Transformation Configuration specified.");
	}
}
