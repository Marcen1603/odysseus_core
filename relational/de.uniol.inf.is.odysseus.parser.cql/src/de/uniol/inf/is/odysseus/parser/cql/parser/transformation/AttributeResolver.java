package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

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
		this.attributes = new HashSet<SDFAttribute>(
				attributeResolver.attributes);
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
					if (curAttribute.getAttributeName().equals(name)) {
						if (result != null) {
							throw new IllegalArgumentException(
									"ambigiuous identifier: " + name);
						} else {
							result = curAttribute;
						}
					}
				}
			}
			for (SDFAttribute curAttribute : this.attributes) {
				if (curAttribute.getAttributeName().equals(name)) {
					if (result != null) {
						throw new IllegalArgumentException(
								"ambigiuous identifier: " + name);
					} else {
						result = curAttribute;
					}
				}
			}
		} else {
			result = getAttribute(parts[0], parts[1]);
		}

		return result;
	}

	private SDFAttribute getAttribute(String sourceName, String attributeName) {
		ILogicalOperator source= this.sources.get(sourceName);
		if (source == null) {
			throw new IllegalArgumentException("no such source: " + sourceName);
		}
		
		for (SDFAttribute attribute : source.getOutputSchema()) {
			SDFAttribute a = findORAttribute(attributeName, attribute);
			if( a != null ) return a;
//			if (attribute.getAttributeName().equals(attributeName)) {
//				return attribute;
//			}
		}
		throw new IllegalArgumentException("no such attribute: " + sourceName
				+ "." + attributeName);
	}
	
	private SDFAttribute findORAttribute( String attributeName, SDFAttribute attr ) {
		if(attr.getQualName().equals(attributeName)) return attr;
		for( SDFAttribute a : attr.getSubattributes() ) {
			SDFAttribute b = findORAttribute(attributeName, a);
			if( b != null ) return b;
		}
		return null;
	}

	public SDFAttribute getAggregateAttribute(ASTAggregateExpression expression) {
		String name = expression.jjtGetChild(1).toString();
		SDFAttribute attribute = getAttribute(name);
		String aggregateName = expression.jjtGetChild(0).toString() + "("
				+ attribute.getPointURI() + ")";
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

	@Override
	public String toString() {
		return "Sources "+sources+" attributes"+attributes;
	}
	
	public AttributeResolver clone()  {
		return new AttributeResolver(this);
	}
	
	@Override
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> updated) {
		Set<String> sourceNames = this.sources.keySet();
		for (String sourceName : sourceNames){
			ILogicalOperator source = sources.get(sourceName);
			ILogicalOperator newSource = updated.get(source);
			if (newSource != null){
				sources.put(sourceName, newSource);
			}
		}
	}
}
