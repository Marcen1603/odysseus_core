package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AndPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AttributeRef;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Bracket;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Comparision;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicateRef;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Equality;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExistPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Minus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NOT;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.OrPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Plus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.QuantificationPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IExistenceParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IPredicateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class CQLPredicateParser implements IPredicateParser {
  private final Logger log = LoggerFactory.getLogger(CQLPredicateParser.class);
  
  private IUtilityService utilityService;
  
  private ICacheService cacheService;
  
  private IAttributeNameParser attributeParser;
  
  private ISelectParser selectParser;
  
  private IExistenceParser existenceParser;
  
  private String predicateString;
  
  private List<String> predicateStringList;
  
  private CharSequence lastPredicateElement;
  
  @Inject
  public CQLPredicateParser(final IUtilityService utilityService, final ICacheService cacheService, final IAttributeNameParser attributeParser, final ISelectParser selectParser, final IExistenceParser existenceParser) {
    this.utilityService = utilityService;
    this.cacheService = cacheService;
    this.attributeParser = attributeParser;
    this.selectParser = selectParser;
    this.existenceParser = existenceParser;
    this.predicateString = "";
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
    this.predicateStringList = _newArrayList;
    this.lastPredicateElement = "";
  }
  
  public CharSequence parse(final Expression e) {
    EList<EObject> _eContents = e.eContents();
    boolean _isEmpty = _eContents.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      boolean _matched = false;
      if (e instanceof OrPredicate) {
        _matched=true;
        Expression _left = ((OrPredicate)e).getLeft();
        this.parse(_left);
        this.buildPredicateString("||");
        Expression _right = ((OrPredicate)e).getRight();
        this.parse(_right);
      }
      if (!_matched) {
        if (e instanceof AndPredicate) {
          _matched=true;
          Expression _left = ((AndPredicate)e).getLeft();
          this.parse(_left);
          this.buildPredicateString("&&");
          Expression _right = ((AndPredicate)e).getRight();
          this.parse(_right);
        }
      }
      if (!_matched) {
        if (e instanceof Equality) {
          _matched=true;
          Expression _left = ((Equality)e).getLeft();
          this.parse(_left);
          String _op = ((Equality)e).getOp();
          boolean _equals = _op.equals("=");
          if (_equals) {
            this.buildPredicateString("==");
          } else {
            String _op_1 = ((Equality)e).getOp();
            this.buildPredicateString(_op_1);
          }
          Expression _right = ((Equality)e).getRight();
          this.parse(_right);
        }
      }
      if (!_matched) {
        if (e instanceof Comparision) {
          _matched=true;
          Expression _left = ((Comparision)e).getLeft();
          this.parse(_left);
          String _op = ((Comparision)e).getOp();
          this.buildPredicateString(_op);
          Expression _right = ((Comparision)e).getRight();
          this.parse(_right);
        }
      }
      if (!_matched) {
        if (e instanceof Plus) {
          _matched=true;
          Expression _left = ((Plus)e).getLeft();
          this.parse(_left);
          this.buildPredicateString("+");
          Expression _right = ((Plus)e).getRight();
          this.parse(_right);
        }
      }
      if (!_matched) {
        if (e instanceof Minus) {
          _matched=true;
          Expression _left = ((Minus)e).getLeft();
          this.parse(_left);
          this.buildPredicateString("-");
          Expression _right = ((Minus)e).getRight();
          this.parse(_right);
        }
      }
      if (!_matched) {
        if (e instanceof MulOrDiv) {
          _matched=true;
          Expression _left = ((MulOrDiv)e).getLeft();
          this.parse(_left);
          String _op = ((MulOrDiv)e).getOp();
          this.buildPredicateString(_op);
          Expression _right = ((MulOrDiv)e).getRight();
          this.parse(_right);
        }
      }
      if (!_matched) {
        if (e instanceof NOT) {
          _matched=true;
          this.buildPredicateString("!");
          Expression _expression = ((NOT)e).getExpression();
          this.parse(_expression);
        }
      }
      if (!_matched) {
        if (e instanceof Bracket) {
          _matched=true;
          this.buildPredicateString("(");
          Expression _inner = ((Bracket)e).getInner();
          this.parse(_inner);
          this.buildPredicateString(")");
        }
      }
      if (!_matched) {
        if (e instanceof AttributeRef) {
          _matched=true;
          Attribute _value = ((AttributeRef)e).getValue();
          String _parse = this.attributeParser.parse(((Attribute) _value));
          this.buildPredicateString(_parse);
        }
      }
      if (!_matched) {
        if (e instanceof ComplexPredicateRef) {
          _matched=true;
          ComplexPredicate _value = ((ComplexPredicateRef)e).getValue();
          ComplexPredicate complexPredicate = ((ComplexPredicate) _value);
          QuantificationPredicate quantification = null;
          ExistPredicate exists = null;
          InPredicate in = null;
          QuantificationPredicate _quantification = complexPredicate.getQuantification();
          QuantificationPredicate _quantification_1 = (quantification = _quantification);
          boolean _tripleNotEquals = (_quantification_1 != null);
          if (_tripleNotEquals) {
            String type = "EXISTS";
            String operator = quantification.getOperator();
            String _predicate = quantification.getPredicate();
            boolean _equalsIgnoreCase = _predicate.equalsIgnoreCase("ALL");
            if (_equalsIgnoreCase) {
              type = "NOT_EXISTS";
              boolean _equals = operator.equals(">=");
              if (_equals) {
                operator = "<";
              } else {
                boolean _equals_1 = operator.equals(">");
                if (_equals_1) {
                  operator = "<=";
                } else {
                  boolean _equals_2 = operator.equals("<=");
                  if (_equals_2) {
                    operator = ">";
                  } else {
                    boolean _equals_3 = operator.equals("<");
                    if (_equals_3) {
                      operator = ">=";
                    }
                  }
                }
              }
            }
            ArrayList<String> predicateStringListBackup = new ArrayList<String>(this.predicateStringList);
            ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
            this.predicateStringList = _newArrayList;
            String predicateBackup = this.predicateString;
            this.predicateString = "";
            InnerSelect _select = complexPredicate.getSelect();
            SimpleSelect select = _select.getSelect();
            this.selectParser.prepare(select);
            String predicate = "";
            ExpressionsModel _predicates = select.getPredicates();
            boolean _tripleEquals = (_predicates == null);
            if (_tripleEquals) {
              this.selectParser.parse(select);
              QueryCache _queryCache = this.cacheService.getQueryCache();
              Collection<QueryCache.QueryAttribute> _projectionAttributes = _queryCache.getProjectionAttributes(select);
              for (final QueryCache.QueryAttribute attribute : _projectionAttributes) {
                String _predicate_1 = predicate;
                Attribute _attribute = quantification.getAttribute();
                String _name = _attribute.getName();
                String _plus = (_name + operator);
                String _plus_1 = (_plus + attribute.name);
                String _plus_2 = (_plus_1 + "&&");
                predicate = (_predicate_1 + _plus_2);
              }
              int _lastIndexOf = predicate.lastIndexOf("&");
              int _minus = (_lastIndexOf - 1);
              String _substring = predicate.substring(0, _minus);
              predicate = _substring;
            } else {
              this.selectParser.parseWithPredicate(select);
              QueryCache _queryCache_1 = this.cacheService.getQueryCache();
              Collection<QueryCache.QueryAttribute> _projectionAttributes_1 = _queryCache_1.getProjectionAttributes(select);
              for (final QueryCache.QueryAttribute attribute_1 : _projectionAttributes_1) {
                String _predicate_2 = predicate;
                Attribute _attribute_1 = quantification.getAttribute();
                String _name_1 = _attribute_1.getName();
                String _plus_3 = (_name_1 + operator);
                String _plus_4 = (_plus_3 + attribute_1.name);
                String _plus_5 = (_plus_4 + "&&");
                predicate = (_predicate_2 + _plus_5);
              }
              int _lastIndexOf_1 = predicate.lastIndexOf("&");
              int _minus_1 = (_lastIndexOf_1 - 1);
              String _substring_1 = predicate.substring(0, _minus_1);
              predicate = _substring_1;
            }
            Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
            args.put("type", type);
            OperatorCache _operatorCache = this.cacheService.getOperatorCache();
            String _lastOperatorId = _operatorCache.lastOperatorId();
            args.put("input", _lastOperatorId);
            args.put("predicate", predicate);
            Collection<Map<String, String>> _operators = this.existenceParser.getOperators();
            _operators.add(args);
            this.predicateString = predicateBackup;
            ArrayList<String> _arrayList = new ArrayList<String>(predicateStringListBackup);
            this.predicateStringList = _arrayList;
          } else {
            ExistPredicate _exists = complexPredicate.getExists();
            ExistPredicate _exists_1 = (exists = _exists);
            boolean _tripleNotEquals_1 = (_exists_1 != null);
            if (_tripleNotEquals_1) {
              String type_1 = "EXISTS";
              boolean _equals_4 = this.lastPredicateElement.equals("!");
              if (_equals_4) {
                type_1 = "NOT_EXISTS";
                int _size = this.predicateStringList.size();
                int _minus_2 = (_size - 1);
                this.predicateStringList.remove(_minus_2);
                int _size_1 = this.predicateStringList.size();
                int _minus_3 = (_size_1 - 1);
                boolean _greaterThan = (_minus_3 > 0);
                if (_greaterThan) {
                  int _size_2 = this.predicateStringList.size();
                  int index = (_size_2 - 2);
                  String element = this.predicateStringList.get(index);
                  if ((element.equals("&&") || element.equals("||"))) {
                    this.predicateStringList.remove(index);
                  }
                }
              } else {
                if ((this.lastPredicateElement.equals("&&") || this.lastPredicateElement.equals("||"))) {
                  int _size_3 = this.predicateStringList.size();
                  boolean _greaterThan_1 = (_size_3 > 0);
                  if (_greaterThan_1) {
                    int _size_4 = this.predicateStringList.size();
                    int _minus_4 = (_size_4 - 1);
                    this.predicateStringList.remove(_minus_4);
                  }
                }
              }
              this.parseComplexPredicate(complexPredicate, type_1);
            } else {
              InPredicate _in = complexPredicate.getIn();
              InPredicate _in_1 = (in = _in);
              boolean _tripleNotEquals_2 = (_in_1 != null);
              if (_tripleNotEquals_2) {
                String type_2 = "EXISTS";
                String operator_1 = "==";
                ArrayList<String> predicateStringListBackup_1 = new ArrayList<String>(this.predicateStringList);
                ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList();
                this.predicateStringList = _newArrayList_1;
                String predicateBackup_1 = this.predicateString;
                this.predicateString = "";
                InnerSelect _select_1 = complexPredicate.getSelect();
                SimpleSelect select_1 = _select_1.getSelect();
                this.selectParser.prepare(select_1);
                String predicate_1 = "";
                ExpressionsModel _predicates_1 = select_1.getPredicates();
                boolean _tripleEquals_1 = (_predicates_1 == null);
                if (_tripleEquals_1) {
                  this.selectParser.parse(select_1);
                  QueryCache _queryCache_2 = this.cacheService.getQueryCache();
                  Collection<QueryCache.QueryAttribute> _projectionAttributes_2 = _queryCache_2.getProjectionAttributes(select_1);
                  for (final QueryCache.QueryAttribute attribute_2 : _projectionAttributes_2) {
                    String _predicate_3 = predicate_1;
                    Attribute _attribute_2 = in.getAttribute();
                    String _name_2 = _attribute_2.getName();
                    String _plus_6 = (_name_2 + operator_1);
                    String _plus_7 = (_plus_6 + attribute_2.name);
                    String _plus_8 = (_plus_7 + "&&");
                    predicate_1 = (_predicate_3 + _plus_8);
                  }
                  int _lastIndexOf_2 = predicate_1.lastIndexOf("&");
                  int _minus_5 = (_lastIndexOf_2 - 1);
                  String _substring_2 = predicate_1.substring(0, _minus_5);
                  predicate_1 = _substring_2;
                } else {
                  this.selectParser.parseWithPredicate(select_1);
                  QueryCache _queryCache_3 = this.cacheService.getQueryCache();
                  Collection<QueryCache.QueryAttribute> _projectionAttributes_3 = _queryCache_3.getProjectionAttributes(select_1);
                  for (final QueryCache.QueryAttribute attribute_3 : _projectionAttributes_3) {
                    String _predicate_4 = predicate_1;
                    Attribute _attribute_3 = in.getAttribute();
                    String _name_3 = _attribute_3.getName();
                    String _plus_9 = (_name_3 + operator_1);
                    String _plus_10 = (_plus_9 + attribute_3.name);
                    String _plus_11 = (_plus_10 + "&&");
                    predicate_1 = (_predicate_4 + _plus_11);
                  }
                  int _lastIndexOf_3 = predicate_1.lastIndexOf("&");
                  int _minus_6 = (_lastIndexOf_3 - 1);
                  String _substring_3 = predicate_1.substring(0, _minus_6);
                  predicate_1 = _substring_3;
                }
                Map<String, String> args_1 = CollectionLiterals.<String, String>newHashMap();
                args_1.put("type", type_2);
                OperatorCache _operatorCache_1 = this.cacheService.getOperatorCache();
                String _lastOperatorId_1 = _operatorCache_1.lastOperatorId();
                args_1.put("input", _lastOperatorId_1);
                args_1.put("predicate", predicate_1);
                Collection<Map<String, String>> _operators_1 = this.existenceParser.getOperators();
                _operators_1.add(args_1);
                this.predicateString = predicateBackup_1;
                ArrayList<String> _arrayList_1 = new ArrayList<String>(predicateStringListBackup_1);
                this.predicateStringList = _arrayList_1;
              }
            }
          }
        }
      }
    } else {
      String str = "";
      boolean _matched_1 = false;
      if (e instanceof IntConstant) {
        _matched_1=true;
        int _value = ((IntConstant)e).getValue();
        String _plus = (Integer.valueOf(_value) + "");
        str = _plus;
      }
      if (!_matched_1) {
        if (e instanceof FloatConstant) {
          _matched_1=true;
          String _value = ((FloatConstant)e).getValue();
          String _plus = (_value + "");
          str = _plus;
        }
      }
      if (!_matched_1) {
        if (e instanceof StringConstant) {
          _matched_1=true;
          String _value = ((StringConstant)e).getValue();
          String _plus = ("\"" + _value);
          String _plus_1 = (_plus + "\"");
          str = _plus_1;
        }
      }
      if (!_matched_1) {
        if (e instanceof BoolConstant) {
          _matched_1=true;
          String _value = ((BoolConstant)e).getValue();
          String _plus = (_value + "");
          str = _plus;
        }
      }
      this.buildPredicateString(str);
    }
    this.log.info(("PREDICATESTRING::" + this.predicateString));
    return this.predicateString;
  }
  
  @Override
  public CharSequence parse(final List<Expression> expressions) {
    this.predicateString = "";
    for (int i = 0; (i < (expressions.size() - 1)); i++) {
      Expression _get = expressions.get(i);
      if ((_get instanceof AttributeRef)) {
      } else {
        Expression _get_1 = expressions.get(i);
        CharSequence _parse = this.parse(_get_1);
        String _string = _parse.toString();
        this.predicateString = _string;
        this.buildPredicateString("&&");
      }
    }
    int _size = expressions.size();
    int _minus = (_size - 1);
    Expression _get = expressions.get(_minus);
    CharSequence _parse = this.parse(_get);
    String _string = _parse.toString();
    this.predicateString = _string;
    return this.predicateString;
  }
  
  @Override
  public CharSequence parsePredicateString(final List<String> predicateString) {
    int _size = predicateString.size();
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      if ((predicateString.get(0).equals("&&") || predicateString.get(0).equals("||"))) {
        predicateString.remove(0);
      }
      if ((predicateString.get((predicateString.size() - 1)).equals("&&") || 
        predicateString.get((predicateString.size() - 1)).equals("||"))) {
        int _size_1 = predicateString.size();
        int _minus = (_size_1 - 1);
        predicateString.remove(_minus);
      }
    }
    String predicate = "";
    for (final String pred : predicateString) {
      String _predicate = predicate;
      predicate = (_predicate + pred);
    }
    return predicate;
  }
  
  public List<String> parseComplexPredicate(final ComplexPredicate complexPredicate, final String type) {
    List<String> _xblockexpression = null;
    {
      ArrayList<String> predicateStringListBackup = new ArrayList<String>(this.predicateStringList);
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      this.predicateStringList = _newArrayList;
      String predicateBackup = this.predicateString;
      this.predicateString = "";
      Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
      args.put("type", type);
      InnerSelect _select = complexPredicate.getSelect();
      SimpleSelect _select_1 = _select.getSelect();
      SimpleSelect subQuery = ((SimpleSelect) _select_1);
      this.selectParser.prepare(subQuery);
      this.selectParser.parse(subQuery);
      InnerSelect _select_2 = complexPredicate.getSelect();
      SimpleSelect _select_3 = _select_2.getSelect();
      ExpressionsModel _predicates = _select_3.getPredicates();
      EList<Expression> _elements = _predicates.getElements();
      Expression _get = _elements.get(0);
      this.parse(_get);
      args.put("predicate", this.predicateString);
      OperatorCache _operatorCache = this.cacheService.getOperatorCache();
      String _lastOperatorId = _operatorCache.lastOperatorId();
      args.put("input", _lastOperatorId);
      Collection<Map<String, String>> _operators = this.existenceParser.getOperators();
      _operators.add(args);
      this.predicateString = predicateBackup;
      ArrayList<String> _arrayList = new ArrayList<String>(predicateStringListBackup);
      _xblockexpression = this.predicateStringList = _arrayList;
    }
    return _xblockexpression;
  }
  
  public CharSequence buildPredicateString(final CharSequence sequence) {
    this.lastPredicateElement = sequence;
    String _string = sequence.toString();
    this.predicateStringList.add(_string);
    String _predicateString = this.predicateString;
    return this.predicateString = (_predicateString + sequence);
  }
  
  @Override
  public void clear() {
    this.predicateString = "";
    this.lastPredicateElement = "";
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
    this.predicateStringList = _newArrayList;
  }
  
  @Override
  public List<String> getPredicateStringList() {
    return this.predicateStringList;
  }
}
