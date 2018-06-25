package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.xtext.EcoreUtil2;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser.QueryAttributeOrder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser.QuerySourceOrder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper.ParsedAggregation;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper.ParsedAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper.ParsedExpression;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IParsedObject;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IParsedObject.Type;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;

public class QueryCache implements Cache {
	
	private Map<SimpleSelect, Collection<QueryAttribute>>		projectionAttributes	= new HashMap<>();
	private Map<SimpleSelect, Collection<QuerySource>> 		sourceEntries 		= new HashMap<>();
	private Map<SimpleSelect, Collection<QueryAttribute>> 	attributeEntries 	= new HashMap<>();
	private Map<SimpleSelect, Collection<QueryAggregate>> 	aggreateEntries 		= new HashMap<>();
	private Map<SimpleSelect, Collection<QueryExpression>> 	expressionEntries 	= new HashMap<>();
	private Map<String, SubQuery> 							subQueryEntries 		= new HashMap<>();
	private Map<SimpleSelect, QueryPredicate>				predicateEntries 	= new HashMap<>();

	private static IUtilityService utilityService;
	private static QueryCache queryCache;
	
	public QueryCache() {
		queryCache = this;
		utilityService = CQLGenerator.injector.getInstance(IUtilityService.class);
	}
	
	public Collection<QueryAttribute> getProjectionAttributes(SimpleSelect query) {
		return projectionAttributes.containsKey(query) ? projectionAttributes.get(query) : new ArrayList<>();
	}

	public Collection<QuerySource> getQuerySources(SimpleSelect query) {
		return sourceEntries.containsKey(query) ? sourceEntries.get(query) : new ArrayList<>();
	}
	
	public Collection<QueryAttribute> getQueryAttributes(SimpleSelect query) {
		return attributeEntries.containsKey(query) ? attributeEntries.get(query) : new ArrayList<>();
	}
	
	public Collection<QueryAggregate> getQueryAggregations(SimpleSelect query) {
		return aggreateEntries.containsKey(query) ? aggreateEntries.get(query) : new ArrayList<>();
	}

	public Collection<QueryExpression> getQueryExpressions(SimpleSelect query) {
		return expressionEntries.containsKey(query) ? expressionEntries.get(query) : new ArrayList<>();
	}

	public void putQueryAttributes(SimpleSelect query, Collection<QueryAttribute> attributes) {
		attributeEntries.put(query, attributes.stream().map(e -> {
			if (e instanceof QueryAggregate) {
				return new QueryAggregate(((QueryAggregate) e).parsedAggregation);
			} else if (e instanceof QueryExpression) {
				return new QueryExpression(((QueryExpression) e).parsedExpression);
			} else {
				return new QueryAttribute(e);
			}
		}).collect(Collectors.toList()));
	}

	public void putProjectionAttributes(SimpleSelect query, QueryAttributeOrder attributes) {
		projectionAttributes.put(query, Arrays.asList(attributes.array).stream().filter(p -> p != null).map(e -> {
			if (e instanceof QueryAggregate) {
				return new QueryAggregate(((QueryAggregate) e).parsedAggregation);
			} else if (e instanceof QueryExpression) {
				return new QueryExpression(((QueryExpression) e).parsedExpression);
			} else {
				return new QueryAttribute(e);
			}
		}).collect(Collectors.toList()));
	}

	public void putProjectionSources(SimpleSelect query, QuerySourceOrder sources) {
		sourceEntries.put(query, sources.get().stream().map(e -> new QuerySource(e)).collect(Collectors.toList()));
	}

	public void putQueryAggregations(SimpleSelect query, Collection<QueryAggregate> aggregations) {
		aggreateEntries.put(query, aggregations.stream().map(e -> new QueryAggregate(e)).collect(Collectors.toList()));
	}
	
	public void putQueryExpressions(SimpleSelect query, Collection<QueryExpression> expressions) {
		expressionEntries.put(query, expressions.stream().map(e -> new QueryExpression(e)).collect(Collectors.toList()));
	}

	public void addSubQuerySource(SubQuery subQuery) {
		subQueryEntries.put(subQuery.alias, subQuery);
	}

	public Collection<SubQuery> getAllSubQueries() {
		return subQueryEntries.values();
	}

	public Collection<QueryAttribute> getAllQueryAttributes() {
		
		Collection<QueryAttribute> col = new ArrayList<>();
		
		attributeEntries.values().stream().forEach(e -> {
			e.stream().forEach(p -> col.add(p));
		});
		
		aggreateEntries.values().stream().forEach(e -> {
			e.stream().forEach(p -> col.add(p));
		});
		
		return col;
	}
	
	public Collection<QueryAggregate> getAllQueryAggregations() {
		
		Collection<QueryAggregate> col = new ArrayList<>();
		
		aggreateEntries.values().stream().forEach(e -> {
			e.stream().forEach(p -> col.add(p));
		});
		
		return col;
	}

	public Collection<QueryExpression> getAllQueryExpressions() {
		
		Collection<QueryExpression> col = new ArrayList<>();
		
		expressionEntries.values().stream().forEach(e -> {
			e.stream().forEach(p -> col.add(p));
		});
		
		return col;
	}

	public void clear() {
		projectionAttributes.clear();
		sourceEntries.clear();
		attributeEntries.clear();
		subQueryEntries.clear();
		aggreateEntries.clear();
		expressionEntries.clear();
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
	}

	public static class QuerySource {
	
		public String name;
		public String alias;
		public Source source;
		
		public QuerySource(Source source) {
			this.source = source;
			if (source instanceof SimpleSource) {
				this.name =  ((SimpleSource) source).getName();
			}
			if (source != null && source.getAlias() != null) {
				this.alias = source.getAlias().getName();
			}
		}
		
		public QuerySource(QuerySource obj) {
			this.name = obj.name;
			this.alias = obj.alias;
			this.source = obj.source;
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if (obj instanceof QuerySource) {
				return this.name.equals(((QuerySource) obj).name) 
						&& this.alias.equals(((QuerySource) obj).alias)
						&& this.source.equals(((QuerySource) obj).source);
			}
			
			return false;
		}
		
	}

	public static class SubQuery extends QuerySource {
		
		public SimpleSelect select;
		public String operator;
		
		public SubQuery(NestedSource subQuery) {
			super(subQuery);
			this.select = subQuery.getStatement().getSelect();
		}
	}

	
	public static class QueryAttribute {
		
		public Attribute attribute;
		public ParsedAttribute parsedAttribute;
		public String dataType;
		public IParsedObject parsedObject;
		public Collection<QuerySource> sources;
		
		public QueryAttribute referencedBy;
		public QueryAttribute referenceOf;
		public IParsedObject.Type type;
		
		private QueryAttribute(Collection<QuerySource> sources){
			this.sources = sources.stream().map(e -> new QuerySource(e)).collect(Collectors.toList());
		}
		
		public QueryAttribute(Attribute attribute, ParsedAttribute parsed, Collection<QuerySource> sources, String dataType) {
			this(sources);
			this.parsedAttribute = parsed;
			this.parsedObject = parsed;
			this.attribute = attribute;
			this.dataType = dataType;
			this.type = parsed.getType();
			
			Optional<SubQuery> o = utilityService.isSubQuery(parsedAttribute.prefix);
			
			// check if this is referenced by another nested select
			if (o.isPresent()) {
				
				Collection<QueryAggregate> queryAggregations = new ArrayList<>();
				
				Optional<QueryAttribute> queryAttribute = queryCache.getProjectionAttributes(o.get().select)
					.stream()
					.map(p -> {
						if(p.type.equals(Type.AGGREGATION)) {
							queryAggregations.add((QueryAggregate) p);
						}
						return p;
					})
					.filter(p -> p.type.equals(Type.ATTRIBUTE))
					.filter(p -> p.parsedAttribute.suffix.equals(parsedAttribute.suffix))
					.findFirst();
				
				// if it is referenced, set referencedBy and referenceOf to associate both with each other
				if (queryAttribute.isPresent()) {
					this.referencedBy = queryAttribute.get();
					queryAttribute.get().referenceOf = this;
					return;
				}

				if (!queryAggregations.isEmpty()) {
					Optional<QueryAggregate> queryAggregate = queryAggregations.stream()
					.filter(p -> p.parsedAggregation.getAlias().equals(parsedAttribute.suffix))
					.findFirst();
					
					if (queryAggregate.isPresent()) {
						
						ParsedAttribute newParsed = ParsedAttribute.convert(queryAggregate.get());
						QueryAttribute newAttribute = new QueryAttribute(
								null, 
								newParsed, 
								queryAggregate.get().sources,
								queryAggregate.get().getDataType()
						);
						this.referencedBy = newAttribute;
						queryAggregate.get().parsedAttribute = newParsed;
						queryAggregate.get().referenceOf = this;
					}
					
				}
				
			}
			
		}
		
//		 copy constructor
		public QueryAttribute(QueryAttribute obj) {
			this(obj.sources);
			this.parsedAttribute = new ParsedAttribute(obj.parsedAttribute);
			this.parsedObject = this.parsedAttribute;
			this.attribute = obj.attribute;
			this.dataType = obj.dataType;
			this.type = obj.type;
			this.referencedBy = obj.referencedBy;
			this.referenceOf = obj.referenceOf;
		}
		
		public String getName() {
			return parsedObject.getName();
		}
		
		public String getDataType() {
			return dataType;
		}
		
		public String getAlias() {
			return parsedObject.getAlias();
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if (obj instanceof QueryAttribute) {
				return this.parsedAttribute.getName().equals(((QueryAttribute) obj).parsedAttribute.getName()) &&
						this.getDataType().equals(((QueryAttribute) obj).getDataType()) &&
						this.sources.equals(((QueryAttribute) obj).sources);
			}
			
			return false;
		}
		
		public boolean equals(Attribute obj) {
			return this.parsedAttribute != null && this.parsedAttribute.equals(new ParsedAttribute(obj));
		}
		
	}

	public static class QueryAggregate extends QueryAttribute {

		public ParsedAggregation parsedAggregation;
		
		public QueryAggregate(QueryAggregate obj) {
			super(obj.parsedAggregation.relatedSources);
			this.parsedAggregation = new ParsedAggregation(obj.parsedAggregation);
			this.parsedObject = this.parsedAggregation;
		}
		
		public QueryAggregate(ParsedAggregation parsed) {
			super(parsed.relatedSources);
			this.parsedAggregation = parsed;
			this.parsedObject = this.parsedAggregation;
			this.type = parsed.getType();
			
		}
		
		@Override
		public String getAlias() {
			return parsedAggregation.getAlias();
		}
		
	}
	

	public static class QueryExpression extends QueryAttribute {
	
		public ParsedExpression parsedExpression;
		
		public QueryExpression(ParsedExpression parsed) {
			super(parsed.relatedSources);
			this.parsedExpression = parsed;
			this.parsedObject = parsed;
			this.type = parsed.getType();
		}
		
		public QueryExpression(QueryExpression expression) {
			super(expression.parsedExpression.relatedSources);
			this.parsedExpression = new ParsedExpression(expression.parsedExpression);
			this.parsedObject = this.parsedExpression;
		}
		
	}

	public Optional<SubQuery> getSubQuery(String sourcename) {
		return subQueryEntries.entrySet()
				.stream()
				.filter(p -> p.getKey().equals(sourcename))
				.map(e -> e.getValue())
				.findFirst();
	}
	
	public Optional<SubQuery> getSubQuery(SimpleSelect select) {
		return subQueryEntries.entrySet()
				.stream()
				.filter(p -> p.getValue().select.equals(select))
				.map(e -> e.getValue())
				.findFirst();
	}

	public void updateSubQuery(String name, QueryCache.SubQuery query) {
		if (name != null && query != null) {
			subQueryEntries.put(name, query);
		}
	}
	
	
	public static class QueryPredicate {
		
		public String stringPredicate;
		public List<Expression> predicate;
		public Collection<QueryAttribute> attributes;

		public QueryPredicate(String stringPredicate, List<Expression> predicate, Collection<QueryAttribute> attributes) {
			super();
			this.stringPredicate = stringPredicate;
			this.predicate = predicate;
			this.attributes = attributes;//attributes.stream().map(e -> new QueryAttribute(e)).collect(Collectors.toList());
		}
		
		
		
	}

	public synchronized void addPredicate(SimpleSelect select, List<Expression> predicate, String stringPredicate) {
		
		final Collection<QueryAttribute> attributes = new ArrayList<>(); 
				
		predicate.stream().forEach(e -> {
			EcoreUtil2.getAllContentsOfType(e, Attribute.class).forEach(k -> {
				Optional<QueryAttribute> queryAttribute = utilityService.getQueryAttribute(k);
				if (queryAttribute.isPresent()) {
					attributes.add(queryAttribute.get());
				}
//				attributes.add(new QueryAttribute(k, QueryAttribute.Type.STANDARD, utilityService.getDataTypeFrom(k)));
			});
		});
		
		predicateEntries.put(select, new QueryPredicate(stringPredicate, predicate, attributes));
	}
	
	public Optional<QueryPredicate> getPredicate(SimpleSelect select) {
		return predicateEntries.containsKey(select) ? Optional.of(predicateEntries.get(select)) : Optional.empty();
	}

	
	
}