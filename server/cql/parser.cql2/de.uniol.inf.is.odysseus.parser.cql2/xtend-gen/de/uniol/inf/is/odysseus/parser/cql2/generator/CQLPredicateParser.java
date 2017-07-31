package de.uniol.inf.is.odysseus.parser.cql2.generator;

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
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Minus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NOT;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.OrPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Plus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.QuantificationPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGeneratorUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class CQLPredicateParser {
  private CQLGenerator generator;
  
  private String predicateString;
  
  private List<String> predicateStringList;
  
  private CharSequence lastPredicateElement;
  
  public CQLPredicateParser(final CQLGenerator generator) {
    this.generator = generator;
    this.predicateString = "";
    this.predicateStringList = CollectionLiterals.<String>newArrayList();
    this.lastPredicateElement = "";
  }
  
  public CharSequence parse(final Expression e) {
    boolean _isEmpty = e.eContents().isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      boolean _matched = false;
      if (e instanceof OrPredicate) {
        _matched=true;
        this.parse(((OrPredicate)e).getLeft());
        this.buildPredicateString("||");
        this.parse(((OrPredicate)e).getRight());
      }
      if (!_matched) {
        if (e instanceof AndPredicate) {
          _matched=true;
          this.parse(((AndPredicate)e).getLeft());
          this.buildPredicateString("&&");
          this.parse(((AndPredicate)e).getRight());
        }
      }
      if (!_matched) {
        if (e instanceof Equality) {
          _matched=true;
          this.parse(((Equality)e).getLeft());
          boolean _equals = ((Equality)e).getOp().equals("=");
          if (_equals) {
            this.buildPredicateString("==");
          } else {
            this.buildPredicateString(((Equality)e).getOp());
          }
          this.parse(((Equality)e).getRight());
        }
      }
      if (!_matched) {
        if (e instanceof Comparision) {
          _matched=true;
          this.parse(((Comparision)e).getLeft());
          this.buildPredicateString(((Comparision)e).getOp());
          this.parse(((Comparision)e).getRight());
        }
      }
      if (!_matched) {
        if (e instanceof Plus) {
          _matched=true;
          this.parse(((Plus)e).getLeft());
          this.buildPredicateString("+");
          this.parse(((Plus)e).getRight());
        }
      }
      if (!_matched) {
        if (e instanceof Minus) {
          _matched=true;
          this.parse(((Minus)e).getLeft());
          this.buildPredicateString("-");
          this.parse(((Minus)e).getRight());
        }
      }
      if (!_matched) {
        if (e instanceof MulOrDiv) {
          _matched=true;
          this.parse(((MulOrDiv)e).getLeft());
          this.buildPredicateString(((MulOrDiv)e).getOp());
          this.parse(((MulOrDiv)e).getRight());
        }
      }
      if (!_matched) {
        if (e instanceof NOT) {
          _matched=true;
          this.buildPredicateString("!");
          this.parse(((NOT)e).getExpression());
        }
      }
      if (!_matched) {
        if (e instanceof Bracket) {
          _matched=true;
          this.buildPredicateString("(");
          this.parse(((Bracket)e).getInner());
          this.buildPredicateString(")");
        }
      }
      if (!_matched) {
        if (e instanceof AttributeRef) {
          _matched=true;
          Attribute _value = ((AttributeRef)e).getValue();
          this.buildPredicateString(this.generator.getAttributename(((Attribute) _value)));
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
          if (((quantification = complexPredicate.getQuantification()) != null)) {
            String type = "EXISTS";
            String operator = quantification.getOperator();
            boolean _equalsIgnoreCase = quantification.getPredicate().equalsIgnoreCase("ALL");
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
            this.predicateStringList = CollectionLiterals.<String>newArrayList();
            String predicateBackup = this.predicateString;
            this.predicateString = "";
            SimpleSelect select = complexPredicate.getSelect().getSelect();
            this.generator.prepareParsingSelect(select);
            String predicate = "";
            ExpressionsModel _predicates = select.getPredicates();
            boolean _tripleEquals = (_predicates == null);
            if (_tripleEquals) {
              this.generator.parseSelectWithoutPredicate(select);
              List<String> _get = CQLGeneratorUtil.getProjectionAttributes().get(select);
              for (final String attribute : _get) {
                String _predicate = predicate;
                String _name = quantification.getAttribute().getName();
                String _plus = (_name + operator);
                String _plus_1 = (_plus + attribute);
                String _plus_2 = (_plus_1 + "&&");
                predicate = (_predicate + _plus_2);
              }
              int _lastIndexOf = predicate.lastIndexOf("&");
              int _minus = (_lastIndexOf - 1);
              predicate = predicate.substring(0, _minus);
            } else {
              this.generator.parseSelectWithPredicate(select);
              List<String> _get_1 = CQLGeneratorUtil.getProjectionAttributes().get(select);
              for (final String attribute_1 : _get_1) {
                String _predicate_1 = predicate;
                String _name_1 = quantification.getAttribute().getName();
                String _plus_3 = (_name_1 + operator);
                String _plus_4 = (_plus_3 + attribute_1);
                String _plus_5 = (_plus_4 + "&&");
                predicate = (_predicate_1 + _plus_5);
              }
              int _lastIndexOf_1 = predicate.lastIndexOf("&");
              int _minus_1 = (_lastIndexOf_1 - 1);
              predicate = predicate.substring(0, _minus_1);
            }
            Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
            args.put("type", type);
            args.put("input", this.generator.getLastOperator());
            Set<Map.Entry<String, List<String>>> _entrySet = this.generator.queryAttributes.get(select).entrySet();
            for (final Map.Entry<String, List<String>> l : _entrySet) {
              List<String> _value_1 = l.getValue();
              for (final String s : _value_1) {
                {
                  String attributename = s;
                  boolean _contains = attributename.contains(".");
                  boolean _not_1 = (!_contains);
                  if (_not_1) {
                    String _key = l.getKey();
                    String _plus_6 = (_key + ".");
                    String _plus_7 = (_plus_6 + attributename);
                    attributename = _plus_7;
                  }
                  predicate = predicate.replace(attributename, attributename.replace(".", "_"));
                }
              }
            }
            args.put("predicate", predicate);
            this.generator.registry_existenceOperators.add(args);
            this.predicateString = predicateBackup;
            ArrayList<String> _arrayList = new ArrayList<String>(predicateStringListBackup);
            this.predicateStringList = _arrayList;
          } else {
            if (((exists = complexPredicate.getExists()) != null)) {
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
              if (((in = complexPredicate.getIn()) != null)) {
                String type_2 = "EXISTS";
                String operator_1 = "==";
                ArrayList<String> predicateStringListBackup_1 = new ArrayList<String>(this.predicateStringList);
                this.predicateStringList = CollectionLiterals.<String>newArrayList();
                String predicateBackup_1 = this.predicateString;
                this.predicateString = "";
                SimpleSelect select_1 = complexPredicate.getSelect().getSelect();
                this.generator.prepareParsingSelect(select_1);
                String predicate_1 = "";
                ExpressionsModel _predicates_1 = select_1.getPredicates();
                boolean _tripleEquals_1 = (_predicates_1 == null);
                if (_tripleEquals_1) {
                  this.generator.parseSelectWithoutPredicate(select_1);
                  List<String> _get_2 = CQLGeneratorUtil.getProjectionAttributes().get(select_1);
                  for (final String attribute_2 : _get_2) {
                    String _predicate_2 = predicate_1;
                    String _name_2 = in.getAttribute().getName();
                    String _plus_6 = (_name_2 + operator_1);
                    String _plus_7 = (_plus_6 + attribute_2);
                    String _plus_8 = (_plus_7 + "&&");
                    predicate_1 = (_predicate_2 + _plus_8);
                  }
                  int _lastIndexOf_2 = predicate_1.lastIndexOf("&");
                  int _minus_5 = (_lastIndexOf_2 - 1);
                  predicate_1 = predicate_1.substring(0, _minus_5);
                } else {
                  this.generator.parseSelectWithPredicate(select_1);
                  List<String> _get_3 = CQLGeneratorUtil.getProjectionAttributes().get(select_1);
                  for (final String attribute_3 : _get_3) {
                    String _predicate_3 = predicate_1;
                    String _name_3 = in.getAttribute().getName();
                    String _plus_9 = (_name_3 + operator_1);
                    String _plus_10 = (_plus_9 + attribute_3);
                    String _plus_11 = (_plus_10 + "&&");
                    predicate_1 = (_predicate_3 + _plus_11);
                  }
                  int _lastIndexOf_3 = predicate_1.lastIndexOf("&");
                  int _minus_6 = (_lastIndexOf_3 - 1);
                  predicate_1 = predicate_1.substring(0, _minus_6);
                }
                Set<Map.Entry<String, List<String>>> _entrySet_1 = this.generator.queryAttributes.get(select_1).entrySet();
                for (final Map.Entry<String, List<String>> l_1 : _entrySet_1) {
                  List<String> _value_2 = l_1.getValue();
                  for (final String s_1 : _value_2) {
                    {
                      String attributename = s_1;
                      boolean _contains = attributename.contains(".");
                      boolean _not_1 = (!_contains);
                      if (_not_1) {
                        String _key = l_1.getKey();
                        String _plus_12 = (_key + ".");
                        String _plus_13 = (_plus_12 + attributename);
                        attributename = _plus_13;
                      }
                      predicate_1 = predicate_1.replace(attributename, attributename.replace(".", "_"));
                    }
                  }
                }
                Map<String, String> args_1 = CollectionLiterals.<String, String>newHashMap();
                args_1.put("type", type_2);
                args_1.put("input", this.generator.getLastOperator());
                args_1.put("predicate", predicate_1);
                this.generator.registry_existenceOperators.add(args_1);
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
    return this.predicateString;
  }
  
  public CharSequence parse(final List<Expression> expressions) {
    this.predicateString = "";
    for (int i = 0; (i < (expressions.size() - 1)); i++) {
      Expression _get = expressions.get(i);
      if ((_get instanceof AttributeRef)) {
      } else {
        this.predicateString = this.parse(expressions.get(i)).toString();
        this.buildPredicateString("&&");
      }
    }
    int _size = expressions.size();
    int _minus = (_size - 1);
    this.predicateString = this.parse(expressions.get(_minus)).toString();
    return this.predicateString;
  }
  
  public String parsePredicateString(final List<String> predicateString) {
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
      this.predicateStringList = CollectionLiterals.<String>newArrayList();
      String predicateBackup = this.predicateString;
      this.predicateString = "";
      Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
      args.put("type", type);
      SimpleSelect _select = complexPredicate.getSelect().getSelect();
      SimpleSelect subQuery = ((SimpleSelect) _select);
      this.generator.prepareParsingSelect(subQuery);
      this.generator.parseSelectWithoutPredicate(subQuery);
      this.parse(complexPredicate.getSelect().getSelect().getPredicates().getElements().get(0));
      Set<Map.Entry<String, List<String>>> _entrySet = this.generator.queryAttributes.get(subQuery).entrySet();
      for (final Map.Entry<String, List<String>> l : _entrySet) {
        List<String> _value = l.getValue();
        for (final String s : _value) {
          {
            String attributename = s;
            boolean _contains = attributename.contains(".");
            boolean _not = (!_contains);
            if (_not) {
              String _key = l.getKey();
              String _plus = (_key + ".");
              String _plus_1 = (_plus + attributename);
              attributename = _plus_1;
            }
            this.predicateString = this.predicateString.replace(attributename, attributename.replace(".", "_"));
          }
        }
      }
      args.put("predicate", this.predicateString);
      args.put("input", this.generator.getLastOperator());
      this.generator.registry_existenceOperators.add(args);
      this.predicateString = predicateBackup;
      ArrayList<String> _arrayList = new ArrayList<String>(predicateStringListBackup);
      _xblockexpression = this.predicateStringList = _arrayList;
    }
    return _xblockexpression;
  }
  
  public CharSequence buildPredicateString(final CharSequence sequence) {
    this.lastPredicateElement = sequence;
    this.predicateStringList.add(sequence.toString());
    String _predicateString = this.predicateString;
    return this.predicateString = (_predicateString + sequence);
  }
  
  public List<String> clear() {
    List<String> _xblockexpression = null;
    {
      this.predicateString = "";
      _xblockexpression = this.predicateStringList = CollectionLiterals.<String>newArrayList();
    }
    return _xblockexpression;
  }
  
  public List<String> getPredicateStringList() {
    return this.predicateStringList;
  }
}
