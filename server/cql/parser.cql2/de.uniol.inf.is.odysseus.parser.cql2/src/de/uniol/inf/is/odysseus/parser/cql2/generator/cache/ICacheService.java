package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;

public interface ICacheService {
	
	Collection<SystemSource> getSystemSources();
//	Collection<Pair<SelectExpression, String>> getAggregationAttributeCache();
	OperatorCache getOperatorCache();
//	Map<String, String> getExpressionCache();
	Map<String, String> getAttributeAliases();
	QueryCache getQueryCache();
	void flushAll();
	SelectCache getSelectCache();
	Map<String, SystemAttribute> getRenamedAttributes();
	
}
