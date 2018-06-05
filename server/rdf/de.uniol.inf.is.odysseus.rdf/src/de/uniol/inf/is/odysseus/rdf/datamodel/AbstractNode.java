package de.uniol.inf.is.odysseus.rdf.datamodel;

import java.util.Map;

public abstract class AbstractNode implements INode {

	private final String name;

	protected AbstractNode(String name) {
		this.name = name;
	}

	@Override
	public INode replacePrefixes(Map<String, String> prefixes) {
		int index = name.indexOf(":");
		String prefix = name.substring(0, index + 1);
		if (prefixes.get(prefix) != null){
			String newName = name.replace(prefix, prefixes.get(prefix));
			return cloneWithNewName(newName);
		}else{
			return this;
		}
	}

	abstract protected INode cloneWithNewName(String newName);
	
	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public boolean isIRI() {
		return false;
	}

	@Override
	public boolean isBlankNode() {
		return false;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Object getValue() {
		return null;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractNode other = (AbstractNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



}
