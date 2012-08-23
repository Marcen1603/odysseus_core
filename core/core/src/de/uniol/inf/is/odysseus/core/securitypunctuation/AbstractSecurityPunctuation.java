package de.uniol.inf.is.odysseus.core.securitypunctuation;

import java.io.Serializable;
import java.util.HashMap;

public abstract class AbstractSecurityPunctuation implements Serializable, ISecurityPunctuation {
	
	protected HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	private static final long serialVersionUID = 1069899975287225287L;
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public Long getLongAttribute(String key) {
		return (Long) attributes.get(key);
	}
	
	public Integer getIntegerAttribute(String key) {
		return (Integer) attributes.get(key);
	}
	
	public String getStringAttribute(String key) {
		return (String) attributes.get(key);
	}
}
