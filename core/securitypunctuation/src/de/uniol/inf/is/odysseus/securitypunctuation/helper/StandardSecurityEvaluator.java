package de.uniol.inf.is.odysseus.securitypunctuation.helper;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

/**
 * @author Jan Sören Schwarz
 */
public class StandardSecurityEvaluator<T extends IMetaAttributeContainer<? extends ITimeInterval>> implements IUserManagementListener {
	
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
		initEvaluate(object);		
		if(sp != null) {			
			getUserRoles(ownerList);							
			if(sp != null && sp.evaluateAll(startPoint, userRoles, (Tuple<?>)object, outputSchema)) {
				return true;
			}
		}
		return false;
	}
	
	public void initEvaluate(T object) {
		startPoint = object.getMetadata().getStart().getMainPoint();	
		sp = null;
		if(!spCache.isEmpty()) {
			sp = spCache.getMatchingSP(startPoint);	
		}
	}
	
	public void getUserRoles(List<IOperatorOwner> ownerList) {
		if(rolesChanged) {
			userRoles.clear();
			for(IOperatorOwner owner:ownerList) {
				for(IRole role:((IPhysicalQuery)owner).getSession().getUser().getRoles()) {
					userRoles.add(role.getName());
				}
			}
			rolesChanged = false;
		}
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
	
//	public IPredicate<?> createPredicate(IAttributeResolver resolver,
//			String predicate) {
//		SDFExpression expression = new SDFExpression("", predicate, resolver, MEP.getInstance());
//		RelationalPredicate pred = new RelationalPredicate(expression);
//		return pred;
//	}
}