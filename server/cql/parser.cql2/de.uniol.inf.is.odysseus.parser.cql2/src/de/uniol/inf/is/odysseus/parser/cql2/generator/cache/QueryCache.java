package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser.QueryAttributeOrder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser.QuerySourceOrder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;

public class QueryCache implements Cache {
	
	private Map<SimpleSelect, Collection<QueryAttribute>>		projectionAttributes	= new HashMap<>();
	private Map<SimpleSelect, Collection<QuerySource>> 		sourceEntries 		= new HashMap<>();
	private Map<SimpleSelect, Collection<QueryAttribute>> 	attributeEntries 	= new HashMap<>();
	private Map<SimpleSelect, Collection<QueryAggregate>> 	aggreateEntries 		= new HashMap<>();
	private Map<SimpleSelect, Collection<QueryExpression>> 	expressionEntries 	= new HashMap<>();
	private Map<String, SubQuery> 							subQueryEntries 		= new HashMap<>();

	private static IUtilityService utilityService;
	private static QueryCache queryCache;
	
	public QueryCache() {
		queryCache = this;
		utilityService = CQLGenerator.injector.getInstance(IUtilityService.class);
	}
	
	public Collection<QueryAttribute> getProjectionAttributes(SimpleSelect query) {
		return projectionAttributes.containsKey(query) ? projectionAttributes.get(query) : new ArrayList<>();
	}

	public Collection<QuerySource> getProjectionSourcess(SimpleSelect query) {
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
		attributeEntries.put(query, attributes.stream().map(e -> new QueryAttribute(e)).collect(Collectors.toList()));
	}

	public void putProjectionAttributes(SimpleSelect query, QueryAttributeOrder attributes) {
		if (attributes != null) {
			projectionAttributes.put(query, Arrays.asList(attributes.array).stream().filter(p -> p != null).map(e -> new QueryAttribute(e)).collect(Collectors.toList()));
		}
	}

	public void putProjectionSources(SimpleSelect query, QuerySourceOrder sources) {
		if (sources != null) {
			sourceEntries.put(query, Arrays.asList(sources.array).stream().filter(p -> p != null).map(e -> new QuerySource(e)).collect(Collectors.toList()));
		}
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
	
		public QuerySource(String sourcename) {
			this.name = sourcename;
		}
	
		public QuerySource(String sourcename, String sourcealias) {
			this(sourcename);
			this.alias = sourcealias;
		}
		
		public QuerySource(SimpleSource source) {
			this(source.getName());
			if (source.getAlias() != null) {
				this.alias = source.getAlias().getName();
			}
		}
		
		public QuerySource(QuerySource obj) {
			this.name = obj.name;
			this.alias = obj.alias;
		}
		
	}

	public static class SubQuery {
		
		public String alias;
		public SimpleSelect select;
		public NestedSource source;
		public String operator;
		
		public SubQuery(NestedSource subQuery) {
			this.source = subQuery;
			this.select = subQuery.getStatement().getSelect();
			this.alias = subQuery.getAlias().getName();
		}
	}

	
	public static class QueryAttribute {
		
		public String name;
		public String prefix = "";
		public String suffix = "";
		public String alias;
		public String datatype; 
		public Collection<QuerySource> sources;
		public Attribute attribute;
		public QueryAttribute referencedBy;
		public QueryAttribute referenceOf;
		public Type type;
		
		public enum Type {
			SELECTALL,
			EXPRESSION,
			AGGREGATE,
			STANDARD,
		}
		
		// copy constructor
		public QueryAttribute(QueryAttribute obj) {
			this(obj.name, obj.type);
			this.alias = obj.alias;
			this.datatype = obj.datatype;
			this.attribute = obj.attribute;
			this.referencedBy = obj.referencedBy;
			this.sources = new ArrayList<>(obj.sources);
		}
		
		private QueryAttribute(String name, Type type) {
			this.name = name;
			this.type = type;
			
			if (name.contains(".")) {
				
				final String[] split = name.split("\\.");
				this.prefix = split[0] != null ? split[0] : "";
				this.suffix = split[1] != null ? split[1] : "";
				
				Optional<SubQuery> o = utilityService.isSubQuery(this.prefix);
				if (o.isPresent()) {
					Optional<QueryAttribute> queryAttribute = queryCache.getProjectionAttributes(o.get().select)
						.stream()
						.filter(p -> p.suffix.equals(this.suffix))
						.findFirst();
					
					if (queryAttribute.isPresent()) {
						this.referencedBy = queryAttribute.get();
						queryAttribute.get().referenceOf = this;
					}
				}
			}
			
			this.sources = new ArrayList<>();
			
		}
		
 		public QueryAttribute(Attribute obj, Type type, String datatype) {
 			this(obj.getName(), type);
			this.attribute = obj;
			this.alias = obj.getAlias() != null ? obj.getAlias().getName() : "";
		}
	
		public QueryAttribute(Attribute e, Type type, String datatype, QuerySource systemSource) {
			this(e, type, datatype);
			this.sources.add(systemSource);
		}
	
		public QueryAttribute(String name, Type type, String datatype, QuerySource systemSource) {
			this(name, type);
			this.sources = new ArrayList<>();
			this.sources.add(systemSource);
		}
	
		public QueryAttribute(String name, Type type, String datatype, Collection<QuerySource> sources) {
			this(name, type);
			this.sources = sources.stream().map(e -> new QuerySource(e.name, e.alias)).collect(Collectors.toList());
		}
		
	}

	public static class QueryAggregate extends QueryAttribute {
		
		public SelectExpression expression;
		
		private QueryAggregate(String name, Type type, String datatype, Collection<QuerySource> sources) {
			super(name, type, datatype, sources);
		}
	
		public QueryAggregate(QueryAggregate obj) {
			super(obj);
			this.expression = obj.expression;
		}
		
		public QueryAggregate(String name, Type type, String datatype, Collection<QuerySource> sources, SelectExpression expression) {
			this(name, type, datatype, sources);
			this.expression = expression;			
		}
	}

	public static class QueryExpression extends QueryAttribute {
	
		public SelectExpression expression;
		
		private QueryExpression(String name, Type type, String datatype, Collection<QuerySource> sources) {
			super(name, type, datatype, sources);
		}
		
		public QueryExpression(QueryExpression obj) {
			super(obj);
			this.expression = obj.expression;// is this cloneable?
		}
	
		public QueryExpression(String name, Type type, String datatype, Collection<QuerySource> sources, SelectExpression expression) {
			this(name, type, datatype, sources);
			this.expression = expression;
			
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

}