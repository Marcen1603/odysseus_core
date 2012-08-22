package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.SecurityPunctuation;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.securitypunctuation.SecurityPunctuationCache;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

/**
 * @author Jan S�ren Schwarz
 */
public class SecurityShieldPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends AbstractPipe<T, T> implements IUserManagementListener {

	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
	private SecurityPunctuationCache spCache = new SecurityPunctuationCache();
	
	private Boolean rolesChanged = true;
	private List<String> userRoles = new ArrayList<String>();
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		//Funktioniert das?
		if (evaluate(object)) {
			transfer(object);
		} else {			
//			 Send filtered data to output port 1
			transfer(object,1);
			//Funktioniert das???
			heartbeatGenerationStrategy.generateHeartbeat(object, this);
		}
		spCache.cleanCache(object.getMetadata().getStart().getMainPoint());
	}	

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new SecurityShieldPO<T>();
	}

	@Override
	public void processSecurityPunctuation(SecurityPunctuation sp, int port) {
		spCache.add(sp);
		this.transferSecurityPunctuation(sp);
	}

	private Boolean evaluate(T object) {
		if(!spCache.isEmpty() && object instanceof Tuple<?>) {
			UserManagement.getUsermanagement().addUserManagementListener(this);
			
			//sch�ner m�glich???
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
				
			Long startPoint = object.getMetadata().getStart().getMainPoint();			
			SecurityPunctuation sp = spCache.getMatchingSP(startPoint);			
			if(sp != null && sp.evaluateAll(startPoint, userRoles, tuple, this.getOutputSchema())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void usersChangedEvent() {
	}

	@Override
	public void roleChangedEvent() {
		rolesChanged = true;
	}
}
