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
package de.uniol.inf.is.odysseus.planmanagement.executor;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.usermanagement.IUserAction;

public enum ExecutorAction implements IUserAction {
	ADD_QUERY, START_QUERY, STOP_QUERY, REMOVE_QUERY,
	START_ALL_QUERIES, STOP_ALL_QUERIES, REMOVE_ALL_QUERIES;
	
	
	static List<IUserAction> all;

	public static String alias = "Executor";
	
	public synchronized static List<IUserAction> getAll() {
		if (all == null) {
			all = new ArrayList<IUserAction>();
			for (IUserAction action : ExecutorAction.class
					.getEnumConstants()) {
				all.add(action);
			}

		}
		return all;
	}
	
	public static IUserAction hasSuperAction(ExecutorAction action) {
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
}
