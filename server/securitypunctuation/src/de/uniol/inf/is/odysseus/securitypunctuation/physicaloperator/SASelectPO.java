package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;

public class SASelectPO<T extends IStreamObject<?>> extends SelectPO<T> implements IHasPredicate {
	private SAOperatorDelegate<T> saOpDel=new SAOperatorDelegate<T>();;
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);
	public SASelectPO(IPredicate<? super T> predicate) {
		super(predicate);
		LOG.info("SASelect eingefügt");
		
	}
	


	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof ISecurityPunctuation) {
			this.saOpDel.override((ISecurityPunctuation) punctuation);
		
		} else sendPunctuation(punctuation);

	}


	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		if (super.getPredicate().evaluate(object)) {
			transferPunctuation();
		} super.process_next(object, port);
	}

	private void transferPunctuation() {
		if (!saOpDel.getRecentSPs().isEmpty()) {
			for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {
				sendPunctuation(sp);
			}saOpDel.getRecentSPs().clear();
		}
		

	}
	

}
