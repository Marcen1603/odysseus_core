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
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;

/**
 * @author Jan Sören Schwarz
 */
public class SecurityShieldPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();
	
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
//			 Send filtered data to output port 1
			transfer(object,1);
			//Funktioniert das???
			heartbeatGenerationStrategy.generateHeartbeat(object, this);
		}
		cleanCache(object);
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
			//schöner möglich???
			Tuple<?> tuple = (Tuple<?>) object;
			
			//Besitzer des Operators - Muss das hier rüber gemacht werden oder reicht der Weg oben?
			//SEHR ineffizient!!!
			List<IOperatorOwner> ownerList = this.getOwner();
			List<String> userRoles = new ArrayList<String>();
			for(IOperatorOwner owner:ownerList) {
				for(IRole role:((IPhysicalQuery)owner).getSession().getUser().getRoles()) {
					userRoles.add(role.getName());
				}
			}
				
			//checken, ob getMetadata wirklich ITimeInterval???
			ITimeInterval metadata = object.getMetadata();
			Long startPoint = metadata.getStart().getMainPoint();
			
			SecurityPunctuation sp = spCache.getMatchingSP(startPoint);
			
			if(sp != null && sp.evaluateAll(startPoint, userRoles, tuple, this.getOutputSchema())) {
				return true;
			}
		}
		
		return false;
	}
	
	private void cleanCache(T object) {
		//Reihenfolge in Cache beachten!!!!
		while(!spCache.isEmpty() && spCache.get(0).getDDPEndtuple() < object.getMetadata().getStart().getMainPoint()) {
//			System.out.println("spCache.get(0).getDDPEndtuple(): " + spCache.get(0).getDDPEndtuple());
//			System.out.println("object.getMetadata().getStart().getMainPoint(): " + object.getMetadata().getStart().getMainPoint());
			spCache.remove(0);
		}
//		System.out.println("aktuelle Size: " + spCache.size());
	}
}
