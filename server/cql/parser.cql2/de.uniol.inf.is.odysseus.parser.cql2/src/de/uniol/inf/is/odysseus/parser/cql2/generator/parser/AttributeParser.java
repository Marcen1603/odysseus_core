package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.xtext.EcoreUtil2;

import com.google.inject.Inject;

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
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;

public class AttributeParser implements IAttributeParser {
	
	private final String EXPRESSSION_NAME_PREFIX = "expression_";
	
	private IUtilityService utilityService;
	private ICacheService cacheService;
	private int expressionCounter = 0;
	private int aggregationCounter = 0;
	
	@Inject
	public AttributeParser(IUtilityService utilityService, ICacheService cacheService) {
		this.utilityService = utilityService;
		this.cacheService = cacheService;
	}
	
	@Override
	public Collection<QueryAttribute> getSelectedAttributes(SimpleSelect select) {

		Collection<QueryAttribute> entryCol = new ArrayList<>();
		
		Map<String, Collection<String>> map = new HashMap<>();
		Collection<Attribute> attributes = new ArrayList<>();
		QueryAttribute[] attributeOrder = new QueryAttribute[select.getArguments().size()];
		String[] sourceOrder = new String[select.getArguments().size()];

		// Get all attributes from select arguments
		for (SelectArgument argument : select.getArguments()) {
			if (argument.getAttribute() != null) {
				attributes.add(argument.getAttribute());
			}
		}
		
		// Check if it is a select * query and add for each source its attributes
		if (attributes.isEmpty() && EcoreUtil2.getAllContentsOfType(select, SelectExpression.class).isEmpty()) {
			
			Collection<QueryAttribute> attributeOrderList = new ArrayList<>();
			List<String> sourceOrderList = new ArrayList<>();

			for (Source source : select.getSources()) {
				if (source instanceof SimpleSource) {
					String sourcename = ((SimpleSource) source).getName();
					for (String attribute : utilityService.getAttributeNamesFromSource(sourcename)) {
						if (source.getAlias() != null) {
							String alias = source.getAlias().getName() + "." + attribute;
							SourceStruct struct = utilityService.getSource(sourcename);
							struct.addAliasTo(attribute, alias);
							struct.associateAttributeAliasWithSourceAlias(alias, source);
							Collection<String> sss = new ArrayList<>();
							sss.add(sourcename);
							attributeOrderList.add(new QueryAttribute(alias, sss));
							map = utilityService.addToMap(map, alias, sourcename);
						} else {
							Collection<String> sss = new ArrayList<>();
							sss.add(sourcename);
							attributeOrderList.add(new QueryAttribute(sourcename + "." + attribute, sss));
						}
						map = utilityService.addToMap(map, attribute, sourcename);
						sourceOrderList.add(sourcename);
					}
				}
			}

			attributeOrder = attributeOrderList.toArray(new QueryAttribute[attributeOrderList.size()]);
			sourceOrder = sourceOrderList.toArray(new String[sourceOrderList.size()]);
		
			cacheService.getQueryCache().putProjectionSources(select, sourceOrder);
			cacheService.getQueryCache().putProjectionAttributes(select, attributeOrder);
			
			map.entrySet().stream().forEach(e -> {
				String name = e.getKey();
				Collection<String> sources = e.getValue();
				QueryAttribute entry = new QueryAttribute(name, sources);		
				entryCol.add(entry);
			});
			
			return entryCol;
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
					if (utilityService.isSourceAlias(sourcename) && utilityService.isAttributeAlias(attributename2)) {
						utilityService.registerAttributeAliases(attribute, attribute.getName(), utilityService.getSourcenameFromAlias(sourcename), sourcename, false);
					} else if (utilityService.isSourceAlias(sourcename) && !utilityService.isAttributeAlias(attributename2)) {
						SourceStruct s = utilityService.getSource(sourcename);
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
							map = utilityService.addToMap(map, name, sourcename);
						}
					}
				}
				
				map = utilityService.addToMap(map, attributename, sourcename);
				
				if(isFromSubQuery) {
					utilityService.getSource(sourcename).addAlias(sourcealias);
				}
				
				attributealias = utilityService.registerAttributeAliases(attribute, attributename, sourcename, sourcealias, isFromSubQuery);
				
			} else {
				if(list != null) {
					for (String name : list) {
						map = utilityService.addToMap(map, name, sourcename);
						utilityService.registerAttributeAliases(attribute, sourcealias + "." + name, sourcename, sourcealias, isFromSubQuery);
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
		
		cacheService.getQueryCache().putProjectionSources(select, sourceOrder);
		cacheService.getQueryCache().putProjectionAttributes(select, Arrays.asList(attributeOrder));
		
		map.entrySet().stream().forEach(e -> {
			String name = e.getKey();
			Collection<String> sources = e.getValue();
			QueryAttribute entry = new QueryAttribute(name, sources);		
			entryCol.add(entry);
		});
		
		
		return entryCol;
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
							if (!utilityService.isAttributeAlias(attribute.getName())) {
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
						
						Collection<SimpleSource> simpleSources = utilityService.getAllSubQuerySource(subQuery.getStatement().getSelect());

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
										if (!utilityService.isAttributeAlias(attribute.getName())) {
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
	
	@Override
	public QueryAttribute[] computeProjectionAttributes(QueryAttribute[] list, SimpleSelect select, Attribute attribute,
			String attributename, String attributealias, String sourcename) {

		expressionCounter = 0;
		aggregationCounter = 0;
		int i = 0;
		Object candidate = null;
		QueryAttribute[] attributeOrder = list;
		Collection<String> sources = new ArrayList<>();
		if (sourcename != null) {
			sources.add(sourcename);
		}

		if (attribute != null) {
			
			for (SelectArgument argument : select.getArguments()) {
				
				if ((candidate = argument.getAttribute()) != null) {
					
					Attribute candidateAttribute = (Attribute) candidate;
					if (candidateAttribute.getName().equals(attribute.getName())) {
						if (candidateAttribute.getAlias() != null) {
							attributeOrder[i] = new QueryAttribute(candidateAttribute.getAlias().getName(), sources);
						} else if (attributealias != null) {
							attributeOrder[i] = new QueryAttribute(attributealias, sources);
						} else {

							if (attributename.contains(".")) {
								String[] split = attributename.split("\\.");
								String name = split[1];
								String source = split[0];
								String salias = source;

								if (utilityService.isSourceAlias(source)) {
									source = utilityService.getSourcenameFromAlias(salias);
								}
								if (name.equals("*")) {
									// TODO Query with stream1.*, stream.* would be overriden!
									Collection<QueryAttribute> attributeOrderList = new ArrayList<>(attributeOrder.length);
									for (String str : utilityService.getAttributeNamesFromSource(source)) {
										attributeOrderList.add(new QueryAttribute(salias + "." + str, sources));
										i++;
									}
									attributeOrder = attributeOrderList.toArray(new QueryAttribute[attributeOrderList.size()]);
								} else {
									attributeOrder[i] = new QueryAttribute(attributename, sources);
								}

							} else {
								attributeOrder[i] = new QueryAttribute(sourcename + "." + attributename, sources);
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
	private QueryAttribute[] foo(Object candidate, QueryAttribute[] attributeOrder, SelectArgument argument, int i) {
		
		if ((candidate = argument.getExpression()) != null) {
			SelectExpression candiateExpression = (SelectExpression) candidate;
			if (candiateExpression.getAlias() != null) {
				attributeOrder[i] = new QueryAttribute(candiateExpression.getAlias().getName(), new ArrayList<>());
			} else {
				if (candiateExpression.getExpressions().size() == 1) {
					ExpressionComponent function = candiateExpression.getExpressions().get(0);
					if (function.getValue() instanceof FunctionImpl ) {
						if (utilityService.isAggregateFunctionName(((Function) function.getValue()).getName())) {
							attributeOrder[i] = new QueryAttribute(getAggregationName(((Function) function.getValue()).getName()), new ArrayList<>());
						} else {
							attributeOrder[i] = new QueryAttribute(getExpressionName(), new ArrayList<>());
						}
					}

				} else {
					attributeOrder[i] = new QueryAttribute(getExpressionName(), new ArrayList<>());
				}
			}
		}

		return attributeOrder;
	}
	
	protected Object[] parseAttribute(Attribute attribute) {
		String sourcename = null;
		String sourcealias = null;
		List<String> list = new ArrayList<>();
		boolean subQuery = false;

		if (attribute.getName().contains(".")) {
			String[] split = attribute.getName().split("\\.");
			sourcename = split[0];

			if (!utilityService.getSourceNames().contains(sourcename)) {
				sourcealias = sourcename;
				if ((sourcename = utilityService.getSourcenameFromAlias(sourcename)) == null
						&& (cacheService.getExpressionCache().get(split[0]) != null)) {
					subQuery = true;
				}

			}

			if (split[1].contains("*")) {
				sourcename = split[0];
				if (utilityService.isSourceAlias(sourcename)) {
					sourcealias = sourcename;
					sourcename = utilityService.getSourcenameFromAlias(sourcename);
				}
			}
			for (String name : utilityService.getAttributeNamesFromSource(sourcename)) {
				list.add(name);
			}

		}

		return new Object[] { attribute.getName(), sourcename, sourcealias, list, subQuery };
	}
	
	@Override
	public String getExpressionName() {
		return EXPRESSSION_NAME_PREFIX + (expressionCounter++);
	}

	@Override
	public String getAggregationName(String name) {
		return name + '_' + (aggregationCounter++);
	}

	
}
