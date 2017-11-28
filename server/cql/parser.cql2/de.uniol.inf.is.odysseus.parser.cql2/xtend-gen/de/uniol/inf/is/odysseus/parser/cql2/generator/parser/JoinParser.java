package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IWindowParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class JoinParser implements IJoinParser {
  private final Logger log = LoggerFactory.getLogger(JoinParser.class);
  
  private IUtilityService utilityService;
  
  private ICacheService cacheService;
  
  private AbstractPQLOperatorBuilder builder;
  
  private IRenameParser renameParser;
  
  private IWindowParser windowParser;
  
  private boolean firstJoinInQuery;
  
  @Inject
  public JoinParser(final IUtilityService utilityService, final ICacheService cacheService, final AbstractPQLOperatorBuilder builder, final IRenameParser renameParser, final IWindowParser windowParser) {
    this.utilityService = utilityService;
    this.cacheService = cacheService;
    this.builder = builder;
    this.renameParser = renameParser;
    this.windowParser = windowParser;
  }
  
  @Override
  public String buildJoin(final Collection<Source> sources) {
    String[] sourceStrings = new String[sources.size()];
    Collection<String> sourcenames = CollectionLiterals.<String>newArrayList();
    for (int i = 0; (i < sources.size()); i++) {
      {
        Source source = ((Source[])Conversions.unwrapArray(sources, Source.class))[i];
        if ((source instanceof NestedSource)) {
          SimpleSelect query = this.cacheService.getSelectCache().last();
          Map<String, Collection<String>> queryAttributess = this.cacheService.getQueryCache().getQueryAttributes(query);
          SimpleSelect _select = ((NestedSource)source).getStatement().getSelect();
          SimpleSelect subQuery = ((SimpleSelect) _select);
          Map<String, Collection<String>> subQueryAttributes = this.cacheService.getQueryCache().getQueryAttributes(subQuery);
          String lastOperator = this.cacheService.getOperatorCache().getSubQueries().get(subQuery);
          ArrayList<String> inputs = CollectionLiterals.<String>newArrayList();
          List<String> attributeAliases = this.utilityService.getAttributeAliasesAsList();
          Set<Map.Entry<String, Collection<String>>> _entrySet = queryAttributess.entrySet();
          for (final Map.Entry<String, Collection<String>> entry : _entrySet) {
            {
              Collection<String> attributes = subQueryAttributes.get(entry.getKey());
              if ((attributes != null)) {
                ArrayList<String> aliasses = CollectionLiterals.<String>newArrayList();
                for (final String name : attributes) {
                  Collection<String> _value = entry.getValue();
                  for (final String name2 : _value) {
                    {
                      String realName = name;
                      String realName2 = name2;
                      boolean _contains = attributeAliases.contains(realName);
                      if (_contains) {
                        realName = this.utilityService.getAttributenameFromAlias(realName);
                      }
                      boolean _contains_1 = attributeAliases.contains(realName2);
                      if (_contains_1) {
                        realName2 = this.utilityService.getAttributenameFromAlias(realName2);
                      }
                      boolean _contains_2 = realName.contains(".");
                      if (_contains_2) {
                        realName = name.split("\\.")[1];
                      }
                      boolean _contains_3 = realName2.contains(".");
                      if (_contains_3) {
                        realName2 = realName2.split("\\.")[1];
                      }
                      boolean _equals = realName.equals(realName2);
                      if (_equals) {
                        aliasses.add(name.replace(".", "_"));
                        aliasses.add(name2);
                      }
                    }
                  }
                }
                String _generateListString = this.utilityService.generateListString(aliasses);
                Pair<String, String> _mappedTo = Pair.<String, String>of("aliases", _generateListString);
                Pair<String, String> _mappedTo_1 = Pair.<String, String>of("pairs", "true");
                Pair<String, String> _mappedTo_2 = Pair.<String, String>of("input", lastOperator);
                inputs.add(
                  this.cacheService.getOperatorCache().registerOperator(
                    this.builder.build(RenameAO.class, 
                      CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1, _mappedTo_2))));
              }
            }
          }
          ArrayList<String> aliasses = CollectionLiterals.<String>newArrayList();
          String subQueryAlias = ((NestedSource)source).getAlias().getName();
          Collection<String> _allQueryAttributes = this.utilityService.getAllQueryAttributes(subQuery);
          for (final String name : _allQueryAttributes) {
            {
              String realName = name;
              boolean _contains = realName.contains(".");
              if (_contains) {
                int _indexOf = realName.indexOf(".");
                int _plus = (_indexOf + 1);
                realName = realName.substring(_plus, realName.length());
                aliasses.add(name.replace(".", "_"));
              } else {
                aliasses.add(name);
              }
              boolean _isAggregationAttribute = this.utilityService.isAggregationAttribute(name);
              if (_isAggregationAttribute) {
                aliasses.add(((subQueryAlias + ".") + realName));
              } else {
                aliasses.add(((subQueryAlias + ".") + realName));
              }
            }
          }
          String _generateListString = this.utilityService.generateListString(aliasses);
          Pair<String, String> _mappedTo = Pair.<String, String>of("aliases", _generateListString);
          Pair<String, String> _mappedTo_1 = Pair.<String, String>of("pairs", "true");
          Pair<String, String> _mappedTo_2 = Pair.<String, String>of("input", lastOperator);
          String op = this.builder.build(RenameAO.class, 
            CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1, _mappedTo_2));
          inputs.add(this.cacheService.getOperatorCache().registerOperator(op));
          final ArrayList<String> _converted_inputs = (ArrayList<String>)inputs;
          sourceStrings[i] = this.buildJoin(((String[])Conversions.unwrapArray(_converted_inputs, String.class))).toString();
        } else {
          if ((source instanceof SimpleSource)) {
            final String sourcename = ((SimpleSource)source).getName();
            final Predicate<String> _function = (String e) -> {
              return e.equals(sourcename);
            };
            final long count = sourcenames.stream().filter(_function).count();
            sourcenames.add(sourcename);
            this.renameParser.setSources(sources);
            sourceStrings[i] = 
              this.renameParser.buildRename(
                this.windowParser.parse(((SimpleSource)source)), ((SimpleSource)source), 
                ((int) count)).toString();
          }
        }
      }
    }
    return this.buildJoin(sourceStrings);
  }
  
  @Override
  public String buildJoin(final String[] srcs) {
    String[] sourcenames = srcs;
    final String[] _converted_sourcenames = (String[])sourcenames;
    int _size = ((List<String>)Conversions.doWrapArray(_converted_sourcenames)).size();
    boolean _lessThan = (_size < 1);
    if (_lessThan) {
      throw new IllegalArgumentException("Invalid number of source elements: There has to be at least one source");
    }
    final String[] _converted_sourcenames_1 = (String[])sourcenames;
    int _size_1 = ((List<String>)Conversions.doWrapArray(_converted_sourcenames_1)).size();
    boolean _equals = (_size_1 == 1);
    if (_equals) {
      this.firstJoinInQuery = true;
      return sourcenames[0];
    }
    List<String> _asList = Arrays.<String>asList(sourcenames);
    List<String> list = new ArrayList<String>(_asList);
    int _size_2 = list.size();
    boolean _equals_1 = (_size_2 == 2);
    if (_equals_1) {
      this.firstJoinInQuery = true;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("JOIN(");
      String _get = sourcenames[0];
      _builder.append(_get);
      _builder.append(",");
      String _get_1 = sourcenames[1];
      _builder.append(_get_1);
      _builder.append(")");
      return _builder.toString();
    }
    list.remove(0);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("JOIN(");
    String _get_2 = sourcenames[0];
    _builder_1.append(_get_2);
    _builder_1.append(",");
    final List<String> _converted_list = (List<String>)list;
    String _buildJoin = this.buildJoin(((String[])Conversions.unwrapArray(_converted_list, String.class)));
    _builder_1.append(_buildJoin);
    _builder_1.append(")");
    return _builder_1.toString();
  }
  
  @Override
  public boolean isJoinInQuery() {
    return this.firstJoinInQuery;
  }
  
  @Override
  public void clear() {
    this.firstJoinInQuery = true;
  }
}
