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
package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification;

/**
 * IPlanModificationHandler describes an object which informs registered
 * {@link IPlanModificationListener} if the registered plan is modified.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanModificationHandler {
	/**
	 * Registers an {@link IPlanModificationListener} to this handler.
	 * 
	 * @param listener
	 *            new {@link IPlanModificationListener}
	 */
	public void addPlanModificationListener(IPlanModificationListener listener);

	/**
	 * Unregisters an {@link IPlanModificationListener} at this handler.
	 * 
	 * @param listener
	 *            {@link IPlanModificationListener} to unregister.
	 */
	public void removePlanModificationListener(
			IPlanModificationListener listener);
}
