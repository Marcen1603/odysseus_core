package de.uniol.inf.is.odysseus.parser.cql2.generator;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.AttributeRef;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class CQLPredicateParser {
  private final Logger log = LoggerFactory.getLogger(CQLPredicateParser.class);
  
  private CQLGenerator generator;
  
  private String predicateString;
  
  private List<String> predicateStringList;
  
  private CharSequence lastPredicateElement;
  
  public CQLPredicateParser(final CQLGenerator generator) {
    this.generator = generator;
    this.predicateString = "";
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
    this.predicateStringList = _newArrayList;
    this.lastPredicateElement = "";
  }
  
  public CharSequence parse(final Expression e) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method getAttributename(Attribute) is undefined for the type CQLGenerator"
      + "\nThe method prepareParsingSelect(SimpleSelect) is undefined for the type CQLGenerator"
      + "\nThe method parseSelectWithoutPredicate(SimpleSelect) is undefined for the type CQLGenerator"
      + "\nThe method getProjectionAttributes() is undefined for the type Class<CQLGeneratorUtil>"
      + "\nThe method parseSelectWithPredicate(SimpleSelect) is undefined for the type CQLGenerator"
      + "\nThe method getProjectionAttributes() is undefined for the type Class<CQLGeneratorUtil>"
      + "\nThe method getLastOperator() is undefined for the type CQLGenerator"
      + "\nThe method getQueryAttributes(SimpleSelect) is undefined for the type Class<CQLGeneratorUtil>"
      + "\nThe method or field registry_existenceOperators is undefined for the type CQLGenerator"
      + "\nThe method prepareParsingSelect(SimpleSelect) is undefined for the type CQLGenerator"
      + "\nThe method parseSelectWithoutPredicate(SimpleSelect) is undefined for the type CQLGenerator"
      + "\nThe method getProjectionAttributes() is undefined for the type Class<CQLGeneratorUtil>"
      + "\nThe method parseSelectWithPredicate(SimpleSelect) is undefined for the type CQLGenerator"
      + "\nThe method getProjectionAttributes() is undefined for the type Class<CQLGeneratorUtil>"
      + "\nThe method getQueryAttributes(SimpleSelect) is undefined for the type Class<CQLGeneratorUtil>"
      + "\nThe method getLastOperator() is undefined for the type CQLGenerator"
      + "\nThe method or field registry_existenceOperators is undefined for the type CQLGenerator"
      + "\nget cannot be resolved"
      + "\nget cannot be resolved"
      + "\nentrySet cannot be resolved"
      + "\nadd cannot be resolved"
      + "\nget cannot be resolved"
      + "\nget cannot be resolved"
      + "\nentrySet cannot be resolved"
      + "\nadd cannot be resolved");
  }
  
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
    throw new Error("Unresolved compilation problems:"
      + "\nThe method prepareParsingSelect(SimpleSelect) is undefined for the type CQLGenerator"
      + "\nThe method parseSelectWithoutPredicate(SimpleSelect) is undefined for the type CQLGenerator"
      + "\nThe method getQueryAttributes(SimpleSelect) is undefined for the type Class<CQLGeneratorUtil>"
      + "\nThe method getLastOperator() is undefined for the type CQLGenerator"
      + "\nThe method or field registry_existenceOperators is undefined for the type CQLGenerator"
      + "\nentrySet cannot be resolved"
      + "\nadd cannot be resolved");
  }
  
  public CharSequence buildPredicateString(final CharSequence sequence) {
    this.lastPredicateElement = sequence;
    String _string = sequence.toString();
    this.predicateStringList.add(_string);
    String _predicateString = this.predicateString;
    return this.predicateString = (_predicateString + sequence);
  }
  
  public List<String> clear() {
    List<String> _xblockexpression = null;
    {
      this.predicateString = "";
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      _xblockexpression = this.predicateStringList = _newArrayList;
    }
    return _xblockexpression;
  }
  
  public List<String> getPredicateStringList() {
    return this.predicateStringList;
  }
}
