/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
	 * Adds sp to the cache
	 * before adding it checks if the sp had to be intersected oder unioned with the previous sp.
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
				LOG.debug("added sp: " + sp.getLongAttribute("ts"));
				return false;
			}
		}
		boolean temp = super.add(sp);
		LOG.debug("added sp: " + sp.getLongAttribute("ts"));
		return temp;
	}
}