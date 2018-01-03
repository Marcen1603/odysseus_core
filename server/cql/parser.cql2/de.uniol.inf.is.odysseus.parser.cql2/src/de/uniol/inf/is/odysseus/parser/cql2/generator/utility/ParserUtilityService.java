package de.uniol.inf.is.odysseus.parser.cql2.generator.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.FunctionStore;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionComponent;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.FunctionImpl;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;

public class ParserUtilityService implements IUtilityService {

	private final Logger LOGGER = LoggerFactory.getLogger(ParserUtilityService.class);
	private final FunctionStore functionStore;
	private final Pattern aggregatePattern;
	private final MEP mep;

	private ICacheService cacheService;

	@Inject
	public ParserUtilityService(ICacheService cacheService) {
		functionStore = FunctionStore.getInstance();
		aggregatePattern = AggregateFunctionBuilderRegistry.getAggregatePattern();
		mep = MEP.getInstance();

		this.cacheService = cacheService;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <K, V extends Collection<E>, E> Map<K, V> addToMap(Map<K, V> map, K key, E value) {
		V list = null;      
		
		if(map == null || key == null || value == null) {		
			StringBuilder builder = new StringBuilder();
			builder.append("[parameters were null:");
			builder.append("map=").append(map);
			builder.append(", key=").append(key);
			builder.append(", value=").append(value);
			builder.append("]");
			String message = builder.toString();
			throw new NullPointerException(message);
		}
		
		if (map.containsKey(key)) {
			list = map.get(key);
			if (!list.contains(value)) {
				list.add(value);
			}
		} else {
			list = (V) new ArrayList<E>();
			map.put(key, list);
		}
		
		return map;
	}

	@Override
	public List<String> getAttributeAliasesAsList() {
		List<String> list = new ArrayList<>();
		Map<SystemAttribute, Collection<String>> map = getAttributeAliasesAsMap();
		for (Map.Entry<SystemAttribute, Collection<String>> e : map.entrySet()) {
			list.addAll(e.getValue());
		}
		return list;
	}

	@Override
	public Map<SystemSource, Collection<String>> getSourceAliases() {
		Map<SystemSource, Collection<String>> map = new HashMap<>();
		for (SystemSource source : cacheService.getSystemSources()) {
			map.put(source, source.getAliasList());
		}
		return map;
	}

	@Override
	public String registerAttributeAliases(Attribute attribute, String attributename, String realSourcename,
			String sourcenamealias, boolean isSubQuery) {

		String sourceAlias = sourcenamealias;
		String simpleName = attribute.getAlias() != null ? attribute.getName() : attributename;
		simpleName = simpleName.contains(".") ? simpleName.split("\\.")[1] : simpleName;
		SystemSource sourceStruct = getSource(realSourcename);

		for (SystemAttribute struct : sourceStruct.getAttributeList()) {
			if (struct.getAttributename().equals(simpleName)) {
				if (sourceAlias == null) {
					sourceAlias = realSourcename;
				}
				if (attribute.getAlias() != null) {
					if (sourceStruct.isAssociatedToASource(attribute)) {
						throw new IllegalArgumentException("given alias " + attribute.getAlias().getName() + " is ambiguous");
					}

					if (!struct.hasAlias(attribute.getAlias())) {
						struct.addAlias(attribute.getAlias());
						sourceStruct.associateAttributeAliasWithSourceAlias(attribute.getAlias(), sourceAlias);
					}

					return attribute.getAlias().getName();
				} else if (attribute.getAlias() == null && getSourceAliasesAsList().contains(sourceAlias)) {
					if (!struct.hasAlias(attributename)) {
						struct.addAlias(attributename);
						sourceStruct.associateAttributeAliasWithSourceAlias(attributename, sourceAlias);
					}
				}
				
				return attributename;
			}
		}

		return null;
	}

	@Override
	public void registerSourceAlias( SimpleSource source) {
		
		LOGGER.info("step3");
	
		SystemSource sourceStruct = getSource(source.getName());
		if (sourceStruct != null) {
			LOGGER.info("step4");	
			sourceStruct.addAlias(source.getAlias());
		}
	}
	
	@Override
	public Collection<SimpleSource> getAllSubQuerySource(SimpleSelect subQuery) {
		Collection<SimpleSource> col = new ArrayList<>();
		for (Source source : subQuery.getSources()) {
			if (source instanceof SimpleSource) {
				col.add((SimpleSource) source);
			} else {
				col.addAll(getAllSubQuerySource(((NestedSource) source).getStatement().getSelect()));
			}
		}

		return col;
	}

	@Override
	public Collection<String> getAttributeNamesFromSource(String name) {
		return getSource(name).attributeList.stream().map(e -> e.getAttributename()).collect(Collectors.toList());
	}

	@Override
	public String getProjectAttribute(String name) {
	
		
		if (name.contains(getExpressionPrefix())) {
			return (String) cacheService.getExpressionCache().get(name);
		}

		if (cacheService.getExpressionCache().get(name) != null) {
			return (String) cacheService.getExpressionCache().get(name);
		}

		if (name.contains(".")) {
			if (isAttributeAlias(name)) {
				return name;
			}

			String[] split = name.split("\\.");
			String realAttributename = split[1];
			String sourcename = split[0];
			String sourcealias = sourcename;
			if (isSourceAlias(sourcename)) {
				sourcename = getSourcenameFromAlias(sourcealias);
			}
			List<String> aliases = getSource(sourcename).findByName(realAttributename).getAliases();
			if (!aliases.isEmpty()) {
				return aliases.get(aliases.size() - 1);
			}

		}

		return name;
	}

	@Override
	public boolean isAttributeAlias(String name) {
		return getAttributeAliasesAsList().contains(name);
	}

	@Override
	public boolean isSourceAlias(String name) {
		return getSourceAliasesAsList().contains(name);
	}

	@Override
	public SystemSource getSource(String name) {
		
		if (name == null) {
			throw new IllegalArgumentException("given source was null");
		}
		
		for (SystemSource source : cacheService.getSystemSources()) {
			if (source.getName().equals(name) || source.hasAlias(name)) {
				return source;
			}
		}
		
		// if no source could be found by given, interpret name 
		// as source alias and try again
		String source =  getSourceNameFromAlias(name);
		if (source != null) {
			return getSource(source);
		}
		
		throw new IllegalArgumentException("given source " + name + " is not registered");
	}

	@Override
	public SystemSource getSource( SimpleSource source) {
		return getSource(source.getName());
	}
	
	protected String getExpressionPrefix() {
		return "expression_";
	}

	@Override
	public Collection<String> getSourceNames() {
		return cacheService.getSystemSources().stream().map(e -> e.getName()).collect(Collectors.toList());
	}

	@Override
	public String getSourcenameFromAlias(String name) {
		for (Map.Entry<SystemSource, Collection<String>> source : getSourceAliases().entrySet()) {
			if (source.getValue().contains(name)) {
				return source.getKey().getName();
			}
		}

		return null;
	}

	@Override
	public List<String> getSourceAliasesAsList() {
		List<String> list = new ArrayList<>();
		for (Collection<String> l : getSourceAliases().values()) {
			list.addAll(l);
		}
		return list;
	}

	@Override
	public SystemAttribute getAttributeFromAlias(String name) {
		for(SystemSource source : cacheService.getSystemSources()) {
			SystemAttribute attribute = source.findByName(name);
			if(attribute != null) {
				return attribute;
			}
		}
		
		return null;
	}
	
	@Override
	public String getAttributenameFromAlias(String name) {
		SystemAttribute attribute = getAttributeFromAlias(name);
		if (attribute != null) {
			return attribute.getAttributename();
		}

		Collection<Pair<SelectExpression, String>> l = cacheService.getAggregationAttributeCache();
		if (l.stream().map(e -> e.getE2()).collect(Collectors.toList()).contains(name)
				|| cacheService.getExpressionCache().values().contains(name)) {
			return name;
		}

		return null;
	}

	@Override
	public Map<SystemAttribute, Collection<String>> getAttributeAliasesAsMap() {

		Map<SystemAttribute, Collection<String>> map = new HashMap<>();
		for (SystemSource source : cacheService.getSystemSources()) {
			for (SystemAttribute attribute : source.getAttributeList()) {
				if (!attribute.aliases.isEmpty()) {
					map.put(attribute, attribute.aliases);
				}
			}
		}

		LOGGER.info("getAttributeAliasesAsMap=" + map.toString());
		
		return map;
	}

	protected Collection<SelectExpression> getAggregations(Collection<SelectArgument> selectArguments,
			int expressionType) {
		Collection<SelectExpression> col = new ArrayList<>();
		for (SelectArgument selectArgument : selectArguments) {
			SelectExpression expression = selectArgument.getExpression();
			if (expression != null) {
				if (expression.getExpressions().size() == 1) {
					ExpressionComponent component = expression.getExpressions().get(0);
					EObject function = component.getValue();
					if (function instanceof Function) {
						if (expressionType == 0) {
							if (isAggregateFunctionName(((Function) function).getName())) {
								col.add(expression);
							}
						} else if (expressionType == 1) {
							String parsedMEP = "";
							if (isMEPFunctionMame(((Function) function).getName(), parsedMEP)) {
								col.add(expression);
							}
						}
					} else {
						if (expressionType == 1) {
							col.add(expression);
						}
					}
				} else {
					if (expressionType == 1) {
						col.add(expression);
					}
				}
			}
		}

		return col;
	}

//	@Override
//	@SuppressWarnings("unchecked")
//	public void addSubQuerySources( NestedSource subQuery) {
//		
//		cacheService.getQueryCache().put(
//				subQuery.getAlias().getName(), 
//				((Map<String, List<String>>) cacheService.getQueryCache().get(subQuery.getStatement().getSelect(), QueryCache.Type.QUERY_ATTRIBUTE)).keySet(), 
//				QueryCache.Type.QUERY_SUBQUERY
//		);
//		
//	}
	
	@Override
	public boolean isMEPFunctionMame(String name, String parsedMEP) {
		if (functionStore.containsSymbol(name)) {
			try {
				SDFDatatype datatype = mep.parse(parsedMEP).getReturnType();
				for (IMepFunction<?> f : functionStore.getFunctions(name))
					if (f.getReturnType().equals(datatype))
						return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

//	@Override
//	@SuppressWarnings("unchecked")
//	public Collection<SelectExpression> getQueryExpression(SimpleSelect select) {
//		return (Collection<SelectExpression>) cacheService.getQueryCache().get(select,
//				QueryCache.Type.QUERY_EXPRESSION);
//	}

//	@Override
//	@SuppressWarnings("unchecked")
//	public Collection<SelectExpression> getQueryAggregations(SimpleSelect select) {
//		return (Collection<SelectExpression>) cacheService.getQueryCache().get(select,
//				QueryCache.Type.QUERY_AGGREGATION);
//	}
//
//	@Override
//	@SuppressWarnings("unchecked")
//	public Collection<String> getQueryProjectionAttributes(SimpleSelect select) {
//		return (Collection<String>) cacheService.getQueryCache().get(select, QueryCache.Type.PROJECTION_ATTRIBUTE);
//	}

	@Override
	/** Returns all attribute names (including aggregations) of a {@link SimpleSelect}. */
	public Collection<String> getAllQueryAttributes(SimpleSelect select) {
		List<String> l = new ArrayList<>();
		
		cacheService.getQueryCache().getQueryAggregations(select).forEach(e -> l.add(e.getAlias().getName()));
		cacheService.getQueryCache().getQueryAttributes(select).stream().forEach(e -> l.add(e.name));
		
		return l;
	}

//	@Override
//	public void addQueryAttributes( SimpleSelect select,  Map<String, List<String>> attributes) {
//		cacheService.getQueryCache().put(select, attributes, QueryCache.Type.QUERY_ATTRIBUTE);
//	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Collection<String>> getSubQuerySources() {
		return (Map<String, Collection<String>>) cacheService.getQueryCache().getAll(QueryCache.Type.QUERY_SUBQUERY);
	}
	
	@Override
	public boolean isAggregateFunctionName(String name) {
		return aggregatePattern.matcher(name).toString().contains(name);
	}

	@Override
	public void clear() {
//		cacheService.flushAll();
//		cacheService.getQueryCache().flush();
//		cacheService.getExpressionCache().flush();
//		cacheService.getSourceCache().flush();
//		cacheService.getAggregationAttributeCache().flush();
	
		
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addAggregationAttribute(SelectExpression aggregation, String alias) {
		cacheService.getAggregationAttributeCache().add(new Pair(aggregation, alias));
	}
	
	@Override
	public String getSourceNameFromAlias(String sourcealias) {
		SystemSource source = getSource(sourcealias);
		if(source != null) {
			return source.getName();
		}
		
		return null;
	}
	
	public Collection<SystemAttribute> getAllAttributes() {
		Collection<SystemAttribute> col = new ArrayList<>();
		cacheService.getSystemSources().forEach(e -> col.addAll(e.getAttributeList()));
		return col;
	}
	
	//TODO method is missleading; if there are more attributes with the same name, only one is returned
	@Override
	public SystemAttribute getAttribute(String name) {
		for (SystemAttribute attr : getAllAttributes()) {
			if (attr.attributename.equals(name))
				return attr;
			else if (attr.aliases.contains(name))
				return attr;
		}
		
		return null;
	}
	
//	@Override
//	public Collection<String> getProjectionAttributes(SimpleSelect select) {
//		return cacheService.getQueryCache().getP(select, QueryCache.Type.PROJECTION_ATTRIBUTE);
//	}

	@Override
	public boolean isAggregationAttribute(String name) {
		return cacheService.getAggregationAttributeCache().stream().anyMatch(e -> e.getE2().equals(name));
	}
	
	@Override
	public void setSourcesStructs(Collection<SystemSource> sources) {
		sources.forEach(e -> cacheService.getSystemSources().add(e));
	}
	
	@Override
	public String generateKeyValueString(String ... s) {
		String str = "[";
		if (s.length == 1) {
			return str += "'" + s[0] + "']";
		}
		for (int i = 0; i < s.length - 2; i++) {
			str += "'" + s[i] + "'" + s[s.length - 1];
		}

		return str += "'" + s[s.length - 2] + "']";
	}

	@Override
	public String generateKeyValueString(List<String> l1, List<String> l2, String s) {
		String str = "";
		for (int i = 0; i < l1.size() - 1; i++) {
			str += generateKeyValueString(l1.get(i), l2.get(i), s) + ",";
		}

		return (str += generateKeyValueString(l1.get(l1.size() - 1), l2.get(l1.size() - 1), s));
	}

	@Override
	public String generateListString(String s1) {
		return "'" + s1 + "'";
	}

	@Override
	public String generateListString(Collection<String> l1) {
		
		ArrayList<String> p = new ArrayList<>(l1);
		
		if (l1 != null && !l1.isEmpty()) {
			String str = "";
			for (int i = 0; i < l1.size() - 1; i++)
				str += generateListString(p.get(i)) + ",";
			return (str += generateListString(p.get(l1.size() - 1)));
		}

		return "";
	}
	
	public String getDataTypeFrom(Attribute attribute) { return getDataTypeFrom(attribute.getName()); }

	public String getDataTypeFrom(String attribute) {
		String attributename = attribute; // getAttributename(attribute)
		String sourcename = "";
		if (attribute.contains(".")) {
			String[] splitted = attribute.split("\\.");
			if (isAttributeAlias(attributename)) {
				String sourceFromAlias = getSourceNameFromAlias(attributename);
				if (isSourceAlias(sourceFromAlias))
					sourceFromAlias = getSourceNameFromAlias(sourceFromAlias);
				attributename = getAttributenameFromAlias(attributename);
				sourcename = sourceFromAlias;
				for (SystemAttribute attr : getSource(sourcename).getAttributeList())
					if (attr.attributename.equals(attributename))
						return attr.datatype;
			}
			sourcename = splitted[0];
			attributename = splitted[1];
			if (isAttributeAlias(attributename))
				attributename = getAttributenameFromAlias(attributename);
			if (isSourceAlias(sourcename))
				sourcename = getSourceNameFromAlias(sourcename);
			try {
				for (SystemAttribute attr : getSource(sourcename).getAttributeList())
					if (attr.attributename.equals(attributename))
						return attr.getDatatype();
			} catch (IllegalArgumentException e) {
				//TODO is still used?
//				for (String attr : getSubQuerySources().get(sourcename)) {
//					if (attributeParser.parse(attr).equals(attributename)) {
//						return getAttribute(attr).getDatatype();
//
//					}
//				}
			}
		} else {
			if (isAttributeAlias(attributename)) {
				String sourceFromAlias = getSourceNameFromAlias(attributename);
				if (isSourceAlias(sourceFromAlias))
					sourceFromAlias = getSourceNameFromAlias(sourceFromAlias);
				attributename = getAttributenameFromAlias(attributename);
				if (attributename == null)
					attributename = attribute;
				for (SystemAttribute attr : getSource(sourceFromAlias).getAttributeList())
					if (attr.attributename.equals(attributename))
						return attr.getDatatype();
			}
		}
		return "Double"; // TODO change to null if you are done with debugging
	}
	
}
