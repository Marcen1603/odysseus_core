package de.uniol.inf.is.odysseus.parser.novel.cql.generator;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.FunctionStore;
import de.uniol.inf.is.odysseus.mep.MEP;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class NameProvider {
  public boolean isAggregation(final String name) {
    Pattern _aggregatePattern = AggregateFunctionBuilderRegistry.getAggregatePattern();
    String _plus = ("AGGREGATEPATTERN::" + _aggregatePattern);
    InputOutput.<String>println(_plus);
    Pattern _aggregatePattern_1 = AggregateFunctionBuilderRegistry.getAggregatePattern();
    Matcher _matcher = _aggregatePattern_1.matcher(name);
    String _string = _matcher.toString();
    return _string.contains(name);
  }
  
  public boolean isMapper(final String name, final String function) {
    FunctionStore _instance = FunctionStore.getInstance();
    boolean _containsSymbol = _instance.containsSymbol(name);
    if (_containsSymbol) {
      try {
        FunctionStore _instance_1 = FunctionStore.getInstance();
        List<IFunction<?>> _functions = _instance_1.getFunctions(name);
        String _plus = ("FUNCTIONSTORES:: " + _functions);
        InputOutput.<String>println(_plus);
        MEP _instance_2 = MEP.getInstance();
        IExpression<?> _parse = _instance_2.parse(function);
        SDFDatatype datatype = _parse.getReturnType();
        InputOutput.<String>println(("TYPE:: " + datatype));
        FunctionStore _instance_3 = FunctionStore.getInstance();
        List<IFunction<?>> _functions_1 = _instance_3.getFunctions(name);
        for (final IFunction<?> f : _functions_1) {
          SDFDatatype _returnType = f.getReturnType();
          boolean _equals = _returnType.equals(datatype);
          if (_equals) {
            return true;
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof IllegalArgumentException) {
          final IllegalArgumentException e = (IllegalArgumentException)_t;
          return false;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    return false;
  }
}
