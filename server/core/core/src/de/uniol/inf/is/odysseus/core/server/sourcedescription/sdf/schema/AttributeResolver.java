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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.AmbiguousAttributeException;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class AttributeResolver implements IAttributeResolver {
	private static final long serialVersionUID = -6960117786021105217L;

	final private Map<Resource, ILogicalOperator> resources;
	final private Map<String, ILogicalOperator> sources;
	final private Map<String, ILogicalOperator> originalSourceNames;
	final private Set<SDFAttribute> attributes;

	public AttributeResolver() {
		this.sources = new HashMap<>();
		this.originalSourceNames = new HashMap<>();
		this.resources = new HashMap<>();
		this.attributes = new HashSet<>();
	}

	public AttributeResolver(AttributeResolver attributeResolver) {
		this.sources = new HashMap<String, ILogicalOperator>(
				attributeResolver.sources);
		this.originalSourceNames = new HashMap<String, ILogicalOperator>(
				attributeResolver.originalSourceNames);
		this.attributes = new HashSet<SDFAttribute>(
				attributeResolver.attributes);
		this.resources = new HashMap<>(attributeResolver.resources);
	}

	@Override
	public List<SDFSchema> getSchema() {
		SDFSchema schema = SDFSchemaFactory.createNewTupleSchema("", attributes);
		List<SDFSchema> schemaList = new LinkedList<>();
		schemaList.add(schema);
		return schemaList;
	}

	public ILogicalOperator getSource(String name) {
		return findSource(name);
	}

	public void addSource(String name, ILogicalOperator op) {
		if (this.sources.containsKey(name)) {
			throw new AmbiguousAttributeException("ambigiuous identifier: " + name);
		}
		this.sources.put(name, op);
		// read attributes from sources!
		SDFSchema schema = op.getOutputSchema();
		addAttributes(schema);
	}

	public void addAttribute(SDFAttribute attribute) {
		if (this.attributes.contains(attribute)) {
			throw new AmbiguousAttributeException("ambigiuous identifier: "
					+ attribute);
		}
		this.attributes.add(attribute);
	}

	public void addAttributes(SDFSchema list) {
		for (SDFAttribute attribute : list) {
			addAttribute(attribute);
		}
	}

	public void addAttributes(Collection<SDFAttribute> attributes) {
		for (SDFAttribute attribute : attributes) {
			addAttribute(attribute);
		}
	}

	@Override
	public Set<SDFAttribute> getAllAttributes() {
		return attributes;
	}

	@Override
	public boolean isEmpty() {
		return attributes.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.querytranslation.parser.transformation
	 * . IAttributeResolver#getAttribute(java.lang.String)
	 */
	@Override
	public SDFAttribute getAttribute(String name) {
		String[] parts = name.split("\\.", 2);
		SDFAttribute result = null;
		if (parts.length == 1 || name.contains("(")) {
//			for (ILogicalOperator source : this.sources.values()) {
//				for (SDFAttribute curAttribute : source.getOutputSchema()) {
//					if (curAttribute.getAttributeName().equals(name)) {
//						if (result != null) {
//							throw new IllegalArgumentException(
//									"ambigiuous identifier: " + name);
//						}
//						result = curAttribute;
//					}
//				}
//			}
			for (SDFAttribute curAttribute : this.attributes) {
				if (curAttribute.getAttributeName().equals(name)) {
					if (result != null) {
						throw new AmbiguousAttributeException(
								"ambigiuous identifier: " + name);
					}
					result = curAttribute;
				}
			}
		} else {
			Exception e1 = null;
			Exception e2 = null;
			try {
				result = getAttribute(parts[0], parts[1]);
			} catch (NoSuchAttributeException exp) {
				e1 = exp;
			}

			if (result == null) {
				parts = name.split("\\.", 3);
				if (parts.length == 3)
					try{
					result = getAttribute(new StringBuffer(parts[0])
							.append(".").append(parts[1]).toString(), parts[2]);
					}catch(Exception exp){
						e2 = exp;
					}
			}

			if (result == null){
				StringBuffer msg = new StringBuffer();
				if (e1 != null){
					msg.append(e1.getMessage()+"\n");
				}
				if (e2 != null){
					msg.append(e2.getMessage()+"\n");
				}
				throw new IllegalArgumentException(msg.toString());
			}
		}

		return result;
	}

	private SDFAttribute getAttribute(String sourceName, String attributeName) {
		ILogicalOperator source = findSource(sourceName);
		if (source == null) {
			throw new IllegalArgumentException("no such source: " + sourceName);
		}

		String[] path = attributeName.split("\\:");
		SDFAttribute attribute = findORAttribute(source.getOutputSchema(),
				path, 0);

		if (attribute != null)
			return attribute;

		throw new NoSuchAttributeException("no such attribute: " + sourceName
				+ "." + attributeName);
	}

	private ILogicalOperator findSource(String sourceName) {
		ILogicalOperator ret = this.sources.get(sourceName);
		if (ret == null) {
			for (Entry<Resource, ILogicalOperator> e : this.resources
					.entrySet()) {
				if (e.getKey().getResourceName().equalsIgnoreCase(sourceName)
						|| e.getKey().toString().equalsIgnoreCase(sourceName)) {
					ret = e.getValue();
					break;
				}
			}
		}
		return ret;
	}

	private SDFAttribute findORAttribute(SDFSchema list, String[] path,
			int index) {
		String toFind = path[index];
		for (SDFAttribute attr : list) {
			if (attr.getAttributeName().equals(toFind)) {
				if (index == path.length - 1) {
					return attr;
				} else if (attr.getDatatype().hasSchema()) {
					return findORAttribute(attr.getDatatype().getSchema(),
							path, index + 1);
				}
			}
		}
		return null;
	}

	public SDFAttribute getAggregateAttribute(String attributeName,
			String aggregateName) {
		SDFAttribute attribute = getAttribute(attributeName);
		aggregateName = aggregateName + "(" + attribute.getURI() + ")";
		for (SDFAttribute curAttribute : this.attributes) {
			if (curAttribute.getAttributeName().equals(aggregateName)) {
				return curAttribute;
			}
		}
		throw new NoSuchAttributeException("no such attribute: "
				+ attributeName + " " + aggregateName);
	}

	// public SDFAttribute getAggregateAttributeOFF(ASTAggregateExpression
	// expression) {
	// String name = expression.jjtGetChild(1).toString();
	// SDFAttribute attribute = getAttribute(name);
	// String aggregateName = expression.jjtGetChild(0).toString() + "(" +
	// attribute.getPointURI() + ")";
	// for (SDFAttribute curAttribute : this.attributes) {
	// if (curAttribute.getAttributeName().equals(aggregateName)) {
	// return curAttribute;
	// }
	// }
	// throw new IllegalArgumentException("no such attribute: " +
	// expression.toString());
	// }

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

	public ILogicalOperator getOriginalSource(String name) {
		return this.originalSourceNames.get(name);
	}

	public void addSourceOriginal(String name, ILogicalOperator op) {
		if (!this.originalSourceNames.containsKey(name)) {
			this.originalSourceNames.put(name, op);
		}
	}

	public void addSource(Resource originalName, ILogicalOperator inputOp) {
		if (!this.resources.containsKey(originalName)) {
			this.resources.put(originalName, inputOp);
		}
	}

}
