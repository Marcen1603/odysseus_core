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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.compiler.Compiler;
import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.GenerateMetadataClassCode;
import de.uniol.inf.is.odysseus.core.metadata.GenericCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.util.FileUtils;

/**
 * Registry for all meta data types currently supported
 *
 * @author Marco Grawunder
 *
 */

public class MetadataRegistry {

	final private static Logger logger = LoggerFactory.getLogger(MetadataRegistry.class);

	final private static InfoService info = InfoServiceFactory.getInfoService(MetadataRegistry.class);

	private static Map<SortedSet<String>, IMetaAttribute> combinedMetadataTypes = new HashMap<>();

	private static Map<String, IMetaAttribute> byName = new HashMap<>();

	public void addMetadataType(IMetaAttribute type) {

		logger.trace("New Metadatatype registered " + type.getClass());


		Class<? extends IMetaAttribute> implementationType = type.getClass();
		SortedSet<String> typeSet = toStringSet(type.getClasses());
		synchronized (combinedMetadataTypes) {
			if (combinedMetadataTypes.containsKey(typeSet)
					&& combinedMetadataTypes.get(typeSet).getClass() != implementationType) {
				throw new IllegalArgumentException("combined metadatatype already exists");
			}
			try {
				if (implementationType.getMethod("clone", (Class<?>[]) null)
						.getDeclaringClass() != implementationType) {
					String msg = "implementation class does not declare a clone method, this will lead to runtime exceptions";
					throw new IllegalArgumentException(msg);
				}
			} catch (Exception e) {
				logger.warn("could not check wether '" + implementationType.getName()
						+ "' supports clone() method, reason:\n\t" + e.getMessage());
			}
			byName.put(type.getName(), type);
			if (type instanceof IHasAlias){
				byName.put(((IHasAlias)type).getAliasName(), type);
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
		if (types.size() == 0) {
			return null;
		}
		synchronized (combinedMetadataTypes) {
			IMetaAttribute type = combinedMetadataTypes.get(types);

			if (type == null) {

				List<IMetaAttribute> metaDataTypes = new ArrayList<>();
				List<Class<? extends IMetaAttribute>> classList = new ArrayList<>();
				for (String t : types) {
					SortedSet<String> toSearchFor = new TreeSet<>();
					toSearchFor.add(t);
					IMetaAttribute mt = combinedMetadataTypes.get(toSearchFor);
					if (mt == null) {
						mt = getMetadataTypeByName(t);
					}
					if (mt instanceof AbstractCombinedMetaAttribute) {
						// TODO Handle
						throw new IllegalArgumentException(
								"Cannot use generic metadata with " + mt + ". Add Basetypes instead");
					} else {
						metaDataTypes.add(mt);
					}
					for (Class<? extends IMetaAttribute> c : mt.getClasses()) {
						classList.add(c);
					}
				}

				// first compile
				logger.info("Trying to compile a new metadata class. Takes some time!");
				info.info("Trying to compile a new metadata class. Takes some time!");

				// todo allow flag do deactivate
				StringBuffer classCode = new StringBuffer();
				try {

					String className = GenerateMetadataClassCode.generateClassCode(classList, metaDataTypes, classCode);
					// Write file for reuse, not necessary, so could fail
					try {
						File file = FileUtils.openOrCreateFile(
								OdysseusBaseConfiguration.getHomeDir() + "/codegen/" + className + ".java");
						BufferedWriter bfw = new BufferedWriter(new FileWriter(file));
						bfw.write(classCode.toString());
						bfw.close();
					} catch (Exception e) {

					}

					Class<?> cl = Compiler.compile(classCode, className);
					type = (IMetaAttribute) cl.newInstance();
					combinedMetadataTypes.put(types, type);

					logger.info("Success.");

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (type == null) {
					info.warning("Could not compile class.");
					logger.warn("No predefined type for " + types + ". Creating generic one. Will have lower performance.");
					info.warning("No predefined type for " + types + ". Creating generic one. Will have lower performance.");
					Object gen = GenericCombinedMetaAttribute.newInstance(classList, metaDataTypes);
					type = (IMetaAttribute) gen;
					combinedMetadataTypes.put(types, type);
				}
				// throw new IllegalArgumentException("No metadata type for: "
				// + types.toString());
			}
			return type;
		}

	}

	public static IMetaAttribute getMetadataType(List<String> metaAttributeNames) {
		SortedSet<String> names = new TreeSet<String>();
		for (String n : metaAttributeNames) {
			names.add(n);
		}
		return getMetadataType(names);
	}

	public static List<SDFMetaSchema> getMetadataSchema(SortedSet<String> types) {
		synchronized (combinedMetadataTypes) {
			IMetaAttribute type = combinedMetadataTypes.get(types);
			if (type == null) {
				throw new IllegalArgumentException("No metadata type for: " + types.toString());
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
	public static void removeCombinedMetadataType(Class<? extends IMetaAttribute>... combinationOf) {
		SortedSet<String> typeSet = toStringSet(combinationOf);

		synchronized (combinedMetadataTypes) {
			combinedMetadataTypes.remove(typeSet);
		}
	}

	public static Set<String> getNames() {
		return byName.keySet();
	}

	@SafeVarargs
	private static SortedSet<String> toStringSet(Class<? extends IMetaAttribute>... combinationOf) {
		SortedSet<String> typeSet = new TreeSet<String>();
		for (Class<?> c : combinationOf) {
			typeSet.add(c.getName());
		}
		return typeSet;
	}

	// Helper methods

	public static IMetaAttribute tryCreateMetadataInstance(String parameter) throws IllegalArgumentException {
		try {
			return MetadataRegistry.getMetadataTypeByName(parameter).getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("Could not create metadata of type '" + parameter + "'", e);
		}
	}

	public static SortedSet<String> toClassNames(List<Class<? extends IMetaAttribute>> classes) {
		SortedSet<String> classNames = new TreeSet<String>();
		for (Class<?> c : classes) {
			classNames.add(c.getName());
		}
		return classNames;
	}

	public static SortedSet<String> toClassNames(Class<? extends IMetaAttribute>[] classes) {
		SortedSet<String> classNames = new TreeSet<String>();
		for (Class<?> c : classes) {
			classNames.add(c.getName());
		}
		return classNames;
	}

	@SuppressWarnings("unchecked")
	public static IMetadataMergeFunction<?> getMergeFunction(List<String> left, List<String> right) {
		// TODO: Find ways to handle different meta schemas
		if (left.size() != right.size()) {
			throw new IllegalArgumentException("Meta data of inputs do not match!");
		}
		CombinedMergeFunction<IMetaAttribute> cmf = new CombinedMergeFunction<>();

		Collections.sort(left);
		Collections.sort(right);

		for (int i = 0; i < left.size(); i++) {
			String l = left.get(i);
			String r = right.get(i);
			if (!l.equals(r)) {
				throw new IllegalArgumentException("Meta data of inputs do not match!");
			}
			List<?> functions = getMetadataType(l).getInlineMergeFunctions();
			for (Object f : functions) {
				cmf.add((IInlineMetadataMergeFunction<? super IMetaAttribute>) f);
			}
		}

		return cmf;
	}

	public static boolean isSame(Class<? extends IMetaAttribute>[] first, List<String> second) {
		List<String> firstStr = new ArrayList<String>(toClassNames(first));
		return isSame(firstStr, second);
	}

	public static boolean isSame(List<String> first, List<String> second) {
		if (first.size() != second.size()) {
			return false;
		}

		Collections.sort(first);
		Collections.sort(second);

		for (int i = 0; i < first.size(); i++) {
			String l = first.get(i);
			String r = second.get(i);
			if (!l.equals(r)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static IMetadataMergeFunction getMergeFunction(List<String> metadataSet) {
		CombinedMergeFunction<IMetaAttribute> cmf = new CombinedMergeFunction<>();
		for (String m : metadataSet) {
			List<?> functions = getMetadataType(m).getInlineMergeFunctions();
			for (Object f : functions) {
				cmf.add((IInlineMetadataMergeFunction<? super IMetaAttribute>) f);
			}
		}
		return cmf;
	}

	public static boolean contains(Class<? extends IMetaAttribute>[] classes, Class<? extends IMetaAttribute> class1) {
		for (Class<? extends IMetaAttribute> c : classes) {
			if (c == class1) {
				return true;
			}
		}
		return false;
	}

}
