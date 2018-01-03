package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.eclipse.xtext.EcoreUtil2;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorInputSchemaLogicalGraphVisitor;
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
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
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
	private int index = 0;
	private String[] sourceOrder;
	
	@Inject
	public AttributeParser(IUtilityService utilityService, ICacheService cacheService) {
		this.utilityService = utilityService;
		this.cacheService = cacheService;
	}
	
	@Override
	public void parsePredicateAttributes(SimpleSelect select) {
		// Get attributes from predicate
		if (select.getPredicates() != null) {
			EcoreUtil2.getAllContentsOfType(select.getPredicates(), Attribute.class).stream().forEach(e -> {

				String attributename = e.getName();
				if (attributename.contains(".")) {
					String[] split = attributename.split("\\.");
					String sourcename = split[0];
					String attributename2 = split[1];
					if (utilityService.isSourceAlias(sourcename) && utilityService.isAttributeAlias(attributename2)) {
						utilityService.registerAttributeAliases(e, e.getName(),
								utilityService.getSourcenameFromAlias(sourcename), sourcename, false);
					} else if (utilityService.isSourceAlias(sourcename)
							&& !utilityService.isAttributeAlias(attributename2)) {
						SystemSource s = utilityService.getSource(sourcename);
						s.findByName(attributename2).addAlias(sourcename + "." + attributename2);
						s.associateAttributeAliasWithSourceAlias(sourcename + "." + attributename2, sourcename);
					}
				}

			});
		}
	}
	
	@Override
	public Collection<QueryAttribute> getSelectedAttributes(SimpleSelect select) {

		QueryAttributeOrder attributeOrder = new QueryAttributeOrder(select.getArguments().size());
		Collection<QueryAttribute> entryCol = new ArrayList<>();
		sourceOrder = new String[select.getArguments().size()];
		index = 0;
	
		select.getArguments().stream()
			.map(e -> e.getAttribute())
			.filter(p -> p != null)
			.forEach(e -> {

					String attributealias = null;
					QueryAttribute queryAttribute = null;
					Collection<SystemSource> candidates = getSourceCandidates(e, select.getSources());
					ParsedAttribute parsed = new ParsedAttribute(e);

					if (candidates.size() > 0) {

						
						if (candidates.size() == 1) {
							if(parsed.containedBySource != null) {
								SystemSource source = candidates.stream().findFirst().get();
								queryAttribute = new QueryAttribute(e, source);
								parsed.sourcename = source.name;
							}
						} else if (candidates.size() > 1) {
							if (parsed.sourcename == null) {
								throw new IllegalArgumentException("attribute " + parsed.attributename + " is ambiguous: possible sources are " + candidates.toString());
							}
						}
//						
						if(parsed.subQuery) {
							utilityService.getSource(parsed.sourcename).addAlias(parsed.sourcealias);
						}
//						
						attributealias = utilityService.registerAttributeAliases(
								e, 
								parsed.attributename, 
								parsed.sourcename, 
								parsed.sourcealias, 
								parsed.subQuery
						);

						entryCol.add(queryAttribute);
						
					} else {
						if (parsed.containedBySource != null) {
							for (String name : parsed.containedBySource) {
								
								queryAttribute = new QueryAttribute(e, utilityService.getSource(parsed.sourcename));
								entryCol.add(queryAttribute);
								
								utilityService.registerAttributeAliases(
										e, 
										parsed.sourcename + "." + name, 
										parsed.sourcename, 
										parsed.sourcealias, 
										parsed.subQuery
								);
							}
						}
					}
					
					attributeOrder.array = computeProjectionAttributes(
						attributeOrder.array, 
						select, 
						e, 
						parsed.attributename,
						attributealias, 
						parsed.sourcename
					);
					
					sourceOrder[index] = parsed.sourcename;
					index++;					
				});
		
		
			// Check if it is a select * query and add for each source its attributes
			if (entryCol.isEmpty() && EcoreUtil2.getAllContentsOfType(select, SelectExpression.class).isEmpty()) {
				
				index = 0;
				sourceOrder = new String[select.getSources().size()];
				Collection<QueryAttribute> attributeOrderList = new ArrayList<>();
				select.getSources().stream().filter(p -> p instanceof SimpleSource).forEach(e -> {
					
					String sourcename = ((SimpleSource) e).getName();
					SystemSource struct = utilityService.getSource(sourcename);
					for (String attribute : struct.attributeList.stream().map(k -> k.getAttributename()).collect(Collectors.toList())) {
						if (e.getAlias() != null) {
							String alias = e.getAlias().getName() + "." + attribute;
							struct.addAliasTo(attribute, alias);
							struct.associateAttributeAliasWithSourceAlias(alias, e);
							attributeOrderList.add(new QueryAttribute(alias, struct));
						} else {
							attributeOrderList.add(new QueryAttribute(sourcename + "." + attribute, struct));
						}
					}
					sourceOrder[index] = sourcename;
					index++;
				});
			}
	
			// but why?
			attributeOrder.array = computeProjectionAttributes(
					attributeOrder.array, 
					select, 
					null, 
					null, 
					null, 
					null
			);
			
			cacheService.getQueryCache().putProjectionSources(select, sourceOrder);
			cacheService.getQueryCache().putProjectionAttributes(select, Arrays.asList(attributeOrder.array));
		
		return entryCol;
	}
	
	protected List<SystemSource> getSourceCandidates( Attribute attribute,  List<Source> sources) {
		List<SystemSource> containedBySources = new ArrayList<>();
		for (Source source : sources) {
			if (source instanceof SimpleSource) {
				for (SystemSource struct : cacheService.getSourceCache()) {
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

							for (SystemSource struct : cacheService.getSourceCache()) {
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
	
	private class QueryAttributeOrder {
		
		QueryAttribute[] array;
		
		public QueryAttributeOrder(int size) {
			array = new QueryAttribute[size];
		}
		
		public QueryAttributeOrder(QueryAttribute[] array) {
			array = Arrays.copyOf(array, array.length);
		}
	}
	
	private class ParsedAttribute {
		
		String attributename = null;
		String sourcename = null;
		String sourcealias = null;
		Collection<String> containedBySource = new ArrayList<>();
		boolean subQuery = false;
		
		@SuppressWarnings("unused")
		public ParsedAttribute(Attribute e) {
			
			attributename = e.getName();
			
			if (e.getName().contains(".")) {
				String[] split = e.getName().split("\\.");
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
					containedBySource.add(name);
				}

			}
			
		}
		
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
