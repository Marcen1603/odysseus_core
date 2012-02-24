package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate;

public interface IAggregateFunctionBuilderRegistry {
	IAggregateFunctionBuilder getBuilder(String datamodel, String functionName);
}
