package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionComponent;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Matrix;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Vector;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.SelectCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAggregationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IExistenceParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IPredicateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IProjectionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class SelectParser implements ISelectParser {
  public enum Operator {
    MAP,
    
    AGGREGATE;
  }
  
  private final Logger log = LoggerFactory.getLogger(SelectParser.class);
  
  private AbstractPQLOperatorBuilder builder;
  
  private ICacheService cacheService;
  
  private IUtilityService utilityService;
  
  private IAttributeNameParser attributeParser;
  
  private IPredicateParser predicateParser;
  
  private IJoinParser joinParser;
  
  private IRenameParser renameParser;
  
  private IProjectionParser projectionParser;
  
  private IAggregationParser aggregateParser;
  
  private IExistenceParser existenceParser;
  
  private boolean prepare;
  
  @Inject
  public SelectParser(final AbstractPQLOperatorBuilder builder, final ICacheService cacheService, final IUtilityService utilityService, final IAttributeNameParser attributeParser, final IPredicateParser predicateParser, final IJoinParser joinParser, final IProjectionParser projectionParser, final IRenameParser renameParser, final IAggregationParser aggregateParser, final IExistenceParser existenceParser) {
    this.builder = builder;
    this.cacheService = cacheService;
    this.utilityService = utilityService;
    this.attributeParser = attributeParser;
    this.predicateParser = predicateParser;
    this.joinParser = joinParser;
    this.projectionParser = projectionParser;
    this.aggregateParser = aggregateParser;
    this.existenceParser = existenceParser;
    this.renameParser = renameParser;
    this.prepare = true;
  }
  
  @Override
  public String parse(final SimpleSelect select) {
    if (this.prepare) {
      this.prepare(select);
      this.prepare = false;
    }
    ExpressionsModel _predicates = select.getPredicates();
    boolean _tripleNotEquals = (_predicates != null);
    if (_tripleNotEquals) {
      return this.parseWithPredicate(select);
    }
    String projectInput = null;
    String operator1 = this.parseAdditionalOperator(SelectParser.Operator.MAP, select);
    String operator2 = this.parseAdditionalOperator(SelectParser.Operator.AGGREGATE, select);
    if ((((operator1 == null) && (operator2 == null)) && select.getArguments().isEmpty())) {
      EList<Source> _sources = select.getSources();
      String _buildJoin = this.joinParser.buildJoin(_sources);
      String _string = _buildJoin.toString();
      projectInput = _string;
      EList<Source> _sources_1 = select.getSources();
      int _size = _sources_1.size();
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        OperatorCache _operatorCache = this.cacheService.getOperatorCache();
        return _operatorCache.registerOperator(projectInput);
      } else {
        OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
        String _parse = this.projectionParser.parse(select, projectInput);
        return _operatorCache_1.registerOperator(_parse);
      }
    } else {
      String _buildInput2 = this.buildInput2(select, operator1, operator2);
      projectInput = _buildInput2;
      OperatorCache _operatorCache_2 = this.cacheService.getOperatorCache();
      String _parse_1 = this.projectionParser.parse(select, projectInput);
      return _operatorCache_2.registerOperator(_parse_1);
    }
  }
  
  public Collection<NestedSource> registerAllSource(final SimpleSelect select) {
    ArrayList<NestedSource> list = CollectionLiterals.<NestedSource>newArrayList();
    EList<Source> _sources = select.getSources();
    for (final Source source : _sources) {
      {
        String name = "";
        if ((source instanceof SimpleSource)) {
          String _name = ((SimpleSource) source).getName();
          String _name_1 = name = _name;
          SourceStruct.addQuerySource(_name_1);
          if (((((SimpleSource)source).getAlias() != null) && (!this.utilityService.getSource(((SimpleSource)source)).hasAlias(((SimpleSource)source).getAlias())))) {
            this.utilityService.registerSourceAlias(((SimpleSource) source));
          }
        } else {
          if ((source instanceof NestedSource)) {
            list.add(((NestedSource)source));
          }
        }
      }
    }
    return list;
  }
  
  public List<SelectExpression> extractAggregationsFromArgument(final List<SelectArgument> args) {
    List<SelectExpression> list = CollectionLiterals.<SelectExpression>newArrayList();
    for (final SelectArgument a : args) {
      SelectExpression _expression = a.getExpression();
      boolean _tripleNotEquals = (_expression != null);
      if (_tripleNotEquals) {
        SelectExpression _expression_1 = a.getExpression();
        EList<ExpressionComponent> _expressions = _expression_1.getExpressions();
        int _size = _expressions.size();
        boolean _equals = (_size == 1);
        if (_equals) {
          SelectExpression _expression_2 = a.getExpression();
          EList<ExpressionComponent> _expressions_1 = _expression_2.getExpressions();
          ExpressionComponent aggregation = _expressions_1.get(0);
          EObject function = aggregation.getValue();
          if ((function instanceof Function)) {
            String _name = ((Function)function).getName();
            boolean _isAggregateFunctionName = this.utilityService.isAggregateFunctionName(_name);
            if (_isAggregateFunctionName) {
              SelectExpression _expression_3 = a.getExpression();
              list.add(_expression_3);
            }
          }
        }
      }
    }
    return list;
  }
  
  public Collection<SelectExpression> extractSelectExpressionsFromArgument(final List<SelectArgument> args) {
    Collection<SelectExpression> list = CollectionLiterals.<SelectExpression>newArrayList();
    for (final SelectArgument a : args) {
      SelectExpression _expression = a.getExpression();
      boolean _tripleNotEquals = (_expression != null);
      if (_tripleNotEquals) {
        SelectExpression _expression_1 = a.getExpression();
        EList<ExpressionComponent> _expressions = _expression_1.getExpressions();
        int _size = _expressions.size();
        boolean _equals = (_size == 1);
        if (_equals) {
          SelectExpression _expression_2 = a.getExpression();
          EList<ExpressionComponent> _expressions_1 = _expression_2.getExpressions();
          ExpressionComponent aggregation = _expressions_1.get(0);
          EObject function = aggregation.getValue();
          if ((function instanceof Function)) {
            String _name = ((Function)function).getName();
            SelectExpression _expression_3 = a.getExpression();
            String _parseExpression = this.parseExpression(((SelectExpression) _expression_3));
            String _string = _parseExpression.toString();
            boolean _isMEPFunctionMame = this.utilityService.isMEPFunctionMame(_name, _string);
            if (_isMEPFunctionMame) {
              SelectExpression _expression_4 = a.getExpression();
              list.add(_expression_4);
            }
          } else {
            SelectExpression _expression_5 = a.getExpression();
            list.add(_expression_5);
          }
        } else {
          SelectExpression _expression_6 = a.getExpression();
          list.add(_expression_6);
        }
      }
    }
    return list;
  }
  
  @Override
  public void prepare(final SimpleSelect select) {
    try {
      try {
        SelectCache _selectCache = this.cacheService.getSelectCache();
        Collection<SimpleSelect> _selects = _selectCache.getSelects();
        boolean _contains = _selects.contains(select);
        boolean _not = (!_contains);
        if (_not) {
          Collection<NestedSource> subQueries = this.registerAllSource(select);
          for (final NestedSource subQuery : subQueries) {
            {
              InnerSelect _statement = subQuery.getStatement();
              SimpleSelect _select = _statement.getSelect();
              this.prepare(_select);
              QueryCache _queryCache = this.cacheService.getQueryCache();
              _queryCache.putSubQuerySources(subQuery);
            }
          }
          Collection<QueryCache.QueryCacheAttributeEntry> attributes2 = this.utilityService.getSelectedAttributes(select);
          EList<SelectArgument> _arguments = select.getArguments();
          List<SelectExpression> aggregations = this.extractAggregationsFromArgument(_arguments);
          EList<SelectArgument> _arguments_1 = select.getArguments();
          Collection<SelectExpression> expressions = this.extractSelectExpressionsFromArgument(_arguments_1);
          if ((aggregations != null)) {
            QueryCache _queryCache = this.cacheService.getQueryCache();
            _queryCache.putQueryAggregations(select, aggregations);
          }
          if ((expressions != null)) {
            QueryCache _queryCache_1 = this.cacheService.getQueryCache();
            _queryCache_1.putQueryExpressions(select, expressions);
          }
          if ((attributes2 != null)) {
            QueryCache _queryCache_2 = this.cacheService.getQueryCache();
            _queryCache_2.putQueryAttributes(select, attributes2);
          }
          SelectCache _selectCache_1 = this.cacheService.getSelectCache();
          _selectCache_1.add(select);
        }
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          String _message = e.getMessage();
          String _plus = ("error occurred while parsing select: " + _message);
          this.log.error(_plus);
          throw e;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public String parseExpression(final SelectExpression e) {
    String str = "";
    for (int i = 0; (i < e.getExpressions().size()); i++) {
      {
        EList<ExpressionComponent> _expressions = e.getExpressions();
        ExpressionComponent _get = _expressions.get(i);
        EObject component = ((ExpressionComponent) _get).getValue();
        boolean _matched = false;
        if (component instanceof Function) {
          _matched=true;
          String _str = str;
          String _name = ((Function)component).getName();
          String _plus = (_name + "(");
          EObject _value = ((Function)component).getValue();
          String _parseExpression = this.parseExpression(((SelectExpression) _value));
          String _plus_1 = (_plus + _parseExpression);
          String _plus_2 = (_plus_1 + ")");
          str = (_str + _plus_2);
        }
        if (!_matched) {
          if (component instanceof Attribute) {
            _matched=true;
            String _str = str;
            String _name = ((Attribute)component).getName();
            String _parse = this.attributeParser.parse(_name);
            str = (_str + _parse);
          }
        }
        if (!_matched) {
          if (component instanceof IntConstant) {
            _matched=true;
            String _str = str;
            int _value = ((IntConstant)component).getValue();
            String _plus = (Integer.valueOf(_value) + "");
            str = (_str + _plus);
          }
        }
        if (!_matched) {
          if (component instanceof FloatConstant) {
            _matched=true;
            String _str = str;
            String _value = ((FloatConstant)component).getValue();
            String _plus = (_value + "");
            str = (_str + _plus);
          }
        }
        if (!_matched) {
          if (component instanceof BoolConstant) {
            _matched=true;
            String _str = str;
            String _value = ((BoolConstant)component).getValue();
            String _plus = (_value + "");
            str = (_str + _plus);
          }
        }
        if (!_matched) {
          if (component instanceof StringConstant) {
            _matched=true;
            String _str = str;
            String _value = ((StringConstant)component).getValue();
            String _plus = ("\"" + _value);
            String _plus_1 = (_plus + "\"");
            str = (_str + _plus_1);
          }
        }
        if (!_matched) {
          if (component instanceof Vector) {
            _matched=true;
            String _str = str;
            String _value = ((Vector)component).getValue();
            str = (_str + _value);
          }
        }
        if (!_matched) {
          if (component instanceof Matrix) {
            _matched=true;
            String _str = str;
            String _value = ((Matrix)component).getValue();
            str = (_str + _value);
          }
        }
        EList<ExpressionComponent> _expressions_1 = e.getExpressions();
        int _size = _expressions_1.size();
        int _minus = (_size - 1);
        boolean _notEquals = (i != _minus);
        if (_notEquals) {
          String _str = str;
          EList<String> _operators = e.getOperators();
          String _get_1 = _operators.get(i);
          str = (_str + _get_1);
        }
      }
    }
    return str;
  }
  
  @Override
  public String parseComplex(final SimpleSelect left, final SimpleSelect right, final String operator) {
    this.parse(left);
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    String rightSelectOperatorName = _operatorCache.lastOperatorId();
    SelectCache _selectCache = this.cacheService.getSelectCache();
    _selectCache.flush();
    this.parse(right);
    OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
    String leftSelectOperatorName = _operatorCache_1.lastOperatorId();
    OperatorCache _operatorCache_2 = this.cacheService.getOperatorCache();
    return _operatorCache_2.registerOperator(
      (((((operator + "(") + rightSelectOperatorName) + ",") + leftSelectOperatorName) + ")"));
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
        throw new IllegalArgumentException("queries with more then one complex predicates are not supported");
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
    this.predicateParser.parse(predicates);
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
      String _registerOperator = _operatorCache.registerOperator(_build);
      select = _registerOperator;
    } else {
      Map<String, String> newArgs = this.existenceParser.getOperator(0);
      String _get_2 = newArgs.get("input");
      String _plus = (_get_2 + ",");
      String _plus_1 = (_plus + selectInput);
      newArgs.put("input", _plus_1);
      OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
      String _build_1 = this.builder.build(ExistenceAO.class, newArgs);
      _operatorCache_1.registerOperator(_build_1);
      OperatorCache _operatorCache_2 = this.cacheService.getOperatorCache();
      OperatorCache _operatorCache_3 = this.cacheService.getOperatorCache();
      String _lastOperatorId = _operatorCache_3.lastOperatorId();
      String _plus_2 = ("JOIN(" + _lastOperatorId);
      String _plus_3 = (_plus_2 + ",");
      String _plus_4 = (_plus_3 + selectInput);
      String _plus_5 = (_plus_4 + ")");
      String _parse = this.projectionParser.parse(stmt, _plus_5);
      return _operatorCache_2.registerOperator(_parse);
    }
    this.existenceParser.register(stmt, select);
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
      OperatorCache _operatorCache_4 = this.cacheService.getOperatorCache();
      String _parse_1 = this.projectionParser.parse(stmt, select);
      return _operatorCache_4.registerOperator(_parse_1);
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
        EList<Source> _sources = select.getSources();
        String _buildJoin = this.joinParser.buildJoin(_sources);
        String _buildJoin_1 = this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin });
        return this.checkForGroupAttributes(aggregateOperator, select, _buildJoin_1);
      } else {
        if ((mapOperator != null)) {
          EList<Source> _sources_1 = select.getSources();
          return this.joinParser.buildJoin(_sources_1);
        } else {
          if ((aggregateOperator != null)) {
            List<String> _xifexpression_2 = null;
            ExpressionsModel _predicates = select.getPredicates();
            boolean _tripleNotEquals = (_predicates != null);
            if (_tripleNotEquals) {
              ExpressionsModel _predicates_1 = select.getPredicates();
              List<Attribute> _allContentsOfType = EcoreUtil2.<Attribute>getAllContentsOfType(_predicates_1, Attribute.class);
              Stream<Attribute> _stream = _allContentsOfType.stream();
              final java.util.function.Function<Attribute, String> _function = (Attribute e) -> {
                return e.getName();
              };
              Stream<String> _map = _stream.<String>map(_function);
              Collector<String, ?, List<String>> _list = Collectors.<String>toList();
              _xifexpression_2 = _map.collect(_list);
            } else {
              _xifexpression_2 = null;
            }
            List<String> predicateAttributes = _xifexpression_2;
            Collection<de.uniol.inf.is.odysseus.core.collection.Pair<SelectExpression, String>> _aggregationAttributeCache = this.cacheService.getAggregationAttributeCache();
            QueryCache _queryCache = this.cacheService.getQueryCache();
            Collection<String> _projectionAttributes = _queryCache.getProjectionAttributes(select);
            boolean _containsAll = _aggregationAttributeCache.containsAll(_projectionAttributes);
            if (_containsAll) {
              if ((((predicateAttributes != null) && (!predicateAttributes.isEmpty())) && 
                this.cacheService.getAggregationAttributeCache().containsAll(predicateAttributes))) {
                return aggregateOperator;
              } else {
                EList<Source> _sources_2 = select.getSources();
                String _buildJoin_2 = this.joinParser.buildJoin(_sources_2);
                String _buildJoin_3 = this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin_2 });
                return this.checkForGroupAttributes(aggregateOperator, select, _buildJoin_3);
              }
            } else {
              EList<Source> _sources_3 = select.getSources();
              String _buildJoin_4 = this.joinParser.buildJoin(_sources_3);
              String _buildJoin_5 = this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin_4 });
              return this.checkForGroupAttributes(aggregateOperator, select, _buildJoin_5);
            }
          }
        }
      }
    }
    EList<Source> _sources_4 = select.getSources();
    return this.joinParser.buildJoin(_sources_4);
  }
  
  private String checkForGroupAttributes(final String aggregateOperator, final SimpleSelect select, final String output) {
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    String _operator = _operatorCache.getOperator(aggregateOperator);
    boolean _contains = _operator.contains("group_by");
    if (_contains) {
      EList<Source> _sources = select.getSources();
      String join = this.joinParser.buildJoin(_sources);
      ArrayList<String> groupAttributes = CollectionLiterals.<String>newArrayList();
      for (int i = 0; (i < select.getOrder().size()); i++) {
        {
          EList<Attribute> _order = select.getOrder();
          Attribute _get = _order.get(i);
          String groupAttribute = _get.getName();
          groupAttributes.add(groupAttribute);
          groupAttributes.add(((groupAttribute + "_groupAttribute#") + Integer.valueOf(i)));
        }
      }
      String _parse = this.renameParser.parse(groupAttributes, aggregateOperator);
      return this.joinParser.buildJoin(new String[] { _parse, join });
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
          Collection<SelectExpression> expressions = _queryCache.getQueryExpressions(select);
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
          Collection<SelectExpression> aggregations = _queryCache_1.getQueryAggregations(select);
          if (((aggregations != null) && (!aggregations.isEmpty()))) {
            EList<Attribute> _order = select.getOrder();
            EList<Source> _sources = select.getSources();
            Object[] _parse_1 = this.aggregateParser.parse(aggregations, _order, _sources);
            result = _parse_1;
            OperatorCache _operatorCache = this.cacheService.getOperatorCache();
            Object _get_1 = result[1];
            String _string_1 = _get_1.toString();
            String _registerOperator = _operatorCache.registerOperator(_string_1);
            operatorName = _registerOperator;
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
