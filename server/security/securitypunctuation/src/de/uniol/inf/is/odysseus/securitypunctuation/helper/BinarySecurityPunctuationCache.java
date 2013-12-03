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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public class BinarySecurityPunctuationCache {
	
    private static Logger LOG = LoggerFactory.getLogger(BinarySecurityPunctuationCache.class);

	private SecurityPunctuationCache[] spCache;
	
	public BinarySecurityPunctuationCache() {
		spCache = new SecurityPunctuationCache[2];
		spCache[0] = new SecurityPunctuationCache();
		spCache[1] = new SecurityPunctuationCache();
	}
	
	public void add(ISecurityPunctuation sp, int port) {
		spCache[port].add(sp);
	}
	
	public ISecurityPunctuation getMatchingSP(Long ts, int port) {
		return spCache[port].getMatchingSP(ts);
	}
	
	public void cleanCache(Long ts, int port) {
		LOG.debug("cleanCache - size: " + spCache[port].size() + " - ts: " + ts);
		spCache[port].cleanCache(ts);
	}
	
	public Integer size(int port) {
		return spCache[port].size();
	}
}
