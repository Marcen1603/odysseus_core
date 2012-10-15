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
package de.uniol.inf.is.odysseus.securitypunctuation.helper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

/**
 * @author Jan Sören Schwarz
 */
public class StandardSecurityEvaluator<T extends IStreamObject<? extends ITimeInterval>> implements IUserManagementListener {
	
    private static Logger LOG = LoggerFactory.getLogger(StandardSecurityEvaluator.class);
	
	protected Boolean rolesChanged = true;
	protected List<String> userRoles = new ArrayList<String>();
	protected SecurityPunctuationCache spCache = new SecurityPunctuationCache();
	
	protected Long startPoint;	
	protected ISecurityPunctuation sp;
	
	public StandardSecurityEvaluator() {
		UserManagement.getUsermanagement().addUserManagementListener(this);
	}
	
	public StandardSecurityEvaluator(AbstractPipe<T, T> po) {
		UserManagement.getUsermanagement().addUserManagementListener(this);
	}
	
	public Boolean evaluate(T object, List<IOperatorOwner> ownerList, SDFSchema outputSchema) {
		startPoint = object.getMetadata().getStart().getMainPoint();	
		if(!spCache.isEmpty()) {
			sp = spCache.getMatchingSP(startPoint);	
			
			LOG.debug("evaluate Tuple: " + object + " with SP: " + sp);
			
			if(sp != null) {
				if(rolesChanged) {
					getUserRoles(ownerList);
				}
				if(sp != null && sp.evaluate(startPoint, userRoles, (Tuple<?>)object, outputSchema)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void getUserRoles(List<IOperatorOwner> ownerList) {
		userRoles.clear();
		for(IOperatorOwner owner:ownerList) {
			for(IRole role:((IPhysicalQuery)owner).getSession().getUser().getRoles()) {
				userRoles.add(role.getName());
			}
		}
		rolesChanged = false;
	}
		
	public void cleanCache(Long ts) {
		spCache.cleanCache(ts);
	}
	
	public void addToCache(ISecurityPunctuation sp) {
		spCache.add(sp);
	}
	
	public ISecurityPunctuation getFromCache(Long ts) {
		return spCache.getMatchingSP(ts);
	}

	@Override
	public void usersChangedEvent() {
		rolesChanged = true;
	}

	@Override
	public void roleChangedEvent() {
	}
}