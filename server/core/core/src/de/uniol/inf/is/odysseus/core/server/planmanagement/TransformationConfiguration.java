/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.planmanagement;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class TransformationConfiguration {
	private final SortedSet<String> metaTypes = new TreeSet<String>();
	final private Map<String, Object> options = new HashMap<String, Object>();
	final private ITransformationHelper transformHelper = new StandardTransformationHelper();;
	private boolean virtualTransformation;
	
//	public TransformationConfiguration(ITransformationHelper transformHelper, String... metaTypes) {
//		this.metaTypes = toSet(metaTypes);
//		this.options = new HashMap<String, Object>();	
//		this.transformHelper = transformHelper;
//	}
	

//	@SafeVarargs
//	public TransformationConfiguration(ITransformationHelper transformHelper,
//			Class<? extends IMetaAttribute>... metaTypes) {
//		HashSet<String> tmp = new HashSet<String>();
//		for(Class<? extends IMetaAttribute> type : metaTypes) {
//			tmp.add(type.getName());
//		}
//		this.metaTypes = Collections.unmodifiableSet(tmp);
//		this.options = new HashMap<String, Object>();
//		this.transformHelper = transformHelper;
//	}

	public TransformationConfiguration(){
		
	}
	
	public TransformationConfiguration(String... metaTypes) {
		this.metaTypes.addAll(Arrays.asList(metaTypes));
	}

	
	@SafeVarargs
	public TransformationConfiguration(Class<? extends IMetaAttribute>... metaTypes) {
		HashSet<String> tmp = new HashSet<String>();
		for(Class<? extends IMetaAttribute> type : metaTypes) {
			tmp.add(type.getName());
		}
		this.metaTypes.addAll(tmp);
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

	public SortedSet<String> getMetaTypes() {
		return (SortedSet<String>) Collections.unmodifiableSet(metaTypes);
	}
	
	public void setVirtualTransformation(boolean virtualTransformation) {
		this.virtualTransformation = virtualTransformation;
	}
	
	public boolean isVirtualTransformation() {
		return virtualTransformation;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("metadata types: ");
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
	
	public boolean hasOption(String key) {
		return this.options.containsKey(key);
	}
	
	public void removeOption(String key){
		this.options.remove(key);
	}
	
	public ITransformationHelper getTransformationHelper(){
		return this.transformHelper;
	}

	public void addTypes(Set<String> types) {
		
		metaTypes.addAll(types);
	}
}
