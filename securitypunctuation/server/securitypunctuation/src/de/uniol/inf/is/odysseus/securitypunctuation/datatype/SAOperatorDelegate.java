package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public class SAOperatorDelegate<T extends IStreamObject<?>> {
	List<ISecurityPunctuation> recentSPs;


	public SAOperatorDelegate() {
		this.recentSPs = new ArrayList<ISecurityPunctuation>();
	}

	// checks, if the SPs in the cache have the same timestamp as the incoming
	// SP and either clears the cache and then adds the sp or simply adds the sp
	// without clearing

	public void override(ISecurityPunctuation sp) {
		if (!recentSPs.isEmpty()) {
			if (!sp.getTime().equals(recentSPs.get(0).getTime())) {
				this.recentSPs.clear();

			}
		}
		this.recentSPs.add(sp);

	}

	public List<ISecurityPunctuation> getRecentSPs() {
		return this.recentSPs;
	}


}
