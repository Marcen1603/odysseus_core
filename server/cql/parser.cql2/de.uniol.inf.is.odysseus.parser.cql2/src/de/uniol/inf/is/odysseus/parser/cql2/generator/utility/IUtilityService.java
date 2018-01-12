package de.uniol.inf.is.odysseus.parser.cql2.generator.utility;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.SubQuery;

public interface IUtilityService {

	List<String> getAttributeAliasesAsList();
	Map<SystemSource, Collection<String>> getSourceAliases();
//	Collection<QueryCacheAttributeEntry> getSelectedAttributes(SimpleSelect select);
	<K, V extends Collection<E>, E> Map<K, V> addToMap(Map<K, V> map, K key, E value);
//	void addSubQuerySources(@NotNull NestedSource subQuery);
	Map<SystemAttribute, Collection<String>> getAttributeAliasesAsMap();
	boolean isAggregateFunctionName(String name);
	boolean isMEPFunctionMame(String name, String parsedMEP);
	Map<String, Collection<String>> getSubQuerySources();
	SystemSource getSystemSource(@NotNull String name);
	SystemSource getSystemSource(@NotNull SimpleSource source); 
//	void addAggregationAttribute(SelectExpression aggregation, String alias);
//	void addQueryAttributes(@NotNull SimpleSelect select, @NotNull Map<String, List<String>> attributes);
	void registerSourceAlias(@NotNull SimpleSource source);
//	Collection<SelectExpression> getQueryExpression(SimpleSelect select);
//	Collection<SelectExpression> getQueryAggregations(SimpleSelect select);
//	Collection<String> getQueryProjectionAttributes(SimpleSelect select);
//	Map<String, Collection<String>> getQueryAttributes(SimpleSelect select);
	Collection<String> getAllQueryAttributes(SimpleSelect select);
	String getSourceNameFromAlias(String sourcealias);
	SystemAttribute getAttributeFromAlias(String attributealias);
	String getAttributenameFromAlias(String attributealias);
	List<String> getSourceAliasesAsList();
//	String getExpressionName();
//	String getAggregationName(String name);
//	String getProjectAttribute(String name);
	boolean isSourceAlias(String name);
	void clear();
	void setSourcesStructs(Collection<SystemSource> sources);
	boolean isAggregationAttribute(String name);
	boolean isAttributeAlias(String string);
	SystemAttribute getAttribute(String string);
//	Collection<String> getProjectionAttributes(SimpleSelect select);
	
	String generateListString(Collection<String> strings);
	String generateListString(String s1);
	String generateKeyValueString(List<String> l1, List<String> l2, String s);
	String generateKeyValueString(String[] s);
	String getDataTypeFrom(String attribute);
	Collection<String> getAttributeNamesFromSource(String name);
	Collection<SimpleSource> getAllSubQuerySource(SimpleSelect subQuery);
	String getSourcenameFromAlias(String name);
	Collection<String> getSourceNames();
//	String registerAttributeAliases(Attribute attribute, String attributename, String realSourcename, String sourcenamealias, boolean isSubQuery);
	QueryAttribute getQueryAttribute(Attribute attribute);
	String getDataTypeFrom(Attribute attribute);
	boolean containsAllAggregates(SimpleSelect query);
	boolean containsAllPredicates(Collection<String> predicates);
	
	Optional<String> getQueryExpressionString(String name);
	Optional<String> getQueryExpressionName(String name);
	boolean existsQueryExpressionString(String name);
	boolean existsQueryExpression(String name);
	Optional<SubQuery> isSubQuery(String sourcename);
	
}
