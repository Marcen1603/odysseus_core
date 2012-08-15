package de.uniol.inf.is.odysseus.core.server.securitypunctuation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.SecurityPunctuation;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

/**
 * @author Jan Sören Schwarz
 */
public class SecurityShieldPO<T> extends AbstractPipe<T, T> {

	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
//	private SecurityPunctuation currentSecurityPunctuation = null; 
	
	private SecurityPunctuationCache spCache = new SecurityPunctuationCache();
	
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
			
			//geht das effizienter???
//			if(spCache.getMatchingSP(((ITimeInterval) ((Tuple<?>) object).getMetadata()).getStart().getMainPoint()) != null) {
//				if(spCache.getMatchingSP(((ITimeInterval) ((Tuple<?>) object).getMetadata()).getStart().getMainPoint()).evaluateAttributes(object) != null) {
//					transfer((T) (spCache.getMatchingSP(((ITimeInterval) ((Tuple<?>) object).getMetadata()).getStart().getMainPoint()).evaluateAttributes(object)));
//				}
//			}
			
//			 Send filtered data to output port 1
			transfer(object,1);
			//Funktioniert das???
			heartbeatGenerationStrategy.generateHeartbeat(object, this);
		}
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
//		currentSecurityPunctuation = sp;
		spCache.add(sp);
		this.transferSecurityPunctuation(sp);
	}

	public Boolean evaluate(Object object) {
		if(!spCache.isEmpty() && object instanceof Tuple<?>) {
			
			//User, die über die aktuelle Session eingeloggt sind... können das mehrere sein, oder ist das immer nur einer???
//			IUserManagement usrMgmt = UserManagement.getUsermanagement();
//			ISession session = OdysseusRCPPlugIn.getActiveSession();
//			List<? extends IUser> userList = usrMgmt.getUsers(session);
//			List<String> userRoles = new ArrayList<String>();
//			for(IUser user:userList) {
//				List<? extends IRole> roles = user.getRoles();
//				for(IRole role:roles) {
//					userRoles.add(role.getName());
//				}
//			}
			
			//Besitzer des Operators - Muss das hier rüber gemacht werden oder reicht der Weg oben?
			List<IOperatorOwner> ownerList = this.getOwner();
			List<String> userRoles = new ArrayList<String>();
			for(IOperatorOwner owner:ownerList) {
				for(IRole role:((IPhysicalQuery)owner).getUser().getUser().getRoles()) {
					userRoles.add(role.getName());
				}
			}
				
			//checken, ob getMetadata wirklich ITimeInterval???
			ITimeInterval metadata = ((ITimeInterval) ((Tuple<?>) object).getMetadata());
			Long startPoint = metadata.getStart().getMainPoint();
			
			SecurityPunctuation sp = spCache.getMatchingSP(startPoint);
			
			if(sp.getSign() == 1 && 
					sp.evaluateRoles(userRoles) // && 
//					sp.evaluateTS(startPoint)  &&
//					sp.evaluateAttributes(object) == null
					) {
				return true;
			}
		}
		
		return false;
	}
}
