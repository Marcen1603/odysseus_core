package de.uniol.inf.is.odysseus.parser.novel.cql.generator;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.FunctionStore;
import de.uniol.inf.is.odysseus.mep.MEP;
import java.util.List;
import java.util.regex.Pattern;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class NameProvider {
  public boolean isAggregation(final String name) {
    Pattern _aggregatePattern = AggregateFunctionBuilderRegistry.getAggregatePattern();
    String _plus = ("AGGREGATEPATTERN::" + _aggregatePattern);
    InputOutput.<String>println(_plus);
    return AggregateFunctionBuilderRegistry.getAggregatePattern().matcher(name).toString().contains(name);
  }
  
  public boolean isMapper(final String name, final String function) {
    boolean _containsSymbol = FunctionStore.getInstance().containsSymbol(name);
    if (_containsSymbol) {
      try {
        SDFDatatype datatype = MEP.getInstance().parse(function).getReturnType();
        List<IFunction<?>> _functions = FunctionStore.getInstance().getFunctions(name);
        for (final IFunction<?> f : _functions) {
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
