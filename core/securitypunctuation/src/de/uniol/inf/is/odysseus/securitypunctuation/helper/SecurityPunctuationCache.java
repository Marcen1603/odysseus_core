package de.uniol.inf.is.odysseus.securitypunctuation.helper;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.sp.SecurityPunctuation;

public class SecurityPunctuationCache extends ArrayList<ISecurityPunctuation> {
	
	private static final long serialVersionUID = 4167234327602968536L;

	public ISecurityPunctuation getMatchingSP(Integer ts) {
		return getMatchingSP(Long.valueOf(ts));
	}
	
	public ISecurityPunctuation getMatchingSP(Long ts) {
		//keine SP vorhanden die älter als das aktuelle Tupel ist... Exception???
		if(!this.isEmpty()) {
			if((((SecurityPunctuation)this.get(0)).getTS()) > ts) {
				return null;
			}
			int i;
			for(i = 0; i < this.size(); i++) {
				if((((SecurityPunctuation)this.get(i)).getTS()) > ts) {
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
				} else if((((SecurityPunctuation)this.get(i)).getTS()) < ts) {
					remove = true;
				}
			}
		}
	}	
	
	/**
	 * Fügt eine neue SP in den Cache hinzu.
	 * Dabei wird überprüft, ob die SP den gleichen Zeitstempel und dementsprechend vererinigt oder geschnitten werden müssen.
	 */
	@Override
	public boolean add(ISecurityPunctuation sp) {
		if(!this.isEmpty()) {
			ISecurityPunctuation newSP = this.get(this.size() - 1).processSP(sp);
			if(newSP != null) {
				return super.add(newSP);
			}
		}
		return super.add(sp);
	}

}