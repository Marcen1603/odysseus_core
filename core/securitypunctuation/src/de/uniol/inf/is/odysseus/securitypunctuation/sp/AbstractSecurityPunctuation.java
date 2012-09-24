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
package de.uniol.inf.is.odysseus.securitypunctuation.sp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

public abstract class AbstractSecurityPunctuation implements Serializable, ISecurityPunctuation {
	
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private SDFSchema schema;
		
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
