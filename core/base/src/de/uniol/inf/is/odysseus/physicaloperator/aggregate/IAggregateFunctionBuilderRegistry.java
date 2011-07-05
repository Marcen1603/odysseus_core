package de.uniol.inf.is.odysseus.physicaloperator.aggregate;

public interface IAggregateFunctionBuilderRegistry {
	IAggregateFunctionBuilder getBuilder(String datamodel, String functionName);
}
