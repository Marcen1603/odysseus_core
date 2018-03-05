package de.uniol.inf.is.odysseus.parser.cql2.generator.utility;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.SubQuery;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper.ParsedAttribute;

public interface IUtilityService {

	List<String> getAttributeAliasesAsList();
	Map<SystemSource, Collection<String>> getSourceAliases();
//	<K, V extends Collection<E>, E> Map<K, V> addToMap(Map<K, V> map, K key, E value);
	Map<SystemAttribute, Collection<String>> getAttributeAliasesAsMap();
	boolean isAggregateFunctionName(String name);
	boolean isMEPFunctionMame(String name, String parsedMEP);
	SystemSource getSystemSource(String name);
	SystemSource getSystemSource(SimpleSource source); 
	void registerSourceAlias(SimpleSource source);
	Collection<String> getAllQueryAttributes(SimpleSelect select);
	String getSourceNameFromAlias(String sourcealias);
	SystemAttribute getAttributeFromAlias(String attributealias);
	String getAttributenameFromAlias(String attributealias);
	List<String> getSourceAliasesAsList();
	boolean isSourceAlias(String name);
	void clear();
	boolean isAggregationAttribute(String name);
	boolean isAttributeAlias(String string);
	
	String generateListString(Collection<String> strings);
	String generateListString(String s1);
	String generateKeyValueString(List<String> l1, List<String> l2, String s);
	String generateKeyValueString(String[] s);
	Collection<String> getAttributeNamesFromSource(String name);
	Collection<SimpleSource> getAllSubQuerySource(SimpleSelect subQuery);
	String getSourcenameFromAlias(String name);
	Collection<String> getSourceNames();
	Optional<QueryAttribute> getQueryAttribute(Attribute attribute);
	boolean containsAllAggregates(SimpleSelect query);
	boolean containsAllPredicates(Collection<String> predicates);
	
	Optional<String> getQueryExpressionString(String name);
	Optional<String> getQueryExpressionName(String name);
	boolean existsQueryExpressionString(String name);
	boolean existsQueryExpression(String name);
	Optional<SubQuery> isSubQuery(String sourcename);
	Optional<SubQuery> containedBySubQuery(Source source);
	Optional<SimpleSelect> getCorrespondingSelect(Attribute attribute);
	Optional<SimpleSelect> getCorrespondingSelect(QueryAttribute queryAttribute);
	String getDataTypeFrom(ParsedAttribute parsedAttribute, String sourcename);
	String getDataTypeFrom(ParsedAttribute parsedAttribute, Collection<QuerySource> attributeSources);
	
}
