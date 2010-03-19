package de.uniol.inf.is.odysseus.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TransformationConfiguration {
	private final Set<String> metaTypes;
	private final String dataType;
	private Map<String, Object> options;

	public TransformationConfiguration(String dataType, String... metaTypes) {
		this.dataType = dataType;
		this.metaTypes = toSet(metaTypes);
		this.options = new HashMap<String, Object>();		
	}

	public TransformationConfiguration(final String dataType,
			Class<? extends IMetaAttribute>... metaTypes) {
		this.dataType = dataType;
		HashSet<String> tmp = new HashSet<String>();
		for(Class<? extends IMetaAttribute> type : metaTypes) {
			tmp.add(type.getName());
		}
		this.metaTypes = Collections.unmodifiableSet(tmp);
		this.options = new HashMap<String, Object>();
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

	@Override
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
		return (T) this.options.get(key);
	}
	
	public void removeOption(String key){
		this.options.remove(key);
	}
}
