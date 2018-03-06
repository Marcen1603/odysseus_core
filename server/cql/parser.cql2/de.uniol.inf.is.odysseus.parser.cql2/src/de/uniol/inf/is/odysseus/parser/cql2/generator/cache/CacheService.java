package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;

public class CacheService implements ICacheService {

	private QueryCache queryCache;
	private Collection<SystemSource> sourceCache;
//	private Collection<Pair<SelectExpression, String>> aggAttributeCache;
//	private Map<String, String> expressionCache;
	private Map<String, String> attributeAliasCache;
	private Map<String, SystemAttribute> renamedAttributes;
	private OperatorCache operatorCache;
	private SelectCache selectCache;

	@Inject
	public CacheService() {
		queryCache = new QueryCache();
		sourceCache = new ArrayList<>();
//		aggAttributeCache = new ArrayList<>();
//		expressionCache = new HashMap<>();
		attributeAliasCache = new HashMap<>();
		operatorCache = new OperatorCache();
		selectCache = new SelectCache();
		renamedAttributes = new HashMap<>();
	}
	
	@Override
	public QueryCache getQueryCache() {
		return queryCache;
	}

	@Override
	public Collection<SystemSource> getSystemSources() {
		return sourceCache;
	}
	
//	@Override
//	public Collection<Pair<SelectExpression, String>> getAggregationAttributeCache() {
//		return aggAttributeCache;
//	}
	
//	@Override
//	public Map<String, String> getExpressionCache() {
//		return expressionCache;
//	}
	
	@Override
	public SelectCache getSelectCache() {
		return selectCache;
	}
	
	@Override
	public void flushAll() {
		queryCache.flush();
		sourceCache.clear();
//		aggAttributeCache.clear();
//		expressionCache.clear();
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

	@Override
	public Map<String, SystemAttribute> getRenamedAttributes() {
		return renamedAttributes;
	}
}
