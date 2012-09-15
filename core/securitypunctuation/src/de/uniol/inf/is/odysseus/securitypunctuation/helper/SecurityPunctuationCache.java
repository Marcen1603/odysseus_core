package de.uniol.inf.is.odysseus.securitypunctuation.helper;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class SecurityPunctuationCache extends ArrayList<ISecurityPunctuation> {
	
    private static Logger LOG = LoggerFactory.getLogger(SecurityPunctuationCache.class);
	
	private static final long serialVersionUID = 4167234327602968536L;

	public ISecurityPunctuation getMatchingSP(Integer ts) {
		return getMatchingSP(Long.valueOf(ts));
	}
	
	public ISecurityPunctuation getMatchingSP(Long ts) {
		//keine SP vorhanden die älter als das aktuelle Tupel ist... Exception???
		if(!this.isEmpty()) {
			if((this.get(0).getLongAttribute("ts")) > ts) {
				return null;
			}
			int i;
			for(i = 0; i < this.size(); i++) {
				if((this.get(i).getLongAttribute("ts")) > ts) {
					return this.get(i-1);
				}
			}
			return this.get(i-1);
		}
		return null;
	}
	
	/**
	 * Entfernt alle SP aus dem Cache, die älter als der Zeitstempel sind
	 * 
	 * @param ts Zeitstempel
	 */
	public void cleanCache(Long ts) {
		// SP kann herausgeschmissen werden, wenn neuere SP angekommen ist und der ts des aktuellen Datentupels bereits für die neue SP gültig ist.
		if(this.size() > 0) {
			for(int i = this.size() - 1; i >= 0; i--) {
				if((this.get(i).getLongAttribute("ts")) < ts) {
					this.removeRange(0, i);
					return;
				}
			}
		}
	}	
	
	/**
	 * Fügt eine neue SP in den Cache hinzu.
	 * Dabei wird überprüft, ob die SP den gleichen Zeitstempel und dementsprechend vererinigt oder geschnitten werden müssen.
	 * 
	 */
	@Override
	public boolean add(ISecurityPunctuation sp) {
		if(!this.isEmpty()) {
			ISecurityPunctuation newSP = this.get(this.size() - 1).processSP(sp);
			if(newSP != null) {
				if(super.add(newSP)) {
					this.remove(this.size()-2);
				}
//				printCache();
				return false;
			}
		}
		boolean temp = super.add(sp);
//		printCache();
		return temp;
	}

	public void printCache() {
//		for(ISecurityPunctuation sp:this) {
//			LOG.debug("SP: " + sp.getLongAttribute("ts") + sp.getIntegerAttribute("sign"));
//		}
	}
}