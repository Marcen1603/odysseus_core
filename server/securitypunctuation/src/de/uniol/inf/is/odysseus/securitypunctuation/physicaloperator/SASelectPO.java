package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;

public class SASelectPO<T extends IStreamObject<?>> extends SelectPO<T> implements IHasPredicate {
	private SAOperatorDelegatePO<T> saOpDelPO=new SAOperatorDelegatePO<T>();;
	//private IPredicate<? super T> predicate;

	public SASelectPO(IPredicate<? super T> predicate) {
		super(predicate);
		
	}
	


	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof AbstractSecurityPunctuation) {
			this.saOpDelPO.override((AbstractSecurityPunctuation) punctuation);
		
		} else sendPunctuation(punctuation);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {

		try {
			if (super.getPredicate().evaluate(object)) {
				transferPunctuation();
				transfer(object);
				

			} else {
				super.getHeartbeatGenerationStrategy().generateHeartbeat(object, this);
			}
		} catch (Exception e) {
			infoService.warning("Cannot evaluate " + super.getPredicate() + " predicate with input " + object, e);
		}
	}

	private void transferPunctuation() {
		if (!saOpDelPO.getRecentSPs().isEmpty()) {
			for (AbstractSecurityPunctuation sp : saOpDelPO.getRecentSPs()) {
				sendPunctuation(sp);
			}saOpDelPO.getRecentSPs().clear();
		}
		

	}


}
