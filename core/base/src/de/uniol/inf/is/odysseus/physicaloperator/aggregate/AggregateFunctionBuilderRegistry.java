package de.uniol.inf.is.odysseus.physicaloperator.aggregate;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.collection.Pair;

public class AggregateFunctionBuilderRegistry implements
		IAggregateFunctionBuilderRegistry {

	private Map<Pair<String, String>, IAggregateFunctionBuilder> builders = new HashMap<Pair<String, String>, IAggregateFunctionBuilder>();
	private List<String> aggregateFunctionNames = new LinkedList<String>();
	static private Pattern aggregatePattern;

	public synchronized void registerAggregateFunctionBuilder(
			IAggregateFunctionBuilder builder) {
		String datamodel = builder.getDatamodel();
		Collection<String> functionNames = builder.getFunctionNames();
		//System.out.println("Found new AggregateBuilder " + builder);
		for (String functionName : functionNames) {
			Pair<String, String> key = new Pair<String, String>(datamodel,
					functionName);
			if (!builders.containsKey(key)) {
				builders.put(key, builder);
				aggregateFunctionNames.add(functionName);
				buildAggregatePattern();
				//System.out.println("Binding " + key);
			} else {
				throw new RuntimeException(datamodel + " and " + functionName
						+ " already registered!");
			}
		}
	}

	/**
	 * This methods builds a new Pattern for SDFExpression
	 */
	private void buildAggregatePattern() {

		StringBuffer aggregateRegexp = new StringBuffer("\\b((");
		for (String funcName : aggregateFunctionNames) {
			aggregateRegexp.append(funcName).append("|");
		}
		aggregateRegexp.deleteCharAt(aggregateRegexp.length() - 1);
		aggregateRegexp.append(")\\([^\\)]*\\))");
		aggregatePattern = Pattern.compile(aggregateRegexp.toString());
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
				functionName);
		return builders.get(key);
	}

}
