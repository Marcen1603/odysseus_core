package de.uniol.inf.is.odysseus.parser.cql2.generator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;

public class SystemAttribute {

	public String attributename;
	public SystemSource source;
	public String datatype;
	public List<String> prefixes;
	public List<String> aliases;

	public SystemAttribute(SystemSource source, String name, String type) {
		this.prefixes = new ArrayList<>();
		this.aliases = new ArrayList<>();
		this.source = source;
		this.attributename = name;
		this.datatype = type;

	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SystemAttribute))
			return false;
		SystemAttribute attribute = (SystemAttribute) obj;
		return attribute.attributename.equals(this.attributename) && attribute.datatype.equals(this.datatype)
				&& attribute.source.equals(this.source);
	}
	
	@Override
	public String toString() {
		return attributename;
	}

	public String getAttributename() {
		return attributename;
	}

	public void setAttributename(String attributename) {
		this.attributename = attributename;
	}

	public SystemSource getSource() {
		return source;
	}

	public void setSource(SystemSource source) {
		this.source = source;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public List<String> getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(List<String> prefixes) {
		this.prefixes = prefixes;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public void update(SystemAttribute attribute) {
		this.aliases = new ArrayList<>(attribute.getAliases());
		this.prefixes = new ArrayList<>(attribute.getPrefixes());
		this.attributename = attribute.attributename;
		this.datatype = attribute.getDatatype();
		this.source = attribute.getSource();
	}

	public void addAlias( Alias alias) {
		addAlias(alias.getName());
	}
	
	public void addAlias( String alias) {
		if (!this.aliases.contains(alias)) {
			if(alias.trim().equals("")) {
				throw new IllegalArgumentException("given alias was empty");
			}
			this.aliases.add(alias);
		}
		
	}

	public boolean hasAlias( Alias alias) {
		return hasAlias(alias.getName());
	}
	
	public boolean hasAlias( String alias) {
		return this.aliases.contains(alias);
	}

	public void removeAlias(String alias) {
		if(aliases.contains(alias)) {
			aliases.remove(alias);
		}
		
	}


}
