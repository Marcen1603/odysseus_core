package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.xtext.EcoreUtil2;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryExpression;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.SubQuery;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;

public class AttributeParser implements IAttributeParser {
	
	private final String EXPRESSSION_NAME_PREFIX = "expression_";
	
	private IUtilityService utilityService;
	private ICacheService cacheService;
	private IExpressionParser expressionParser;
	private int expressionCounter = 0;
	private int aggregationCounter = 0;
	private int index = 0;
	private int index2 = 0;
	private QuerySourceOrder sourceOrder;
	private QueryAttributeOrder attributeOrder;
	private Collection<QueryAggregate> aggregates;
	private Collection<QueryExpression> expressions;
	
	@Inject
	public AttributeParser(IUtilityService utilityService, ICacheService cacheService, IExpressionParser expressionParser) {
		this.utilityService = utilityService;
		this.cacheService = cacheService;
		this.expressionParser = expressionParser;
		this.aggregates = new ArrayList<>();
		this.expressions = new ArrayList<>();
	}
	
	@Override
	public void registerAttributesFromPredicate(SimpleSelect select) {
		// Get attributes from predicate
		if (select.getPredicates() != null) {
			
			EcoreUtil2.getAllContentsOfType(select.getPredicates(), Attribute.class).stream().forEach(e -> {

				final String attributename = e.getName();
				
				if (attributename.contains(".")) {
					String[] split = attributename.split("\\.");
					String sourcename = split[0];
					String attributename2 = split[1];
					if (utilityService.isSourceAlias(sourcename) && utilityService.isAttributeAlias(attributename2)) {
						registerAttributeAliases(new ParsedAttribute(e));
					} else if (utilityService.isSourceAlias(sourcename) && !utilityService.isAttributeAlias(attributename2)) {
						SystemSource s = utilityService.getSystemSource(sourcename);
						s.findByName(attributename2).addAlias(sourcename + "." + attributename2);
						s.associateAttributeAliasWithSourceAlias(sourcename + "." + attributename2, sourcename);
					}
				}

			});

		}
	}
	
	@Override
	public Collection<QueryAttribute> getSelectedAttributes(SimpleSelect select) {

		attributeOrder = new QueryAttributeOrder(select.getArguments().size());
		sourceOrder = new QuerySourceOrder(select.getArguments().size());
		Collection<QueryAttribute> entryCol = new ArrayList<>();
		index = 0;
	
		select.getArguments().stream()
			.map(e -> e.getAttribute())
			.filter(p -> p != null)
			.forEach(e -> {

					String attributealias = null;
					QueryAttribute queryAttribute = null;
					Collection<SystemSource> candidates = computeSourceCandidates(e, select.getSources());
					ParsedAttribute parsed = new ParsedAttribute(e);

					if (candidates.size() > 0) {

						
						if (candidates.size() == 1) {
//							if(parsed.containedBySubQuery != null) {
								SystemSource source = candidates.stream().findFirst().get();
								queryAttribute = new QueryAttribute(e, QueryAttribute.Type.STANDARD, utilityService.getDataTypeFrom(e), new QuerySource(source.name));
								parsed.sourcename = source.name;
//							}
						} else if (candidates.size() > 1) {
							if (parsed.sourcename == null) {
								throw new IllegalArgumentException("attribute " + parsed.name + " is ambiguous: possible sources are " + candidates.toString());
							}
						}
						
//TODO is this still needed?
//						if(parsed.subQuery) {
//							utilityService.getSystemSource(parsed.sourcename).addAlias(parsed.sourcealias);
//						}

						attributealias = registerAttributeAliases(parsed);

						entryCol.add(queryAttribute);
						
					} else {
//TODO does it make any sense?
//						if (parsed.containedBySubQuery != null) {
//							for (String name : parsed.containedBySubQuery) {
//								
//								queryAttribute = new QueryAttribute(e, QueryAttribute.Type.STANDARD, utilityService.getDataTypeFrom(e), new QuerySource(parsed.sourcename));
//								entryCol.add(queryAttribute);
//								
//								registerAttributeAliases(parsed);
//								
//							}
//						}
					}
					
					attributeOrder.array = computeProjectionAttributes(
						attributeOrder.array, 
						select, 
						e, 
						parsed.name,
						attributealias, 
						parsed.sourcename
					);
					
					sourceOrder.array[index] = new QuerySource(parsed.sourcename);
					index++;					
				});
		
		
			// Check if it is a select * query and add for each source its attributes
			if (entryCol.isEmpty() && EcoreUtil2.getAllContentsOfType(select, SelectExpression.class).isEmpty()) {
				
				index = 0;
				List<QueryAttribute> list = new ArrayList<>();
				sourceOrder = new QuerySourceOrder(select.getSources().size());
				
				select.getSources().stream().filter(p -> p instanceof SimpleSource).forEach(e -> {
					
					String sourcename = ((SimpleSource) e).getName();
					SystemSource struct = utilityService.getSystemSource(sourcename);
					
					struct.attributeList.stream().forEach(k -> {
						
						final String name = k.getAttributename();
						final String data = k.getDatatype();	
						String attributename = null;
						
						if (e.getAlias() != null) {
							
							final String alias = e.getAlias().getName() + "." + name;
							struct.addAliasTo(name, alias);
							struct.associateAttributeAliasWithSourceAlias(alias, e);
							attributename = alias;
							
						} else {
							attributename = sourcename + "." + name;
						}
						
						list.add(new QueryAttribute(
								attributename, 
								QueryAttribute.Type.SELECTALL,
								data, 
								new QuerySource(struct.name))
								);
						
					});
					sourceOrder.array[index] = new QuerySource(sourcename);
					index++;
					
				});
				attributeOrder = new QueryAttributeOrder(list);
				
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
			
		return entryCol;
	}
	
	@Override
	public QueryAttributeOrder getAttributeOrder() {
		return attributeOrder;
	}
	
	@Override
	public QuerySourceOrder getSourceOrder() {
		return sourceOrder;
	}
	
	@Override
	public Collection<QueryAggregate> getAggregates() {
		return aggregates;
	}
	
	@Override
	public Collection<QueryExpression> getExpressions() {
		return expressions;
	}

	private Collection<SystemSource> computeSourceCandidates(Attribute attribute,  Collection<Source> sources) {
		
		Collection<SystemSource> containedBySources = new ArrayList<>();
		
		sources.stream().forEach(s -> {
			
			if (s instanceof SimpleSource) {

				cacheService.getSystemSources().stream()
					.filter(p -> p.isSame((SimpleSource) s) 
							&& p.hasAttribute(attribute)
							&& p.isContainedBy(sources) 
							&& !containedBySources.contains(p))
					.forEach(e -> containedBySources.add(e));
				
			} else {
				
				String subQueryAlias = ((NestedSource) s).getAlias().getName();
				cacheService.getQueryCache().getAllSubQueries().stream()
					.filter(p -> p.source.getAlias().getName().equals(subQueryAlias))
					.forEach(q -> {
						
						utilityService.getAllSubQuerySource(q.source.getStatement().getSelect()).stream().forEach(sub -> {
							cacheService.getSystemSources().stream().forEach(sys -> {
								
								String realName = attribute.getName();
								if (realName.contains(".")) {
									realName = realName.split("\\.")[1];
								}
								if (sys.isSame(sub) && sys.hasAttribute(realName)) {
									if (!containedBySources.contains(sys)) {
										containedBySources.add(sys);
									} else if (!attribute.getName().contains(".")) {
										if (!utilityService.isAttributeAlias(attribute.getName())) {
											throw new IllegalArgumentException("error occurred: name=" + attribute.getName());
										}
									}
								}		
								
							});
						});
						
					});
				
			}
		});
		
		return containedBySources;
	}
	
	private QueryAttribute[] computeProjectionAttributes(QueryAttribute[] list, SimpleSelect select, Attribute attribute, String attributename, String attributealias, String sourcename) {

		expressionCounter = 0;
		aggregationCounter = 0;
		index2 = 0;
		attributeOrder = new QueryAttributeOrder(list);
		Collection<QuerySource> sources = new ArrayList<>();
		if (sourcename != null) {
			sources.add(new QuerySource(sourcename));
		}

		if (attribute != null) {
			
			select.getArguments().stream().forEach(argument -> {

				Object candidate = null;
				
				if ((candidate = argument.getAttribute()) != null) {
					
					Attribute candidateAttribute = (Attribute) candidate;
					if (candidateAttribute.getName().equals(attribute.getName())) {
						if (candidateAttribute.getAlias() != null) {
							
							attributeOrder.array[index2] = new QueryAttribute(
									candidateAttribute.getAlias().getName(), 
									QueryAttribute.Type.STANDARD, 
									utilityService.getDataTypeFrom(candidateAttribute), 
									sources
								);
							
						} else if (attributealias != null) {
							attributeOrder.array[index2] = new QueryAttribute(
									attributealias, 
									QueryAttribute.Type.STANDARD,
									utilityService.getDataTypeFrom(candidateAttribute),
									sources
								);
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
									Collection<QueryAttribute> attributeOrderList = new ArrayList<>(attributeOrder.array.length);
									for (String str : utilityService.getAttributeNamesFromSource(source)) {
										attributeOrderList.add(new QueryAttribute(
												salias + "." + str, 
												QueryAttribute.Type.STANDARD, 
												utilityService.getDataTypeFrom(candidateAttribute), 
												sources
											)
										);
										index2++;
									}
									attributeOrder.array = attributeOrderList.toArray(new QueryAttribute[attributeOrderList.size()]);
								} else {
									attributeOrder.array[index2] = new QueryAttribute(
											attributename, 
											QueryAttribute.Type.STANDARD,
											utilityService.getDataTypeFrom(candidateAttribute), 
											sources
										);
								}

							} else {
								attributeOrder.array[index2] = new QueryAttribute(
										sourcename + "." + attributename, 
										QueryAttribute.Type.STANDARD,
										utilityService.getDataTypeFrom(candidateAttribute), 
										sources
									);
							}
						}
					}
				}
				attributeOrder.array = parseExpression(attributeOrder.array, argument, index2);
				index2++;
			});
			
		} else {
			for (SelectArgument argument : select.getArguments()) {
				attributeOrder.array = parseExpression(attributeOrder.array, argument, index2);
				index2++;
			}
		}

		expressionCounter = 0;
		aggregationCounter = 0;
		return attributeOrder.array;
	}

	private QueryAttribute[] parseExpression(QueryAttribute[] attributeOrder, SelectArgument argument, int i) {

		SelectExpression candidate = argument.getExpression();
		
		if (candidate != null) {
			
			final List<Function> functions = EcoreUtil2.getAllContentsOfType(candidate, Function.class);
			
			if (candidate.getAlias() != null) {
				
				final String name = candidate.getAlias().getName();
				
				if (functions.size() == 1) {
					
					if (functions.get(0) instanceof Function) {
					
						final String functionName = ((Function) functions.get(0)).getName();
						
						if (utilityService.isAggregateFunctionName(functionName)) {
							
							Optional<QueryAggregate> aggregate = createQueryAggregate(name, utilityService.getDataTypeFrom(name), candidate);
							
							if (aggregate.isPresent()) {
								attributeOrder[i] = aggregate.get();
								aggregates.add(aggregate.get());
							}
							
						} else {
							attributeOrder = createQueryExpression(attributeOrder, candidate, name, i);
						}
						
					} else {
						attributeOrder = createQueryExpression(attributeOrder, candidate, name, i);
					}
				} else {
					attributeOrder = createQueryExpression(attributeOrder, candidate, name, i);					
				}
			} else {

				if (functions.size() == 1) {

					if (functions.get(0) instanceof Function) {
						
						final String functionName = ((Function) functions.get(0)).getName();

						if (utilityService.isAggregateFunctionName(functionName)) {
	
							final String name = getAggregationName(((Function) functions.get(0).getValue()).getName());
							Optional<QueryAggregate> aggregate = createQueryAggregate(name, utilityService.getDataTypeFrom(name), candidate);
	
							if (aggregate.isPresent()) {
								attributeOrder[i] = aggregate.get();
								aggregates.add(aggregate.get());
							}
	
						} else {
							attributeOrder = createQueryExpression(attributeOrder, candidate, getExpressionName(), i);
						}
					} else {
						attributeOrder = createQueryExpression(attributeOrder, candidate, getExpressionName(), i);
					}

				} else {
					attributeOrder = createQueryExpression(attributeOrder, candidate, getExpressionName(), i);
				}

			}

		}
		
		return attributeOrder;
	}
	
	private String registerAttributeAliases(ParsedAttribute parsed) {

		String sourceAlias = parsed.sourcealias;
		String simpleName = parsed.name;//attribute.getAlias() != null ? attribute.getName() : attributename;
//		simpleName = simpleName.contains(".") ? simpleName.split("\\.")[1] : simpleName;
		SystemSource sourceStruct = utilityService.getSystemSource(parsed.sourcename);

		for (SystemAttribute sysAttribute : sourceStruct.getAttributeList()) {
			
			if (parsed.matches(sysAttribute)) {//parsed.name.equals(systemAttributename)|| (parsed.prefix != null && systemAttributename.equals(parsed.prefix))) {
				if (sourceAlias == null) {
					sourceAlias = parsed.sourcename;
				}
				if (parsed.alias != null) {
					
					if (sourceStruct.isAssociatedToASource(parsed.alias)) {
						throw new IllegalArgumentException("given alias " + parsed.alias + " is ambiguous");
					}

					if (!sysAttribute.hasAlias(parsed.alias)) {
						sysAttribute.addAlias(parsed.alias);
						sourceStruct.associateAttributeAliasWithSourceAlias(parsed.alias, sourceAlias);
					}

					return parsed.alias;
					
				} else if (parsed.alias == null && utilityService.getSourceAliasesAsList().contains(sourceAlias)) {
					if (!sysAttribute.hasAlias(parsed.name)) {
						sysAttribute.addAlias(parsed.name);
						sourceStruct.associateAttributeAliasWithSourceAlias(parsed.name, sourceAlias);
					}
				}
				
				return parsed.name;
			}
		}

		return parsed.alias;
	}

	private Optional<QueryAggregate> createQueryAggregate(String name, String datatype, SelectExpression expression) {
		
		final QueryAggregate aggregate = new QueryAggregate(
				name,
				QueryAttribute.Type.AGGREGATE, 
				datatype, 
				new ArrayList<>(),
				expression
			);
		
		if (aggregates.stream().anyMatch(p -> p.name.equals(aggregate.name))) {
			return Optional.empty();
		}
		
		return Optional.of(aggregate);
	}

	private QueryAttribute[] createQueryExpression(QueryAttribute[] attributeOrder, SelectExpression candidate, String name, int i) {
		
		if (!expressions.stream().map(e -> e.name).anyMatch(p -> p.equals(name))) {
			
			final QueryExpression queryExpression = new QueryExpression(
					name,
					QueryAttribute.Type.EXPRESSION, 
					utilityService.getDataTypeFrom(name), 
					new ArrayList<>(),
					candidate
				);
		
			queryExpression.alias = "['" + expressionParser.parse(candidate).toString() +"','" + name + "']";
		
			expressions.add(queryExpression);
			attributeOrder[i] = queryExpression;
		}
		
		return attributeOrder;
	}
	
	@Override
	public String getExpressionName() {
		return EXPRESSSION_NAME_PREFIX + (expressionCounter++);
	}

	@Override
	public String getAggregationName(String name) {
		return name + '_' + (aggregationCounter++);
	}
	
	@Override
	public void clear() {
		aggregates.clear();
		expressions.clear();
		index = 0;
		index2 = 0;
		expressionCounter = 0;
		aggregationCounter = 0;
		attributeOrder = new QueryAttributeOrder(1);
		sourceOrder = new QuerySourceOrder(1);
	}
	
	public static class QueryAttributeOrder {
		
		public QueryAttribute[] array;
		
		public QueryAttributeOrder(int size) {
			array = new QueryAttribute[size];
		}
		
		public QueryAttributeOrder(QueryAttribute[] array) {
			this.array = new QueryAttribute[array.length];
			// deep copy
			for(int i = 0; i < array.length; i++) {
				if (array[i] != null) {
					this.array[i] = new QueryAttribute(array[i]);
				}
			}
		}
		
		public QueryAttributeOrder(List<QueryAttribute> col) {
			this.array = new QueryAttribute[col.size()];
			for(int i = 0; i < col.size(); i++) {
				if (col.get(i) != null) {
					this.array[i] = new QueryAttribute(col.get(i));
				}
			}
		}
	}
	
	public static class QuerySourceOrder {
		
		public QuerySource[] array;
		
		public QuerySourceOrder(int size) {
			array = new QuerySource[size];
		}
		
		public QuerySourceOrder(QuerySource[] array) {
			this.array = new QuerySource[array.length];
			// deep copy
			for(int i = 0; i < array.length; i++) {
				if (array[i] != null) {
					this.array[i] = new QuerySource(array[i]);
				}
			}
		}
		
	}
	
	private class ParsedAttribute {

		String name = null;
		String prefix = null;
		String alias = null;
		String sourcename = null;
		String sourcealias = null;
		Collection<String> containedBySubQuery = new ArrayList<>();

		public ParsedAttribute(Attribute e) {

			name = e.getName();

			if (name.contains(".")) {
				
				final String[] split = e.getName().split("\\.");
				
				prefix = split[1];
				sourcename = split[0];

				if (utilityService.isSourceAlias(sourcename)) {
					sourcealias = sourcename;
					sourcename = utilityService.getSourcenameFromAlias(sourcename);
				}
//TODO is this still needed?				
//				if (!utilityService.getSourceNames().contains(sourcename)) {
//					sourcealias = sourcename;
//					if (utilityService.getSourcenameFromAlias(sourcename) == null && utilityService.existsQueryExpressionString(split[0])) {
//						sourcename = utilityService.getSourcenameFromAlias(sourcename);
//					}
//				}

				
				Optional<SubQuery> o = utilityService.isSubQuery(sourcename);
				
				if (o.isPresent()) {
					
					
					final SubQuery subQuery = o.get();
					containedBySubQuery.addAll(cacheService.getQueryCache().getQueryAttributes(subQuery.select)
							.stream()
							.map(k -> k.name)
							.collect(Collectors.toList())
					);
					
				}

			}
			
			alias = e.getAlias() != null ? e.getAlias().getName() : null;

		}
		
		public boolean matches(SystemAttribute e) {
			return name.equals(e.getAttributename())|| (prefix != null && e.getAttributename().equals(prefix));
		}

		public boolean hasStarPrefix() {
			return prefix != null ? prefix.equals("*") : false;
		}
		
	}
	
}
