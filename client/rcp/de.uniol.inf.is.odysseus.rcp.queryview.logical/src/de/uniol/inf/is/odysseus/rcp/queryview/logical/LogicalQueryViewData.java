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
package de.uniol.inf.is.odysseus.rcp.queryview.logical;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;

public class LogicalQueryViewData implements IQueryViewData {

	private static final String LOGICAL_QUERY_STATUS = "<unknown>";
//	private static final String LOGICAL_QUERY_NAME = "<unknown>";

	private final int id;
	private final String status;
	private final int priority;
	private final String parser;
	private final String username;
	private final String queryText;
	private final String queryName;

	public LogicalQueryViewData(ILogicalQuery logicalQuery) {
		Preconditions.checkNotNull(logicalQuery,
				"LogicalQuery must not be null");

		id = logicalQuery.getID();
		status = LOGICAL_QUERY_STATUS;
		priority = logicalQuery.getPriority();
		parser = logicalQuery.getParserId();
		username = logicalQuery.getUser().getUser().getName();
		queryText = logicalQuery.getQueryText();
		queryName = logicalQuery.getName();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public String getParserId() {
		return parser;
	}

	@Override
	public String getUserName() {
		return username;
	}

	@Override
	public String getQueryText() {
		return queryText;
	}

	@Override
	public String getName() {
		return queryName;
	}

}
