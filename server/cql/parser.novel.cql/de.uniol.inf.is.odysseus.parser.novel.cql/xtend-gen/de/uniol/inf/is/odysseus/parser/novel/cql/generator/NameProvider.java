package de.uniol.inf.is.odysseus.parser.novel.cql.generator;

import com.google.common.collect.ImmutableSet;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.FunctionSignature;
import de.uniol.inf.is.odysseus.mep.MEP;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class NameProvider {
  public boolean isAggregation(final String name) {
    Pattern _aggregatePattern = AggregateFunctionBuilderRegistry.getAggregatePattern();
    Matcher _matcher = _aggregatePattern.matcher(name);
    return _matcher.find();
  }
  
  public boolean isMapper(final String name) {
    ImmutableSet<FunctionSignature> _functions = MEP.getFunctions();
    for (final FunctionSignature sig : _functions) {
      String _symbol = sig.getSymbol();
      String _upperCase = name.toUpperCase();
      boolean _equals = _symbol.equals(_upperCase);
      if (_equals) {
        return true;
      }
    }
    return false;
  }
}
