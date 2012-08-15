package de.uniol.inf.is.odysseus.core.server.securitypunctuation;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.SecurityPunctuation;

public class SecurityPunctuationCache extends ArrayList<SecurityPunctuation>{

	private static final long serialVersionUID = 5252721143346679062L;

	public SecurityPunctuation getMatchingSP(Integer ts) {
		return getMatchingSP(Long.valueOf(ts));
	}
	
	public SecurityPunctuation getMatchingSP(Long ts) {
		//keine SP vorhanden die älter als das aktuelle Tupel ist... Exception???
		if(!this.isEmpty()) {
			if(this.get(0).getTS() > ts) {
				return null;
			}
			int i;
			for(i = 0; i < this.size(); i++) {
				if(this.get(i).getTS() > ts) {
					return this.get(i-1);
				}
			}
			return this.get(i-1);
		}
		return null;
	}
}