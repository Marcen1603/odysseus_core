package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.SecurityPunctuationCache;

public class SASelectPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends SelectPO<T> {
	
    private static Logger LOG = LoggerFactory.getLogger(SASelectPO.class);

	private SecurityPunctuationCache spCache = new SecurityPunctuationCache();	
	
	public SASelectPO(IPredicate<? super T> predicate) {
		super(predicate);
	}
	
	public SASelectPO(SASelectPO<T> po){
		super(po);
	}
	
	@Override
	protected void process_next(T object, int port) {
		if (getPredicate().evaluate(object)) {
			Long ts = object.getMetadata().getStart().getMainPoint();
			ISecurityPunctuation sp = spCache.getMatchingSP(ts);
			if(sp != null) {
				super.processSecurityPunctuation(sp, port);
				
				LOG.debug("SP wird in SASelectPO gesendet: " + ts);
				
				spCache.cleanCache(ts);
			}
			transfer(object);
		} else {
			// Send filtered data to output port 1
			transfer(object,1);
			getHeartbeatGenerationStrategy().generateHeartbeat(object, this);
		}
	}
	
	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		spCache.add(sp);
		LOG.debug("SASelectPO:");
		spCache.printCache();
	}
	
	@Override
	public String getName() {
		return "SASelect";
	}
}
