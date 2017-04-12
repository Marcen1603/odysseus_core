package de.uniol.inf.is.odysseus.parser.novel.cql.tests.util;

import de.uniol.inf.is.odysseus.parser.novel.cql.generator.NameProvider;
import java.util.ArrayList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class NameProviderHelper extends NameProvider {
  @Override
  public boolean isAggregation(final String name) {
    ArrayList<String> list = CollectionLiterals.<String>newArrayList();
    list.add("COUNT");
    list.add("MAX");
    list.add("MIN");
    list.add("SUM");
    list.add("AVG");
    return list.contains(name);
  }
  
  @Override
  public boolean isMapper(final String name, final String function) {
    ArrayList<String> list = CollectionLiterals.<String>newArrayList();
    list.add("DolToEur");
    list.add("Max");
    list.add("Min");
    list.add("Det");
    return list.contains(name);
  }
}
