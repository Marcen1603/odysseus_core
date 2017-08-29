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
package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.server.util.OSGI;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public enum ExecutorPermission implements IPermission {
	ADD_QUERY, GET_QUERY, START_QUERY, STOP_QUERY, REMOVE_QUERY, SUSPEND_QUERY, RESUME_QUERY, PARTIAL_QUERY, START_ALL_QUERIES, STOP_ALL_QUERIES, REMOVE_ALL_QUERIES, GET_ALL_QUERIES, SET_SCHEDULER, START_SCHEDULER, STOP_SCHEDULER, RUN_COMMAND;

	public final static String objectURI = "queryexecutor";

	public static IPermission hasSuperAction(ExecutorPermission action) {
		switch (action) {
		case START_QUERY:
			return START_ALL_QUERIES;
		case STOP_QUERY:
			return STOP_ALL_QUERIES;
		case REMOVE_QUERY:
			return REMOVE_ALL_QUERIES;
		default:
			return null;
		}
	}

	/**
	 * returns whether the given action (permission) operates with an objecturi or
	 * the action class alias.
	 *
	 * @param action
	 * @return
	 */
	public static boolean needsNoObject(IPermission action) {
		switch ((ExecutorPermission) action) {
		default:
			return true;
		}
	}

	public static void validateUserRight(IPhysicalQuery query, ISession caller, ExecutorPermission executorAction) {
		if (!(hasUserRight(query, caller, executorAction))) {
			throw new PermissionException("No Right to execute " + executorAction + " on Query " + query.getID()
					+ " for " + caller.getUser().getName());
		}

	}

	public static boolean hasUserRight(IPhysicalQuery query, ISession caller, ExecutorPermission executorAction) {
		if (query == null) {
			return false;
		}
		UserManagementProvider userMangementProvider = OSGI.get(UserManagementProvider.class);
		return
		// User has right
		userMangementProvider.getUsermanagement(true).hasPermission(caller, executorAction, "Query " + query.getID()) ||
		// User is owner
				query.isOwner(caller) ||
				// User has higher right
				userMangementProvider.getUsermanagement(true).hasPermission(caller,
						ExecutorPermission.hasSuperAction(executorAction), ExecutorPermission.objectURI);
	}

	public static void validateUserRight(ISession caller, ExecutorPermission executorAction) {
		if (!(hasUserRight(caller, executorAction))) {
			throw new PermissionException(
					"No Right to execute " + executorAction + " for " + caller.getUser().getName());
		}

	}

	public static boolean hasUserRight(ISession caller, ExecutorPermission executorAction) {
		UserManagementProvider userMangementProvider = OSGI.get(UserManagementProvider.class);
		return // User has right
		userMangementProvider.getUsermanagement(true).hasPermission(caller, executorAction,
				ExecutorPermission.objectURI) ||
		// User has higher right
				userMangementProvider.getUsermanagement(true).hasPermission(caller,
						ExecutorPermission.hasSuperAction(executorAction), ExecutorPermission.objectURI);
	}

}
