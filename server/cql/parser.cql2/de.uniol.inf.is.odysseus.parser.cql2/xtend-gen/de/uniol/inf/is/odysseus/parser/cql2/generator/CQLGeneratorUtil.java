package de.uniol.inf.is.odysseus.parser.cql2.generator;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.AttributeStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class CQLGeneratorUtil {
  private final static Logger log = LoggerFactory.getLogger(CQLGeneratorUtil.class);
  
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
  
  private static List<SourceStruct> registry_Sources = CollectionLiterals.<SourceStruct>newArrayList();
  
  private static Map<String, String> registry_Expressions = CollectionLiterals.<String, String>newHashMap();
  
  private static List<String> registry_AggregationAttributes = CollectionLiterals.<String>newArrayList();
  
  private static Map<SelectExpression, String> registry_AggregationAttributes2 = CollectionLiterals.<SelectExpression, String>newHashMap();
  
  private static Map<String, Set<String>> registry_SubQuerySources = CollectionLiterals.<String, Set<String>>newHashMap();
  
  private static Map<String, String> registry_AttributeAliases = CollectionLiterals.<String, String>newHashMap();
  
  /**
   * Contains all selected attributes for each registered query
   */
  private static Map<SimpleSelect, List<String>> projectionAttributes = CollectionLiterals.<SimpleSelect, List<String>>newHashMap();
  
  /**
   * Contains the corresponding sources to the attributes in projectionAttributes
   */
  private static Map<SimpleSelect, List<SelectExpression>> queryExpressions = CollectionLiterals.<SimpleSelect, List<SelectExpression>>newHashMap();
  
  /**
   * Contains string representations of all attributes mapped by their corresponding sources.
   */
  private static Map<SimpleSelect, Map<String, List<String>>> queryAttributes = CollectionLiterals.<SimpleSelect, Map<String, List<String>>>newHashMap();
  
  /**
   * Contains {@SelectExpression} objects that corresponds to aggregations of the given source.
   */
  private static Map<SimpleSelect, List<SelectExpression>> queryAggregations = CollectionLiterals.<SimpleSelect, List<SelectExpression>>newHashMap();
  
  private static int aggregationCounter = 0;
  
  private static int expressionCounter = 0;
  
  private static QueryCache<SimpleSelect> generatorCache;
  
  private CQLGeneratorUtil(final CQLGenerator generator) {
    this.generator = generator;
    QueryCache _queryCache = new QueryCache();
    CQLGeneratorUtil.generatorCache = _queryCache;
  }
  
  public static Map<String, List<String>> addToMap(final Map<String, List<String>> map, final String attribute, final String realSourcename) {
    List<String> attributeList = map.get(realSourcename);
    if ((attributeList == null)) {
      attributeList = CollectionLiterals.<String>newArrayList();
    }
    boolean _contains = attributeList.contains(attribute);
    boolean _not = (!_contains);
    if (_not) {
      attributeList.add(attribute);
    }
    map.put(realSourcename, attributeList);
    return map;
  }
  
  public static List<String> getAttributeAliasesAsList() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method getAttributeAliasesAsMap() is undefined"
      + "\nvalues cannot be resolved");
  }
  
  public static boolean isAttributeAlias(final String attributename) {
    return CQLGeneratorUtil.getAttributeAliasesAsList().contains(attributename);
  }
  
  public static Object isSourceAlias(final String sourcename) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method getSourceAliasesAsList() is undefined"
      + "\ncontains cannot be resolved");
  }
  
  public static boolean isAggregationAttribute(final String name) {
    return CQLGeneratorUtil.registry_AggregationAttributes.contains(name);
  }
  
  public static List<AttributeStruct> getAttributes() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field attributes is undefined for the type SourceStruct");
  }
  
  public static AttributeStruct getAttribute(final String name) {
    List<AttributeStruct> _attributes = CQLGeneratorUtil.getAttributes();
    for (final AttributeStruct attr : _attributes) {
      boolean _equals = attr.attributename.equals(name);
      if (_equals) {
        return attr;
      } else {
        boolean _contains = attr.aliases.contains(name);
        if (_contains) {
          return attr;
        }
      }
    }
    return null;
  }
  
  private static String registerAttributeAliases(final Attribute attribute, final String attributename, final String realSourcename, final String sourcenamealias, final boolean isSubQuery) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method getSource(String) is undefined"
      + "\nThe method getSourceAliasesAsList() is undefined"
      + "\nattributes cannot be resolved"
      + "\ncontains cannot be resolved");
  }
  
  /**
   * Returns all attributes with its corresponding sources from a select statement.
   */
  public static Map<String, List<String>> getSelectedAttributes(final SimpleSelect select, final Map<String, List<String>> var2) {
    throw new Error("Unresolved compilation problems:"
      + "\nmismatched input \',\' expecting \'}\'"
      + "\nmismatched input \'else\' expecting \'}\'"
      + "\nThe method getAttributeNamesFrom(String) is undefined"
      + "\nThe method getSource(String) is undefined"
      + "\nThe method put(SimpleSelect, String[], Type) is undefined for the type QueryCache<SimpleSelect>"
      + "\nThe method getSourcenameFromAlias(String) is undefined"
      + "\nThe method getSourceCandidates(Attribute, EList<Source>) is undefined"
      + "\nThe method parseAttribute(Attribute) is undefined"
      + "\nThe method registerSourceAlias(String, String) is undefined"
      + "\nThe method isSourceAlias(String) from the type CQLGeneratorUtil refers to the missing type Object"
      + "\nfindbyName cannot be resolved"
      + "\naliases cannot be resolved"
      + "\nadd cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nget cannot be resolved"
      + "\nget cannot be resolved"
      + "\nget cannot be resolved"
      + "\nget cannot be resolved"
      + "\nget cannot be resolved"
      + "\nsize cannot be resolved"
      + "\n> cannot be resolved"
      + "\nsize cannot be resolved"
      + "\n> cannot be resolved"
      + "\n&& cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nsize cannot be resolved"
      + "\n== cannot be resolved"
      + "\nget cannot be resolved"
      + "\nsourcename cannot be resolved");
  }
  
  private Procedure1<? super String> name;
}
