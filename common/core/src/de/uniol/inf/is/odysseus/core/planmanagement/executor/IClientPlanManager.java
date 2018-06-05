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
package de.uniol.inf.is.odysseus.core.planmanagement.executor;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * 
 * @author wolf
 * @author Marco Grawunder
 * 
 */
public interface IClientPlanManager{
	/**
	 * remove query from system
	 * 
	 * @param queryID the id of the query to remove
	 * @throws PlanManagementException
	 */
	public void removeQuery(int queryID, ISession caller) throws PlanManagementException;

	/**
	 * remove query from system
	 * 
	 * @param queryName of the query to remove
	 * @throws PlanManagementException
	 */
	public void removeQuery(Resource queryName, ISession caller) throws PlanManagementException;

	/**
	 * startQuery startet eine Anfrage.
	 * 
	 * @param queryID
	 *            ID der Anfrage
	 * @throws PlanManagementException
	 */
	public void startQuery(int queryID, ISession caller) throws PlanManagementException;
	public void startQuery(Resource queryName, ISession caller) throws PlanManagementException;

	/**
	 * stopQuery stoppt eine Anfrage und entfernt sie
	 * 
	 * @param queryID
	 * @throws PlanManagementException
	 */
	public void stopQuery(int queryID, ISession caller) throws PlanManagementException;
	public void stopQuery(Resource queryName, ISession caller) throws PlanManagementException;
	
	/**
	 * suspend query (i.e. query execution is paused and can be resumed. element will be buffered!)
	 * @param queryID
	 * @param caller
	 * @throws PlanManagementException
	 */
	public void suspendQuery(int queryID, ISession caller) throws PlanManagementException;
	public void suspendQuery(Resource queryName, ISession caller) throws PlanManagementException;
	
	/**
	 * resume a suspended query (first all buffered elements are send)
	 * @param queryID
	 * @param caller
	 * @throws PlanManagementException
	 */
	
	public void resumeQuery(int queryID, ISession caller) throws PlanManagementException;
	public void resumeQuery(Resource queryName, ISession caller) throws PlanManagementException;

	/**
	 * Set a query to query sharing mode
	 * @param queryID
	 * @param sheddingFactor value between 0 and 100 (0=no shedding, 100 = shedd all)
	 * @param caller
	 * @throws PlanManagementException
	 */
	void partialQuery(int queryID, int sheddingFactor, ISession caller)
			throws PlanManagementException;
	void partialQuery(Resource queryName, int sheddingFactor, ISession caller)
			throws PlanManagementException;

}