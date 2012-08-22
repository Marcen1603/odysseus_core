package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.util.HashMap;

public abstract class AbstractSecurityPunctuation implements Serializable, ISecurityPunctuation {
	
	protected HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	private static final long serialVersionUID = 1069899975287225287L;
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
}
