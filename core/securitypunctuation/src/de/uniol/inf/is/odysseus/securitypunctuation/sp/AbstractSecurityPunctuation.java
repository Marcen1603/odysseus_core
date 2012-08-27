package de.uniol.inf.is.odysseus.securitypunctuation.sp;

import java.io.Serializable;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public abstract class AbstractSecurityPunctuation implements Serializable, ISecurityPunctuation {
	
	protected HashMap<String, Object> attributes = new HashMap<String, Object>();
	protected SDFSchema schema;
	
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
	
//	public RelationalPredicate getPredicateAttribute(String key) {
//		return (RelationalPredicate) attributes.get(key);
//	}
}
