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
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.query.IQueryOptimizer;

/**
 * Parameter which indicates contains a query optimizer. Can be used to provide
 * such a n optimizer in a plan optimizer.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterQueryOptimizer extends Setting<IQueryOptimizer> implements
		IOptimizationSetting<IQueryOptimizer> {

	/**
	 * Creates a new ParameterQueryOptimizer with a specific query optimizer.
	 * 
	 * @param value
	 *            query optimizer which should be provided.
	 */
	public ParameterQueryOptimizer(IQueryOptimizer value) {
		super(value);
	}
}
