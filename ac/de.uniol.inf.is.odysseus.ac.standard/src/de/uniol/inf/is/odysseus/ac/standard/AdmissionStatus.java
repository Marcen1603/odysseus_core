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

package de.uniol.inf.is.odysseus.ac.standard;

import java.util.Map;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public final class AdmissionStatus {

	private final int runningQueries;
	private final int stoppedQueries;
	private final int addedQueries;
	
	private final ICost actCost;
	private final ICost maxCost;
	private final ICost underloadCost;
	
	private final Map<IUser, ICost> userCosts;
	private final Map<IUser, ICost> maxUserCosts;
	private final Map<IUser, ICost> underloadUserCosts;
	
	private final Map<IPhysicalQuery, ICost> queryCosts; 
	
	private final long timestamp;
	private final long runningTime;
	
	private final String costModel;

	AdmissionStatus(int runningQueries, int stoppedQueries, int addedQueries, ICost actCost, ICost maxCost, ICost underloadCost, Map<IUser, ICost> userCosts, Map<IUser, ICost> maxUserCosts,
			Map<IUser, ICost> underloadUserCost, Map<IPhysicalQuery, ICost> queryCosts, long timestamp, long runningTime, String costModel) {
		this.runningQueries = runningQueries;
		this.stoppedQueries = stoppedQueries;
		this.addedQueries = addedQueries;
		this.actCost = actCost;
		this.maxCost = maxCost;
		this.underloadCost = underloadCost;
		this.userCosts = userCosts;
		this.maxUserCosts = maxUserCosts;
		this.underloadUserCosts = underloadUserCost;
		this.queryCosts = queryCosts;
		this.timestamp = timestamp;
		this.costModel = costModel;
		this.runningTime = runningTime;
	}

	public int getRunningQueryCount() {
		return runningQueries;
	}

	public int getStoppedQueryCount() {
		return stoppedQueries;
	}

	public int getAddedQueryCount() {
		return addedQueries;
	}

	public ICost getActCost() {
		return actCost;
	}

	public ICost getMaxCost() {
		return maxCost;
	}

	public ICost getUnderloadCost() {
		return underloadCost;
	}

	public ICost getUserCost( IUser user ) {
		Preconditions.checkNotNull(user, "User to get actual costs must not be null!");
		
		return userCosts.get(user);
	}

	public ICost getMaxUserCost(IUser user) {
		Preconditions.checkNotNull(user, "User to get maximum costs must not be null!");
		
		return maxUserCosts.get(user);
	}

	public ICost getUnderloadUserCost(IUser user) {
		Preconditions.checkNotNull(user, "User to get underload costs must not be null!");
		
		return underloadUserCosts.get(user);
	}

	public ICost getQueryCosts(IPhysicalQuery query) {
		Preconditions.checkNotNull(query, "Query to get actual costs must not be null!");
		
		return queryCosts.get(query);
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public long getRunningTime() {
		return runningTime;
	}

	public String getCostModelName() {
		return costModel;
	}
}
