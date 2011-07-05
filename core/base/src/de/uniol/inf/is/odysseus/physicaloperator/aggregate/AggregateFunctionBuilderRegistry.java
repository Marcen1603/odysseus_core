package de.uniol.inf.is.odysseus.physicaloperator.aggregate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.collection.Pair;

public class AggregateFunctionBuilderRegistry implements
		IAggregateFunctionBuilderRegistry {

	private Map<Pair<String, String>, IAggregateFunctionBuilder> builders = new HashMap<Pair<String, String>, IAggregateFunctionBuilder>();

	public void registerAggregateFunctionBuilder(
			IAggregateFunctionBuilder builder) {
		String datamodel = builder.getDatamodel();
		Collection<String> functionNames = builder.getFunctionNames();
		System.out.println("Found new AggregateBuilder "+builder);
		for (String functionName : functionNames) {
			Pair<String, String> key = new Pair<String, String>(datamodel,
					functionName);
			if (!builders.containsKey(key)) {
				builders.put(key, builder);
				System.out.println("Binding "+key);
			}else{
				throw new RuntimeException(datamodel+" and "+functionName+" already registered!");
			}
		}
	}

	public void removeAggregateFunctionBuilder(
			IAggregateFunctionBuilder builder) {
		String datamodel = builder.getDatamodel();
		Collection<String> functionNames = builder.getFunctionNames();
		for (String functionName : functionNames) {
			Pair<String, String> key = new Pair<String, String>(datamodel,
					functionName);
			if (builders.containsKey(key)) {
				builders.remove(key);
			}else{
				throw new RuntimeException(datamodel+" and "+functionName+" not registered!");
			}
		}
	}
	
	@Override
	public IAggregateFunctionBuilder getBuilder(String datamodel, String functionName){
		Pair<String, String> key = new Pair<String, String>(datamodel,
				functionName);
		return builders.get(key);
	}

}
