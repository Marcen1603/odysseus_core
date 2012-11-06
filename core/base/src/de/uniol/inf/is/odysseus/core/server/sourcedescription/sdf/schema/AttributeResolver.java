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
/** Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jonas Jacobi
 */
public class AttributeResolver implements IAttributeResolver {
	private static final long serialVersionUID = -6960117786021105217L;

	private Map<String, ILogicalOperator> sources;
	private Set<SDFAttribute> attributes;

	public AttributeResolver() {
		this.sources = new HashMap<String, ILogicalOperator>();
		this.attributes = new HashSet<SDFAttribute>();
	}

	public AttributeResolver(AttributeResolver attributeResolver) {
		this.sources = new HashMap<String, ILogicalOperator>(attributeResolver.sources);
		this.attributes = new HashSet<SDFAttribute>(attributeResolver.attributes);
	}

    @Override
    public SDFSchema getSchema() {
        SDFSchema schema = new SDFSchema("", this.attributes);
        return schema;
    }
    
	public ILogicalOperator getSource(String name) {
		return this.sources.get(name);
	}

	public void addSource(String name, ILogicalOperator op) {
		if (this.sources.containsKey(name)) {
			throw new IllegalArgumentException("ambigiuous identifier: " + name);
		}
		this.sources.put(name, op);
	}

	public void addAttribute(SDFAttribute attribute) {
		if (this.attributes.contains(attribute)) {
			throw new IllegalArgumentException("ambigiuous identifier: " + attribute);
		}
		this.attributes.add(attribute);
	}

	public void addAttributes(SDFSchema list){
		for(SDFAttribute attribute : list){
			addAttribute(attribute);
		}
	}
	
	public void addAttributes(Collection<SDFAttribute> attributes) {
		for (SDFAttribute attribute : attributes) {
			addAttribute(attribute);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.querytranslation.parser.transformation.
	 * IAttributeResolver#getAttribute(java.lang.String)
	 */
	@Override
	public SDFAttribute getAttribute(String name) {
		String[] parts = name.split("\\.", 2);
		SDFAttribute result = null;
		if (parts.length == 1 || name.contains("(")) {
			for (ILogicalOperator source : this.sources.values()) {
				for (SDFAttribute curAttribute : source.getOutputSchema()) {
					if (curAttribute.getAttributeName().equals(name)) {
						if (result != null) {
							throw new IllegalArgumentException("ambigiuous identifier: " + name);
						}
                        result = curAttribute;
					}
				}
			}
			for (SDFAttribute curAttribute : this.attributes) {
				if (curAttribute.getAttributeName().equals(name)) {
					if (result != null) {
						throw new IllegalArgumentException("ambigiuous identifier: " + name);
					}
                    result = curAttribute;
				}
			}
		} else {
			result = getAttribute(parts[0], parts[1]);
		}

		return result;
	}

	private SDFAttribute getAttribute(String sourceName, String attributeName) {
		ILogicalOperator source = this.sources.get(sourceName);
		if (source == null) {
			throw new IllegalArgumentException("no such source: " + sourceName);
		}

		String[] path = attributeName.split("\\:");
		SDFAttribute attribute = findORAttribute(source.getOutputSchema(), path, 0);

		if (attribute != null)
			return attribute;

		throw new IllegalArgumentException("no such attribute: " + sourceName + "." + attributeName);
	}

	private SDFAttribute findORAttribute(SDFSchema list, String[] path, int index) {
		String toFind = path[index];
		for (SDFAttribute attr : list) {
			if (attr.getAttributeName().equals(toFind)) {
				if (index == path.length - 1) {
					return attr;
				} else if (attr.getDatatype().hasSchema()) {
					return findORAttribute(attr.getDatatype().getSchema(), path, index + 1);
				}
			}
		}
		return null;
	}

	public SDFAttribute getAggregateAttribute(String attributeName, String aggregateName) {
		SDFAttribute attribute = getAttribute(attributeName);
		aggregateName = aggregateName + "(" + attribute.getURI() + ")";
		for (SDFAttribute curAttribute : this.attributes) {
			if (curAttribute.getAttributeName().equals(aggregateName)) {
				return curAttribute;
			}
		}
		throw new IllegalArgumentException("no such attribute: " + attributeName +" "+ aggregateName);
	}

//	public SDFAttribute getAggregateAttributeOFF(ASTAggregateExpression expression) {
//		String name = expression.jjtGetChild(1).toString();
//		SDFAttribute attribute = getAttribute(name);
//		String aggregateName = expression.jjtGetChild(0).toString() + "(" + attribute.getPointURI() + ")";
//		for (SDFAttribute curAttribute : this.attributes) {
//			if (curAttribute.getAttributeName().equals(aggregateName)) {
//				return curAttribute;
//			}
//		}
//		throw new IllegalArgumentException("no such attribute: " + expression.toString());
//	}

	public boolean isAttributeValid(String name) {
		return getAttribute(name) != null;
	}

	@Override
	public String toString() {
		return "Sources " + sources + " attributes" + attributes;
	}

	@Override
	public AttributeResolver clone() {
		return new AttributeResolver(this);
	}

}

