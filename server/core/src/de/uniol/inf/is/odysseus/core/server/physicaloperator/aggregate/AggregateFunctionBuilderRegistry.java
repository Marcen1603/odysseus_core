/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

@SuppressWarnings("rawtypes")
public class AggregateFunctionBuilderRegistry {

	static Logger logger = LoggerFactory
			.getLogger(AggregateFunctionBuilderRegistry.class);

	static private Map<Pair<String, String>, IAggregateFunctionBuilder> builders = new HashMap<>();
	static private List<String> aggregateFunctionNames = new LinkedList<String>();
	static private Map<String, List<String>> aggFuncNames = new HashMap<>();
	static private Pattern aggregatePattern;

	public synchronized void registerAggregateFunctionBuilder(
			IAggregateFunctionBuilder builder) {
		String datamodel = builder.getDatamodel().getName();
		
		Collection<String> functionNames = builder.getFunctionNames();
		if (!aggFuncNames.containsKey(datamodel)){
			aggFuncNames.put(datamodel, new LinkedList<String>());
		}
		logger.trace("Found new AggregateBuilder " + builder);
		for (String functionName : functionNames) {
			Pair<String, String> key = new Pair<>(datamodel,
					functionName.toUpperCase());
			if (!builders.containsKey(key)) {
				builders.put(key, builder);
				aggregateFunctionNames.add(functionName.toUpperCase());
				aggFuncNames.get(datamodel).add(functionName.toUpperCase());
				buildAggregatePattern();
				logger.trace("Binding " + key);
			} else {
				throw new RuntimeException(datamodel + " and " + functionName
						+ " already registered!");
			}
		}
	}	

	/**
	 * This methods builds a new Pattern for SDFExpression
	 */
	private synchronized void buildAggregatePattern() {

		if (aggregateFunctionNames.size() > 0) {

			StringBuffer aggregateRegexp = new StringBuffer("\\b((");
			for (String funcName : aggregateFunctionNames) {
				aggregateRegexp.append(funcName).append("|");
			}
			if (aggregateRegexp.lastIndexOf("|") == aggregateRegexp.length() - 1) {
				aggregateRegexp.deleteCharAt(aggregateRegexp.length() - 1);
			}
			aggregateRegexp.append(")\\([^\\)]*\\))");
			aggregatePattern = Pattern.compile(aggregateRegexp.toString());
		}
	}

	public static Pattern getAggregatePattern() {
		return aggregatePattern;
	} 
	
	public synchronized void removeAggregateFunctionBuilder(
			IAggregateFunctionBuilder builder) {
		String datamodel = builder.getDatamodel().getName();
		Collection<String> functionNames = builder.getFunctionNames();
		for (String functionName : functionNames) {
			Pair<String, String> key = new Pair<>(datamodel,
					functionName.toUpperCase());
			if (builders.containsKey(key)) {
				builders.remove(key);
				aggregateFunctionNames.remove(functionName.toUpperCase());
				aggFuncNames.get(datamodel).remove(functionName.toUpperCase());
				buildAggregatePattern();
			} else {
				throw new RuntimeException(datamodel + " and " + functionName
						+ " not registered!");
			}
		}
	}

	public static Collection<String> getFunctionNames(Class<? extends IStreamObject> datamodel){
		return getFunctionNames(datamodel.getName());
	}

	public static Collection<String> getFunctionNames(String datamodel) {
		if (datamodel == null) {
			return Collections.emptyList();
		}
		List<String> result = aggFuncNames.get(datamodel);
		if (result == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableCollection(result);
		}
	}

	static public IAggregateFunctionBuilder getBuilder(
			Class<? extends IStreamObject> datamodel, String functionName) {
		Pair<String, String> key = new Pair<String, String>(
				datamodel.getName(), functionName.toUpperCase());
		return builders.get(key);
	}

	
}
