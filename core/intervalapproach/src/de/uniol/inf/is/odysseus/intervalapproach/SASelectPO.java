package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.SecurityPunctuation;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.securitypunctuation.SecurityPunctuationCache;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

public class SASelectPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends SelectPO<T> implements IUserManagementListener {

	private IPredicate<? super T> predicate;
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
	private SecurityPunctuationCache spCache = new SecurityPunctuationCache();
	// speichert SP zwischen, bis erstes dazugehörige Datentupel gesendet wird. Dann wird SP weitergesendet.
	private SecurityPunctuationCache preSPCache = new SecurityPunctuationCache();
	
	private Boolean rolesChanged = true;
	private List<String> userRoles = new ArrayList<String>();
		
	public SASelectPO(IPredicate<? super T> predicate) {
		super(predicate);
		this.predicate = predicate.clone();	
	}
	
	public SASelectPO(SASelectPO<T> po){
		super(po);
		this.predicate = po.predicate.clone();
		this.heartbeatGenerationStrategy = po.heartbeatGenerationStrategy.clone();
	}
	
	@Override
	protected void process_next(T object, int port) {
		if (predicate.evaluate(object)) {
			if(evaluate(object)) {
				transfer(object);
			}
		}else{
			// Send filtered data to output port 1
			transfer(object,1);
			heartbeatGenerationStrategy.generateHeartbeat(object, this);
		}
	}

	private Boolean evaluate(T object) {
		Long startPoint = object.getMetadata().getStart().getMainPoint();	
		SecurityPunctuation sp = null;
		Boolean checkSendingSP = false;
		
		if(!preSPCache.isEmpty() && preSPCache.getMatchingSP(startPoint) != null) {
			sp = preSPCache.getMatchingSP(startPoint);	
			checkSendingSP = true;
		} else if(!spCache.isEmpty()) {
			sp = spCache.getMatchingSP(startPoint);	
		}
		
		UserManagement.getUsermanagement().addUserManagementListener(this);
		
		//schöner möglich???
		Tuple<?> tuple = (Tuple<?>) object;
		
		if(rolesChanged) {
			List<IOperatorOwner> ownerList = this.getOwner();
			userRoles.clear();
			for(IOperatorOwner owner:ownerList) {
				for(IRole role:((IPhysicalQuery)owner).getSession().getUser().getRoles()) {
					userRoles.add(role.getName());
				}
			}
			rolesChanged = false;
		}
					
		if(sp != null && sp.evaluateAll(startPoint, userRoles, tuple, this.getOutputSchema())) {
			if(checkSendingSP) {
				System.out.println("SP wird gesendet: " + sp.getAttribute("ts"));
				spCache.add(sp);
				this.transferSecurityPunctuation(sp);
				// alle SP aus preSPCache löschen, die älter als sp sind, da sie nicht mehr aktiv werden können. Neuere müssen noch im Cache bleiben.
				for(int i = preSPCache.indexOf(sp); i >= 0; i--) {
					preSPCache.remove(i);
				}					
				checkSendingSP = false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void processSecurityPunctuation(SecurityPunctuation sp, int port) {
		preSPCache.add(sp);
	}

	@Override
	public void usersChangedEvent() {		
	}

	@Override
	public void roleChangedEvent() {
		rolesChanged = true;
	}

}
