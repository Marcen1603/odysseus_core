package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;

public class CacheService implements ICacheService {

	private QueryCache queryCache;
	private Collection<SourceStruct> sourceCache;
	private Collection<Pair<SelectExpression, String>> aggAttributeCache;
	private Map<String, String> expressionCache;
	private Map<String, String> attributeAliasCache;
	private OperatorCache operatorCache;
	private SelectCache selectCache;

	public CacheService() {
		queryCache = new QueryCache();
		sourceCache = new ArrayList<>();
		aggAttributeCache = new ArrayList<>();
		expressionCache = new HashMap<>();
		attributeAliasCache = new HashMap<>();
		operatorCache = new OperatorCache();
		selectCache = new SelectCache();
	}
	
	@Override
	public QueryCache getQueryCache() {
		return queryCache;
	}

	@Override
	public Collection<SourceStruct> getSourceCache() {
		return sourceCache;
	}
	
	@Override
	public Collection<Pair<SelectExpression, String>> getAggregationAttributeCache() {
		return aggAttributeCache;
	}
	
	@Override
	public Map<String, String> getExpressionCache() {
		return expressionCache;
	}
	
	@Override
	public SelectCache getSelectCache() {
		return selectCache;
	}
	
	@Override
	public void flushAll() {
		queryCache.flush();
		sourceCache.clear();
		aggAttributeCache.clear();
		expressionCache.clear();
		operatorCache.flush();
		selectCache.flush();
	}

	@Override
	public Map<String, String> getAttributeAliases() {
		return attributeAliasCache;
	}

	@Override
	public OperatorCache getOperatorCache() {
		return operatorCache;
	}


}
