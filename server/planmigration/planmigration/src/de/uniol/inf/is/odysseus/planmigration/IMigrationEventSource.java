/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
 ******************************************************************************/
package de.uniol.inf.is.odysseus.planmigration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;


/**
 * @author Merlin Wasmann
 *
 */
public interface IMigrationEventSource {
	
	public IPhysicalQuery getPhysicalQuery();
	
	public boolean hasPhysicalQuery();

	public void addMigrationListener(IMigrationListener listener);
	
	public void removeMigrationListener(IMigrationListener listener);
	
	/**
	 * Sends a finished migration event to all registered IMigrationListeners 
	 * @param sender
	 */
	public void fireMigrationFinishedEvent(IMigrationEventSource sender);
	
	/**
	 * Sends a finished migration event to all registered IMigrationListeners
	 * @param sender
	 * @param ex
	 */
	public void fireMigrationFailedEvent(IMigrationEventSource sender, Throwable ex);
}
