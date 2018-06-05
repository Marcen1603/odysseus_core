package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.xtext.EcoreUtil2;

import com.google.inject.Inject;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLFactory;
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
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper.ParsedAggregation;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper.ParsedAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper.ParsedExpression;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExpressionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;

@SuppressWarnings("unused")
public class AttributeParser implements IAttributeParser {
	private final String EXPRESSSION_NAME_PREFIX = "expression_";
	private static IUtilityService utilityService;
	private ICacheService cacheService;

	private IExpressionParser expressionParser;
	private static int expressionCounter = 0;
	private static int aggregationCounter = 0;
	private int index = 0;
	private int index2 = 0;
	private QuerySourceOrder sourceOrder = new QuerySourceOrder();
	private QueryAttributeOrder attributeOrder;
	private Collection<QueryAggregate> aggregates;
	private Collection<QueryExpression> expressions;

	@Inject
	public AttributeParser(IUtilityService utilityService, ICacheService cacheService,
			IExpressionParser expressionParser) {
		this.utilityService = utilityService;
		this.cacheService = cacheService;
		this.expressionParser = expressionParser;
		this.aggregates = new ArrayList<>();
		this.expressions = new ArrayList<>();
	}

	@Override
	public void registerAttributesFromPredicate(SimpleSelect select) {

		if (select.getPredicates() != null) {
			EcoreUtil2.getAllContentsOfType(select.getPredicates(), Attribute.class).stream().forEach(e -> {

				final String attributename = e.getName();

				if (attributename.contains(".")) {
					String[] split = attributename.split("\\.");
					String sourcename = split[0];
					String attributename2 = split[1];
					if (utilityService.isSourceAlias(sourcename) && utilityService.isAttributeAlias(attributename2)) {
						registerAttributeAliases(new ParsedAttribute(e));
					} else if (utilityService.isSourceAlias(sourcename)
							&& !utilityService.isAttributeAlias(attributename2)) {
						SystemSource s = utilityService.getSystemSource(sourcename);
						s.findByName(attributename2).addAlias(sourcename + "." + attributename2);
						s.associateAttributeAliasWithSourceAlias(sourcename + "." + attributename2, sourcename);
					}
				}

				Optional<SimpleSelect> s = utilityService.getCorrespondingSelect(e);
				if (s.isPresent()) {
					computeSourceCandidates(e, s.get().getSources()).stream().forEach(k -> {
						sourceOrder.add(new QuerySource(k.getE1()));
					});
					;
				}

			});
		}

	}

	@Override
	public Collection<QueryAttribute> getSelectedAttributes(SimpleSelect select) {

		attributeOrder = new QueryAttributeOrder(select.getArguments().size());
		Collection<QueryAttribute> entryCol = new ArrayList<>();
		index = 0;

		select.getArguments().stream().map(e -> e.getAttribute()).filter(p -> p != null).forEach(e -> {

			String attributealias = null;
			QueryAttribute queryAttribute = null;
			ParsedAttribute parsed = null;
			Collection<Pair<Source, String>> candidates = computeSourceCandidates(e, select.getSources());
			Collection<Pair<Source, String>> noDuplicates = new ArrayList<>();
			candidates.stream().forEach(k -> {
				if (!noDuplicates.stream().anyMatch(p -> p.getE2().equals(k.getE2()))) {
					noDuplicates.add(k);
				}
			});

			Source source = null;

			if (noDuplicates.size() > 0) {
				if (noDuplicates.size() == 1) {
					Pair<Source, String> pair = noDuplicates.stream().findFirst().get();
					source = pair.getE1();
					queryAttribute = createQueryAttribute(e, Collections.singletonList(new QuerySource(source)));// new
																													// QueryAttribute(e,
																													// parsed,
																													// Collections.singletonList(new
																													// QuerySource(source)));
					parsed = queryAttribute.parsedAttribute;
				} else if (noDuplicates.size() > 1) {
					throw new IllegalArgumentException("attribute could not be parsed");
				}
				attributealias = registerAttributeAliases(parsed);
				entryCol.add(queryAttribute);
			}

			parsed = new ParsedAttribute(e);

			attributeOrder.array = computeProjectionAttributes(attributeOrder.array, select, e, parsed.getName(),
					attributealias, parsed.sourcename, source);

			sourceOrder.add(new QuerySource(source));
			index++;
		});

		// Check if it is a select * query and add for each source its attributes
		if (entryCol.isEmpty() && EcoreUtil2.getAllContentsOfType(select, SelectExpression.class).isEmpty()) {

			index = 0;
			List<QueryAttribute> list = new ArrayList<>();

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
					Attribute obj = CQLFactory.eINSTANCE.createAttribute();
					obj.setName(attributename);
					// obj.setAlias(e.getAlias());
					list.add(createQueryAttribute(obj, Collections.singletonList(new QuerySource(e))));
				});
				sourceOrder.add(new QuerySource(e));
				index++;

			});
			attributeOrder = new QueryAttributeOrder(list);

		}

		attributeOrder.array = computeProjectionAttributes(attributeOrder.array, select, null, null, null, null, null);

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

	@Override
	public Collection<Pair<Source, String>> computeSourceCandidates(Attribute attribute, Collection<Source> sources) {

		Collection<Pair<Source, String>> containedBySources = new ArrayList<>();

		sources.stream().forEach(s -> {

			if (s instanceof SimpleSource) {

				Collection<Pair<Source, String>> col = cacheService.getSystemSources().stream().filter(p -> {
					if (p.isSame((SimpleSource) s) && p.hasAttribute(attribute) && p.isContainedBy(sources)
							&& !containedBySources.stream().anyMatch(e -> e.getE1().equals(s))) {

						if (attribute.getName().contains(".")) {
							String name = attribute.getName().split("\\.")[0];
							if (s.getAlias() != null && s.getAlias().getName().equals(name)) {
								return true;
							}
						} else {
							return true;
						}
					}
					return false;
				}).map(e -> new Pair<Source, String>(s, e.name)).collect(Collectors.toList());

				containedBySources.addAll(col);

			} else {

				String subQueryAlias = ((NestedSource) s).getAlias().getName();
				cacheService.getQueryCache().getAllSubQueries().stream()
						.filter(p -> p.source.getAlias().getName().equals(subQueryAlias)).forEach(q -> {

							utilityService.getAllSubQuerySource(((NestedSource) q.source).getStatement().getSelect())
									.stream().forEach(sub -> {
										cacheService.getSystemSources().stream().forEach(sys -> {

											String realName = attribute.getName();
											if (realName.contains(".")) {
												realName = realName.split("\\.")[1];
											}
											if (sys.isSame(sub) && sys.hasAttribute(realName)) {
												if (!containedBySources.stream()
														.anyMatch(e -> e.getE2().equals(sys.name))) {
													containedBySources.add(new Pair<Source, String>(sub, sys.name));
												} else if (!attribute.getName().contains(".")) {
													if (!utilityService.isAttributeAlias(attribute.getName())) {
														throw new IllegalArgumentException(
																"error occurred: name=" + attribute.getName());
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

	private QueryAttribute[] computeProjectionAttributes(QueryAttribute[] list, SimpleSelect select,
			Attribute attribute, String attributename, String attributealias, String sourcename, Source source) {

		expressionCounter = 0;
		aggregationCounter = 0;
		index2 = 0;
		attributeOrder = new QueryAttributeOrder(list);
		Collection<QuerySource> sources = new ArrayList<>();
		if (source != null) {
			sources.add(new QuerySource(source));
		}

		if (attribute != null) {

			select.getArguments().stream().forEach(argument -> {

				Object candidate = null;

				if ((candidate = argument.getAttribute()) != null) {

					Attribute candidateAttribute = (Attribute) candidate;
					if (candidateAttribute.getName().equals(attribute.getName())) {
						if (candidateAttribute.getAlias() != null) {

							// attributeOrder.array[index2] = new QueryAttribute(
							// candidateAttribute.getAlias().getName(),
							// QueryAttribute.Type.STANDARD,
							// utilityService.getDataTypeFrom(candidateAttribute),
							// sources
							// );

							attributeOrder.array[index2] = createQueryAttribute(candidateAttribute, sources);

						} else if (attributealias != null) {
							// attributeOrder.array[index2] = new QueryAttribute(
							// attributealias,
							// QueryAttribute.Type.STANDARD,
							// utilityService.getDataTypeFrom(candidateAttribute),
							// sources
							// );

							attributeOrder.array[index2] = createQueryAttribute(candidateAttribute, sources);

						} else {

							if (attributename.contains(".")) {
								String[] split = attributename.split("\\.");
								String name = split[1];
								String sourcen = split[0];
								String salias = sourcen;

								if (utilityService.isSourceAlias(sourcen)) {
									sourcen = utilityService.getSourcenameFromAlias(salias);
								}
								if (name.equals("*")) {
									// TODO Query with stream1.*, stream.* would be overriden!
									Collection<QueryAttribute> attributeOrderList = new ArrayList<>(
											attributeOrder.array.length);
									for (String str : utilityService.getAttributeNamesFromSource(sourcen)) {

										Attribute obj = CQLFactory.eINSTANCE.createAttribute();
										obj.setName(salias + "." + str);
										attributeOrderList.add(createQueryAttribute(obj, sources));

										index2++;
									}
									attributeOrder.array = attributeOrderList
											.toArray(new QueryAttribute[attributeOrderList.size()]);
								} else {
									Attribute obj = CQLFactory.eINSTANCE.createAttribute();
									obj.setName(attributename);
									attributeOrder.array[index2] = createQueryAttribute(obj, sources);
								}

							} else {
								Attribute obj = CQLFactory.eINSTANCE.createAttribute();
								obj.setName(sourcename + "." + attributename);
								attributeOrder.array[index2] = createQueryAttribute(attribute, sources);

							}
						}
					}
				}
				attributeOrder.array = parseExpression(select, attributeOrder.array, argument, index2);
				index2++;
			});

		} else {
			for (SelectArgument argument : select.getArguments()) {
				attributeOrder.array = parseExpression(select, attributeOrder.array, argument, index2);
				index2++;
			}
		}

		expressionCounter = 0;
		aggregationCounter = 0;
		return attributeOrder.array;
	}

	private QueryAttribute createQueryAttribute(Attribute attribute, Collection<QuerySource> sources) {

		Attribute obj = attribute;
		String sourcename = null;
		if (!attribute.getName().contains(".")) {
			obj = CQLFactory.eINSTANCE.createAttribute();
			if (sources.stream().findFirst().get().alias != null) {
				sourcename = sources.stream().findFirst().get().alias;
			} else {
				sourcename = sources.stream().findFirst().get().name;
			}
			sourcename = sourcename + "." + attribute.getName();
			obj.setName(sourcename);
		}

		Attribute clone = EcoreUtil2.clone(attribute);
		obj.setAlias(clone.getAlias());

		ParsedAttribute parsedAttribute = new ParsedAttribute(obj);

		return new QueryAttribute(attribute, parsedAttribute, sources,
				utilityService.getDataTypeFrom(parsedAttribute, sourcename));
	}

	private QueryAttribute[] parseExpression(SimpleSelect select, QueryAttribute[] attributeOrder,
			SelectArgument argument, int i) {

		SelectExpression candidate = argument.getExpression();

		if (candidate != null) {

			final List<Function> functions = EcoreUtil2.getAllContentsOfType(candidate, Function.class);

			if (candidate.getAlias() != null) {

				final String name = candidate.getAlias().getName();

				if (functions.size() == 1) {

					if (functions.get(0) instanceof Function) {

						final String functionName = ((Function) functions.get(0)).getName();

						if (utilityService.isAggregateFunctionName(functionName)) {

							Optional<QueryAggregate> aggregate = createQueryAggregate(select, name, null, candidate);

							if (aggregate.isPresent()) {
								attributeOrder[i] = aggregate.get();
								aggregates.add(aggregate.get());
							}

						} else {
							attributeOrder = createQueryExpression(select, attributeOrder, candidate, name, i);
						}

					} else {
						attributeOrder = createQueryExpression(select, attributeOrder, candidate, name, i);
					}
				} else {
					attributeOrder = createQueryExpression(select, attributeOrder, candidate, name, i);
				}
			} else {

				if (functions.size() == 1) {

					if (functions.get(0) instanceof Function) {

						final String functionName = ((Function) functions.get(0)).getName();

						if (utilityService.isAggregateFunctionName(functionName)) {

							final String name = getAggregationName(((Function) functions.get(0)).getName());
							Optional<QueryAggregate> aggregate = createQueryAggregate(select, name, null, candidate);

							if (aggregate.isPresent()) {
								attributeOrder[i] = aggregate.get();
								aggregates.add(aggregate.get());
							}

						} else {
							attributeOrder = createQueryExpression(select, attributeOrder, candidate,
									getExpressionName(), i);
						}
					} else {
						attributeOrder = createQueryExpression(select, attributeOrder, candidate, getExpressionName(),
								i);
					}

				} else {
					attributeOrder = createQueryExpression(select, attributeOrder, candidate, getExpressionName(), i);
				}

			}

		}

		aggregationCounter = 0;
		expressionCounter = 0;
		return attributeOrder;
	}

	private String registerAttributeAliases(ParsedAttribute parsed) {

		String sourceAlias = parsed.sourcealias;

		if (!utilityService.isSubQuery(parsed.sourcename).isPresent()) {
			SystemSource sourceStruct = utilityService.getSystemSource(parsed.sourcename);

			for (SystemAttribute sysAttribute : sourceStruct.getAttributeList()) {

				if (parsed.matches(sysAttribute)) {// parsed.name.equals(systemAttributename)|| (parsed.prefix != null
													// && systemAttributename.equals(parsed.prefix))) {
					if (sourceAlias == null) {
						sourceAlias = parsed.sourcename;
					}
					if (parsed.getAlias() != null) {

						if (sourceStruct.isAssociatedToASource(parsed.getAlias())) {
							throw new IllegalArgumentException("given alias " + parsed.getAlias() + " is ambiguous");
						}

						if (!sysAttribute.hasAlias(parsed.getAlias())) {
							sysAttribute.addAlias(parsed.getAlias());
							sourceStruct.associateAttributeAliasWithSourceAlias(parsed.getAlias(), sourceAlias);
						}

						return parsed.getAlias();

					} else if (parsed.getAlias() == null
							&& utilityService.getSourceAliasesAsList().contains(sourceAlias)) {
						if (!sysAttribute.hasAlias(parsed.getName())) {
							sysAttribute.addAlias(parsed.getName());
							sourceStruct.associateAttributeAliasWithSourceAlias(parsed.getName(), sourceAlias);
						}
					}

					return parsed.getName();
				}
			}
		}

		return parsed.getAlias();
	}

	private Optional<QueryAggregate> createQueryAggregate(SimpleSelect select, String name, String datatype,
			SelectExpression expression) {

		final QueryAggregate aggregate = new QueryAggregate(new ParsedAggregation(select, expression, name));

		aggregate.sources.stream().forEach(e -> {
			sourceOrder.add(e);
		});

		if (aggregates.stream()
				.anyMatch(p -> p.parsedAggregation.getName().equals(aggregate.parsedAggregation.getName()))) {
			return Optional.empty();
		}

		return Optional.of(aggregate);
	}

	private QueryAttribute[] createQueryExpression(SimpleSelect select, QueryAttribute[] attributeOrder,
			SelectExpression candidate, String name, int i) {

		if (!expressions.stream().map(e -> e.parsedExpression.getName()).anyMatch(p -> p.equals(name))) {

			final QueryExpression queryExpression = new QueryExpression(new ParsedExpression(select, candidate, name));
			queryExpression.sources.stream().forEach(e -> {
				sourceOrder.add(e);
			});

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
		sourceOrder = new QuerySourceOrder();
	}

	public static class QueryAttributeOrder {

		public QueryAttribute[] array;

		public QueryAttributeOrder(int size) {
			array = new QueryAttribute[size];
		}

		public QueryAttributeOrder(QueryAttribute[] array) {
			this.array = new QueryAttribute[array.length];
			// deep copy
			for (int i = 0; i < array.length; i++) {
				if (array[i] != null) {
					this.array[i] = array[i];
				}
			}
		}

		public QueryAttributeOrder(List<QueryAttribute> col) {
			this.array = new QueryAttribute[col.size()];
			for (int i = 0; i < col.size(); i++) {
				if (col.get(i) != null) {
					this.array[i] = col.get(i);
				}
			}
		}
	}

	public static class QuerySourceOrder {

		private Collection<QuerySource> array = new ArrayList<>();

		public QuerySourceOrder() {

		}

		public void add(QuerySource querySource) {

			if (querySource.source == null) {
				System.out.println("hey!");
			}

			if (querySource.source != null && !array.stream().anyMatch(p -> p.source.equals(querySource.source))) {

				Optional<SubQuery> subQuery = AttributeParser.utilityService.containedBySubQuery(querySource.source);
				if (!subQuery.isPresent()) {
					array.add(querySource);
				}
			}
		}

		public void add2(QuerySource querySource) {

			if (querySource.source != null && !array.stream().anyMatch(p -> p.source.equals(querySource.source))) {

				array.add(querySource);
			}
		}

		public Collection<QuerySource> get() {
			return array;
		}

	}
}
