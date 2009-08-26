package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * @author Jonas Jacobi
 */
public class AttributeResolver implements IAttributeResolver {
	private static final long serialVersionUID = -6960117786021105217L;

	private Map<String, AbstractLogicalOperator> sources;

	private Set<CQLAttribute> attributes;

	public AttributeResolver() {
		this.sources = new HashMap<String, AbstractLogicalOperator>();
		this.attributes = new HashSet<CQLAttribute>();
	}

	public AttributeResolver(AttributeResolver attributeResolver) {
		this.sources = new HashMap<String, AbstractLogicalOperator>(attributeResolver.sources);
		this.attributes = new HashSet<CQLAttribute>(
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

	public void addAttribute(CQLAttribute attribute) {
		if (this.attributes.contains(attribute)) {
			throw new IllegalArgumentException("ambigiuous identifier: "
					+ attribute);
		}
		this.attributes.add(attribute);
	}
	
	public void addAttributes(Collection<CQLAttribute> attributes) {
		for(CQLAttribute attribute : attributes) {
			addAttribute(attribute);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.querytranslation.parser.transformation.IAttributeResolver#getAttribute(java.lang.String)
	 */
	public CQLAttribute getAttribute(String name) {
		String[] parts = name.split("\\.", 2);
		CQLAttribute result = null;
		if (parts.length == 1 || name.contains("(")) {
			for (ILogicalOperator source : this.sources.values()) {
				for (SDFAttribute curAttribute : source.getOutputSchema()) {
					CQLAttribute cqlAttribute = (CQLAttribute) curAttribute;
					if (cqlAttribute.getAttributeName().equals(name)) {
						if (result != null) {
							throw new IllegalArgumentException(
									"ambigiuous identifier: " + name);
						} else {
							result = cqlAttribute;
						}
					}
				}
			}
			for (CQLAttribute cqlAttribute : this.attributes) {
				if (cqlAttribute.getAttributeName().equals(name)) {
					if (result != null) {
						throw new IllegalArgumentException(
								"ambigiuous identifier: " + name);
					} else {
						result = cqlAttribute;
					}
				}
			}
		} else {
			result = getAttribute(parts[0], parts[1]);
		}

		return result;
	}

	private CQLAttribute getAttribute(String sourceName, String attributeName) {
		ILogicalOperator source = this.sources.get(sourceName);
		if (source == null) {
			throw new IllegalArgumentException("no such source: " + sourceName);
		}
		for (SDFAttribute attribute : source.getOutputSchema()) {
			CQLAttribute cqlAttribute = (CQLAttribute) attribute;
			if (cqlAttribute.getAttributeName().equals(attributeName)) {
				return cqlAttribute;
			}
		}
		throw new IllegalArgumentException("no such attribute: " + sourceName
				+ "." + attributeName);
	}

	public CQLAttribute getAggregateAttribute(ASTAggregateExpression expression) {
		String name = expression.jjtGetChild(1).toString();
		CQLAttribute attribute = getAttribute(name);
		String aggregateName = expression.jjtGetChild(0).toString() + "("
				+ attribute.getURI(false) + ")";
		for (CQLAttribute curAttribute : this.attributes) {
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
