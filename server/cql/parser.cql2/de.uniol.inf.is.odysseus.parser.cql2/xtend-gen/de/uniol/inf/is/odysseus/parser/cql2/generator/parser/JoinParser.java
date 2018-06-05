package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NestedSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IWindowParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class JoinParser implements IJoinParser {
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
  public String buildJoin(final Collection<QueryCache.QuerySource> sources, final SimpleSelect select) {
    try {
      int _size = sources.size();
      String[] sourceStrings = new String[_size];
      Collection<String> sourcenames = CollectionLiterals.<String>newArrayList();
      for (int i = 0; (i < sources.size()); i++) {
        {
          QueryCache.QuerySource _get = ((QueryCache.QuerySource[])Conversions.unwrapArray(sources, QueryCache.QuerySource.class))[i];
          Source source = _get.source;
          if ((source instanceof NestedSource)) {
            QueryCache _queryCache = this.cacheService.getQueryCache();
            Alias _alias = ((NestedSource)source).getAlias();
            String _name = _alias.getName();
            final Optional<QueryCache.SubQuery> o = _queryCache.getSubQuery(_name);
            boolean _isPresent = o.isPresent();
            if (_isPresent) {
              final QueryCache.SubQuery subQuery = o.get();
              QueryCache _queryCache_1 = this.cacheService.getQueryCache();
              Collection<QueryCache.QueryAttribute> _projectionAttributes = _queryCache_1.getProjectionAttributes(subQuery.select);
              Stream<QueryCache.QueryAttribute> _stream = _projectionAttributes.stream();
              final Predicate<QueryCache.QueryAttribute> _function = (QueryCache.QueryAttribute p) -> {
                return (!Objects.equal(p.referenceOf, null));
              };
              Stream<QueryCache.QueryAttribute> _filter = _stream.filter(_function);
              Collector<QueryCache.QueryAttribute, ?, List<QueryCache.QueryAttribute>> _list = Collectors.<QueryCache.QueryAttribute>toList();
              final Collection<QueryCache.QueryAttribute> col = _filter.collect(_list);
              final Collection<String> renames = CollectionLiterals.<String>newArrayList();
              Stream<QueryCache.QueryAttribute> _stream_1 = col.stream();
              final Consumer<QueryCache.QueryAttribute> _function_1 = (QueryCache.QueryAttribute e) -> {
                String _name_1 = e.parsedAttribute.getName();
                String _replace = _name_1.replace(".", "_");
                renames.add(_replace);
                String _xifexpression = null;
                String _alias_1 = e.referenceOf.parsedAttribute.getAlias();
                boolean _notEquals = (!Objects.equal(_alias_1, null));
                if (_notEquals) {
                  _xifexpression = e.referenceOf.parsedAttribute.getAlias();
                } else {
                  _xifexpression = e.referenceOf.parsedAttribute.getName();
                }
                renames.add(_xifexpression);
              };
              _stream_1.forEach(_function_1);
              String _parse = this.renameParser.parse(renames, subQuery.operator, select);
              sourceStrings[i] = _parse;
            }
          } else {
            if ((source instanceof SimpleSource)) {
              final String sourcename = ((SimpleSource)source).getName();
              Stream<String> _stream_2 = sourcenames.stream();
              final Predicate<String> _function_2 = (String e) -> {
                return e.equals(sourcename);
              };
              Stream<String> _filter_1 = _stream_2.filter(_function_2);
              final long count = _filter_1.count();
              sourcenames.add(sourcename);
              this.renameParser.setSources(sources);
              String _parse_1 = this.windowParser.parse(((SimpleSource)source));
              CharSequence _buildRename = this.renameParser.buildRename(_parse_1, ((SimpleSource)source), select, 
                ((int) count));
              String _string = _buildRename.toString();
              sourceStrings[i] = _string;
            }
          }
        }
      }
      return this.buildJoin(sourceStrings);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
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
