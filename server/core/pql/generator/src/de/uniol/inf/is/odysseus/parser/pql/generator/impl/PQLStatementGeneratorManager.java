package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLStatementGenerator;

public class PQLStatementGeneratorManager {

	private static final IPQLStatementGenerator<ILogicalOperator> STANDARD_GENERATOR = new StandardPQLStatementGenerator<ILogicalOperator>();

	private static PQLStatementGeneratorManager instance;
	
	private final Map<Class<? extends ILogicalOperator>, IPQLStatementGenerator<ILogicalOperator>> generators = Maps.newHashMap();
	
	// called by OSGi-DS
	public final void activate() {
		instance = this;
	}
	
	// called by OSGi-DS
	public final void deactivate() {
		instance = null;
	}
	
	public static PQLStatementGeneratorManager getInstance() {
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void bindPQLStatementGenerator( IPQLStatementGenerator<?> generator ) {
		generators.put(generator.getOperatorClass(), (IPQLStatementGenerator<ILogicalOperator>) generator);
	}
	
	public void unbindPQLStatementGenerator( IPQLStatementGenerator<?> generator ) {
		generators.remove(generator.getOperatorClass());
	}
	
	public IPQLStatementGenerator<ILogicalOperator> getPQLStatementGenerator( ILogicalOperator operator ) {
		Preconditions.checkNotNull(operator, "Operator to get pql-generator must not be null!");
		
		IPQLStatementGenerator<ILogicalOperator> gen = generators.get(operator.getClass());
		if( gen == null ) {
			return STANDARD_GENERATOR;
		}
		return gen;
	}
}
