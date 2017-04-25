package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;
@Deprecated
public abstract class AbstractSAOperatorPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> implements ISAOperatorPO<T> {
	List<AbstractSecurityPunctuation> recentSPs;

	// checks, if the SPs in the buffer have the same timestamp as the incoming
	// SP and adds the new SP
	@Override
	public void override(AbstractSecurityPunctuation sp) {
		if (sp.getTime() != recentSPs.get(recentSPs.size() - 1).getTime()) {
			this.recentSPs.clear();
			// this.recentSPs.add(sp);
		}
		this.recentSPs.add(sp);

	}

	// Attribut könnte auch an anderen Stellen sein
	@Override
	public boolean match(Tuple t) {
		for (AbstractSecurityPunctuation sp : recentSPs) {
			if(sp.getDDP().match(t)){
				return true;
			}
		}
		
		return false;

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}





	protected void process_next(T object, int port) {
		// TODO Auto-generated method stub
		
	}

}
