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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.executorcommand;

/**
 * {@code IExecutorCommandHandler} describes an object which informs registered
 * {@link IExecutorCommandListener} if an {@code IExecutorCommand} is executed.
 * 
 * @author Michael Brand
 * 
 */
public interface IExecutorCommandHandler {

	/**
	 * Registers an {@link IExecutorCommandListener} to this handler.
	 * 
	 * @param listener
	 *            {@link IExecutorCommandListener} to register.
	 */
	public void addExecutorCommandListener(IExecutorCommandListener listener);

	/**
	 * Unregisters an {@link IExecutorCommandListener} at this handler.
	 * 
	 * @param listener
	 *            {@link IExecutorCommandListener} to unregister.
	 */
	public void removeExecutorCommandListener(IExecutorCommandListener listener);

}