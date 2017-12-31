package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.InnerSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.SelectCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IWindowParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
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
    int _size = sources.size();
    String[] sourceStrings = new String[_size];
    Collection<String> sourcenames = CollectionLiterals.<String>newArrayList();
    for (int i = 0; (i < sources.size()); i++) {
      {
        Source source = ((Source[])Conversions.unwrapArray(sources, Source.class))[i];
        if ((source instanceof NestedSource)) {
          SelectCache _selectCache = this.cacheService.getSelectCache();
          SimpleSelect query = _selectCache.last();
          QueryCache _queryCache = this.cacheService.getQueryCache();
          Collection<QueryCache.QueryCacheAttributeEntry> queryAttributess = _queryCache.getQueryAttributes(query);
          InnerSelect _statement = ((NestedSource)source).getStatement();
          SimpleSelect _select = _statement.getSelect();
          SimpleSelect subQuery = ((SimpleSelect) _select);
          QueryCache _queryCache_1 = this.cacheService.getQueryCache();
          Collection<QueryCache.QueryCacheAttributeEntry> subQueryAttributes = _queryCache_1.getQueryAttributes(subQuery);
          OperatorCache _operatorCache = this.cacheService.getOperatorCache();
          Map<SimpleSelect, String> _subQueries = _operatorCache.getSubQueries();
          String lastOperator = _subQueries.get(subQuery);
          ArrayList<String> inputs = CollectionLiterals.<String>newArrayList();
          List<String> attributeAliases = this.utilityService.getAttributeAliasesAsList();
          for (final QueryCache.QueryCacheAttributeEntry entry : queryAttributess) {
            for (final QueryCache.QueryCacheAttributeEntry entry2 : subQueryAttributes) {
              if ((entry2 != null)) {
                ArrayList<String> aliasses = CollectionLiterals.<String>newArrayList();
                for (final String name : entry2.sources) {
                  for (final String name2 : entry.sources) {
                    {
                      String realName = name;
                      String realName2 = name2;
                      boolean _contains = attributeAliases.contains(realName);
                      if (_contains) {
                        String _attributenameFromAlias = this.utilityService.getAttributenameFromAlias(realName);
                        realName = _attributenameFromAlias;
                      }
                      boolean _contains_1 = attributeAliases.contains(realName2);
                      if (_contains_1) {
                        String _attributenameFromAlias_1 = this.utilityService.getAttributenameFromAlias(realName2);
                        realName2 = _attributenameFromAlias_1;
                      }
                      boolean _contains_2 = realName.contains(".");
                      if (_contains_2) {
                        String[] _split = name.split("\\.");
                        String _get = _split[1];
                        realName = _get;
                      }
                      boolean _contains_3 = realName2.contains(".");
                      if (_contains_3) {
                        String[] _split_1 = realName2.split("\\.");
                        String _get_1 = _split_1[1];
                        realName2 = _get_1;
                      }
                      boolean _equals = realName.equals(realName2);
                      if (_equals) {
                        String _replace = name.replace(".", "_");
                        aliasses.add(_replace);
                        aliasses.add(name2);
                      }
                    }
                  }
                }
                String _parse = this.renameParser.parse(aliasses, lastOperator);
                inputs.add(_parse);
              }
            }
          }
          ArrayList<String> aliasses_1 = CollectionLiterals.<String>newArrayList();
          Alias _alias = ((NestedSource)source).getAlias();
          String subQueryAlias = _alias.getName();
          Collection<String> _allQueryAttributes = this.utilityService.getAllQueryAttributes(subQuery);
          for (final String name_1 : _allQueryAttributes) {
            {
              String realName = name_1;
              boolean _contains = realName.contains(".");
              if (_contains) {
                int _indexOf = realName.indexOf(".");
                int _plus = (_indexOf + 1);
                int _length = realName.length();
                String _substring = realName.substring(_plus, _length);
                realName = _substring;
                String _replace = name_1.replace(".", "_");
                aliasses_1.add(_replace);
              } else {
                aliasses_1.add(name_1);
              }
              boolean _isAggregationAttribute = this.utilityService.isAggregationAttribute(name_1);
              if (_isAggregationAttribute) {
                aliasses_1.add(((subQueryAlias + ".") + realName));
              } else {
                aliasses_1.add(((subQueryAlias + ".") + realName));
              }
            }
          }
          String op = this.renameParser.parse(aliasses_1, lastOperator);
          inputs.add(op);
          final ArrayList<String> _converted_inputs = (ArrayList<String>)inputs;
          String _buildJoin = this.buildJoin(((String[])Conversions.unwrapArray(_converted_inputs, String.class)));
          String _string = _buildJoin.toString();
          sourceStrings[i] = _string;
        } else {
          if ((source instanceof SimpleSource)) {
            final String sourcename = ((SimpleSource)source).getName();
            Stream<String> _stream = sourcenames.stream();
            final Predicate<String> _function = (String e) -> {
              return e.equals(sourcename);
            };
            Stream<String> _filter = _stream.filter(_function);
            final long count = _filter.count();
            sourcenames.add(sourcename);
            this.renameParser.setSources(sources);
            String _parse_1 = this.windowParser.parse(((SimpleSource)source));
            CharSequence _buildRename = this.renameParser.buildRename(_parse_1, ((SimpleSource)source), 
              ((int) count));
            String _string_1 = _buildRename.toString();
            sourceStrings[i] = _string_1;
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
      _builder.append(_get, "");
      _builder.append(",");
      String _get_1 = sourcenames[1];
      _builder.append(_get_1, "");
      _builder.append(")");
      return _builder.toString();
    }
    list.remove(0);
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("JOIN(");
    String _get_2 = sourcenames[0];
    _builder_1.append(_get_2, "");
    _builder_1.append(",");
    final List<String> _converted_list = (List<String>)list;
    String _buildJoin = this.buildJoin(((String[])Conversions.unwrapArray(_converted_list, String.class)));
    _builder_1.append(_buildJoin, "");
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
