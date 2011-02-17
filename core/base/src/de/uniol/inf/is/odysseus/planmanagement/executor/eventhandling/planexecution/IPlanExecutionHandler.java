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
package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution;

/**
 * IPlanExecutionHandler describes an object which informs registered
 * {@link IPlanExecutionListener} if the registered plan is modified.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanExecutionHandler {
	/**
	 * Registers an {@link IPlanExecutionListener} to this handler.
	 * 
	 * @param listener
	 *            new {@link IPlanExecutionListener}
	 */
	public void addPlanExecutionListener(IPlanExecutionListener listener);

	/**
	 * Unregisters an {@link IPlanExecutionListener} at this handler.
	 * 
	 * @param listener
	 *            {@link IPlanExecutionListener} to unregister.
	 */
	public void removePlanExecutionListener(IPlanExecutionListener listener);
}
