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
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;

/**
 * @author Merlin Wasmann, Thore Stratmann, Marco Grawunder
 *
 */
public class QueryResponse extends Response {

	private LogicalQuery query;

	private QueryState queryState;

	private String username;

	private List<String> roots;

	public QueryResponse() {
		super();
	}

	public QueryResponse(LogicalQuery value, String username,
			QueryState queryState, List<String> roots, boolean success) {
		super(success);
		this.query = value;
		this.username = username;
		this.queryState = queryState;
		this.roots = roots;
	}

	public void setRoots(List<String> roots) {
		this.roots = roots;
	}

	public int getNumberOfRoots() {
		return this.roots.size();
	}

	public List<String> getRoots() {
		return this.roots;
	}

	public LogicalQuery getResponseValue() {
		return this.query;
	}

	public void setResponseValue(LogicalQuery value) {
		this.query = value;
	}

	public QueryState getQueryState() {
		return queryState;
	}
	
	public void setQueryState(QueryState queryState) {
		this.queryState = queryState;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
