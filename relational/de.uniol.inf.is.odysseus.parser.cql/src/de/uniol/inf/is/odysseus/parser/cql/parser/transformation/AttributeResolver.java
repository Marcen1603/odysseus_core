package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * @author Jonas Jacobi
 */
public class AttributeResolver implements IAttributeResolver {
	private static final long serialVersionUID = -6960117786021105217L;

	private Map<String, AbstractLogicalOperator> sources;

	private Set<SDFAttribute> attributes;

	public AttributeResolver() {
		this.sources = new HashMap<String, AbstractLogicalOperator>();
		this.attributes = new HashSet<SDFAttribute>();
	}

	public AttributeResolver(AttributeResolver attributeResolver) {
		this.sources = new HashMap<String, AbstractLogicalOperator>(attributeResolver.sources);
		this.attributes = new HashSet<SDFAttribute>(
				attributeResolver.attributes);
	}

	public AbstractLogicalOperator getSource(String name) {
		return this.sources.get(name);
	}

	public void addSource(String name, AbstractLogicalOperator op) {
		if (this.sources.containsKey(name)) {
			throw new IllegalArgumentException("ambigiuous identifier: " + name);
		}
		this.sources.put(name, op);
	}

	public void addAttribute(SDFAttribute attribute) {
		if (this.attributes.contains(attribute)) {
			throw new IllegalArgumentException("ambigiuous identifier: "
					+ attribute);
		}
		this.attributes.add(attribute);
	}
	
	public void addAttributes(Collection<SDFAttribute> attributes) {
		for(SDFAttribute attribute : attributes) {
			addAttribute(attribute);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.querytranslation.parser.transformation.IAttributeResolver#getAttribute(java.lang.String)
	 */
	public SDFAttribute getAttribute(String name) {
		String[] parts = name.split("\\.", 2);
		SDFAttribute result = null;
		if (parts.length == 1 || name.contains("(")) {
			for (ILogicalOperator source : this.sources.values()) {
				for (SDFAttribute curAttribute : source.getOutputSchema()) {
					SDFAttribute SDFAttribute = (SDFAttribute) curAttribute;
					if (SDFAttribute.getAttributeName().equals(name)) {
						if (result != null) {
							throw new IllegalArgumentException(
									"ambigiuous identifier: " + name);
						} else {
							result = SDFAttribute;
						}
					}
				}
			}
			for (SDFAttribute SDFAttribute : this.attributes) {
				if (SDFAttribute.getAttributeName().equals(name)) {
					if (result != null) {
						throw new IllegalArgumentException(
								"ambigiuous identifier: " + name);
					} else {
						result = SDFAttribute;
					}
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
		for (SDFAttribute attribute : source.getOutputSchema()) {
			SDFAttribute SDFAttribute = (SDFAttribute) attribute;
			if (SDFAttribute.getAttributeName().equals(attributeName)) {
				return SDFAttribute;
			}
		}
		throw new IllegalArgumentException("no such attribute: " + sourceName
				+ "." + attributeName);
	}

	public SDFAttribute getAggregateAttribute(ASTAggregateExpression expression) {
		String name = expression.jjtGetChild(1).toString();
		SDFAttribute attribute = getAttribute(name);
		String aggregateName = expression.jjtGetChild(0).toString() + "("
				+ attribute.getURI(false) + ")";
		for (SDFAttribute curAttribute : this.attributes) {
			if (curAttribute.getAttributeName().equals(aggregateName)) {
				return curAttribute;
			}
		}
		throw new IllegalArgumentException("no such attribute: "
				+ expression.toString());
	}

	public boolean isAttributeValid(String name) {
		return getAttribute(name) != null;
	}

}
