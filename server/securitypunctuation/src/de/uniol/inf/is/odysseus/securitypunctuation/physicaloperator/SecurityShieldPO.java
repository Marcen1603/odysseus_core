package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;

public class SecurityShieldPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	ArrayList<String> roles;
	SAOperatorDelegatePO<T> saOpDelPO = new SAOperatorDelegatePO<>();
	boolean match = false;
	boolean SPchecked = false;

	@Override
	/***
	 * if a new SP arrives SPchecked is set to false. When a new Tuple arrives
	 * the compatibility of the SP in the buffer and the role of the Query
	 * Specifier will be checked again
	 */
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof AbstractSecurityPunctuation)
			saOpDelPO.override((AbstractSecurityPunctuation) punctuation);
		SPchecked = false;

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		/**
		 * If the compatibility of the SP(s) in the buffer and the role of the
		 * query specifier hasn't been checked yet, it will be controlled. Else
		 * if the is compatible with any of the SPs the object and the SP(s)
		 * will be transferred.
		 */
		if (!SPchecked) {
			for (AbstractSecurityPunctuation sp : saOpDelPO.getRecentSPs()) {
				for (String role : roles) {
					if (sp.getSRP().getRoles().contains(role)) {
						match = true;

						break;
					} else
						match = false;
				}
			}
			SPchecked = true;
		}
		if (match) {
			if (!saOpDelPO.getRecentSPs().isEmpty()) {
				for (AbstractSecurityPunctuation sp : saOpDelPO.getRecentSPs()) {
					sendPunctuation(sp);
				}
				saOpDelPO.recentSPs.clear();
			}
			transfer(object);
		}
	}

}
