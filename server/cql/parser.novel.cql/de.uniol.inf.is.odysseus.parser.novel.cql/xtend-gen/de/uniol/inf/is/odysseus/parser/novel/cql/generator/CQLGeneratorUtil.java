package de.uniol.inf.is.odysseus.parser.novel.cql.generator;

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.AttributeStruct;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.SourceStruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class CQLGeneratorUtil {
  public static CQLGeneratorUtil instance = null;
  
  public static CQLGeneratorUtil getInstance(final CQLGenerator generator) {
    CQLGeneratorUtil _xifexpression = null;
    if ((CQLGeneratorUtil.instance == null)) {
      CQLGeneratorUtil _cQLGeneratorUtil = new CQLGeneratorUtil(generator);
      _xifexpression = CQLGeneratorUtil.instance = _cQLGeneratorUtil;
    } else {
      _xifexpression = CQLGeneratorUtil.instance;
    }
    return _xifexpression;
  }
  
  private CQLGenerator generator;
  
  private List<SourceStruct> registry_Sources = CollectionLiterals.<SourceStruct>newArrayList();
  
  private Map<String, String> registry_Expressions = CollectionLiterals.<String, String>newHashMap();
  
  private List<String> registry_AggregationAttributes = CollectionLiterals.<String>newArrayList();
  
  private Map<String, Set<String>> registry_SubQuerySources = CollectionLiterals.<String, Set<String>>newHashMap();
  
  private CQLGeneratorUtil(final CQLGenerator generator) {
    this.generator = generator;
  }
  
  /**
   * Returns all attributes with its corresponding sources from a select statement.
   */
  public static Map<String, List<String>> getSelectedAttributes(final SimpleSelect select, final Map<String, List<String>> var2) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method getSource(String) is undefined"
      + "\nThe method or field registry_AttributeAliases is undefined"
      + "\nThe method addToMap(Map<String, List<String>>, String, String) is undefined"
      + "\nThe method addToMap(Map<String, List<String>>, String, String) is undefined"
      + "\nThe method or field projectionAttributes is undefined"
      + "\nThe method or field projectionSources is undefined"
      + "\nThe method or field isSourceAlias is undefined for the type String"
      + "\nThe method or field isAttributeAlias is undefined for the type String"
      + "\nThe method registerAttributeAliases(Attribute, String, String, String, boolean) is undefined"
      + "\nThe method getSourceCandidates(Attribute, EList<Source>) is undefined"
      + "\nThe method addToMap(Map<String, List<String>>, String, String) is undefined"
      + "\nThe method addToMap(Map<String, List<String>>, String, String) is undefined"
      + "\nThe method registerSourceAlias(String, String) is undefined"
      + "\nThe method registerAttributeAliases(Attribute, String, String, String, Boolean) is undefined"
      + "\nThe method addToMap(Map<String, List<String>>, String, String) is undefined"
      + "\nThe method registerAttributeAliases(Attribute, String, String, String, Boolean) is undefined"
      + "\nThe method computeProjectionAttributes(String[], SimpleSelect, Attribute, String, String, String) is undefined"
      + "\nThe method computeProjectionAttributes(String[], SimpleSelect, Object, Object, Object, Object) is undefined"
      + "\nThe method or field projectionAttributes is undefined"
      + "\nThe method or field projectionSources is undefined"
      + "\nCannot make a static reference to the non-static method getAttributeNamesFrom from the type CQLGeneratorUtil"
      + "\nCannot make a static reference to the non-static method getSourcenameFromAlias from the type CQLGeneratorUtil"
      + "\nCannot make a static reference to the non-static method parseAttribute from the type CQLGeneratorUtil"
      + "\nfindbyName cannot be resolved"
      + "\naliases cannot be resolved"
      + "\nadd cannot be resolved"
      + "\nput cannot be resolved"
      + "\nput cannot be resolved"
      + "\nput cannot be resolved"
      + "\n&& cannot be resolved"
      + "\n! cannot be resolved"
      + "\nsize cannot be resolved"
      + "\n> cannot be resolved"
      + "\nsize cannot be resolved"
      + "\n> cannot be resolved"
      + "\n&& cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nsize cannot be resolved"
      + "\n== cannot be resolved"
      + "\nget cannot be resolved"
      + "\nsourcename cannot be resolved"
      + "\nput cannot be resolved"
      + "\nput cannot be resolved");
  }
  
  /**
   * Returns all {@link Attribute} elements from the corresponding source.
   */
  public List<String> getAttributeNamesFrom(final String srcname) {
    for (final SourceStruct source : this.registry_Sources) {
      if ((source.sourcename.equals(srcname) || source.aliases.contains(srcname))) {
        final Function<AttributeStruct, String> _function = (AttributeStruct e) -> {
          return e.attributename;
        };
        return source.attributes.stream().<String>map(_function).collect(Collectors.<String>toList());
      }
    }
    return null;
  }
  
  private String parseAdditionalOperator(final CQLGenerator.Operator operator, final SimpleSelect select) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field queryExpressions is undefined"
      + "\nThe method buildMapOperator(Object) is undefined"
      + "\nThe method or field queryAggregations is undefined"
      + "\nThe method buildAggregateOP(Object, EList<Attribute>, EList<Source>) is undefined"
      + "\nThe method registerOperator(String) is undefined"
      + "\nget cannot be resolved"
      + "\n!== cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nempty cannot be resolved"
      + "\n! cannot be resolved"
      + "\nget cannot be resolved"
      + "\n!== cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nempty cannot be resolved"
      + "\n! cannot be resolved");
  }
  
  private Object[] parseAttribute(final Attribute attribute) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field isSourceAlias is undefined for the type String");
  }
  
  public List<String> getSourceNames() {
    final Function<SourceStruct, String> _function = (SourceStruct e) -> {
      return e.sourcename;
    };
    return this.registry_Sources.stream().<String>map(_function).collect(Collectors.<String>toList());
  }
  
  public String getSourcenameFromAlias(final String sourcealias) {
    Set<Map.Entry<SourceStruct, List<String>>> _entrySet = this.getSourceAliases().entrySet();
    for (final Map.Entry<SourceStruct, List<String>> source : _entrySet) {
      boolean _contains = source.getValue().contains(sourcealias);
      if (_contains) {
        return source.getKey().sourcename;
      }
    }
    return null;
  }
  
  public AttributeStruct getAttributeFromAlias(final String alias) {
    InputOutput.<String>println(this.getAttributeAliases().toString());
    Set<Map.Entry<AttributeStruct, List<String>>> _entrySet = this.getAttributeAliases().entrySet();
    for (final Map.Entry<AttributeStruct, List<String>> entry : _entrySet) {
      boolean _contains = entry.getValue().contains(alias);
      if (_contains) {
        return entry.getKey();
      }
    }
    return null;
  }
  
  public String getAttributenameFromAlias(final String alias) {
    AttributeStruct attribute = this.getAttributeFromAlias(alias);
    if ((attribute != null)) {
      return attribute.attributename;
    }
    if ((this.registry_AggregationAttributes.contains(alias) || 
      this.registry_Expressions.keySet().contains(alias))) {
      return alias;
    }
    return null;
  }
  
  public List<String> getSourceAliasesAsList() {
    ArrayList<String> list = CollectionLiterals.<String>newArrayList();
    Collection<List<String>> _values = this.getSourceAliases().values();
    for (final List<String> l : _values) {
      list.addAll(l);
    }
    return list;
  }
  
  public Map<SourceStruct, List<String>> getSourceAliases() {
    HashMap<SourceStruct, List<String>> map = CollectionLiterals.<SourceStruct, List<String>>newHashMap();
    for (final SourceStruct source : this.registry_Sources) {
      map.put(source, source.aliases);
    }
    return map;
  }
  
  public Map<AttributeStruct, List<String>> getAttributeAliases() {
    HashMap<AttributeStruct, List<String>> map = CollectionLiterals.<AttributeStruct, List<String>>newHashMap();
    for (final SourceStruct source : this.registry_Sources) {
      for (final AttributeStruct attribute : source.attributes) {
        boolean _isEmpty = attribute.aliases.isEmpty();
        boolean _not = (!_isEmpty);
        if (_not) {
          map.put(attribute, attribute.aliases);
        }
      }
    }
    return map;
  }
}
