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
//		LOG.debug("cleanCache - size: " + spCache[port].size());
		spCache[port].cleanCache(ts);
	}
}
