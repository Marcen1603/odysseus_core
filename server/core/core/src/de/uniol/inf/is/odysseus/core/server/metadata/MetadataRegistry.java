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
package de.uniol.inf.is.odysseus.core.server.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;

/**
 * Registry for all meta data types currently supported
 * @author Marco Grawunder
 *
 */

public class MetadataRegistry {

	private static Logger logger = LoggerFactory
			.getLogger(MetadataRegistry.class);
	
	private static Map<SortedSet<String>, IMetaAttribute> combinedMetadataTypes = new HashMap<>();

	private static Map<String, IMetaAttribute> byName = new HashMap<>();

	public static void addMetadataType(IMetaAttribute type) {

		logger.trace("New Metadatatype registered " + type.getClass());

		byName.put(type.getName(), type);

		Class<? extends IMetaAttribute> implementationType = type.getClass();
		SortedSet<String> typeSet = toStringSet(type.getClasses());
		synchronized (combinedMetadataTypes) {
			if (combinedMetadataTypes.containsKey(typeSet)
					&& combinedMetadataTypes.get(typeSet).getClass() != implementationType) {
				throw new IllegalArgumentException(
						"combined metadatatype already exists");
			}
			try {
				if (implementationType.getMethod("clone", (Class<?>[]) null)
						.getDeclaringClass() != implementationType) {
					String msg = "implementation class does not declare a clone method, this will lead to runtime exceptions";
					throw new IllegalArgumentException(msg);
				}
			} catch (Exception e) {
				logger.warn(
						"could not check wether '"
								+ implementationType.getName()
								+ "' supports clone() method, reason:\n\t"
								+ e.getMessage());
			}
			combinedMetadataTypes.put(typeSet, type);
		}
	}

	public static IMetaAttribute getMetadataType(String... types) {
		SortedSet<String> typeSet = new TreeSet<String>();
		for (String typeString : types) {
			typeSet.add(typeString);
		}
		return getMetadataType(typeSet);
	}

	public static IMetaAttribute getMetadataType(SortedSet<String> types) {
		synchronized (combinedMetadataTypes) {
			IMetaAttribute type = combinedMetadataTypes.get(types);
			if (type == null) {
				throw new IllegalArgumentException("No metadata type for: "
						+ types.toString());
			}
			return type;
		}
	}
	
	public static IMetaAttribute getMetadataType(List<String> metaAttributeNames) {
		SortedSet<String> names = new TreeSet<String>();
		for (String n: metaAttributeNames){
			names.add(n);
		}
		return getMetadataType(names);
	}

	public static List<SDFMetaSchema> getMetadataSchema(SortedSet<String> types) {
		synchronized (combinedMetadataTypes) {
			IMetaAttribute type = combinedMetadataTypes.get(types);
			if (type == null) {
				throw new IllegalArgumentException("No metadata type for: "
						+ types.toString());
			}
			return type.getSchema();
		}
	}

	public static IMetaAttribute getMetadataTypeByName(String name) {
		IMetaAttribute type = byName.get(name);
		if (type == null) {
			throw new IllegalArgumentException("No metadata type for: " + name);
		}
		return type;
	}

	public static Set<SortedSet<String>> getAvailableMetadataCombinations() {
		synchronized (combinedMetadataTypes) {
			return combinedMetadataTypes.keySet();
		}
	}

	public static void removeMetadataType(IMetaAttribute type) {
		SortedSet<String> typeSet = toStringSet(type.getClasses());
		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(typeSet);
		}
	}

	@SafeVarargs
	public static void removeCombinedMetadataType(
			Class<? extends IMetaAttribute>... combinationOf) {
		SortedSet<String> typeSet = toStringSet(combinationOf);

		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(typeSet);
		}
	}

	public static Set<String> getNames() {
		return byName.keySet();
	}

	@SafeVarargs
	private static SortedSet<String> toStringSet(
			Class<? extends IMetaAttribute>... combinationOf) {
		SortedSet<String> typeSet = new TreeSet<String>();
		for (Class<?> c : combinationOf) {
			typeSet.add(c.getName());
		}
		return typeSet;
	}

	// Helper methods

	public static IMetaAttribute tryCreateMetadataInstance(String parameter)
			throws IllegalArgumentException {
		try {
			return MetadataRegistry.getMetadataTypeByName(parameter).getClass()
					.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(
					"Could not create metadata of type '" + parameter + "'", e);
		}
	}

	public static SortedSet<String> toClassNames(
			List<Class<? extends IMetaAttribute>> classes) {
		SortedSet<String> classNames = new TreeSet<String>();
		for (Class<?> c : classes) {
			classNames.add(c.getName());
		}
		return classNames;
	}
	
	public static SortedSet<String> toClassNames(
			Class<? extends IMetaAttribute>[] classes) {
		SortedSet<String> classNames = new TreeSet<String>();
		for (Class<?> c : classes) {
			classNames.add(c.getName());
		}
		return classNames;
	}

	@SuppressWarnings("unchecked")
	public static IMetadataMergeFunction<?> getMergeFunction(List<String> left,
			List<String> right) {
		// TODO: Find ways to handle different meta schemas
		if (left.size() != right.size()){
			throw new IllegalArgumentException("Meta data of inputs do not match!");
		}
		CombinedMergeFunction<IMetaAttribute> cmf = new CombinedMergeFunction<>();

		Collections.sort(left);
		Collections.sort(right);
		
		for (int i=0;i<left.size();i++){
			String l = left.get(i);
			String r = right.get(i);
			if (!l.equals(r)){
				throw new IllegalArgumentException("Meta data of inputs do not match!");
			}
			List<?> functions = getMetadataType(l).getInlineMergeFunctions();
			for (Object f :functions){
				cmf.add((IInlineMetadataMergeFunction<? super IMetaAttribute>) f);
			}
		}
		
		
		return cmf;
	}

	public static boolean isSame(Class<? extends IMetaAttribute>[] first, List<String> second){
		List<String> firstStr = new ArrayList<String>((Collection<String>) toClassNames(first));
		return isSame(firstStr, second);
	}
	
	public static boolean isSame(List<String> first, List<String> second){
		if (first.size() != second.size()){
			return false;
		}
		
		Collections.sort(first);
		Collections.sort(second);
		
		for (int i=0;i<first.size();i++){
			String l = first.get(i);
			String r = second.get(i);
			if (!l.equals(r)){
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static IMetadataMergeFunction getMergeFunction(
			List<String> metadataSet) {
		CombinedMergeFunction<IMetaAttribute> cmf = new CombinedMergeFunction<>();
		for (String m:metadataSet){
			List<?> functions = getMetadataType(m).getInlineMergeFunctions();
			for (Object f :functions){
				cmf.add((IInlineMetadataMergeFunction<? super IMetaAttribute>) f);
			}
		}
		return cmf;
	}

	public static boolean contains(Class<? extends IMetaAttribute>[] classes,
			Class<? extends IMetaAttribute> class1) {
		for (Class<? extends IMetaAttribute> c:classes){
			if (c == class1){
				return true;
			}
		}
		return false;
	}




}
