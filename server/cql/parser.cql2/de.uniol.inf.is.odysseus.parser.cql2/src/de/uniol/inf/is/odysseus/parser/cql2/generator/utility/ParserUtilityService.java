package de.uniol.inf.is.odysseus.parser.cql2.generator.utility;

import java.util.ArrayList;
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
import de.uniol.inf.is.odysseus.parser.cql2.generator.AttributeStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;

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
		Map<AttributeStruct, Collection<String>> map = getAttributeAliasesAsMap();
		for (Map.Entry<AttributeStruct, Collection<String>> e : map.entrySet()) {
			list.addAll(e.getValue());
		}
		return list;
	}

	@Override
	public Map<SourceStruct, Collection<String>> getSourceAliases() {
		Map<SourceStruct, Collection<String>> map = new HashMap<>();
		for (SourceStruct source : cacheService.getSourceCache()) {
			map.put(source, source.getAliasList());
		}
		return map;
	}

	int expressionCounter = 0;
	int aggregationCounter = 0;

	@Override
	public Map<String, Collection<String>> getSelectedAttributes(SimpleSelect select, Map<String, Collection<String>> var2) {
		Map<String, Collection<String>> map = new HashMap<>(var2);
		Collection<Attribute> attributes = new ArrayList<>();
		String[] attributeOrder = new String[select.getArguments().size()];
		String[] sourceOrder = new String[select.getArguments().size()];

		// Get all attributes from select arguments
		for (SelectArgument argument : select.getArguments()) {
			if (argument.getAttribute() != null) {
				attributes.add(argument.getAttribute());
			}
		}
		
		// Check if it is a select * query and add for each source its attributes
		if (attributes.isEmpty() && EcoreUtil2.getAllContentsOfType(select, SelectExpression.class).isEmpty()) {
			List<String> attributeOrderList = new ArrayList<>();
			List<String> sourceOrderList = new ArrayList<>();

			for (Source source : select.getSources()) {
				if (source instanceof SimpleSource) {
					String sourcename = ((SimpleSource) source).getName();
					for (String attribute : getAttributeNamesFromSource(sourcename)) {
						if (source.getAlias() != null) {
							String alias = source.getAlias().getName() + "." + attribute;
							SourceStruct struct = getSource(sourcename);
							struct.addAliasTo(attribute, alias);
							struct.associateAttributeAliasWithSourceAlias(alias, source);
							attributeOrderList.add(alias);
							map = addToMap(map, alias, sourcename);
						} else {
							attributeOrderList.add(sourcename + "." + attribute);
						}
						map = addToMap(map, attribute, sourcename);
						sourceOrderList.add(sourcename);
					}
				}
			}

			attributeOrder = attributeOrderList.toArray(new String[attributeOrderList.size()]);
			sourceOrder = sourceOrderList.toArray(new String[sourceOrderList.size()]);
		
			cacheService.getQueryCache().putProjectionAttributes(select, attributeOrder);
			cacheService.getQueryCache().putProjectionSources(select, sourceOrder);

			return map;
		}

		// Get all attributes from predicates
		if (select.getPredicates() != null) {
			List<Attribute> attributeList = EcoreUtil2.getAllContentsOfType(select.getPredicates(), Attribute.class);
			for (Attribute attribute : attributeList) {
				String attributename = attribute.getName();
				if (attributename.contains(".")) {
					String[] split = attributename.split("\\.");
					String sourcename = split[0];
					String attributename2 = split[1];
					if (isSourceAlias(sourcename) && isAttributeAlias(attributename2)) {
						registerAttributeAliases(attribute, attribute.getName(), getSourcenameFromAlias(sourcename), sourcename, false);
					} else if (isSourceAlias(sourcename) && !isAttributeAlias(attributename2)) {
						SourceStruct s = getSource(sourcename);
						s.findByName(attributename2).addAlias(sourcename + "." + attributename2);
						s.associateAttributeAliasWithSourceAlias(sourcename + "." + attributename2, sourcename);
					}
				}
			}
		}

		int i = 0;
		// Iterate over all found attributes
		for (Attribute attribute : attributes) {
			// Compute source candidates for the current attribute
			List<SourceStruct> sourceCandidates = getSourceCandidates(attribute, select.getSources());
			
			// Parse the current attribute and get its informations			
			Object[] parsedAttribute = parseAttribute(attribute);
			String attributename = (String) parsedAttribute[0];
			String attributealias = attribute.getAlias() != null ? attribute.getAlias().getName() : null;
			String sourcename = (String) parsedAttribute[1];
			String sourcealias = (String) parsedAttribute[2];
			List<String> list = (List<String>) parsedAttribute[3];
			boolean isFromSubQuery = (boolean) parsedAttribute[4];
			
			if (sourceCandidates.size() > 0) {
				
				if (sourceCandidates.size() > 1 && sourcename == null) {
					throw new IllegalArgumentException("attribute " + attributename + " is ambiguous: possible sources are " + sourceCandidates.toString());
				}
				
				if (sourceCandidates.size() == 1) {
					sourcename = sourceCandidates.get(0).getName();
//					sourcealias = getSource(sourcename).getAliasList().get(0);
					if( list != null) {
						for (String name : list) {
							map = addToMap(map, name, sourcename);
						}
					}
				}
				
				map = addToMap(map, attributename, sourcename);
				
				if(isFromSubQuery) {
					getSource(sourcename).addAlias(sourcealias);
				}
				
				attributealias = registerAttributeAliases(attribute, attributename, sourcename, sourcealias, isFromSubQuery);
				
			} else {
				if(list != null) {
					for (String name : list) {
						map = addToMap(map, name, sourcename);
						registerAttributeAliases(attribute, sourcealias + "." + name, sourcename, sourcealias, isFromSubQuery);
					}
				}
			}
			
			attributeOrder = computeProjectionAttributes(
					attributeOrder, 
					select, 
					attribute, 
					attributename,
					attributealias, 
					sourcename
			);
			
			sourceOrder[i] = sourcename;
			i++;
			
		}
		
		attributeOrder = computeProjectionAttributes(attributeOrder, select, null, null, null, null);
		cacheService.getQueryCache().putProjectionAttributes(select, attributeOrder);
		cacheService.getQueryCache().putProjectionSources(select, sourceOrder);
		
		return map;
	}

	private String registerAttributeAliases(Attribute attribute, String attributename, String realSourcename,
			String sourcenamealias, boolean isSubQuery) {

		String sourceAlias = sourcenamealias;
		String simpleName = attribute.getAlias() != null ? attribute.getName() : attributename;
		simpleName = simpleName.contains(".") ? simpleName.split("\\.")[1] : simpleName;
		SourceStruct sourceStruct = getSource(realSourcename);

		for (AttributeStruct struct : sourceStruct.getAttributeList()) {
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
	
		SourceStruct sourceStruct = getSource(source.getName());
		if (sourceStruct != null) {
			LOGGER.info("step4");	
			sourceStruct.addAlias(source.getAlias());
		}
	}
	
	private String[] computeProjectionAttributes(String[] list, SimpleSelect select, Attribute attribute,
			String attributename, String attributealias, String sourcename) {

		expressionCounter = 0;
		aggregationCounter = 0;
		int i = 0;
		String[] attributeOrder = list;
		Object candidate = null;

		if (attribute != null) {
			
			for (SelectArgument argument : select.getArguments()) {
				
				if ((candidate = argument.getAttribute()) != null) {
					
					Attribute candidateAttribute = (Attribute) candidate;
					if (candidateAttribute.getName().equals(attribute.getName())) {
						if (candidateAttribute.getAlias() != null) {
							attributeOrder[i] = candidateAttribute.getAlias().getName();
						} else if (attributealias != null) {
							attributeOrder[i] = attributealias;
						} else {

							if (attributename.contains(".")) {
								String[] split = attributename.split("\\.");
								String name = split[1];
								String source = split[0];
								String salias = source;

								if (isSourceAlias(source)) {
									source = getSourcenameFromAlias(salias);
								}
								if (name.equals("*")) {
									// TODO Query with stream1.*, stream.* would be overriden!
									Collection<String> attributeOrderList = new ArrayList<>(attributeOrder.length);
									for (String str : getAttributeNamesFromSource(source)) {
										attributeOrderList.add(salias + "." + str);
										i++;
									}
									attributeOrder = attributeOrderList.toArray(new String[attributeOrderList.size()]);
								} else {
									attributeOrder[i] = attributename;
								}

							} else {
								attributeOrder[i] = sourcename + "." + attributename;
							}
						}
					}
				}
				attributeOrder = foo(candidate, attributeOrder, argument, i);
				i++;
			}
		} else {
			for (SelectArgument argument : select.getArguments()) {
				attributeOrder = foo(candidate, attributeOrder, argument, i);
				i++;
			}
		}

		expressionCounter = 0;
		aggregationCounter = 0;
		return attributeOrder;
	}

	// TODO rename
	private String[] foo(Object candidate, String[] attributeOrder, SelectArgument argument, int i) {
		
		if ((candidate = argument.getExpression()) != null) {
			SelectExpression candiateExpression = (SelectExpression) candidate;
			if (candiateExpression.getAlias() != null) {
				attributeOrder[i] = candiateExpression.getAlias().getName();
			} else {
				if (candiateExpression.getExpressions().size() == 1) {
					ExpressionComponent function = candiateExpression.getExpressions().get(0);
					if (function.getValue() instanceof FunctionImpl ) {
						if (isAggregateFunctionName(((Function) function.getValue()).getName())) {
							attributeOrder[i] = getAggregationName(((Function) function.getValue()).getName());
						} else {
							attributeOrder[i] = getExpressionName();
						}
					}

				} else {
					attributeOrder[i] = getExpressionName();
				}
			}
		}

		return attributeOrder;
	}

	private static String EXPRESSSION_NAME_PREFIX = "expression_";

	@Override
	public String getExpressionName() {
		return EXPRESSSION_NAME_PREFIX + (expressionCounter++);
	}

	@Override
	public String getAggregationName(String name) {
		return name + '_' + (aggregationCounter++);
	}

	protected List<SourceStruct> getSourceCandidates( Attribute attribute,  List<Source> sources) {
		List<SourceStruct> containedBySources = new ArrayList<>();
		for (Source source : sources) {
			if (source instanceof SimpleSource) {
				for (SourceStruct struct : cacheService.getSourceCache()) {
					if (struct.isSame((SimpleSource) source) && struct.hasAttribute(attribute)
							&& struct.isContainedBy(sources)) {
						if (!containedBySources.contains(struct)) {
							containedBySources.add(struct);
						} else if (!attribute.getName().contains(".")) {
							if (!isAttributeAlias(attribute.getName())) {
								throw new IllegalArgumentException("error occurred: name=" + attribute.getName());
							}
						}
					}
				}

			} else {
				String subQueryAlias = ((NestedSource) source).getAlias().getName();

				@SuppressWarnings("unchecked")
				Collection<NestedSource> subQueries = (Collection<NestedSource>) cacheService.getQueryCache().getAll(QueryCache.Type.QUERY_SUBQUERY);
				for (NestedSource subQuery : subQueries) {
					if (subQuery.getAlias().getName().equals(subQueryAlias)) {
						Collection<SimpleSource> simpleSources = getAllSubQuerySource(
								subQuery.getStatement().getSelect());

						for (SimpleSource simpleSource : simpleSources) {

							for (SourceStruct struct : cacheService.getSourceCache()) {
								String realName = attribute.getName();
								if (realName.contains(".")) {
									realName = realName.split("\\.")[1];
								}
								if (struct.isSame(simpleSource) && struct.hasAttribute(realName)) {
									if (!containedBySources.contains(struct)) {
										containedBySources.add(struct);
									} else if (!attribute.getName().contains(".")) {
										if (!isAttributeAlias(attribute.getName())) {
											throw new IllegalArgumentException(
													"error occurred: name=" + attribute.getName());
										}
									}
								}
							}

						}
					}
				}

			}

		}

		return containedBySources;
	}

	private Collection<SimpleSource> getAllSubQuerySource(SimpleSelect subQuery) {
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

	// TODO should this be private?
	protected Object[] parseAttribute(Attribute attribute) {
		String sourcename = null;
		String sourcealias = null;
		List<String> list = new ArrayList<>();
		boolean subQuery = false;

		if (attribute.getName().contains(".")) {
			String[] split = attribute.getName().split("\\.");
			sourcename = split[0];

			if (!getSourceNames().contains(sourcename)) {
				sourcealias = sourcename;
				if ((sourcename = getSourcenameFromAlias(sourcename)) == null
						&& (cacheService.getExpressionCache().get(split[0]) != null)) {
					subQuery = true;
				}

			}

			if (split[1].contains("*")) {
				sourcename = split[0];
				if (isSourceAlias(sourcename)) {
					sourcealias = sourcename;
					sourcename = getSourcenameFromAlias(sourcename);
				}
			}
			for (String name : getAttributeNamesFromSource(sourcename)) {
				list.add(name);
			}

		}

		return new Object[] { attribute.getName(), sourcename, sourcealias, list, subQuery };
	}

	protected List<String> getAttributeNamesFromSource(String name) {
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
	public SourceStruct getSource( String name) {
		for (SourceStruct source : cacheService.getSourceCache()) {
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
	public SourceStruct getSource( SimpleSource source) {
		return getSource(source.getName());
	}
	
	protected String getExpressionPrefix() {
		return "expression_";
	}

	protected List<String> getSourceNames() {
		return cacheService.getSourceCache().stream().map(e -> e.getName()).collect(Collectors.toList());
	}

	protected String getSourcenameFromAlias(String name) {
		for (Map.Entry<SourceStruct, Collection<String>> source : getSourceAliases().entrySet()) {
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
	public AttributeStruct getAttributeFromAlias(String name) {
		for(SourceStruct source : cacheService.getSourceCache()) {
			AttributeStruct attribute = source.findByName(name);
			if(attribute != null) {
				return attribute;
			}
		}
		
		return null;
	}
	
	@Override
	public String getAttributenameFromAlias(String name) {
		AttributeStruct attribute = getAttributeFromAlias(name);
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
	public Map<AttributeStruct, Collection<String>> getAttributeAliasesAsMap() {

		Map<AttributeStruct, Collection<String>> map = new HashMap<>();
		for (SourceStruct source : cacheService.getSourceCache()) {
			for (AttributeStruct attribute : source.getAttributeList()) {
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
		cacheService.getQueryCache().getQueryAttributes(select).values().forEach(e -> e.forEach(k -> l.add(k)));
		
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
		
		aggregationCounter = 0;
		expressionCounter = 0;
		
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addAggregationAttribute(SelectExpression aggregation, String alias) {
		cacheService.getAggregationAttributeCache().add(new Pair(aggregation, alias));
	}
	
	@Override
	public String getSourceNameFromAlias(String sourcealias) {
		SourceStruct source = getSource(sourcealias);
		if(source != null) {
			return source.getName();
		}
		
		return null;
	}
	
	public Collection<AttributeStruct> getAllAttributes() {
		Collection<AttributeStruct> col = new ArrayList<>();
		cacheService.getSourceCache().forEach(e -> col.addAll(e.getAttributeList()));
		return col;
	}
	
	//TODO method is missleading; if there are more attributes with the same name, only one is returned
	@Override
	public AttributeStruct getAttribute(String name) {
		for (AttributeStruct attr : getAllAttributes()) {
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
	public void setSourcesStructs(Collection<SourceStruct> sources) {
		sources.forEach(e -> cacheService.getSourceCache().add(e));
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
				for (AttributeStruct attr : getSource(sourcename).getAttributeList())
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
				for (AttributeStruct attr : getSource(sourcename).getAttributeList())
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
				for (AttributeStruct attr : getSource(sourceFromAlias).getAttributeList())
					if (attr.attributename.equals(attributename))
						return attr.getDatatype();
			}
		}
		return "Double"; // TODO change to null if you are done with debugging
	}
	
}
