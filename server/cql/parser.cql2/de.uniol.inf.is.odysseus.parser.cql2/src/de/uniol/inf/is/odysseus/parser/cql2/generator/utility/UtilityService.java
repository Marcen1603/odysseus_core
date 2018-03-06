package de.uniol.inf.is.odysseus.parser.cql2.generator.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.xtext.EcoreUtil2;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.FunctionStore;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.SubQuery;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper.ParsedAttribute;

public class UtilityService implements IUtilityService {
	
//	private final Logger log = LoggerFactory.getLogger(ParserUtilityService.class);
	private final FunctionStore functionStore;
	private final Pattern aggregatePattern;
	private final MEP mep;

	private ICacheService cacheService;

	@Inject
	public UtilityService(ICacheService cacheService) {
		functionStore = FunctionStore.getInstance();
		aggregatePattern = AggregateFunctionBuilderRegistry.getAggregatePattern();
		mep = MEP.getInstance();

		this.cacheService = cacheService;

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
	public void registerSourceAlias(SimpleSource source) {
		
		SystemSource sourceStruct = getSystemSource(source.getName());
		if (sourceStruct != null) {
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
		
		final SystemSource source = getSystemSource(name);
		
		if (source != null) {
			return source.attributeList
					.stream()
					.map(e -> e.getAttributename())
					.collect(Collectors.toList());
		}
		
		return new ArrayList<>();
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
	public SystemSource getSystemSource(String name) {
		
		if (name == null) {
			throw new IllegalArgumentException("given source was null");
		}
		
		if (isSubQuery(name).isPresent()) {
			throw new IllegalArgumentException("given source " + name + " relates to a sub query");
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
			return getSystemSource(source);
		}
		
		throw new IllegalArgumentException("given source " + name + " is not registered");
	}

	@Override
	public SystemSource getSystemSource(SimpleSource source) {
		return getSystemSource(source.getName());
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

		if (existsQueryExpressionString(name)) {
			return name;
		}
		
		return cacheService.getQueryCache().getAllQueryAggregations()
				.stream()
				.anyMatch(p -> p.getName().equals(name)) ? name : null;
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

		return map;
	}

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

	@Override
	/** Returns all attribute names (including aggregations) of a {@link SimpleSelect}. */
	public Collection<String> getAllQueryAttributes(SimpleSelect select) {
		List<String> l = new ArrayList<>();
		
		cacheService.getQueryCache().getQueryAggregations(select).forEach(e -> l.add(e.getName()));
		cacheService.getQueryCache().getQueryAttributes(select).stream().forEach(e -> l.add(e.getName()));
		
		return l;
	}

	@Override
	public boolean isAggregateFunctionName(String name) {
		return aggregatePattern.matcher(name).toString().contains(name);
	}

	@Override
	public void clear() { }
	
	@Override
	public String getSourceNameFromAlias(String sourcealias) {

		if (isSourceAlias(sourcealias)) {
			
			SystemSource source = getSystemSource(sourcealias);
			if (source != null) {
				return source.getName();
			}
			
		} 
		return null;
	}
	
	public Collection<SystemAttribute> getAllAttributes() {
		Collection<SystemAttribute> col = new ArrayList<>();
		cacheService.getSystemSources().forEach(e -> col.addAll(e.getAttributeList()));
		return col;
	}
	
	@Override
	public boolean isAggregationAttribute(String name) {
		return cacheService.getQueryCache().getAllQueryAggregations().stream().anyMatch(p -> p.getName().equals(name));
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
	
	public Collection<QueryAttribute> getAttributesFrom(QuerySource querySource) {
		 return cacheService.getQueryCache().getAllQueryAttributes()
				.stream()
				.filter(e -> e.sources.contains(querySource.name))
				.collect(Collectors.toList());
	}
	
	@Override
	public Optional<QueryAttribute> getQueryAttribute(Attribute attribute) {
		
		for (QueryAttribute o : cacheService.getQueryCache().getAllQueryAttributes()) {
			
			if (o instanceof QueryAggregate) {
				
				Optional<QueryAttribute> queryAttribute = ((QueryAggregate) o).parsedAggregation.relatedAttributes.stream()
					.filter(p -> {
						return p.equals(attribute) 
								|| (p.parsedAttribute.getAlias() != null && p.parsedAttribute.getAlias().equals(attribute.getName()));
					})
					.findFirst();
				
				if (queryAttribute.isPresent()) {
					return queryAttribute;
				}
				
			} else {
				if(o.equals(attribute) 
						|| ((o.parsedAttribute.getAlias() != null && o.parsedAttribute.getAlias().equals(attribute.getName())))) {
					return Optional.of(o);
				}
			}
			
		}
		
		
//		Optional<QueryAttribute> o = cacheService.getQueryCache().getAllQueryAttributes()
//			.stream()
//			.filter(p -> {
//				
//				if (p instanceof QueryAggregate) {
//					return ((QueryAggregate) p).parsedAggregation.relatedAttributes.stream()
//						.filter(u -> {
//							return u.equals(attribute) 
//									|| (u.parsedAttribute.getAlias() != null && u.parsedAttribute.getAlias().equals(attribute.getName()));
//						}).findFirst()
//						.isPresent();// return related query attribute
//					
//				} else {
//					return p.equals(attribute) 
//							|| ((p.parsedAttribute.getAlias() != null && p.parsedAttribute.getAlias().equals(attribute.getName())));
//				}
//				
//			})
//			.findFirst();
//		
//		if (o.isPresent()) {
//			return o;
//		}

		return Optional.of(new QueryAttribute(attribute, new ParsedAttribute(attribute), Collections.EMPTY_LIST, null));//TODO retrieve correct datatype
	}
	
	//TODO rename
	@Override
	public boolean containsAllAggregates(SimpleSelect query) {
		
		final Collection<String> projection = cacheService.getQueryCache().getProjectionAttributes(query)
				.stream()
				.map(e -> e.getName())
				.collect(Collectors.toList());

		return cacheService.getQueryCache().getAllQueryAggregations()
				.stream()
				.map(e -> e.getName())
				.collect(Collectors.toList())
				.containsAll(projection);
	}
	
	//TODO rename
	@Override
	public boolean containsAllPredicates(Collection<String> predicates) {
		return cacheService.getQueryCache().getAllQueryAggregations()
				.stream()
				.map(e -> e.getName())
				.collect(Collectors.toList())
				.containsAll(predicates);
	}
	
	@Override
	public Optional<String> getQueryExpressionString(String name) {
		return cacheService.getQueryCache().getAllQueryExpressions()
				.stream()
				.filter(p -> p.parsedExpression.getName().equals(name))
				.map(e -> e.parsedExpression.toString())
				.findFirst();
	}
	
	@Override 
	public Optional<String> getQueryExpressionName(String name) {
		return cacheService.getQueryCache().getAllQueryExpressions()
			.stream()
			.map(e -> e.parsedExpression.getName())
			.filter(p -> p.equals(name))
			.findFirst();
	}
	
	@Override 
	public boolean existsQueryExpressionString(String name) {
		return getQueryExpressionString(name).isPresent();
	}
	
	@Override
	public boolean existsQueryExpression(String name) {
		return getQueryExpressionName(name).isPresent();
	}

	@Override
	public Optional<SubQuery> isSubQuery(String sourcename) {
		return cacheService.getQueryCache().getAllSubQueries()
				.stream()
				.filter(p -> p.alias.equals(sourcename))
				.findFirst();
	}
	
	@Override
	public Optional<SimpleSelect> getCorrespondingSelect(Attribute attribute) {
		Optional<SimpleSelect> select = cacheService.getSelectCache().getSelects()
			.stream()
			.filter(p -> p.getArguments()
					.stream()
					.filter(i -> i.getAttribute() != null && i.getAttribute().getName().equals(attribute.getName()))
					.findFirst()
					.isPresent())
			.findFirst();
		
		if (select.isPresent()) {
			return select;
		} 
		
		return cacheService.getSelectCache().getSelects()
				.stream()
				.filter(p -> p.getPredicates() != null && EcoreUtil2.getAllContentsOfType(p.getPredicates(), Attribute.class)
						.stream()
						.filter(i -> i.getName().equals(attribute.getName()))
						.findFirst()
						.isPresent())
				.findFirst();
	}
	
	@Override
	public Optional<SimpleSelect> getCorrespondingSelect(QueryAttribute queryAttribute) {
		Optional<SimpleSelect> o =  cacheService.getSelectCache().getSelects()
			.stream()
			.filter(p -> cacheService.getQueryCache().getQueryAttributes(p).stream().anyMatch(i -> i.getName().equals(queryAttribute.getName())))
			.findFirst();
		
		if (o.isPresent()) {
			return o;
		}
		
		return cacheService.getSelectCache().getSelects()
				.stream()
				.filter(p -> p.getPredicates() != null && EcoreUtil2.getAllContentsOfType(p.getPredicates(), SimpleSelect.class)
						.stream()
						.filter(i -> cacheService.getQueryCache().getQueryAttributes(p).stream().anyMatch(k -> k.getName().equals(queryAttribute.getName())))
						.findFirst()
						.isPresent())
				.findFirst();
	}
	
	@Override
	public Optional<SubQuery> containedBySubQuery(Source source) {
		return cacheService.getQueryCache().getAllSubQueries()
				.stream()
				.filter(p -> p.select.getSources()
						.stream()
						.filter(i -> i.equals(source))
						.findFirst()
						.isPresent())
				.findFirst();
	}

//	@Override
//	public String getDataTypeFrom(Attribute attribute) { return getDataTypeFrom(attribute.getName()); }
//
//	//FIXME
//	public String getDataTypeFrom(String attribute) {
//		
//		String attributename = attribute; // getAttributename(attribute)
//		String attributealias = "";
//		String sourcename = "";
//		String sourcealias = "";
//		
//		if (attribute.contains(".")) {
//			
//			String[] splitted = attribute.split("\\.");
//			
//			if (isAttributeAlias(attributename)) {
//				////
////				String sourceFromAlias = getSourceNameFromAlias(attributename);
////				
////				if (isSourceAlias(sourceFromAlias)) {
////					sourceFromAlias = getSourceNameFromAlias(sourceFromAlias);
////				}
////				
////				attributename = getAttributenameFromAlias(attributename);
////				sourcename = sourceFromAlias;
////				
////				for (SystemAttribute attr : getSource(sourcename).getAttributeList()) {
////					if (attr.attributename.equals(attributename))
////						return attr.datatype;
////				}
////				
//			}
//			
//			sourcename = splitted[0];
//			attributename = splitted[1];
//			
//			if (isAttributeAlias(attributename)) {
//				attributealias = attributename;
//				attributename = getAttributenameFromAlias(attributename);
//			}
//			
//			if (isSourceAlias(sourcename)) {
//				sourcealias = sourcename;
//				sourcename = getSourceNameFromAlias(sourcename);
//			}
//			
////			try {
//			
//			if (isSubQuery(sourcename).isPresent()) {
//				
//				final String aname = attributename;
//				final String aalias = attributealias;
//				
//				final String asourcename = sourcename;
//				final String asourcealias = sourcealias;
//				
//				Optional<SubQuery> subQuery = cacheService.getQueryCache().getSubQuery(sourcename);
//				
//				if (subQuery.isPresent()) {
//					
//					for (QuerySource e : cacheService.getQueryCache().getQuerySources(subQuery.get().select)) {
//						
//						Optional<String> o = getAttributesFrom(e).stream().map(k -> {
//							
//							final String kname = k.getName().split("\\.")[1];
//							final String ksource = k.getName().split("\\.")[0];
//							String ksourcealias = "";
//							
//							if(isSourceAlias(ksource)) {
//								ksourcealias = getSourceNameFromAlias(ksource);
//							}
//							
//							if (kname.equals(aname) && (ksource.equals(e.name) || ksourcealias.equals(e.name))) {
//								for (SystemAttribute attr : getSystemSource(e.name).getAttributeList()) {
//									if (attr.attributename.equals(aname)) {
//										 return attr.getDatatype();
//									}
//								}
//							}
//							
//							return "Double";//TODO this should always return the real datatype
//						}).findFirst();
//						
//						if (o.isPresent()) {
//							return o.get();
//						}
//						
//					}
//				}
//			} else {
//			
//				for (SystemAttribute attr : getSystemSource(sourcename).getAttributeList()) {
//					if (attr.attributename.equals(attributename)) {
//						return attr.getDatatype();
//					}
//				}
//			}
//				
////			} catch (IllegalArgumentException e) {
////				//TODO is still used?
//////				for (String attr : getSubQuerySources().get(sourcename)) {
//////					if (attributeParser.parse(attr).equals(attributename)) {
//////						return getAttribute(attr).getDatatype();
//////
//////					}
//////				}
////			}
//				
//				
//		} else {
//			
//			if (isAttributeAlias(attributename)) {
//				String sourceFromAlias = getSourceNameFromAlias(attributename);
//				if (isSourceAlias(sourceFromAlias))
//					sourceFromAlias = getSourceNameFromAlias(sourceFromAlias);
//				attributename = getAttributenameFromAlias(attributename);
//				if (attributename == null)
//					attributename = attribute;
//				if (sourceFromAlias != null) {
//					for (SystemAttribute attr : getSystemSource(sourceFromAlias).getAttributeList())
//						if (attr.attributename.equals(attributename))
//							return attr.getDatatype();
//				}
//			}
//		}
//		
//		return null;//"Double"; // TODO change to null if you are done with debugging
//	}
	
	@Override
	public String getDataTypeFrom(ParsedAttribute parsedAttribute, String sourcename) {
		
		for (String source : SystemSource.getQuerySources()) {
			for(SystemAttribute attribute : getSystemSource(source).getAttributeList()) {
				boolean namesAreEqual = parsedAttribute.matches(attribute);
				if (namesAreEqual || parsedAttribute.getName().equals(sourcename)) {
					return attribute.getDatatype();
				}
			}
		}
		
		return null;
	}

	@Override
	public String getDataTypeFrom(ParsedAttribute parsedAttribute, Collection<QuerySource> attributeSources) {
		
		for (QuerySource source : attributeSources) {
			String dataType = getDataTypeFrom(parsedAttribute, source.name);
			if (dataType != null) {
				return dataType;
			}
		}
		
		return null;
	}
	
}
