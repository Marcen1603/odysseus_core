package de.uniol.inf.is.odysseus.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class TransformationConfiguration {
	private Set<String> metaTypes = new HashSet<String>();
	private String dataType;
	private Properties options;

	public TransformationConfiguration(String dataType, String... metaTypes) {
		this.dataType = dataType;
		this.metaTypes = toSet(metaTypes);
		this.options = new Properties();
	}

	public boolean metaTypesEqual(String... types) {
		return metaTypes.equals(toSet(types));
	}

	public static Set<String> toSet(String... strings) {
		if (strings == null) {
			return new HashSet<String>();
		}
		return Collections.unmodifiableSet(new HashSet<String>(Arrays
				.asList(strings)));
	}

	public String getDataType() {
		return dataType;
	}

	public Set<String> getMetaTypes() {
		return metaTypes;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("data type: ");
		builder.append(this.dataType);
		builder.append("; metadata types: ");
		int i = 0;
		for (String s : metaTypes) {
			if (++i > 1) {
				builder.append(", ");
			}
			builder.append(s);
		}
		return builder.toString();
	}
	
	public void setOption(String key, Object value) {
		this.options.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOption(String key) {
		return (T)this.options.get(key);
	}
}
