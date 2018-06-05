package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionComponent;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Starthing;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAggregationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class AggregationParser implements IAggregationParser {
  private AbstractPQLOperatorBuilder builder;
  
  private IUtilityService utilityService;
  
  private IJoinParser joinParser;
  
  private IAttributeNameParser nameParser;
  
  private IAttributeParser attributeParser;
  
  private ICacheService cacheService;
  
  private String argsstr = "";
  
  private final String regex = ",$";
  
  @Inject
  public AggregationParser(final AbstractPQLOperatorBuilder builder, final IUtilityService utilityService, final IJoinParser joinParser, final IAttributeNameParser nameParser, final IAttributeParser attributeParser, final ICacheService cacheService) {
    this.builder = builder;
    this.utilityService = utilityService;
    this.joinParser = joinParser;
    this.nameParser = nameParser;
    this.attributeParser = attributeParser;
    this.cacheService = cacheService;
  }
  
  @Override
  public Object[] parse(final Collection<QueryCache.QueryAggregate> list, final Collection<Attribute> list2, final Collection<QueryCache.QuerySource> srcs, final SimpleSelect select) {
    return this.buildAggregateOP(list, list2, srcs, select);
  }
  
  private Object[] buildAggregateOP(final Collection<QueryCache.QueryAggregate> aggAttr, final Collection<Attribute> orderAttr, final CharSequence input) throws PQLOperatorBuilderException {
    this.argsstr = "";
    String mapName = "";
    final Collection<String> args = CollectionLiterals.<String>newArrayList();
    final Collection<String> aliases = CollectionLiterals.<String>newArrayList();
    Stream<QueryCache.QueryAggregate> _stream = aggAttr.stream();
    final Consumer<QueryCache.QueryAggregate> _function = (QueryCache.QueryAggregate e) -> {
      this.argsstr = "";
      EList<ExpressionComponent> _expressions = e.parsedAggregation.expression.getExpressions();
      ExpressionComponent _get = _expressions.get(0);
      EObject _value = _get.getValue();
      final Function aggregate = ((Function) _value);
      EObject _value_1 = aggregate.getValue();
      final EList<ExpressionComponent> components = ((SelectExpression) _value_1).getExpressions();
      String attributename = "";
      String datatype = "";
      int _size = components.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        ExpressionComponent _get_1 = components.get(0);
        final EObject comp = _get_1.getValue();
        boolean _matched = false;
        if (comp instanceof Attribute) {
          _matched=true;
          Optional<QueryCache.QueryAttribute> queryAttribute = this.utilityService.getQueryAttribute(((Attribute)comp));
          boolean _isPresent = queryAttribute.isPresent();
          if (_isPresent) {
            QueryCache.QueryAttribute _get_2 = queryAttribute.get();
            String _name = _get_2.getName();
            attributename = _name;
            QueryCache.QueryAttribute _get_3 = queryAttribute.get();
            String _dataType = _get_3.getDataType();
            datatype = _dataType;
          }
        }
        if (!_matched) {
          if (comp instanceof Starthing) {
            _matched=true;
            attributename = "*";
          }
        }
      } else {
      }
      String _name = aggregate.getName();
      args.add(_name);
      args.add(attributename);
      String alias = "";
      Alias _alias = e.parsedAggregation.expression.getAlias();
      boolean _tripleNotEquals = (_alias != null);
      if (_tripleNotEquals) {
        Alias _alias_1 = e.parsedAggregation.expression.getAlias();
        String _name_1 = _alias_1.getName();
        alias = _name_1;
      } else {
        String _name_2 = aggregate.getName();
        String _aggregationName = this.attributeParser.getAggregationName(_name_2);
        alias = _aggregationName;
      }
      args.add(alias);
      aliases.add(alias);
      boolean _notEquals = (!Objects.equal(datatype, ""));
      if (_notEquals) {
        args.add(datatype);
      }
      args.add(",");
      String _argsstr = this.argsstr;
      String _generateKeyValueString = this.utilityService.generateKeyValueString(((String[])Conversions.unwrapArray(args, String.class)));
      this.argsstr = (_argsstr + _generateKeyValueString);
      args.clear();
    };
    _stream.forEach(_function);
    this.argsstr.replaceAll(this.regex, "");
    String groupby = "";
    boolean _isEmpty = orderAttr.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      String _groupby = groupby;
      Stream<Attribute> _stream_1 = orderAttr.stream();
      final java.util.function.Function<Attribute, String> _function_1 = (Attribute e) -> {
        return e.getName();
      };
      Stream<String> _map = _stream_1.<String>map(_function_1);
      Collector<String, ?, List<String>> _list = Collectors.<String>toList();
      List<String> _collect = _map.collect(_list);
      String _generateListString = this.utilityService.generateListString(_collect);
      groupby = (_groupby + _generateListString);
    }
    Pair<String, String> _mappedTo = Pair.<String, String>of("aggregations", this.argsstr);
    String _xifexpression = null;
    boolean _notEquals = (!Objects.equal(groupby, ""));
    if (_notEquals) {
      _xifexpression = groupby;
    } else {
      _xifexpression = null;
    }
    Pair<String, String> _mappedTo_1 = Pair.<String, String>of("group_by", _xifexpression);
    String _xifexpression_1 = null;
    boolean _notEquals_1 = (!Objects.equal(mapName, ""));
    if (_notEquals_1) {
      _xifexpression_1 = mapName;
    } else {
      _xifexpression_1 = input.toString();
    }
    Pair<String, String> _mappedTo_2 = Pair.<String, String>of("input", _xifexpression_1);
    HashMap<String, String> _newHashMap = CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1, _mappedTo_2);
    String _build = this.builder.build(AggregateAO.class, _newHashMap);
    return new Object[] { aliases, _build };
  }
  
  private Object[] buildAggregateOP(final Collection<QueryCache.QueryAggregate> list, final Collection<Attribute> list2, final Collection<QueryCache.QuerySource> srcs, final SimpleSelect select) {
    try {
      String _buildJoin = this.joinParser.buildJoin(srcs, select);
      return this.buildAggregateOP(list, list2, _buildJoin);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
