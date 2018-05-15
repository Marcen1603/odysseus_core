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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class OperatorBuilderFactory implements IOperatorBuilderFactory {
	private static Map<String, IOperatorBuilder> operatorBuilders = new HashMap<String, IOperatorBuilder>();
	private static Map<String, IExpressionBuilder<?,?>> expressionsBuilders = new HashMap<String, IExpressionBuilder<?,?>>();
	private static Map<String, Object> udfs = new HashMap<String, Object>();

	public static IOperatorBuilder createOperatorBuilder(String name,
			ISession caller, IDataDictionary dataDictionary, Context context, IServerExecutor executor) {
		name = name.toUpperCase();
		if (!operatorBuilders.containsKey(name)) {
			throw new IllegalArgumentException("no such operator builder: "
					+ name);
		}
		try {
			IOperatorBuilder builder = operatorBuilders.get(name).cleanCopy();
			builder.setCaller(caller);
			builder.setDataDictionary(dataDictionary);
			builder.setServerExecutor(executor);
			builder.setContext(context);
			return builder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean containsOperatorBuilderType(String name) {
		return operatorBuilders.containsKey(name.toUpperCase());
	}

	public static IOperatorBuilder getOperatorBuilderType(String name) {
		return operatorBuilders.get(name.toUpperCase());
	}

	public static Set<String> getOperatorBuilderNames() {
		return operatorBuilders.keySet();
	}

	public static void addExpressionBuilder(IExpressionBuilder<?,?> builder) {
		addExpressionBuilder(builder, builder.getName());
		if (builder.getAliasName() != null) {
			addExpressionBuilder(builder, builder.getAliasName());
		}
	}

	private static void addExpressionBuilder(IExpressionBuilder<?, ?> builder, String identifier) {
		identifier = identifier.toUpperCase();
		if (expressionsBuilders.containsKey(identifier)) {
			throw new IllegalArgumentException(
					"multiple definitions of expression builder: " + identifier);
		}
		expressionsBuilders.put(identifier, builder);
	}
	

	public static void removeExpressionBuilder(IExpressionBuilder<?, ?> builder) {
		String identifier = builder.getName().toUpperCase();
		expressionsBuilders.remove(identifier);
	}

	public static IExpressionBuilder<?,?> getExpressionBuilder(String expressionType) {
		expressionType = expressionType.toUpperCase();
		return expressionsBuilders.get(expressionType);
	}

	/**
	 * Liefert alle Namen der registrierten ExpressionBuilder. Ist kein
	 * ExpressionBuilder registriert, so wird ein leeres Set zurückgegeben.
	 *
	 * @return Menge an Namen der registrierten ExpressionBuilder. Ist nie
	 *         <code>null</code>. Wenn keine ExpressionBuilder registriert sind,
	 *         so wird eine leere Menge zurückgegeben.
	 *
	 * @author Timo Michelsen
	 */
	public static Set<String> getExpressionBuilderNames() {
		return expressionsBuilders.keySet();
	}

	public static void putUdf(
			String name,
			@SuppressWarnings("rawtypes") Class<? extends IUserDefinedFunction> classObject) {
		udfs.put(name, classObject);
	}

	@SuppressWarnings("rawtypes")
	public static IUserDefinedFunction getUDf(String name)
			throws InstantiationException, IllegalAccessException {
		Class udfClass = (Class) udfs.get(name);
		if (udfClass != null) {
			return (IUserDefinedFunction) udfClass.newInstance();
		}
		return null;
	}

    public static Collection<String> getUdfs() {
        return Collections.unmodifiableCollection(udfs.keySet());
    }

	public static void removeUdf(String nameToRemove) {
		udfs.remove(nameToRemove);
	}

	public static void addOperatorBuilder(IOperatorBuilder builder) {
		putOperatorBuilderType(builder.getName(), builder);
	}

	public static void removeOperatorBuilder(IOperatorBuilder builder) {
		removeOperatorBuilderType(builder.getName());
	}

	public static void removeOperatorBuilderByName(String name) {
	    removeOperatorBuilderType(name);
	}

	@Override
	public List<IOperatorBuilder> getOperatorBuilder() {
		return ImmutableList.copyOf(operatorBuilders.values());
	}

	private static void putOperatorBuilderType(String name,
			IOperatorBuilder builder) {
		if (operatorBuilders.containsKey(name)) {
			throw new RuntimeException(
					"multiple definitions of logicaloperator: " + name);
		}
		operatorBuilders.put(name.toUpperCase(), builder);
	}

	private static void removeOperatorBuilderType(String name) {
		operatorBuilders.remove(name.toUpperCase());
	}

}
