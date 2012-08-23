package de.uniol.inf.is.odysseus.core.server.securitypunctuation;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.securitypunctuation.SecurityPunctuation;

public class SecurityPunctuationCache extends ArrayList<SecurityPunctuation> {
	
	private static final long serialVersionUID = 4167234327602968536L;

	public SecurityPunctuation getMatchingSP(Integer ts) {
		return getMatchingSP(Long.valueOf(ts));
	}
	
	public SecurityPunctuation getMatchingSP(Long ts) {
		//keine SP vorhanden die älter als das aktuelle Tupel ist... Exception???
		if(!this.isEmpty()) {
			if(((Long)this.get(0).getAttribute("ts")) > ts) {
				return null;
			}
			int i;
			for(i = 0; i < this.size(); i++) {
				if(((Long)this.get(i).getAttribute("ts")) > ts) {
					return this.get(i-1);
				}
			}
			return this.get(i-1);
		}
		return null;
	}
	
	public void cleanCache(Long ts) {
		// SP kann herausgeschmissen werden, wenn neuere SP angekommen ist und der ts des aktuellen Datentupels bereits für die neue SP gültig ist.
		// sich addierende Tupel sollen beim ankommen zu einem einzigen Tupel addiert werden?!?!?
		Boolean remove = false;
		if(this.size() > 0) {
			for(int i = this.size() - 1; i >= 0; i--) {
				if(remove) {
					this.remove(i);
				} else if(((Long)this.get(i).getAttribute("ts")) < ts) {
					remove = true;
				}
			}
		}
	}	

}