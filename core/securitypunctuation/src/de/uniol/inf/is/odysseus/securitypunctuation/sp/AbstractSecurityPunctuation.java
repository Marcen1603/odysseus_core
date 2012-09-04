package de.uniol.inf.is.odysseus.securitypunctuation.sp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public abstract class AbstractSecurityPunctuation implements Serializable, ISecurityPunctuation, Cloneable {
	
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private SDFSchema schema;
	
	protected Long ts;
	protected Integer immutable;
	protected Integer sign;
	
	private static final long serialVersionUID = 1069899975287225287L;
	
	public abstract ISecurityPunctuation clone();
	
	/**
	 * Logik zum entscheiden, ob auf Tupel union() oder intersect() angewendet werden soll.
	 */
	@Override
	public boolean processSP(ISecurityPunctuation sp2) {
		AbstractSecurityPunctuation sp = (AbstractSecurityPunctuation)sp2;
		if(this.ts == sp.ts) {
			if(this.getSchema().getURI().equals(sp.getSchema().getURI())) {
				//Was passiert hier, wenn Immutable = 0???
				if(this.immutable >= 1 && sp.immutable >= 1) {
					if(sp.sign == 1) {
						return this.union(sp);
					} else {
						return this.intersect(sp);
					}
				}
			} else {
				//Was passiert hier, wenn Immutable = 0???
				return this.intersect(sp);
			}
		} 
		if(sp.immutable == 2 && this.immutable >= 1) {
			if(sp.sign == 1) {
				return this.union(sp);
			} else {
				return this.intersect(sp);
			}
		}
		return false;
	}
	
	public Long getTS() {
		return ts;
	}
	
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
	
	public String[] getStringArrayAttribute(String key) {
		return (String[]) attributes.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getStringArrayListAttribute(String key) {
		return (ArrayList<String>) attributes.get(key);
	}

	public IPredicate<?> getPredicateAttribute(String key) {
		return (IPredicate<?>) attributes.get(key);
	}
	
	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}
	
	public void setSchema(SDFSchema schema) {
		this.schema = schema;
	}
	public SDFSchema getSchema() {
		return this.schema;
	}
	
	public int getNumberofAttributes() {
		return attributes.size();
	}
}
