package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;

public class SAOperatorDelegatePO<T extends IStreamObject<?>> implements ISAOperatorPO<T> {
	List<AbstractSecurityPunctuation> recentSPs;

	
	public SAOperatorDelegatePO(){
		this.recentSPs=new ArrayList<AbstractSecurityPunctuation>();
	}
	// checks, if the SPs in the buffer have the same timestamp as the incoming
	// SP and adds the new SP
	@Override
	public void override(AbstractSecurityPunctuation sp) {
		if (!recentSPs.isEmpty()) {
			if (sp.getTime() != recentSPs.get(recentSPs.size()-1).getTime()) {
				this.recentSPs.clear();

			}
		}
		this.recentSPs.add(sp);

	}

	@Override
	public ArrayList<AbstractSecurityPunctuation> match(T object) {
		ArrayList<AbstractSecurityPunctuation> matches = new ArrayList<AbstractSecurityPunctuation>();
		for (AbstractSecurityPunctuation sp : recentSPs) {
			if (sp.getDDP().match(object)) {
				matches.add(sp);

			}
		}

		return matches;
	}

	public List<AbstractSecurityPunctuation> getRecentSPs() {
		return this.recentSPs;
	}
	/*
	 * @Override public boolean match(Tuple t) { for
	 * (AbstractSecurityPunctuation sp : recentSPs) { if (sp.getDDP().match(t))
	 * { return true; } }
	 * 
	 * return false;
	 * 
	 * }
	 * 
	 * 
	 */

}
