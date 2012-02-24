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
package de.uniol.inf.is.odysseus.loadshedding;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IPartialPlan;

/**
 * Encapsulates the functionality to calculate the current system resource capacity and
 * to activate or deactivate the load shedding.
 * @author jan
 *
 */
public interface ILoadSheddingStrategy {

	public double calcCapacity(IPartialPlan plan);

	public void activateLoadShedding(double percentToRemove, List<DirectLoadSheddingBuffer<?>> shedders);

	public void deactivateLoadShedding(List<DirectLoadSheddingBuffer<?>> shedders);
	
}
