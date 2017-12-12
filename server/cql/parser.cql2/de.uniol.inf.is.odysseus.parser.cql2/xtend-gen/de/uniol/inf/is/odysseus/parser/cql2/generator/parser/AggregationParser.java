package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionComponent;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Starthing;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAggregationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class AggregationParser implements IAggregationParser {
  private AbstractPQLOperatorBuilder builder;
  
  private IUtilityService utilityService;
  
  private IJoinParser joinParser;
  
  private IAttributeNameParser attributeParser;
  
  @Inject
  public AggregationParser(final AbstractPQLOperatorBuilder builder, final IUtilityService utilityService, final IJoinParser joinParser, final IAttributeNameParser attributeParser) {
    this.builder = builder;
    this.utilityService = utilityService;
    this.joinParser = joinParser;
    this.attributeParser = attributeParser;
  }
  
  @Override
  public Object[] parse(final Collection<SelectExpression> list, final List<Attribute> list2, final List<Source> srcs) {
    return this.buildAggregateOP(list, list2, srcs);
  }
  
  private Object[] buildAggregateOP(final Collection<SelectExpression> aggAttr, final List<Attribute> orderAttr, final CharSequence input) {
    String argsstr = "";
    List<String> args = CollectionLiterals.<String>newArrayList();
    List<String> aliases = CollectionLiterals.<String>newArrayList();
    String mapName = "";
    for (int i = 0; (i < ((Object[])Conversions.unwrapArray(aggAttr, Object.class)).length); i++) {
      {
        SelectExpression _get = ((SelectExpression[])Conversions.unwrapArray(aggAttr, SelectExpression.class))[i];
        EList<ExpressionComponent> _expressions = _get.getExpressions();
        ExpressionComponent _get_1 = _expressions.get(0);
        EObject _value = _get_1.getValue();
        Function aggregation = ((Function) _value);
        String attributename = "";
        String datatype = "";
        EObject _value_1 = aggregation.getValue();
        EList<ExpressionComponent> components = ((SelectExpression) _value_1).getExpressions();
        int _size = components.size();
        boolean _equals = (_size == 1);
        if (_equals) {
          ExpressionComponent _get_2 = components.get(0);
          EObject comp = _get_2.getValue();
          boolean _matched = false;
          if (comp instanceof Attribute) {
            _matched=true;
            String _name = ((Attribute)comp).getName();
            String _parse = this.attributeParser.parse(_name);
            attributename = _parse;
            String _dataTypeFrom = this.utilityService.getDataTypeFrom(attributename);
            datatype = _dataTypeFrom;
          }
          if (!_matched) {
            if (comp instanceof Starthing) {
              _matched=true;
              attributename = "*";
            }
          }
        } else {
        }
        String _name = aggregation.getName();
        args.add(_name);
        args.add(attributename);
        String alias = "";
        SelectExpression _get_3 = ((SelectExpression[])Conversions.unwrapArray(aggAttr, SelectExpression.class))[i];
        Alias _alias = _get_3.getAlias();
        boolean _tripleNotEquals = (_alias != null);
        if (_tripleNotEquals) {
          SelectExpression _get_4 = ((SelectExpression[])Conversions.unwrapArray(aggAttr, SelectExpression.class))[i];
          Alias _alias_1 = _get_4.getAlias();
          String _name_1 = _alias_1.getName();
          alias = _name_1;
        } else {
          String _name_2 = aggregation.getName();
          String _aggregationName = this.utilityService.getAggregationName(_name_2);
          alias = _aggregationName;
        }
        args.add(alias);
        aliases.add(alias);
        boolean _notEquals = (!Objects.equal(datatype, ""));
        if (_notEquals) {
          args.add(datatype);
        }
        SelectExpression _get_5 = ((SelectExpression[])Conversions.unwrapArray(aggAttr, SelectExpression.class))[i];
        this.utilityService.addAggregationAttribute(_get_5, alias);
        args.add(",");
        String _argsstr = argsstr;
        final List<String> _converted_args = (List<String>)args;
        String _generateKeyValueString = this.utilityService.generateKeyValueString(((String[])Conversions.unwrapArray(_converted_args, String.class)));
        argsstr = (_argsstr + _generateKeyValueString);
        int _length = ((Object[])Conversions.unwrapArray(aggAttr, Object.class)).length;
        int _minus = (_length - 1);
        boolean _notEquals_1 = (i != _minus);
        if (_notEquals_1) {
          String _argsstr_1 = argsstr;
          argsstr = (_argsstr_1 + ",");
        }
        args.clear();
      }
    }
    String groupby = "";
    boolean _isEmpty = orderAttr.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      String _groupby = groupby;
      Stream<Attribute> _stream = orderAttr.stream();
      final java.util.function.Function<Attribute, String> _function = (Attribute e) -> {
        String _name = e.getName();
        return this.attributeParser.parse(_name, null);
      };
      Stream<String> _map = _stream.<String>map(_function);
      Collector<String, ?, List<String>> _list = Collectors.<String>toList();
      List<String> _collect = _map.collect(_list);
      String _generateListString = this.utilityService.generateListString(_collect);
      groupby = (_groupby + _generateListString);
    }
    Pair<String, String> _mappedTo = Pair.<String, String>of("aggregations", argsstr);
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
  
  private Object[] buildAggregateOP(final Collection<SelectExpression> list, final List<Attribute> list2, final List<Source> srcs) {
    String _buildJoin = this.joinParser.buildJoin(srcs);
    return this.buildAggregateOP(list, list2, _buildJoin);
  }
}
