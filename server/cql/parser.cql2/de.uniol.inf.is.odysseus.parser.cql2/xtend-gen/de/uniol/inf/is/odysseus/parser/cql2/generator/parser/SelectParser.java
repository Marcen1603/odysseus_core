package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.SelectCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAggregationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExistenceParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExpressionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IPredicateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IProjectionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class SelectParser implements ISelectParser {
  public enum Operator {
    MAP,
    
    AGGREGATE;
  }
  
  private AbstractPQLOperatorBuilder builder;
  
  private ICacheService cacheService;
  
  private IUtilityService utilityService;
  
  private IAttributeNameParser nameParser;
  
  private IPredicateParser predicateParser;
  
  private IJoinParser joinParser;
  
  private IRenameParser renameParser;
  
  private IProjectionParser projectionParser;
  
  private IAggregationParser aggregateParser;
  
  private IExistenceParser existenceParser;
  
  private IAttributeParser attributeParser;
  
  private IExpressionParser expressionParser;
  
  private boolean prepare;
  
  @Inject
  public SelectParser(final AbstractPQLOperatorBuilder builder, final ICacheService cacheService, final IUtilityService utilityService, final IAttributeNameParser nameParser, final IPredicateParser predicateParser, final IJoinParser joinParser, final IProjectionParser projectionParser, final IRenameParser renameParser, final IAggregationParser aggregateParser, final IExistenceParser existenceParser, final IAttributeParser attributeParser, final IExpressionParser expressionParser) {
    this.builder = builder;
    this.cacheService = cacheService;
    this.utilityService = utilityService;
    this.nameParser = nameParser;
    this.attributeParser = attributeParser;
    this.predicateParser = predicateParser;
    this.joinParser = joinParser;
    this.projectionParser = projectionParser;
    this.aggregateParser = aggregateParser;
    this.existenceParser = existenceParser;
    this.renameParser = renameParser;
    this.expressionParser = expressionParser;
    this.prepare = true;
  }
  
  @Override
  public void parse(final SimpleSelect select) {
    if (this.prepare) {
      ExpressionsModel _predicates = select.getPredicates();
      boolean _notEquals = (!Objects.equal(_predicates, null));
      if (_notEquals) {
        ExpressionsModel _predicates_1 = select.getPredicates();
        List<SimpleSelect> _allContentsOfType = EcoreUtil2.<SimpleSelect>getAllContentsOfType(_predicates_1, SimpleSelect.class);
        Stream<SimpleSelect> _stream = _allContentsOfType.stream();
        final Consumer<SimpleSelect> _function = (SimpleSelect e) -> {
          this.registerAllSource(e);
          this.fooor(e);
        };
        _stream.forEach(_function);
      }
      this.prepare(select, null);
      SelectCache _selectCache = this.cacheService.getSelectCache();
      Collection<SimpleSelect> _selects = _selectCache.getSelects();
      final Consumer<SimpleSelect> _function_1 = (SimpleSelect e) -> {
        this.fooor(e);
      };
      _selects.forEach(_function_1);
      this.prepare = false;
    }
    SelectCache _selectCache_1 = this.cacheService.getSelectCache();
    Collection<SimpleSelect> _selects_1 = _selectCache_1.getSelects();
    Iterator<SimpleSelect> iter = _selects_1.iterator();
    while (iter.hasNext()) {
      {
        final SimpleSelect e = iter.next();
        boolean root = true;
        boolean _equals = select.equals(e);
        boolean _not = (!_equals);
        if (_not) {
          root = false;
        }
        this.parseSingleSelect(e);
        if ((!root)) {
          QueryCache _queryCache = this.cacheService.getQueryCache();
          final Optional<QueryCache.SubQuery> o = _queryCache.getSubQuery(e);
          boolean _isPresent = o.isPresent();
          if (_isPresent) {
            QueryCache.SubQuery _get = o.get();
            OperatorCache _operatorCache = this.cacheService.getOperatorCache();
            String _last = _operatorCache.last();
            _get.operator = _last;
          }
        }
      }
    }
  }
  
  @Override
  public void parseSingleSelect(final SimpleSelect e) {
    ExpressionsModel _predicates = e.getPredicates();
    boolean _tripleNotEquals = (_predicates != null);
    if (_tripleNotEquals) {
      this.parseWithPredicate(e);
      OperatorCache _operatorCache = this.cacheService.getOperatorCache();
      OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
      String _last = _operatorCache_1.last();
      _operatorCache.registerLastOperator(e, _last);
      return;
    }
    String projectInput = null;
    String operator1 = this.parseAdditionalOperator(SelectParser.Operator.MAP, e);
    String operator2 = this.parseAdditionalOperator(SelectParser.Operator.AGGREGATE, e);
    if ((((operator1 == null) && (operator2 == null)) && e.getArguments().isEmpty())) {
      QueryCache _queryCache = this.cacheService.getQueryCache();
      Collection<QueryCache.QuerySource> _querySources = _queryCache.getQuerySources(e);
      String _buildJoin = this.joinParser.buildJoin(_querySources, e);
      String _string = _buildJoin.toString();
      projectInput = _string;
      EList<Source> _sources = e.getSources();
      int _size = _sources.size();
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        OperatorCache _operatorCache_2 = this.cacheService.getOperatorCache();
        _operatorCache_2.add(e, projectInput);
        OperatorCache _operatorCache_3 = this.cacheService.getOperatorCache();
        OperatorCache _operatorCache_4 = this.cacheService.getOperatorCache();
        String _last_1 = _operatorCache_4.last();
        _operatorCache_3.registerLastOperator(e, _last_1);
        return;
      } else {
        OperatorCache _operatorCache_5 = this.cacheService.getOperatorCache();
        String _parse = this.projectionParser.parse(e, projectInput);
        _operatorCache_5.add(e, _parse);
        OperatorCache _operatorCache_6 = this.cacheService.getOperatorCache();
        OperatorCache _operatorCache_7 = this.cacheService.getOperatorCache();
        String _last_2 = _operatorCache_7.last();
        _operatorCache_6.registerLastOperator(e, _last_2);
        return;
      }
    } else {
      String _buildInput2 = this.buildInput2(e, operator1, operator2);
      projectInput = _buildInput2;
      OperatorCache _operatorCache_8 = this.cacheService.getOperatorCache();
      String _parse_1 = this.projectionParser.parse(e, projectInput);
      _operatorCache_8.add(e, _parse_1);
      OperatorCache _operatorCache_9 = this.cacheService.getOperatorCache();
      OperatorCache _operatorCache_10 = this.cacheService.getOperatorCache();
      String _last_3 = _operatorCache_10.last();
      _operatorCache_9.registerLastOperator(e, _last_3);
      return;
    }
  }
  
  public Collection<NestedSource> registerAllSource(final SimpleSelect select) {
    final ArrayList<NestedSource> col = CollectionLiterals.<NestedSource>newArrayList();
    EList<Source> _sources = select.getSources();
    Stream<Source> _stream = _sources.stream();
    final Consumer<Source> _function = (Source e) -> {
      if ((e instanceof SimpleSource)) {
        final String name = ((SimpleSource) e).getName();
        SystemSource.addQuerySource(name);
        if (((((SimpleSource)e).getAlias() != null) && (!this.utilityService.getSystemSource(((SimpleSource)e)).hasAlias(((SimpleSource)e).getAlias())))) {
          this.utilityService.registerSourceAlias(((SimpleSource) e));
        }
      } else {
        if ((e instanceof NestedSource)) {
          col.add(((NestedSource)e));
          QueryCache _queryCache = this.cacheService.getQueryCache();
          QueryCache.SubQuery _subQuery = new QueryCache.SubQuery(((NestedSource)e));
          _queryCache.addSubQuerySource(_subQuery);
        }
      }
    };
    _stream.forEach(_function);
    return col;
  }
  
  /**
   * Collects and saves attribute, aggregation and expression information about a select statement before it will be parsed.
   * This method is called recursively such that the most nested select statement will be processed first. Therefore, the
   * processing order of the selects statements is determined by this method and is retained by {@link SelectCache}.
   */
  @Override
  public void prepare(final SimpleSelect select, final NestedSource innerSelect) {
    SelectCache _selectCache = this.cacheService.getSelectCache();
    Collection<SimpleSelect> _selects = _selectCache.getSelects();
    boolean _contains = _selects.contains(select);
    boolean _not = (!_contains);
    if (_not) {
      Collection<NestedSource> _registerAllSource = this.registerAllSource(select);
      Stream<NestedSource> _stream = _registerAllSource.stream();
      final Consumer<NestedSource> _function = (NestedSource e) -> {
        InnerSelect _statement = e.getStatement();
        SimpleSelect _select = _statement.getSelect();
        this.prepare(_select, e);
      };
      _stream.forEach(_function);
      SelectCache _selectCache_1 = this.cacheService.getSelectCache();
      _selectCache_1.add(select);
    }
  }
  
  @Override
  public void fooor(final SimpleSelect select) {
    this.attributeParser.clear();
    this.attributeParser.registerAttributesFromPredicate(select);
    QueryCache _queryCache = this.cacheService.getQueryCache();
    Collection<QueryCache.QueryAttribute> _selectedAttributes = this.attributeParser.getSelectedAttributes(select);
    _queryCache.putQueryAttributes(select, _selectedAttributes);
    EList<Source> _sources = select.getSources();
    final Consumer<Source> _function = (Source e) -> {
      AttributeParser.QuerySourceOrder _sourceOrder = this.attributeParser.getSourceOrder();
      QueryCache.QuerySource _querySource = new QueryCache.QuerySource(e);
      _sourceOrder.add2(_querySource);
    };
    _sources.forEach(_function);
    QueryCache _queryCache_1 = this.cacheService.getQueryCache();
    AttributeParser.QuerySourceOrder _sourceOrder = this.attributeParser.getSourceOrder();
    _queryCache_1.putProjectionSources(select, _sourceOrder);
    QueryCache _queryCache_2 = this.cacheService.getQueryCache();
    AttributeParser.QueryAttributeOrder _attributeOrder = this.attributeParser.getAttributeOrder();
    _queryCache_2.putProjectionAttributes(select, _attributeOrder);
    QueryCache _queryCache_3 = this.cacheService.getQueryCache();
    Collection<QueryCache.QueryAggregate> _aggregates = this.attributeParser.getAggregates();
    _queryCache_3.putQueryAggregations(select, _aggregates);
    QueryCache _queryCache_4 = this.cacheService.getQueryCache();
    Collection<QueryCache.QueryExpression> _expressions = this.attributeParser.getExpressions();
    _queryCache_4.putQueryExpressions(select, _expressions);
  }
  
  @Override
  public String parseComplex(final SimpleSelect left, final SimpleSelect right, final String operator) {
    this.parse(left);
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    String rightSelectOperatorName = _operatorCache.last();
    SelectCache _selectCache = this.cacheService.getSelectCache();
    _selectCache.flush();
    this.prepare = true;
    this.parse(right);
    OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
    String leftSelectOperatorName = _operatorCache_1.last();
    OperatorCache _operatorCache_2 = this.cacheService.getOperatorCache();
    return _operatorCache_2.add(right, (((((operator + "(") + rightSelectOperatorName) + ",") + leftSelectOperatorName) + ")"));
  }
  
  @Override
  public String parseWithPredicate(final SimpleSelect stmt) {
    List<Expression> predicates = CollectionLiterals.<Expression>newArrayList();
    List<Source> sources = CollectionLiterals.<Source>newArrayList();
    ExpressionsModel _predicates = stmt.getPredicates();
    boolean _tripleNotEquals = (_predicates != null);
    if (_tripleNotEquals) {
      ExpressionsModel _predicates_1 = stmt.getPredicates();
      EList<Expression> _elements = _predicates_1.getElements();
      Expression _get = _elements.get(0);
      predicates.add(0, _get);
      ExpressionsModel _predicates_2 = stmt.getPredicates();
      List<ComplexPredicate> complexPredicates = EcoreUtil2.<ComplexPredicate>getAllContentsOfType(_predicates_2, ComplexPredicate.class);
      if ((((complexPredicates != null) && (!complexPredicates.isEmpty())) && (complexPredicates.size() > 1))) {
        throw new IllegalArgumentException("queries with more than one complex predicate are not supported");
      }
    }
    ExpressionsModel _having = stmt.getHaving();
    boolean _tripleNotEquals_1 = (_having != null);
    if (_tripleNotEquals_1) {
      ExpressionsModel _having_1 = stmt.getHaving();
      EList<Expression> _elements_1 = _having_1.getElements();
      Expression _get_1 = _elements_1.get(0);
      predicates.add(0, _get_1);
    }
    EList<Source> _sources = stmt.getSources();
    sources.addAll(_sources);
    String operator1 = this.parseAdditionalOperator(SelectParser.Operator.MAP, stmt);
    String operator2 = this.parseAdditionalOperator(SelectParser.Operator.AGGREGATE, stmt);
    this.predicateParser.clear();
    CharSequence _parse = this.predicateParser.parse(predicates, stmt);
    String predicateString = _parse.toString();
    QueryCache _queryCache = this.cacheService.getQueryCache();
    _queryCache.addPredicate(stmt, predicates, predicateString);
    String _buildInput2 = this.buildInput2(stmt, operator1, operator2);
    String selectInput = _buildInput2.toString();
    List<String> _predicateStringList = this.predicateParser.getPredicateStringList();
    CharSequence predicate = this.predicateParser.parsePredicateString(_predicateStringList);
    String select = "";
    boolean _equals = predicate.equals("");
    boolean _not = (!_equals);
    if (_not) {
      OperatorCache _operatorCache = this.cacheService.getOperatorCache();
      String _string = predicate.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("predicate", _string);
      Pair<String, String> _mappedTo_1 = Pair.<String, String>of("input", selectInput);
      LinkedHashMap<String, String> _newLinkedHashMap = CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1);
      String _build = this.builder.build(SelectAO.class, _newLinkedHashMap);
      String _add = _operatorCache.add(stmt, _build);
      select = _add;
    } else {
      OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
      OperatorCache _operatorCache_2 = this.cacheService.getOperatorCache();
      String _last = _operatorCache_2.last();
      String _parse_1 = this.projectionParser.parse(stmt, _last);
      return _operatorCache_1.add(stmt, _parse_1);
    }
    ArrayList<Attribute> attributes = CollectionLiterals.<Attribute>newArrayList();
    EList<SelectArgument> _arguments = stmt.getArguments();
    for (final SelectArgument arg : _arguments) {
      Attribute _attribute = arg.getAttribute();
      boolean _tripleNotEquals_2 = (_attribute != null);
      if (_tripleNotEquals_2) {
        Attribute _attribute_1 = arg.getAttribute();
        attributes.add(_attribute_1);
      }
    }
    if ((((!this.checkIfSelectAll(attributes)) || (!this.cacheService.getQueryCache().getQueryAggregations(stmt).isEmpty())) || 
      (!this.cacheService.getQueryCache().getQueryExpressions(stmt).isEmpty()))) {
      OperatorCache _operatorCache_3 = this.cacheService.getOperatorCache();
      String _parse_2 = this.projectionParser.parse(stmt, select);
      return _operatorCache_3.add(stmt, _parse_2);
    }
    return select;
  }
  
  private String buildInput2(final SimpleSelect select, final String... operators) {
    if ((operators != null)) {
      String _xifexpression = null;
      int _size = ((List<String>)Conversions.doWrapArray(operators)).size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _xifexpression = operators[0];
      } else {
        _xifexpression = null;
      }
      String mapOperator = _xifexpression;
      String _xifexpression_1 = null;
      int _size_1 = ((List<String>)Conversions.doWrapArray(operators)).size();
      boolean _greaterThan_1 = (_size_1 > 1);
      if (_greaterThan_1) {
        _xifexpression_1 = operators[1];
      } else {
        _xifexpression_1 = null;
      }
      String aggregateOperator = _xifexpression_1;
      if (((mapOperator != null) && (aggregateOperator != null))) {
        QueryCache _queryCache = this.cacheService.getQueryCache();
        Collection<QueryCache.QuerySource> _querySources = _queryCache.getQuerySources(select);
        String _buildJoin = this.joinParser.buildJoin(_querySources, select);
        String _buildJoin_1 = this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin });
        return this.checkForGroupAttributes(aggregateOperator, select, _buildJoin_1);
      } else {
        if ((mapOperator != null)) {
          QueryCache _queryCache_1 = this.cacheService.getQueryCache();
          Collection<QueryCache.QuerySource> _querySources_1 = _queryCache_1.getQuerySources(select);
          return this.joinParser.buildJoin(_querySources_1, select);
        } else {
          if ((aggregateOperator != null)) {
            List<String> _xifexpression_2 = null;
            ExpressionsModel _predicates = select.getPredicates();
            boolean _tripleNotEquals = (_predicates != null);
            if (_tripleNotEquals) {
              ExpressionsModel _predicates_1 = select.getPredicates();
              List<Attribute> _allContentsOfType = EcoreUtil2.<Attribute>getAllContentsOfType(_predicates_1, Attribute.class);
              Stream<Attribute> _stream = _allContentsOfType.stream();
              final Function<Attribute, String> _function = (Attribute e) -> {
                return e.getName();
              };
              Stream<String> _map = _stream.<String>map(_function);
              Collector<String, ?, List<String>> _list = Collectors.<String>toList();
              _xifexpression_2 = _map.collect(_list);
            } else {
              _xifexpression_2 = null;
            }
            List<String> predicateAttributes = _xifexpression_2;
            boolean _containsAllAggregates = this.utilityService.containsAllAggregates(select);
            if (_containsAllAggregates) {
              if ((((predicateAttributes != null) && (!predicateAttributes.isEmpty())) && this.utilityService.containsAllPredicates(predicateAttributes))) {
                return aggregateOperator;
              } else {
                QueryCache _queryCache_2 = this.cacheService.getQueryCache();
                Collection<QueryCache.QuerySource> _querySources_2 = _queryCache_2.getQuerySources(select);
                String _buildJoin_2 = this.joinParser.buildJoin(_querySources_2, select);
                String _buildJoin_3 = this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin_2 });
                return this.checkForGroupAttributes(aggregateOperator, select, _buildJoin_3);
              }
            } else {
              QueryCache _queryCache_3 = this.cacheService.getQueryCache();
              Collection<QueryCache.QuerySource> _querySources_3 = _queryCache_3.getQuerySources(select);
              String _buildJoin_4 = this.joinParser.buildJoin(_querySources_3, select);
              String _buildJoin_5 = this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin_4 });
              return this.checkForGroupAttributes(aggregateOperator, select, _buildJoin_5);
            }
          }
        }
      }
    }
    QueryCache _queryCache_4 = this.cacheService.getQueryCache();
    Collection<QueryCache.QuerySource> _querySources_4 = _queryCache_4.getQuerySources(select);
    return this.joinParser.buildJoin(_querySources_4, select);
  }
  
  private String checkForGroupAttributes(final String aggregateOperator, final SimpleSelect select, final String output) {
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    Optional<String> o = _operatorCache.getOperator(aggregateOperator);
    boolean _isPresent = o.isPresent();
    if (_isPresent) {
      OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
      Optional<String> _operator = _operatorCache_1.getOperator(aggregateOperator);
      String _get = _operator.get();
      boolean _contains = _get.contains("group_by");
      if (_contains) {
        QueryCache _queryCache = this.cacheService.getQueryCache();
        Collection<QueryCache.QuerySource> _querySources = _queryCache.getQuerySources(select);
        String join = this.joinParser.buildJoin(_querySources, select);
        ArrayList<String> groupAttributes = CollectionLiterals.<String>newArrayList();
        for (int i = 0; (i < select.getOrder().size()); i++) {
          {
            EList<Attribute> _order = select.getOrder();
            Attribute _get_1 = _order.get(i);
            String groupAttribute = _get_1.getName();
            groupAttributes.add(groupAttribute);
            groupAttributes.add(((groupAttribute + "_groupAttribute#") + Integer.valueOf(i)));
          }
        }
        String _parse = this.renameParser.parse(groupAttributes, aggregateOperator, select);
        return this.joinParser.buildJoin(new String[] { _parse, join });
      }
    }
    return output;
  }
  
  public boolean checkIfSelectAll(final List<Attribute> attributes) {
    boolean _isEmpty = attributes.isEmpty();
    if (_isEmpty) {
      return true;
    } else {
      for (final Attribute attribute : attributes) {
        String _name = attribute.getName();
        boolean _contains = _name.contains(".*");
        boolean _not = (!_contains);
        if (_not) {
          return false;
        }
      }
    }
    return true;
  }
  
  private String parseAdditionalOperator(final SelectParser.Operator operator, final SimpleSelect select) {
    Object[] result = null;
    String operatorName = null;
    if (operator != null) {
      switch (operator) {
        case MAP:
          QueryCache _queryCache = this.cacheService.getQueryCache();
          Collection<QueryCache.QueryExpression> expressions = _queryCache.getQueryExpressions(select);
          if (((expressions != null) && (!expressions.isEmpty()))) {
            Object[] _parse = this.projectionParser.parse(expressions, null);
            result = _parse;
            Object _get = result[1];
            String _string = _get.toString();
            operatorName = _string;
          }
          break;
        case AGGREGATE:
          QueryCache _queryCache_1 = this.cacheService.getQueryCache();
          Collection<QueryCache.QueryAggregate> aggregations = _queryCache_1.getQueryAggregations(select);
          if (((aggregations != null) && (!aggregations.isEmpty()))) {
            EList<Attribute> _order = select.getOrder();
            QueryCache _queryCache_2 = this.cacheService.getQueryCache();
            Collection<QueryCache.QuerySource> _querySources = _queryCache_2.getQuerySources(select);
            Object[] _parse_1 = this.aggregateParser.parse(aggregations, _order, _querySources, select);
            result = _parse_1;
            OperatorCache _operatorCache = this.cacheService.getOperatorCache();
            Object _get_1 = result[1];
            String _string_1 = _get_1.toString();
            String _add = _operatorCache.add(select, _string_1);
            operatorName = _add;
          }
          break;
        default:
          break;
      }
    }
    return operatorName;
  }
  
  @Override
  public void clear() {
    this.prepare = true;
  }
}
