package de.uniol.inf.is.odysseus.securitypunctuation.helper;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class PreCacheSecurityEvaluator<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends StandardSecurityEvaluator<T> {

	// speichert SP zwischen, bis erstes dazugehörige Datentupel gesendet wird. Dann wird SP weitergesendet.
	private SecurityPunctuationCache preSPCache = new SecurityPunctuationCache();
	private Boolean checkSendingSP = false;
	private AbstractPipe<T,T> po;
	
	public PreCacheSecurityEvaluator(AbstractPipe<T, T> po) {
		super();
		this.po = po;
	}
	
	public Boolean preCacheEvaluate(T object, List<IOperatorOwner> ownerList, SDFSchema outputSchema) {
		checkSendingSP = false;
		initEvaluate(object);
		
		if(!preSPCache.isEmpty() && preSPCache.getMatchingSP(startPoint) != null) {
			sp = preSPCache.getMatchingSP(startPoint);	
			checkSendingSP = true;
		}
		
		if(sp != null) {	
			getUserRoles(ownerList);							
			if(sp != null && sp.evaluateAll(startPoint, userRoles, (Tuple<?>)object, outputSchema)) {
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
				return true;
			}
		}
		return false;
	}
	
	public void addToPreCache(ISecurityPunctuation sp) {
		preSPCache.add(sp);
	}
}
