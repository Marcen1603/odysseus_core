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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class AggregateFunctionBuilderRegistry implements
		IAggregateFunctionBuilderRegistry {

	private Map<Pair<String, String>, IAggregateFunctionBuilder> builders = new HashMap<Pair<String, String>, IAggregateFunctionBuilder>();
	private List<String> aggregateFunctionNames = new LinkedList<String>();
	static private Pattern aggregatePattern;

	public synchronized void registerAggregateFunctionBuilder(
			IAggregateFunctionBuilder builder) {
		String datamodel = builder.getDatamodel();
		Collection<String> functionNames = builder.getFunctionNames();
		// System.out.println("Found new AggregateBuilder " + builder);
		for (String functionName : functionNames) {
			Pair<String, String> key = new Pair<String, String>(datamodel,
					functionName.toUpperCase());
			if (!builders.containsKey(key)) {
				builders.put(key, builder);
				aggregateFunctionNames.add(functionName.toUpperCase());
				buildAggregatePattern();
				// System.out.println("Binding " + key);
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
		String datamodel = builder.getDatamodel();
		Collection<String> functionNames = builder.getFunctionNames();
		for (String functionName : functionNames) {
			Pair<String, String> key = new Pair<String, String>(datamodel,
					functionName);
			if (builders.containsKey(key)) {
				builders.remove(key);
				aggregateFunctionNames.remove(functionName);
				buildAggregatePattern();
			} else {
				throw new RuntimeException(datamodel + " and " + functionName
						+ " not registered!");
			}
		}
	}

	@Override
	public IAggregateFunctionBuilder getBuilder(String datamodel,
			String functionName) {
		Pair<String, String> key = new Pair<String, String>(datamodel,
				functionName.toUpperCase());
		return builders.get(key);
	}

}
