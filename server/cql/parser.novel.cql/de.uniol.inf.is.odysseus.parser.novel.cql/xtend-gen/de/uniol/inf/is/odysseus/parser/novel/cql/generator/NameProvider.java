package de.uniol.inf.is.odysseus.parser.novel.cql.generator;

@Deprecated
@SuppressWarnings("all")
public class NameProvider {
  public boolean isAggregation(final String name) {
    return false;
  }
  
  public boolean isMapper(final String name, final String function) {
    return false;
  }
}
