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
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Minus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NOT;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.OrPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Plus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IExistenceParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IPredicateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IQuantificationParser;
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
public class PredicateParser implements IPredicateParser {
  private final Logger log = LoggerFactory.getLogger(PredicateParser.class);
  
  private IUtilityService utilityService;
  
  private ICacheService cacheService;
  
  private IAttributeNameParser attributeParser;
  
  private ISelectParser selectParser;
  
  private IExistenceParser existenceParser;
  
  private IQuantificationParser quantificationParser;
  
  private String predicateString;
  
  private List<String> predicateStringList;
  
  private CharSequence lastPredicateElement;
  
  @Inject
  public PredicateParser(final IUtilityService utilityService, final ICacheService cacheService, final IAttributeNameParser attributeParser, final ISelectParser selectParser, final IExistenceParser existenceParser, final IQuantificationParser quantificationParser) {
    this.utilityService = utilityService;
    this.cacheService = cacheService;
    this.attributeParser = attributeParser;
    this.selectParser = selectParser;
    this.existenceParser = existenceParser;
    this.quantificationParser = quantificationParser;
    this.predicateString = "";
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
    this.predicateStringList = _newArrayList;
    this.lastPredicateElement = "";
  }
  
  public CharSequence parse(final Expression e, final SimpleSelect select) {
    EList<EObject> _eContents = e.eContents();
    boolean _isEmpty = _eContents.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      boolean _matched = false;
      if (e instanceof OrPredicate) {
        _matched=true;
        Expression _left = ((OrPredicate)e).getLeft();
        this.parse(_left, select);
        this.buildPredicateString("||");
        Expression _right = ((OrPredicate)e).getRight();
        this.parse(_right, select);
      }
      if (!_matched) {
        if (e instanceof AndPredicate) {
          _matched=true;
          Expression _left = ((AndPredicate)e).getLeft();
          this.parse(_left, select);
          this.buildPredicateString("&&");
          Expression _right = ((AndPredicate)e).getRight();
          this.parse(_right, select);
        }
      }
      if (!_matched) {
        if (e instanceof Equality) {
          _matched=true;
          Expression _left = ((Equality)e).getLeft();
          this.parse(_left, select);
          String _op = ((Equality)e).getOp();
          boolean _equals = _op.equals("=");
          if (_equals) {
            this.buildPredicateString("==");
          } else {
            String _op_1 = ((Equality)e).getOp();
            this.buildPredicateString(_op_1);
          }
          Expression _right = ((Equality)e).getRight();
          this.parse(_right, select);
        }
      }
      if (!_matched) {
        if (e instanceof Comparision) {
          _matched=true;
          Expression _left = ((Comparision)e).getLeft();
          this.parse(_left, select);
          String _op = ((Comparision)e).getOp();
          this.buildPredicateString(_op);
          Expression _right = ((Comparision)e).getRight();
          this.parse(_right, select);
        }
      }
      if (!_matched) {
        if (e instanceof Plus) {
          _matched=true;
          Expression _left = ((Plus)e).getLeft();
          this.parse(_left, select);
          this.buildPredicateString("+");
          Expression _right = ((Plus)e).getRight();
          this.parse(_right, select);
        }
      }
      if (!_matched) {
        if (e instanceof Minus) {
          _matched=true;
          Expression _left = ((Minus)e).getLeft();
          this.parse(_left, select);
          this.buildPredicateString("-");
          Expression _right = ((Minus)e).getRight();
          this.parse(_right, select);
        }
      }
      if (!_matched) {
        if (e instanceof MulOrDiv) {
          _matched=true;
          Expression _left = ((MulOrDiv)e).getLeft();
          this.parse(_left, select);
          String _op = ((MulOrDiv)e).getOp();
          this.buildPredicateString(_op);
          Expression _right = ((MulOrDiv)e).getRight();
          this.parse(_right, select);
        }
      }
      if (!_matched) {
        if (e instanceof NOT) {
          _matched=true;
          this.buildPredicateString("!");
          Expression _expression = ((NOT)e).getExpression();
          this.parse(_expression, select);
        }
      }
      if (!_matched) {
        if (e instanceof Bracket) {
          _matched=true;
          this.buildPredicateString("(");
          Expression _inner = ((Bracket)e).getInner();
          this.parse(_inner, select);
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
          this.quantificationParser.parse(_value, select);
          return "";
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
  public CharSequence parse(final List<Expression> expressions, final SimpleSelect select) {
    this.predicateString = "";
    for (int i = 0; (i < (expressions.size() - 1)); i++) {
      Expression _get = expressions.get(i);
      if ((_get instanceof AttributeRef)) {
      } else {
        Expression _get_1 = expressions.get(i);
        CharSequence _parse = this.parse(_get_1, select);
        String _string = _parse.toString();
        this.predicateString = _string;
        this.buildPredicateString("&&");
      }
    }
    int _size = expressions.size();
    int _minus = (_size - 1);
    Expression _get = expressions.get(_minus);
    CharSequence _parse = this.parse(_get, select);
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
  
  public List<String> parseComplexPredicate(final SimpleSelect select, final ComplexPredicate complexPredicate, final String type) {
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
      this.selectParser.prepare(subQuery, null);
      this.selectParser.parse(subQuery);
      InnerSelect _select_2 = complexPredicate.getSelect();
      SimpleSelect _select_3 = _select_2.getSelect();
      ExpressionsModel _predicates = _select_3.getPredicates();
      EList<Expression> _elements = _predicates.getElements();
      Expression _get = _elements.get(0);
      this.parse(_get, select);
      args.put("predicate", this.predicateString);
      OperatorCache _operatorCache = this.cacheService.getOperatorCache();
      String _last = _operatorCache.last();
      args.put("input", _last);
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
