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
public class SecurityEvaluator<T extends IMetaAttributeContainer<? extends ITimeInterval>> implements IUserManagementListener {

	private Boolean hasPreCache;
	
	private Boolean rolesChanged = true;
	private List<String> userRoles = new ArrayList<String>();
	private SecurityPunctuationCache spCache = new SecurityPunctuationCache();
	// speichert SP zwischen, bis erstes dazugehörige Datentupel gesendet wird. Dann wird SP weitergesendet.
	private SecurityPunctuationCache preSPCache = new SecurityPunctuationCache();
		
	private AbstractPipe<T,T> po;
	
	public SecurityEvaluator(AbstractPipe<T,T> po, Boolean hasPreCache) {
		this.hasPreCache = hasPreCache;
		this.po = po;
		UserManagement.getUsermanagement().addUserManagementListener(this);
	}
	
	public Boolean evaluate(T object, List<IOperatorOwner> ownerList, SDFSchema outputSchema) {
		Long startPoint = object.getMetadata().getStart().getMainPoint();	
		ISecurityPunctuation sp = null;
		Boolean checkSendingSP = false;
		
		if(!spCache.isEmpty()) {
			sp = spCache.getMatchingSP(startPoint);	
		}
		
		if(hasPreCache && !preSPCache.isEmpty() && preSPCache.getMatchingSP(startPoint) != null) {
			sp = preSPCache.getMatchingSP(startPoint);	
			checkSendingSP = true;
		}
		
		if(sp != null) {				
			if(rolesChanged) {
				userRoles.clear();
				for(IOperatorOwner owner:ownerList) {
					for(IRole role:((IPhysicalQuery)owner).getSession().getUser().getRoles()) {
						userRoles.add(role.getName());
					}
				}
				rolesChanged = false;
			}
							
			if(sp != null && sp.evaluateAll(startPoint, userRoles, (Tuple<?>)object, outputSchema)) {
				if(hasPreCache) {
					if(checkSendingSP) {
						System.out.println("SP wird gesendet: " + sp.getAttribute("ts"));
						spCache.add(sp);
						po.transferSecurityPunctuation(sp);
						// alle SP aus preSPCache löschen, die älter als sp sind, da sie nicht mehr aktiv werden können. Neuere müssen noch im Cache bleiben.
						for(int i = preSPCache.indexOf(sp); i >= 0; i--) {
							preSPCache.remove(i);
						}					
						checkSendingSP = false;
					}
				}
				return true;
			}
		}
		return false;
	}
		
	public void cleanCache(Long ts) {
		spCache.cleanCache(ts);
	}
	
	public void addToCache(ISecurityPunctuation sp) {
		spCache.add(sp);
	}
	
	public void addToPreCache(ISecurityPunctuation sp) {
		preSPCache.add(sp);
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