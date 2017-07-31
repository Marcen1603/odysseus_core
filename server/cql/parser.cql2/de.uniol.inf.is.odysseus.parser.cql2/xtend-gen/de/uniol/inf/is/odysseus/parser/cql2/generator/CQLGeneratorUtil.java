package de.uniol.inf.is.odysseus.parser.cql2.generator;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.FunctionStore;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.AttributeStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;

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
  
  private static List<SourceStruct> registry_Sources = CollectionLiterals.<SourceStruct>newArrayList();
  
  private static Map<String, String> registry_Expressions = CollectionLiterals.<String, String>newHashMap();
  
  private static List<String> registry_AggregationAttributes = CollectionLiterals.<String>newArrayList();
  
  private static Map<String, Set<String>> registry_SubQuerySources = CollectionLiterals.<String, Set<String>>newHashMap();
  
  private static Map<String, String> registry_AttributeAliases = CollectionLiterals.<String, String>newHashMap();
  
  /**
   * Contains all selected attributes for each registered query
   */
  private static Map<SimpleSelect, List<String>> projectionAttributes = CollectionLiterals.<SimpleSelect, List<String>>newHashMap();
  
  /**
   * Contains the corresponding sources to the attributes in projectionAttributes
   */
  private static Map<SimpleSelect, List<String>> projectionSources = CollectionLiterals.<SimpleSelect, List<String>>newHashMap();
  
  private static Map<SimpleSelect, List<SelectExpression>> queryExpressions = CollectionLiterals.<SimpleSelect, List<SelectExpression>>newHashMap();
  
  private static int aggregationCounter = 0;
  
  private static int expressionCounter = 0;
  
  private CQLGeneratorUtil(final CQLGenerator generator) {
    this.generator = generator;
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
    ArrayList<String> list = CollectionLiterals.<String>newArrayList();
    Collection<List<String>> _values = CQLGeneratorUtil.getAttributeAliasesAsMap().values();
    for (final List<String> l : _values) {
      for (final String alias : l) {
        list.add(alias);
      }
    }
    return list;
  }
  
  public static boolean isAttributeAlias(final String attributename) {
    return CQLGeneratorUtil.getAttributeAliasesAsList().contains(attributename);
  }
  
  public static boolean isSourceAlias(final String sourcename) {
    return CQLGeneratorUtil.getSourceAliasesAsList().contains(sourcename);
  }
  
  public static List<AttributeStruct> getAttributes() {
    ArrayList<AttributeStruct> list = CollectionLiterals.<AttributeStruct>newArrayList();
    for (final SourceStruct source : CQLGeneratorUtil.registry_Sources) {
      list.addAll(source.attributes);
    }
    return list;
  }
  
  private static String registerAttributeAliases(final Attribute attribute, final String attributename, final String realSourcename, final String sourcenamealias, final boolean isSubQuery) {
    String _xifexpression = null;
    Alias _alias = attribute.getAlias();
    boolean _tripleNotEquals = (_alias != null);
    if (_tripleNotEquals) {
      _xifexpression = attribute.getName();
    } else {
      _xifexpression = attributename;
    }
    String simpleAttributename = _xifexpression;
    boolean _contains = simpleAttributename.contains(".");
    if (_contains) {
      simpleAttributename = simpleAttributename.split("\\.")[1];
    }
    String alias = sourcenamealias;
    for (final AttributeStruct attr1 : CQLGeneratorUtil.getSource(realSourcename).attributes) {
      boolean _equals = attr1.attributename.equals(simpleAttributename);
      if (_equals) {
        if ((alias == null)) {
          alias = realSourcename;
        }
        Alias _alias_1 = attribute.getAlias();
        boolean _tripleNotEquals_1 = (_alias_1 != null);
        if (_tripleNotEquals_1) {
          boolean _contains_1 = CQLGeneratorUtil.registry_AttributeAliases.entrySet().contains(attribute.getAlias().getName());
          if (_contains_1) {
            String _name = attribute.getAlias().getName();
            String _plus = ("given alias " + _name);
            String _plus_1 = (_plus + " is ambiguous");
            throw new IllegalArgumentException(_plus_1);
          }
          boolean _contains_2 = attr1.aliases.contains(attribute.getAlias().getName());
          boolean _not = (!_contains_2);
          if (_not) {
            attr1.aliases.add(attribute.getAlias().getName());
            CQLGeneratorUtil.registry_AttributeAliases.put(attribute.getAlias().getName(), alias);
          }
          return attribute.getAlias().getName();
        } else {
          if (((attribute.getAlias() == null) && CQLGeneratorUtil.getSourceAliasesAsList().contains(alias))) {
            boolean _contains_3 = attr1.aliases.contains(attributename);
            boolean _not_1 = (!_contains_3);
            if (_not_1) {
              attr1.aliases.add(attributename);
              CQLGeneratorUtil.registry_AttributeAliases.put(attributename, alias);
            }
            return attributename;
          }
        }
      }
    }
    return null;
  }
  
  /**
   * Returns all attributes with its corresponding sources from a select statement.
   */
  public static Map<String, List<String>> getSelectedAttributes(final SimpleSelect select, final Map<String, List<String>> var2) {
    Map<String, List<String>> map = var2;
    ArrayList<Attribute> attributes = CollectionLiterals.<Attribute>newArrayList();
    String[] attributeOrder = new String[select.getArguments().size()];
    String[] sourceOrder = new String[select.getArguments().size()];
    EList<SelectArgument> _arguments = select.getArguments();
    for (final SelectArgument argument : _arguments) {
      Attribute _attribute = argument.getAttribute();
      boolean _tripleNotEquals = (_attribute != null);
      if (_tripleNotEquals) {
        attributes.add(argument.getAttribute());
      }
    }
    if ((attributes.isEmpty() && EcoreUtil2.<SelectExpression>getAllContentsOfType(select, SelectExpression.class).isEmpty())) {
      List<String> attributeOrderList = CollectionLiterals.<String>newArrayList();
      List<String> sourceOrderList = CollectionLiterals.<String>newArrayList();
      EList<Source> _sources = select.getSources();
      for (final Source source : _sources) {
        if ((source instanceof SimpleSource)) {
          List<String> _attributeNamesFrom = CQLGeneratorUtil.getAttributeNamesFrom(((SimpleSource)source).getName());
          for (final String attribute : _attributeNamesFrom) {
            {
              Alias _alias = ((SimpleSource)source).getAlias();
              boolean _tripleNotEquals_1 = (_alias != null);
              if (_tripleNotEquals_1) {
                String _name = ((SimpleSource)source).getAlias().getName();
                String _plus = (_name + ".");
                String attributealias = (_plus + attribute);
                CQLGeneratorUtil.getSource(((SimpleSource)source).getName()).findbyName(attribute).aliases.add(attributealias);
                CQLGeneratorUtil.registry_AttributeAliases.put(attributealias, ((SimpleSource)source).getAlias().getName());
                attributeOrderList.add(attributealias);
                map = CQLGeneratorUtil.addToMap(map, attributealias, ((SimpleSource)source).getName());
              } else {
                String _name_1 = ((SimpleSource)source).getName();
                String _plus_1 = (_name_1 + ".");
                String _plus_2 = (_plus_1 + attribute);
                attributeOrderList.add(_plus_2);
              }
              map = CQLGeneratorUtil.addToMap(map, attribute, ((SimpleSource)source).getName());
              sourceOrderList.add(((SimpleSource)source).getName());
            }
          }
        }
      }
      final List<String> _converted_attributeOrderList = (List<String>)attributeOrderList;
      attributeOrder = ((String[])Conversions.unwrapArray(_converted_attributeOrderList, String.class));
      final List<String> _converted_sourceOrderList = (List<String>)sourceOrderList;
      sourceOrder = ((String[])Conversions.unwrapArray(_converted_sourceOrderList, String.class));
      final String[] _converted_attributeOrder = (String[])attributeOrder;
      CQLGeneratorUtil.projectionAttributes.put(select, ((List<String>)Conversions.doWrapArray(_converted_attributeOrder)));
      final String[] _converted_sourceOrder = (String[])sourceOrder;
      CQLGeneratorUtil.projectionSources.put(select, ((List<String>)Conversions.doWrapArray(_converted_sourceOrder)));
      return map;
    }
    ExpressionsModel _predicates = select.getPredicates();
    boolean _tripleNotEquals_1 = (_predicates != null);
    if (_tripleNotEquals_1) {
      List<Attribute> list = EcoreUtil2.<Attribute>getAllContentsOfType(select.getPredicates(), Attribute.class);
      for (final Attribute attribute_1 : list) {
        boolean _contains = attribute_1.getName().contains(".");
        if (_contains) {
          String[] split = attribute_1.getName().split("\\.");
          String sourcename = split[0];
          String attributename = split[1];
          if ((CQLGeneratorUtil.isSourceAlias(sourcename) && (!CQLGeneratorUtil.isAttributeAlias(attributename)))) {
            CQLGeneratorUtil.registerAttributeAliases(attribute_1, attribute_1.getName(), CQLGeneratorUtil.getSourcenameFromAlias(sourcename), sourcename, false);
          }
        }
      }
    }
    int i = 0;
    for (final Attribute attribute_2 : attributes) {
      {
        List<SourceStruct> sourceCandidates = CQLGeneratorUtil.getSourceCandidates(attribute_2, select.getSources());
        Object[] result = CQLGeneratorUtil.parseAttribute(attribute_2);
        Object _get = result[0];
        String attributename_1 = ((String) _get);
        Object _get_1 = result[1];
        String sourcename_1 = ((String) _get_1);
        Object _get_2 = result[2];
        String sourcealias = ((String) _get_2);
        String attributealias = null;
        Object _get_3 = result[3];
        List<String> list_1 = ((List<String>) _get_3);
        Object _get_4 = result[4];
        Boolean isFromSubQuery = ((Boolean) _get_4);
        Alias _alias = attribute_2.getAlias();
        boolean _tripleNotEquals_2 = (_alias != null);
        if (_tripleNotEquals_2) {
          attributename_1 = attribute_2.getAlias().getName();
        }
        int _size = sourceCandidates.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          if (((sourceCandidates.size() > 1) && (sourcename_1 == null))) {
            String _string = sourceCandidates.toString();
            String _plus = ((("attribute " + attributename_1) + " is ambiguous: possible sources are ") + _string);
            throw new IllegalArgumentException(_plus);
          }
          int _size_1 = sourceCandidates.size();
          boolean _equals = (_size_1 == 1);
          if (_equals) {
            sourcename_1 = sourceCandidates.get(0).sourcename;
            if ((list_1 != null)) {
              for (final String name : list_1) {
                map = CQLGeneratorUtil.addToMap(map, name, sourcename_1);
              }
            }
          }
          map = CQLGeneratorUtil.addToMap(map, attributename_1, sourcename_1);
          if ((isFromSubQuery).booleanValue()) {
            CQLGeneratorUtil.registerSourceAlias(sourcename_1, sourcealias);
          }
          attributealias = CQLGeneratorUtil.registerAttributeAliases(attribute_2, attributename_1, sourcename_1, sourcealias, (isFromSubQuery).booleanValue());
        } else {
          if ((list_1 != null)) {
            for (final String name_1 : list_1) {
              {
                map = CQLGeneratorUtil.addToMap(map, name_1, sourcename_1);
                CQLGeneratorUtil.registerAttributeAliases(attribute_2, ((sourcealias + ".") + name_1), sourcename_1, sourcealias, (isFromSubQuery).booleanValue());
              }
            }
          }
        }
        attributeOrder = CQLGeneratorUtil.computeProjectionAttributes(attributeOrder, select, attribute_2, attributename_1, attributealias, sourcename_1);
        sourceOrder[i] = sourcename_1;
        i++;
      }
    }
    attributeOrder = CQLGeneratorUtil.computeProjectionAttributes(attributeOrder, select, null, null, null, null);
    final String[] _converted_attributeOrder_1 = (String[])attributeOrder;
    CQLGeneratorUtil.projectionAttributes.put(select, ((List<String>)Conversions.doWrapArray(_converted_attributeOrder_1)));
    final String[] _converted_sourceOrder_1 = (String[])sourceOrder;
    CQLGeneratorUtil.projectionSources.put(select, ((List<String>)Conversions.doWrapArray(_converted_sourceOrder_1)));
    return map;
  }
  
  public static String[] computeProjectionAttributes(final String[] list, final SimpleSelect select, final Attribute attribute, final String attributename, final String attributealias, final String sourcename) {
    CQLGeneratorUtil.expressionCounter = 0;
    CQLGeneratorUtil.aggregationCounter = 0;
    int i = 0;
    String[] attributeOrder = list;
    Object candidate = null;
    if ((attribute != null)) {
      EList<SelectArgument> _arguments = select.getArguments();
      for (final SelectArgument argument : _arguments) {
        {
          if (((candidate = argument.getAttribute()) != null)) {
            boolean _equals = ((Attribute) candidate).getName().equals(attribute.getName());
            if (_equals) {
              Alias _alias = ((Attribute) candidate).getAlias();
              boolean _tripleNotEquals = (_alias != null);
              if (_tripleNotEquals) {
                attributeOrder[i] = ((Attribute) candidate).getAlias().getName();
              } else {
                if ((attributealias != null)) {
                  attributeOrder[i] = attributealias;
                } else {
                  boolean _contains = attributename.contains(".");
                  if (_contains) {
                    String[] split = attributename.split("\\.");
                    String name = split[1];
                    String source = split[0];
                    String salias = source;
                    boolean _isSourceAlias = CQLGeneratorUtil.isSourceAlias(source);
                    if (_isSourceAlias) {
                      source = CQLGeneratorUtil.getSourcenameFromAlias(salias);
                    }
                    boolean _equals_1 = name.equals("*");
                    if (_equals_1) {
                      final String[] _converted_attributeOrder = (String[])attributeOrder;
                      int _size = ((List<String>)Conversions.doWrapArray(_converted_attributeOrder)).size();
                      ArrayList<String> attributeOrderList = new ArrayList<String>(_size);
                      List<String> _attributeNamesFrom = CQLGeneratorUtil.getAttributeNamesFrom(source);
                      for (final String str : _attributeNamesFrom) {
                        {
                          attributeOrderList.add(((salias + ".") + str));
                          i++;
                        }
                      }
                      final ArrayList<String> _converted_attributeOrderList = (ArrayList<String>)attributeOrderList;
                      attributeOrder = ((String[])Conversions.unwrapArray(_converted_attributeOrderList, String.class));
                    } else {
                      attributeOrder[i] = attributename;
                    }
                  } else {
                    attributeOrder[i] = ((sourcename + ".") + attributename);
                  }
                }
              }
            }
          }
          if (((candidate = argument.getExpression()) != null)) {
            Alias _alias_1 = ((SelectExpression) candidate).getAlias();
            boolean _tripleNotEquals_1 = (_alias_1 != null);
            if (_tripleNotEquals_1) {
              attributeOrder[i] = ((SelectExpression) candidate).getAlias().getName();
            } else {
              int _size_1 = ((SelectExpression) candidate).getExpressions().size();
              boolean _equals_2 = (_size_1 == 1);
              if (_equals_2) {
                EObject function = ((SelectExpression) candidate).getExpressions().get(0).getValue();
                if ((function instanceof Function)) {
                  boolean _isAggregateFunction = CQLGeneratorUtil.isAggregateFunction(((Function)function).getName());
                  if (_isAggregateFunction) {
                    attributeOrder[i] = CQLGeneratorUtil.getAggregationName(((Function)function).getName());
                  } else {
                    attributeOrder[i] = CQLGeneratorUtil.getExpressionName();
                  }
                }
              } else {
                attributeOrder[i] = CQLGeneratorUtil.getExpressionName();
              }
            }
          }
          i++;
        }
      }
    } else {
      EList<SelectArgument> _arguments_1 = select.getArguments();
      for (final SelectArgument argument_1 : _arguments_1) {
        {
          if (((candidate = argument_1.getExpression()) != null)) {
            Alias _alias = ((SelectExpression) candidate).getAlias();
            boolean _tripleNotEquals = (_alias != null);
            if (_tripleNotEquals) {
              attributeOrder[i] = ((SelectExpression) candidate).getAlias().getName();
            } else {
              int _size = ((SelectExpression) candidate).getExpressions().size();
              boolean _equals = (_size == 1);
              if (_equals) {
                EObject function = ((SelectExpression) candidate).getExpressions().get(0).getValue();
                if ((function instanceof Function)) {
                  boolean _isAggregateFunction = CQLGeneratorUtil.isAggregateFunction(((Function)function).getName());
                  if (_isAggregateFunction) {
                    attributeOrder[i] = CQLGeneratorUtil.getAggregationName(((Function)function).getName());
                  } else {
                    attributeOrder[i] = CQLGeneratorUtil.getExpressionName();
                  }
                }
              } else {
                attributeOrder[i] = CQLGeneratorUtil.getExpressionName();
              }
            }
          }
          i++;
        }
      }
    }
    CQLGeneratorUtil.expressionCounter = 0;
    CQLGeneratorUtil.aggregationCounter = 0;
    return attributeOrder;
  }
  
  private static List<SourceStruct> getSourceCandidates(final Attribute attribute, final List<Source> sources) {
    ArrayList<SourceStruct> containedBySources = CollectionLiterals.<SourceStruct>newArrayList();
    for (final Source source1 : sources) {
      if ((source1 instanceof SimpleSource)) {
        for (final SourceStruct source2 : CQLGeneratorUtil.registry_Sources) {
          if (((((SimpleSource)source1).getName().equals(source2.sourcename) && source2.containsAttribute(attribute.getName())) && 
            sources.stream().<String>map(((java.util.function.Function<Source, String>) (Source e) -> {
              String _xifexpression = null;
              if ((e instanceof SimpleSource)) {
                _xifexpression = ((SimpleSource)e).getName();
              }
              return _xifexpression;
            })).collect(Collectors.<String>toList()).contains(source2.sourcename))) {
            boolean _contains = containedBySources.contains(source2);
            boolean _not = (!_contains);
            if (_not) {
              containedBySources.add(source2);
            } else {
              boolean _contains_1 = attribute.getName().contains(".");
              boolean _not_1 = (!_contains_1);
              if (_not_1) {
                boolean _isAttributeAlias = CQLGeneratorUtil.isAttributeAlias(attribute.getName());
                boolean _not_2 = (!_isAttributeAlias);
                if (_not_2) {
                  String _name = attribute.getName();
                  throw new IllegalArgumentException(_name);
                }
              }
            }
          }
        }
      } else {
        String subQueryAlias = ((NestedSource) source1).getAlias().getName();
        Set<String> _get = CQLGeneratorUtil.registry_SubQuerySources.get(subQueryAlias);
        for (final String source : _get) {
          for (final SourceStruct source2_1 : CQLGeneratorUtil.registry_Sources) {
            {
              String realName = attribute.getName();
              boolean _contains_2 = realName.contains(".");
              if (_contains_2) {
                realName = realName.split("\\.")[1];
              }
              if ((source.equals(source2_1.sourcename) && source2_1.containsAttribute(realName))) {
                boolean _contains_3 = containedBySources.contains(source2_1);
                boolean _not_3 = (!_contains_3);
                if (_not_3) {
                  containedBySources.add(source2_1);
                } else {
                  boolean _contains_4 = attribute.getName().contains(".");
                  boolean _not_4 = (!_contains_4);
                  if (_not_4) {
                    boolean _isAttributeAlias_1 = CQLGeneratorUtil.isAttributeAlias(attribute.getName());
                    boolean _not_5 = (!_isAttributeAlias_1);
                    if (_not_5) {
                      String _name_1 = attribute.getName();
                      throw new IllegalArgumentException(_name_1);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return containedBySources;
  }
  
  /**
   * Returns all {@link Attribute} elements from the corresponding source.
   */
  public static List<String> getAttributeNamesFrom(final String srcname) {
    for (final SourceStruct source : CQLGeneratorUtil.registry_Sources) {
      if ((source.sourcename.equals(srcname) || source.aliases.contains(srcname))) {
        final java.util.function.Function<AttributeStruct, String> _function = (AttributeStruct e) -> {
          return e.attributename;
        };
        return source.attributes.stream().<String>map(_function).collect(Collectors.<String>toList());
      }
    }
    return null;
  }
  
  private static Object[] parseAttribute(final Attribute attribute) {
    String sourcename = null;
    String sourcealias = null;
    List<String> list = null;
    boolean subQuery = false;
    boolean _contains = attribute.getName().contains(".");
    if (_contains) {
      String[] split = attribute.getName().split("\\.");
      sourcename = split[0];
      boolean _contains_1 = CQLGeneratorUtil.getSourceNames().contains(sourcename);
      boolean _not = (!_contains_1);
      if (_not) {
        sourcealias = sourcename;
        if ((((sourcename = CQLGeneratorUtil.getSourcenameFromAlias(sourcename)) == null) && 
          CQLGeneratorUtil.registry_SubQuerySources.keySet().contains(split[0]))) {
          subQuery = true;
        }
      }
      boolean _contains_2 = split[1].contains("*");
      if (_contains_2) {
        list = CollectionLiterals.<String>newArrayList();
        sourcename = split[0];
        boolean _isSourceAlias = CQLGeneratorUtil.isSourceAlias(sourcename);
        if (_isSourceAlias) {
          sourcealias = sourcename;
          sourcename = CQLGeneratorUtil.getSourcenameFromAlias(sourcename);
        }
        List<String> _attributeNamesFrom = CQLGeneratorUtil.getAttributeNamesFrom(sourcename);
        for (final String str : _attributeNamesFrom) {
          list.add(str);
        }
      }
    }
    String _name = attribute.getName();
    return new Object[] { _name, sourcename, sourcealias, list, Boolean.valueOf(subQuery) };
  }
  
  public static String getProjectAttribute(final String attribute) {
    boolean _contains = attribute.contains(CQLGeneratorUtil.getExpressionPrefix());
    if (_contains) {
      return CQLGeneratorUtil.getRegisteredExpressions().get(attribute);
    }
    boolean _contains_1 = CQLGeneratorUtil.getRegisteredExpressions().keySet().contains(attribute);
    if (_contains_1) {
      return CQLGeneratorUtil.getRegisteredExpressions().get(attribute);
    }
    boolean _contains_2 = attribute.contains(".");
    if (_contains_2) {
      boolean _isAttributeAlias = CQLGeneratorUtil.isAttributeAlias(attribute);
      if (_isAttributeAlias) {
        return attribute;
      }
      String[] split = attribute.split("\\.");
      String realAttributename = split[1];
      String sourcename = split[0];
      String sourcealias = sourcename;
      boolean _isSourceAlias = CQLGeneratorUtil.isSourceAlias(sourcename);
      if (_isSourceAlias) {
        sourcename = CQLGeneratorUtil.getSourcenameFromAlias(sourcealias);
      }
      List<String> aliases = CQLGeneratorUtil.getSource(sourcename).findbyName(realAttributename).aliases;
      boolean _isEmpty = aliases.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        int _size = aliases.size();
        int _minus = (_size - 1);
        return aliases.get(_minus);
      }
      return attribute;
    }
    return attribute;
  }
  
  public static List<String> getSourceNames() {
    final java.util.function.Function<SourceStruct, String> _function = (SourceStruct e) -> {
      return e.sourcename;
    };
    return CQLGeneratorUtil.registry_Sources.stream().<String>map(_function).collect(Collectors.<String>toList());
  }
  
  public static String getSourcenameFromAlias(final String sourcealias) {
    Set<Map.Entry<SourceStruct, List<String>>> _entrySet = CQLGeneratorUtil.getSourceAliases().entrySet();
    for (final Map.Entry<SourceStruct, List<String>> source : _entrySet) {
      boolean _contains = source.getValue().contains(sourcealias);
      if (_contains) {
        return source.getKey().sourcename;
      }
    }
    return null;
  }
  
  public AttributeStruct getAttributeFromAlias(final String alias) {
    Set<Map.Entry<AttributeStruct, List<String>>> _entrySet = CQLGeneratorUtil.getAttributeAliasesAsMap().entrySet();
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
    if ((CQLGeneratorUtil.registry_AggregationAttributes.contains(alias) || 
      CQLGeneratorUtil.registry_Expressions.keySet().contains(alias))) {
      return alias;
    }
    return null;
  }
  
  public static List<String> getSourceAliasesAsList() {
    ArrayList<String> list = CollectionLiterals.<String>newArrayList();
    Collection<List<String>> _values = CQLGeneratorUtil.getSourceAliases().values();
    for (final List<String> l : _values) {
      list.addAll(l);
    }
    return list;
  }
  
  public static Map<SourceStruct, List<String>> getSourceAliases() {
    HashMap<SourceStruct, List<String>> map = CollectionLiterals.<SourceStruct, List<String>>newHashMap();
    for (final SourceStruct source : CQLGeneratorUtil.registry_Sources) {
      map.put(source, source.aliases);
    }
    return map;
  }
  
  public static Map<AttributeStruct, List<String>> getAttributeAliasesAsMap() {
    HashMap<AttributeStruct, List<String>> map = CollectionLiterals.<AttributeStruct, List<String>>newHashMap();
    for (final SourceStruct source : CQLGeneratorUtil.registry_Sources) {
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
  
  public static SourceStruct getSource(final String name) {
    for (final SourceStruct source : CQLGeneratorUtil.registry_Sources) {
      boolean _equals = source.sourcename.equals(name);
      if (_equals) {
        return source;
      } else {
        boolean _contains = source.aliases.contains(name);
        if (_contains) {
          return source;
        }
      }
    }
    throw new IllegalArgumentException((("given source " + name) + " is not registered"));
  }
  
  public static SourceStruct getSource(final Source source) {
    if ((source instanceof SimpleSource)) {
      return CQLGeneratorUtil.getSource(((SimpleSource)source).getName());
    }
    return null;
  }
  
  public static boolean registerSourceAlias(final String sourcename, final String sourcealias) {
    boolean _xblockexpression = false;
    {
      SourceStruct source = CQLGeneratorUtil.getSource(sourcename);
      boolean _xifexpression = false;
      boolean _contains = source.aliases.contains(sourcealias);
      boolean _not = (!_contains);
      if (_not) {
        _xifexpression = source.aliases.add(sourcealias);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private final static String EXPRESSSION_NAME_PREFIX = "expression_";
  
  public static String getExpressionName() {
    int _plusPlus = CQLGeneratorUtil.expressionCounter++;
    return (CQLGeneratorUtil.EXPRESSSION_NAME_PREFIX + Integer.valueOf(_plusPlus));
  }
  
  public static String getAggregationName(final String name) {
    int _plusPlus = CQLGeneratorUtil.aggregationCounter++;
    return ((name + "_") + Integer.valueOf(_plusPlus));
  }
  
  public static boolean isAggregateFunction(final String name) {
    return CQLGeneratorUtil.aggregatePattern.matcher(name).toString().contains(name);
  }
  
  private static FunctionStore functionStore;
  
  private static Pattern aggregatePattern;
  
  private static MEP mep;
  
  public static FunctionStore setFunctionStore(final FunctionStore store) {
    return CQLGeneratorUtil.functionStore = store;
  }
  
  public static Pattern setAggregatePattern(final Pattern pattern) {
    return CQLGeneratorUtil.aggregatePattern = pattern;
  }
  
  public static MEP setMEP(final MEP mepp) {
    return CQLGeneratorUtil.mep = mepp;
  }
  
  public static String getExpressionPrefix() {
    return CQLGeneratorUtil.EXPRESSSION_NAME_PREFIX;
  }
  
  public static List<SourceStruct> getRegisteredSources() {
    return CQLGeneratorUtil.registry_Sources;
  }
  
  public static List<SourceStruct> setRegisteredSources(final List<SourceStruct> schemata) {
    return CQLGeneratorUtil.registry_Sources = schemata;
  }
  
  public static Map<String, String> getRegisteredExpressions() {
    return CQLGeneratorUtil.registry_Expressions;
  }
  
  public static List<String> getRegisteredAggregationAttributes() {
    return CQLGeneratorUtil.registry_AggregationAttributes;
  }
  
  public static Map<SimpleSelect, List<String>> getProjectionAttributes() {
    return CQLGeneratorUtil.projectionAttributes;
  }
  
  public static Map<SimpleSelect, List<SelectExpression>> getQueryExpressions() {
    return CQLGeneratorUtil.queryExpressions;
  }
  
  public static Map<SimpleSelect, List<String>> getGetProjectSources() {
    return CQLGeneratorUtil.projectionSources;
  }
  
  public static Map<String, Set<String>> getSubQuerySources() {
    return CQLGeneratorUtil.registry_SubQuerySources;
  }
  
  public static Map<String, String> getAttributeAliases() {
    return CQLGeneratorUtil.registry_AttributeAliases;
  }
  
  public static int clear() {
    int _xblockexpression = (int) 0;
    {
      CQLGeneratorUtil.registry_AggregationAttributes.clear();
      CQLGeneratorUtil.registry_AttributeAliases.clear();
      CQLGeneratorUtil.registry_Expressions.clear();
      CQLGeneratorUtil.registry_Sources.clear();
      CQLGeneratorUtil.registry_SubQuerySources.clear();
      CQLGeneratorUtil.projectionSources.clear();
      CQLGeneratorUtil.projectionAttributes.clear();
      CQLGeneratorUtil.queryExpressions.clear();
      CQLGeneratorUtil.aggregationCounter = 0;
      _xblockexpression = CQLGeneratorUtil.expressionCounter = 0;
    }
    return _xblockexpression;
  }
  
  public static boolean isMEPFunction(final String name, final String function) {
    boolean _containsSymbol = CQLGeneratorUtil.functionStore.containsSymbol(name);
    if (_containsSymbol) {
      try {
        SDFDatatype datatype = CQLGeneratorUtil.mep.parse(function).getReturnType();
        List<IMepFunction<?>> _functions = FunctionStore.getInstance().getFunctions(name);
        for (final IMepFunction<?> f : _functions) {
          boolean _equals = f.getReturnType().equals(datatype);
          if (_equals) {
            return true;
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          return false;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    return false;
  }
}
