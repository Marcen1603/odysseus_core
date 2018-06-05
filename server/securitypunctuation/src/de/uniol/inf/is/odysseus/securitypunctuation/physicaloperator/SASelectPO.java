package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;

public class SASelectPO<T extends IStreamObject<?>> extends SelectPO<T> implements IHasPredicate {
	private SAOperatorDelegate<T> saOpDel = new SAOperatorDelegate<T>();;


	public SASelectPO(IPredicate<? super T> predicate) {
		super(predicate);

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof ISecurityPunctuation) {
			this.saOpDel.override((ISecurityPunctuation) punctuation);

		} else
			sendPunctuation(punctuation);

	}

	//only transfers the SPs if the object fulfills the Select predicate
	@Override
	protected void process_next(T object, int port) {
		if (super.getPredicate().evaluate(object)) {
			transferPunctuation();
		}
		super.process_next(object, port);
	}

	//transfers all the currently active SPs in the SAOperatorDelegate
	private void transferPunctuation() {
		if (!saOpDel.getRecentSPs().isEmpty()) {
			for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {
				sendPunctuation(sp);
			}
			saOpDel.getRecentSPs().clear();
		}

	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof SASelectPO<?>)) {
			return false;
		}
		return super.process_isSemanticallyEqual(ipo);
	}
	

}
