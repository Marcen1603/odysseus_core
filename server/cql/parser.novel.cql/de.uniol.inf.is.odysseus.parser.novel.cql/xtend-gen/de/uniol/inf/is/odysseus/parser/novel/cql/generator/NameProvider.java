package de.uniol.inf.is.odysseus.parser.novel.cql.generator;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.FunctionStore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class NameProvider {
  public boolean isAggregation(final String name) {
    Pattern _aggregatePattern = AggregateFunctionBuilderRegistry.getAggregatePattern();
    Matcher _matcher = _aggregatePattern.matcher(name);
    String _string = _matcher.toString();
    return _string.contains(name);
  }
  
  public boolean isMapper(final String name) {
    return (FunctionStore.getInstance().containsSymbol(name) && (!this.isAggregation(name)));
  }
}
