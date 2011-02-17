/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class OperatorBuilderFactory {
	private static Map<String, Class<? extends IOperatorBuilder>> operatorBuilders = new HashMap<String, Class<? extends IOperatorBuilder>>();
	private static Map<String, IPredicateBuilder> predicateBuilders = new HashMap<String, IPredicateBuilder>();

	@SuppressWarnings("unchecked")
	public static <T extends IOperatorBuilder> T createOperatorBuilder(String name, User caller, IDataDictionary dataDictionary) {
		name = name.toUpperCase();
		if (!operatorBuilders.containsKey(name)) {
			throw new IllegalArgumentException("no such operator builder: " + name);
		}
		try {
			T builder = (T) operatorBuilders.get(name).newInstance();
			builder.setCaller(caller);
			builder.setDataDictionary(dataDictionary);
			return builder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean containsOperatorBuilderType(String name) {
		return operatorBuilders.containsKey(name.toUpperCase());
	}

	public static Set<String> getOperatorBuilderNames() {
		return operatorBuilders.keySet();
	}

	public static void putOperatorBuilderType(String name, Class<? extends IOperatorBuilder> builder) {
		operatorBuilders.put(name.toUpperCase(), builder);
	}

	public static void removeOperatorBuilderType(String name) {
		operatorBuilders.remove(name.toUpperCase());
	}

	public static void putPredicateBuilder(String identifier, IPredicateBuilder builder) {
		if (predicateBuilders.containsKey(identifier)) {
			throw new IllegalArgumentException("multiple definitions of predicate builder: " + identifier);
		}

		predicateBuilders.put(identifier, builder);
	}

	public static void removePredicateBuilder(String identifier) {
		predicateBuilders.remove(identifier);
	}

	public static IPredicateBuilder getPredicateBuilder(String predicateType) {
		return predicateBuilders.get(predicateType);
	}

	/**
	 * Liefert alle Namen der registrierten PredicateBuilder. Ist kein
	 * PredicateBuilder registriert, so wird ein leeres Set zurückgegeben.
	 * 
	 * @return Menge an Namen der registrierten PredicateBuilder. Ist nie
	 *         <code>null</code>. Wenn keine PredicateBuilder registriert sind,
	 *         so wird eine leere Menge zurückgegeben.
	 *         
	 * @author Timo Michelsen
	 */
	public static Set<String> getPredicateBuilderNames() {
		return predicateBuilders.keySet();
	}
}
