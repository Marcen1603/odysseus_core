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
package de.uniol.inf.is.odysseus.rcp.queryview.physical;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.rcp.views.query.IQueryViewData;

public class PhysicalQueryViewData implements IQueryViewData {

	private static final String UNKNOWN_TEXT = "<unknown>";
	private final int id;
	private final int priority;
	private final String parserId;
	private final String userName;
	private final String queryText;
	private final String queryName;
	private long queryStartTS;

	private String status;

	public PhysicalQueryViewData(int id, String status, int priority, String parserId, String userName, String queryText, Resource queryName, long queryStartTS) {
		this.id = id;
		this.status = Strings.isNullOrEmpty(status) ? UNKNOWN_TEXT : status;
		this.priority = priority;
		this.parserId = Strings.isNullOrEmpty(parserId) ? UNKNOWN_TEXT : parserId;
		this.userName = Strings.isNullOrEmpty(userName) ? UNKNOWN_TEXT : userName;
		this.queryText = Strings.isNullOrEmpty(queryText) ? UNKNOWN_TEXT : queryText;
		this.queryName = queryName == null ? "" : queryName.toString();
		this.queryStartTS = queryStartTS;
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
		return parserId;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	@Override
	public String getQueryText() {
		return queryText;
	}

	@Override
	public String getName() {
		return queryName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStartTS(long queryStartTS) {
		this.queryStartTS = queryStartTS;
	}

	@Override
	public long getStartTS() {
		return queryStartTS;
	}
}
