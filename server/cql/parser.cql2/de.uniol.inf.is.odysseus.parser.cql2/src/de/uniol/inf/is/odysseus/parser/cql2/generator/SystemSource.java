package de.uniol.inf.is.odysseus.parser.cql2.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;

public class SystemSource {

	protected static Map<String, String> attributeAliases = new HashMap<>();
	protected static Collection<String> querySources = new HashSet<>();

	//private final Logger log = LoggerFactory.getLogger(SystemSource.class);

	public String name;
	public Collection<SystemAttribute> attributeList;
	public List<String> aliasList;
	public boolean meta;

	public SystemSource() {
		attributeList = new ArrayList<>();
		aliasList = new ArrayList<>();

	}
	
	public SystemSource(boolean meta) {
		this();
		this.meta = meta;
	}
	
	public SystemSource(SystemSource source, boolean meta) {
		this.name = source.name;
		this.attributeList = source.attributeList;
		this.aliasList = source.aliasList;
		this.meta = meta;
	}

	public boolean isMeta() {
		return meta;
	}

	public void associateAttributeAliasWithSourceAlias(Alias alias, String sourceAlias) {
		associateAttributeAliasWithSourceAlias(alias.getName(), sourceAlias);
	}

	public void associateAttributeAliasWithSourceAlias(String attributealias, Source source) {
		associateAttributeAliasWithSourceAlias(attributealias, source.getAlias().getName());
	}

	public void associateAttributeAliasWithSourceAlias(String attributealias, String source) {
		attributeAliases.put(attributealias, source);
	}

	public boolean isAssociatedToASource(String attributealias) {
		return hasAttribute(attributealias);
	}

	public boolean isAssociatedToASource(Attribute attribute) {
		if (attribute.getAlias() != null) {
			return isAssociatedToASource(attribute.getAlias().getName());
		}
		return false;
	}

	public String getAssociatedSource(String attributealias) {
		return isAssociatedToASource(attributealias) ? attributeAliases.get(attributealias) : null;
	}

	public void update(SystemAttribute attribute) {
		for (SystemAttribute e : attributeList) {
			if (e.equals(attribute)) {
				attribute.update(attribute);
			}
		}
	}

	public void add(SystemAttribute attribute) {
		if (!attributeList.contains(attribute)) {
			attributeList.add(attribute);
		}

	}

	public void addRenamedAttribute(List<String> names) {
		List<SystemAttribute> list = new ArrayList<>();
		for (SystemAttribute attribute : attributeList) {
			for (int i = 1; i < list.size(); i++) {
				String name = names.get(i).split("\\.")[1];
				SystemAttribute newAttribute = new SystemAttribute(null, name, attribute.datatype);
				attributeList.add(newAttribute);
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns an {@link SystemAttribute} object for a given string that is either
	 * its name or an alias.
	 * 
	 * @param name
	 * @return
	 */
	public SystemAttribute findByName(String name) {
		SystemAttribute attribute = null;
		if (name.contains(".")) {
			String[] split = name.split("\\.");
			if (split[0].equals(this.name)) {
				for (SystemAttribute e : attributeList) {
					if (e.attributename.equals(split[1]) || e.aliases.contains(split[1])) {
						attribute = e;
					}
				}
			} else if (aliasList.contains(split[0])) {
				for (SystemAttribute e : attributeList) {
					if (e.attributename.equals(split[1]) || e.aliases.contains(split[1])) {
						attribute = e;
					}
				}
			}
		} else {
			for (SystemAttribute e : attributeList) {
				if (e.attributename.equals(name) || e.aliases.contains(name)) {
					attribute = e;
				}
			}
		}

		return attribute;
	}

	public List<SystemAttribute> findByType(String type) {
		List<SystemAttribute> list = new ArrayList<>();
		for (SystemAttribute e : attributeList) {
			if (e.datatype.equalsIgnoreCase(type)) {
				list.add(e);
			}
		}

		return list;
	}

	public boolean hasAttribute(String name) {
		return findByName(name) != null ? true : false;
	}

	public boolean hasAttribute(Attribute attribute) {
		return hasAttribute(attribute.getName());
	}

	public boolean isSame(SimpleSource source) {
		return this.getName().equals(source.getName());
	}

	public boolean isContainedBy(Collection<Source> sourceList) {
		for (Source source : sourceList) {
			if (source instanceof SimpleSource) {
				if (((SimpleSource) source).getName().equals(this.getName())) {
					return true;
				}
				Alias alias = ((SimpleSource) source).getAlias();
				if (alias != null && alias.getName().equals(this.getName()) || this.hasAlias(alias)) {
					return true;
				}
			}
		}

		return false;
	}

	//TODO remove
	public SystemAttribute getStartTimestampAttribute() {
		return findByName("StartTimestamp");
	}

	//TODO remove
	public SystemAttribute getEndTimestampAttribute() {
		return findByName("EndTimestamp");
	}

	@Override
	public String toString() {
		return "["+ this.getClass() +"::name=" + name + ", aliases=" + aliasList.toString() + "]";
	}

	public Collection<SystemAttribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<SystemAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	public List<String> getAliasList() {
		return aliasList;
	}

	public void setAliasList(List<String> aliasList) {
		this.aliasList = aliasList;
	}

	public String getName() {
		return name;
	}

	public void addAliasTo(String attribute, String alias) {
		SystemAttribute struct = findByName(attribute);
		if (struct != null) {
			struct.addAlias(alias);
		}

	}

	public void addAlias(Alias alias) {
		addAlias(alias.getName());
	}

	public void addAlias(String sourcealias) {
		if (!aliasList.contains(sourcealias)) {
			aliasList.add(sourcealias);
		}

	}

	public boolean hasAlias(Alias alias) {
		return alias!= null ? hasAlias(alias.getName()) : false;
	}

	public boolean hasAlias(String alias) {
		return aliasList.contains(alias);
	}

	public void removeAliasFrom(String attributename, String alias) {
		SystemAttribute attribute = findByName(attributename);
		if (attribute != null) {
			attribute.removeAlias(alias);
		}
	}
	
	public static Collection<String> getQuerySources() {
		return querySources;
	}

	public static void addQuerySource(String querySource) {
		if (querySource != null && !querySource.isEmpty()) {
			querySources.add(querySource);
		}
	}

	public static void setQuerySources(Collection<String> querySources) {
		SystemSource.querySources = querySources;
	}

	public static boolean existQuerySource(String querySource) {
		if (querySource != null && !querySource.isEmpty()) {
			return querySources.contains(querySource);
		}

		return false;
	}

	public static void clearQuerySources() {
		if (querySources != null) {
			querySources.clear();
		}
	}

	public static void clearAttributeAliases() {
		if (attributeAliases != null) {
			attributeAliases.clear();
		}
	}

}
