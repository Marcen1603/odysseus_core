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
package de.uniol.inf.is.odysseus.core.server.planmanagement;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Describes a strategy for buffer placement in physical plans. A buffer is an
 * operator between two operators which stores data elements.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IBufferPlacementStrategy {
	/**
	 * Add buffer in a physical plan.
	 * 
	 * @param plan
	 *            Physical plan in which buffer should be placend.
	 */
	public void addBuffers(IPhysicalQuery plan);

	/**
	 * Returns the name of this strategy. This name should be unique.
	 * 
	 * @return The name of this strategy. This name should be unique.
	 */
	public String getName();
}
