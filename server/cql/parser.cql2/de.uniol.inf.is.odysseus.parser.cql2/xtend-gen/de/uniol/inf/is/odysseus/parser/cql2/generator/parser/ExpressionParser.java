package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ExpressionComponent;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Function;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Matrix;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectArgument;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Vector;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExpressionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class ExpressionParser implements IExpressionParser {
  private IAttributeNameParser nameParser;
  
  private IUtilityService utilityService;
  
  @Inject
  public ExpressionParser(final IUtilityService utilityService, final IAttributeNameParser nameParser) {
    this.utilityService = utilityService;
    this.nameParser = nameParser;
  }
  
  @Override
  public String parse(final SelectExpression e) {
    String str = "";
    for (int i = 0; (i < e.getExpressions().size()); i++) {
      {
        EList<ExpressionComponent> _expressions = e.getExpressions();
        ExpressionComponent _get = _expressions.get(i);
        EObject component = ((ExpressionComponent) _get).getValue();
        boolean _matched = false;
        if (component instanceof Function) {
          _matched=true;
          String _str = str;
          String _name = ((Function)component).getName();
          String _plus = (_name + "(");
          EObject _value = ((Function)component).getValue();
          String _parse = this.parse(((SelectExpression) _value));
          String _plus_1 = (_plus + _parse);
          String _plus_2 = (_plus_1 + ")");
          str = (_str + _plus_2);
        }
        if (!_matched) {
          if (component instanceof Attribute) {
            _matched=true;
            String _str = str;
            String _name = ((Attribute)component).getName();
            String _parse = this.nameParser.parse(_name);
            str = (_str + _parse);
          }
        }
        if (!_matched) {
          if (component instanceof IntConstant) {
            _matched=true;
            String _str = str;
            int _value = ((IntConstant)component).getValue();
            String _plus = (Integer.valueOf(_value) + "");
            str = (_str + _plus);
          }
        }
        if (!_matched) {
          if (component instanceof FloatConstant) {
            _matched=true;
            String _str = str;
            String _value = ((FloatConstant)component).getValue();
            String _plus = (_value + "");
            str = (_str + _plus);
          }
        }
        if (!_matched) {
          if (component instanceof BoolConstant) {
            _matched=true;
            String _str = str;
            String _value = ((BoolConstant)component).getValue();
            String _plus = (_value + "");
            str = (_str + _plus);
          }
        }
        if (!_matched) {
          if (component instanceof StringConstant) {
            _matched=true;
            String _str = str;
            String _value = ((StringConstant)component).getValue();
            String _plus = ("\"" + _value);
            String _plus_1 = (_plus + "\"");
            str = (_str + _plus_1);
          }
        }
        if (!_matched) {
          if (component instanceof Vector) {
            _matched=true;
            String _str = str;
            String _value = ((Vector)component).getValue();
            str = (_str + _value);
          }
        }
        if (!_matched) {
          if (component instanceof Matrix) {
            _matched=true;
            String _str = str;
            String _value = ((Matrix)component).getValue();
            str = (_str + _value);
          }
        }
        EList<ExpressionComponent> _expressions_1 = e.getExpressions();
        int _size = _expressions_1.size();
        int _minus = (_size - 1);
        boolean _notEquals = (i != _minus);
        if (_notEquals) {
          String _str = str;
          EList<String> _operators = e.getOperators();
          String _get_1 = _operators.get(i);
          str = (_str + _get_1);
        }
      }
    }
    return str;
  }
  
  @Override
  public Collection<SelectExpression> extractSelectExpressionsFromArgument(final Collection<SelectArgument> args) {
    final ArrayList<SelectExpression> list = CollectionLiterals.<SelectExpression>newArrayList();
    Stream<SelectArgument> _stream = args.stream();
    final Predicate<SelectArgument> _function = (SelectArgument a) -> {
      SelectExpression _expression = a.getExpression();
      return (!Objects.equal(_expression, null));
    };
    Stream<SelectArgument> _filter = _stream.filter(_function);
    final Consumer<SelectArgument> _function_1 = (SelectArgument a) -> {
      SelectExpression _expression = a.getExpression();
      EList<ExpressionComponent> _expressions = _expression.getExpressions();
      int _size = _expressions.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        SelectExpression _expression_1 = a.getExpression();
        EList<ExpressionComponent> _expressions_1 = _expression_1.getExpressions();
        ExpressionComponent aggregation = _expressions_1.get(0);
        EObject function = aggregation.getValue();
        if ((function instanceof Function)) {
          String _name = ((Function)function).getName();
          SelectExpression _expression_2 = a.getExpression();
          String _parse = this.parse(((SelectExpression) _expression_2));
          String _string = _parse.toString();
          boolean _isMEPFunctionMame = this.utilityService.isMEPFunctionMame(_name, _string);
          if (_isMEPFunctionMame) {
            SelectExpression _expression_3 = a.getExpression();
            list.add(_expression_3);
          }
        } else {
          SelectExpression _expression_4 = a.getExpression();
          list.add(_expression_4);
        }
      } else {
        SelectExpression _expression_5 = a.getExpression();
        list.add(_expression_5);
      }
    };
    _filter.forEach(_function_1);
    return list;
  }
}
