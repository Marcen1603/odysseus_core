package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.StandardSecurityEvaluator;

/**
 * @author Jan Sören Schwarz
 */
public class SecurityShieldPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends AbstractPipe<T, T> {
	
    private static Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);

	private StandardSecurityEvaluator<T> evaluator = new StandardSecurityEvaluator<T>((AbstractPipe<T, T>) this);
	
	private IHeartbeatGenerationStrategy<T> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<T>();

	@Override
	protected void process_next(T object, int port) {
		if(evaluator.evaluate(object, this.getOwner(), this.getOutputSchema())) {
			transfer(object);
			evaluator.cleanCache(object.getMetadata().getStart().getMainPoint());
		} else {			
			transfer(object,1);
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
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		
		LOG.debug("SecurityShieldPO: " + sp);
		
		evaluator.addToCache(sp);
//		evaluator.createPredicates(sp, this.getOwner(), this.getOutputSchema());
		this.transferSecurityPunctuation(sp);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
}
