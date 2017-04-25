package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SASelectionPO<T extends IStreamObject<?>> extends SelectPO<T> implements IHasPredicate {
	private SAOperatorDelegatePO<T> saOpDelPO = new SAOperatorDelegatePO<T>();
	private IPredicate<? super T> predicate;

	public SASelectionPO(IPredicate<? super T> predicate) {
		super(predicate);

	}

	@Override
	public IPredicate<? super T> getPredicate() {
		return predicate;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof AbstractSecurityPunctuation) {
			this.saOpDelPO.override((AbstractSecurityPunctuation) punctuation);
		}

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}



	@Override
	protected void process_next(T object, int port) {
		matchPunctuation(object);
		try {
			if (predicate.evaluate(object)) {
				transfer(object);

			} else {
				super.getHeartbeatGenerationStrategy().generateHeartbeat(object, this);
			}
		} catch (Exception e) {
			infoService.warning("Cannot evaluate " + predicate + " predicate with input " + object, e);
		}
	}

	public void matchPunctuation(T object) {
		List<AbstractSecurityPunctuation> result = this.saOpDelPO.match(object);
		if (!result.isEmpty()) {
			for (AbstractSecurityPunctuation sp : result) {
				sendPunctuation(sp);
			}
		}
		/**
		 * sends empty SP so the preceding SPs aren't longer active than intended
		 */
		else sendPunctuation(new SecurityPunctuation());
	}





}
