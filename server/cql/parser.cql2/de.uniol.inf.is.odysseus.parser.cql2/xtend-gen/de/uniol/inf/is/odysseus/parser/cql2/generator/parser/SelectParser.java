package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionComponent;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function;
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
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IExistenceParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IPredicateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IProjectionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
  
  private IProjectionParser projectionParser;
  
  private IExistenceParser existenceParser;
  
  private boolean prepare;
  
  @Inject
  public SelectParser(final AbstractPQLOperatorBuilder builder, final ICacheService cacheService, final IUtilityService utilityService, final IAttributeNameParser attributeParser, final IPredicateParser predicateParser, final IJoinParser joinParser, final IProjectionParser projectionParser, final IExistenceParser existenceParser) {
    this.builder = builder;
    this.cacheService = cacheService;
    this.utilityService = utilityService;
    this.attributeParser = attributeParser;
    this.predicateParser = predicateParser;
    this.joinParser = joinParser;
    this.projectionParser = projectionParser;
    this.existenceParser = existenceParser;
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
      projectInput = this.joinParser.buildJoin(select.getSources()).toString();
      int _size = select.getSources().size();
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        return this.cacheService.getOperatorCache().registerOperator(projectInput);
      } else {
        return this.cacheService.getOperatorCache().registerOperator(this.projectionParser.parse(select, projectInput));
      }
    } else {
      projectInput = this.buildInput2(select, operator1, operator2);
      return this.cacheService.getOperatorCache().registerOperator(this.projectionParser.parse(select, projectInput));
    }
  }
  
  public Collection<NestedSource> registerAllSource(final SimpleSelect select) {
    ArrayList<NestedSource> list = CollectionLiterals.<NestedSource>newArrayList();
    EList<Source> _sources = select.getSources();
    for (final Source source : _sources) {
      {
        String name = "";
        if ((source instanceof SimpleSource)) {
          SourceStruct.addQuerySource(name = ((SimpleSource) source).getName());
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
        int _size = a.getExpression().getExpressions().size();
        boolean _equals = (_size == 1);
        if (_equals) {
          ExpressionComponent aggregation = a.getExpression().getExpressions().get(0);
          EObject function = aggregation.getValue();
          if ((function instanceof Function)) {
            boolean _isAggregateFunctionName = this.utilityService.isAggregateFunctionName(((Function)function).getName());
            if (_isAggregateFunctionName) {
              list.add(a.getExpression());
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
        int _size = a.getExpression().getExpressions().size();
        boolean _equals = (_size == 1);
        if (_equals) {
          ExpressionComponent aggregation = a.getExpression().getExpressions().get(0);
          EObject function = aggregation.getValue();
          if ((function instanceof Function)) {
            SelectExpression _expression_1 = a.getExpression();
            boolean _isMEPFunctionMame = this.utilityService.isMEPFunctionMame(((Function)function).getName(), 
              this.parseExpression(((SelectExpression) _expression_1)).toString());
            if (_isMEPFunctionMame) {
              list.add(a.getExpression());
            }
          } else {
            list.add(a.getExpression());
          }
        } else {
          list.add(a.getExpression());
        }
      }
    }
    return list;
  }
  
  @Override
  public void prepare(final SimpleSelect select) {
    try {
      try {
        boolean _contains = this.cacheService.getSelectCache().getSelects().contains(select);
        boolean _not = (!_contains);
        if (_not) {
          Collection<NestedSource> subQueries = this.registerAllSource(select);
          for (final NestedSource subQuery : subQueries) {
            {
              this.prepare(subQuery.getStatement().getSelect());
              this.cacheService.getQueryCache().putSubQuerySources(subQuery);
            }
          }
          Map<String, Collection<String>> attributes2 = CollectionLiterals.<String, Collection<String>>newHashMap();
          attributes2 = this.utilityService.getSelectedAttributes(select, attributes2);
          List<SelectExpression> aggregations = this.extractAggregationsFromArgument(select.getArguments());
          Collection<SelectExpression> expressions = this.extractSelectExpressionsFromArgument(select.getArguments());
          if ((aggregations != null)) {
            this.cacheService.getQueryCache().putQueryAggregations(select, aggregations);
          }
          if ((expressions != null)) {
            this.cacheService.getQueryCache().putQueryExpressions(select, expressions);
          }
          if ((attributes2 != null)) {
            this.cacheService.getQueryCache().putQueryAttributes(select, attributes2);
          }
          this.cacheService.getSelectCache().add(select);
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
        ExpressionComponent _get = e.getExpressions().get(i);
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
            String _parse = this.attributeParser.parse(((Attribute)component).getName());
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
        int _size = e.getExpressions().size();
        int _minus = (_size - 1);
        boolean _notEquals = (i != _minus);
        if (_notEquals) {
          String _str = str;
          String _get_1 = e.getOperators().get(i);
          str = (_str + _get_1);
        }
      }
    }
    return str;
  }
  
  @Override
  public String parseComplex(final SimpleSelect left, final SimpleSelect right, final String operator) {
    this.parse(left);
    String rightSelectOperatorName = this.cacheService.getOperatorCache().lastOperatorId();
    this.cacheService.getSelectCache().flush();
    this.parse(right);
    String leftSelectOperatorName = this.cacheService.getOperatorCache().lastOperatorId();
    return this.cacheService.getOperatorCache().registerOperator(
      (((((operator + "(") + rightSelectOperatorName) + ",") + leftSelectOperatorName) + ")"));
  }
  
  @Override
  public String parseWithPredicate(final SimpleSelect stmt) {
    List<Expression> predicates = CollectionLiterals.<Expression>newArrayList();
    List<Source> sources = CollectionLiterals.<Source>newArrayList();
    ExpressionsModel _predicates = stmt.getPredicates();
    boolean _tripleNotEquals = (_predicates != null);
    if (_tripleNotEquals) {
      predicates.add(0, stmt.getPredicates().getElements().get(0));
      List<ComplexPredicate> complexPredicates = EcoreUtil2.<ComplexPredicate>getAllContentsOfType(stmt.getPredicates(), ComplexPredicate.class);
      if ((((complexPredicates != null) && (!complexPredicates.isEmpty())) && (complexPredicates.size() > 1))) {
        throw new IllegalArgumentException("queries with more then one complex predicates are not supported");
      }
    }
    ExpressionsModel _having = stmt.getHaving();
    boolean _tripleNotEquals_1 = (_having != null);
    if (_tripleNotEquals_1) {
      predicates.add(0, stmt.getHaving().getElements().get(0));
    }
    sources.addAll(stmt.getSources());
    String operator1 = this.parseAdditionalOperator(SelectParser.Operator.MAP, stmt);
    String operator2 = this.parseAdditionalOperator(SelectParser.Operator.AGGREGATE, stmt);
    this.predicateParser.clear();
    this.predicateParser.parse(predicates);
    String selectInput = this.buildInput2(stmt, operator1, operator2).toString();
    CharSequence predicate = this.predicateParser.parsePredicateString(this.predicateParser.getPredicateStringList());
    String select = "";
    boolean _equals = predicate.equals("");
    boolean _not = (!_equals);
    if (_not) {
      String _string = predicate.toString();
      Pair<String, String> _mappedTo = Pair.<String, String>of("predicate", _string);
      Pair<String, String> _mappedTo_1 = Pair.<String, String>of("input", selectInput);
      select = this.cacheService.getOperatorCache().registerOperator(
        this.builder.build(SelectAO.class, 
          CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1)));
    } else {
      Map<String, String> newArgs = this.existenceParser.getOperator(0);
      String _get = newArgs.get("input");
      String _plus = (_get + ",");
      String _plus_1 = (_plus + selectInput);
      newArgs.put("input", _plus_1);
      this.cacheService.getOperatorCache().registerOperator(this.builder.build(ExistenceAO.class, newArgs));
      String _lastOperatorId = this.cacheService.getOperatorCache().lastOperatorId();
      String _plus_2 = ("JOIN(" + _lastOperatorId);
      String _plus_3 = (_plus_2 + ",");
      String _plus_4 = (_plus_3 + selectInput);
      String _plus_5 = (_plus_4 + ")");
      return this.cacheService.getOperatorCache().registerOperator(
        this.projectionParser.parse(stmt, _plus_5));
    }
    this.existenceParser.register(stmt, select);
    ArrayList<Attribute> attributes = CollectionLiterals.<Attribute>newArrayList();
    EList<SelectArgument> _arguments = stmt.getArguments();
    for (final SelectArgument arg : _arguments) {
      Attribute _attribute = arg.getAttribute();
      boolean _tripleNotEquals_2 = (_attribute != null);
      if (_tripleNotEquals_2) {
        attributes.add(arg.getAttribute());
      }
    }
    if ((((!this.checkIfSelectAll(attributes)) || (!this.cacheService.getQueryCache().getQueryAggregations(stmt).isEmpty())) || 
      (!this.cacheService.getQueryCache().getQueryExpressions(stmt).isEmpty()))) {
      return this.cacheService.getOperatorCache().registerOperator(this.projectionParser.parse(stmt, select));
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
        String _buildJoin = this.joinParser.buildJoin(select.getSources());
        return this.checkForGroupAttributes(aggregateOperator, select, 
          this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin }));
      } else {
        if ((mapOperator != null)) {
          return this.joinParser.buildJoin(select.getSources());
        } else {
          if ((aggregateOperator != null)) {
            List<String> _xifexpression_2 = null;
            ExpressionsModel _predicates = select.getPredicates();
            boolean _tripleNotEquals = (_predicates != null);
            if (_tripleNotEquals) {
              final java.util.function.Function<Attribute, String> _function = (Attribute e) -> {
                return e.getName();
              };
              _xifexpression_2 = EcoreUtil2.<Attribute>getAllContentsOfType(select.getPredicates(), Attribute.class).stream().<String>map(_function).collect(
                Collectors.<String>toList());
            } else {
              _xifexpression_2 = null;
            }
            List<String> predicateAttributes = _xifexpression_2;
            boolean _containsAll = this.cacheService.getAggregationAttributeCache().containsAll(
              this.cacheService.getQueryCache().getProjectionAttributes(select));
            if (_containsAll) {
              if ((((predicateAttributes != null) && (!predicateAttributes.isEmpty())) && 
                this.cacheService.getAggregationAttributeCache().containsAll(predicateAttributes))) {
                return aggregateOperator;
              } else {
                String _buildJoin_1 = this.joinParser.buildJoin(select.getSources());
                return this.checkForGroupAttributes(aggregateOperator, select, 
                  this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin_1 }));
              }
            } else {
              String _buildJoin_2 = this.joinParser.buildJoin(select.getSources());
              return this.checkForGroupAttributes(aggregateOperator, select, 
                this.joinParser.buildJoin(new String[] { aggregateOperator, _buildJoin_2 }));
            }
          }
        }
      }
    }
    return this.joinParser.buildJoin(select.getSources());
  }
  
  private String checkForGroupAttributes(final String aggregateOperator, final SimpleSelect select, final String output) {
    boolean _contains = this.cacheService.getOperatorCache().getOperator(aggregateOperator).contains("group_by");
    if (_contains) {
      String join = this.joinParser.buildJoin(select.getSources());
      ArrayList<String> groupAttributes = CollectionLiterals.<String>newArrayList();
      for (int i = 0; (i < select.getOrder().size()); i++) {
        {
          String groupAttribute = select.getOrder().get(i).getName();
          groupAttributes.add(groupAttribute);
          groupAttributes.add(((groupAttribute + "_groupAttribute#") + Integer.valueOf(i)));
        }
      }
      Pair<String, String> _mappedTo = Pair.<String, String>of("pairs", "true");
      String _generateListString = this.utilityService.generateListString(groupAttributes);
      Pair<String, String> _mappedTo_1 = Pair.<String, String>of("aliases", _generateListString);
      Pair<String, String> _mappedTo_2 = Pair.<String, String>of("input", aggregateOperator);
      String _build = this.builder.build(RenameAO.class, 
        CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1, _mappedTo_2));
      return this.joinParser.buildJoin(
        new String[] { _build, join });
    }
    return output;
  }
  
  public boolean checkIfSelectAll(final List<Attribute> attributes) {
    boolean _isEmpty = attributes.isEmpty();
    if (_isEmpty) {
      return true;
    } else {
      for (final Attribute attribute : attributes) {
        boolean _contains = attribute.getName().contains(".*");
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
          Collection<SelectExpression> expressions = this.cacheService.getQueryCache().getQueryExpressions(select);
          if (((expressions != null) && (!expressions.isEmpty()))) {
            result = this.projectionParser.parse(expressions, null);
            operatorName = result[1].toString();
          }
          break;
        case AGGREGATE:
          Collection<SelectExpression> aggregations = this.cacheService.getQueryCache().getQueryAggregations(select);
          if (((aggregations != null) && (!aggregations.isEmpty()))) {
            operatorName = this.cacheService.getOperatorCache().registerOperator(result[1].toString());
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
