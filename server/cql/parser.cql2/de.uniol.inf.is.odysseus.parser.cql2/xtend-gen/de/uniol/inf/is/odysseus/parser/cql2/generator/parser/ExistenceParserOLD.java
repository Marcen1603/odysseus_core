package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IExistenceParser;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class ExistenceParserOLD implements IExistenceParser {
  private List<Map<String, String>> registry_existenceOperators = CollectionLiterals.<Map<String, String>>newArrayList();
  
  private AbstractPQLOperatorBuilder builder;
  
  private ICacheService cacheService;
  
  @Inject
  public ExistenceParserOLD(final AbstractPQLOperatorBuilder builder, final ICacheService cacheService) {
    this.builder = builder;
    this.cacheService = cacheService;
  }
  
  @Override
  public Object parse(final ComplexPredicate predicate, final SimpleSelect parent) {
    return null;
  }
  
  @Override
  public void register(final SimpleSelect selectInput, final String select) {
    boolean _isEmpty = this.registry_existenceOperators.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      boolean _equals = select.equals("");
      boolean _not_1 = (!_equals);
      if (_not_1) {
        for (final Map<String, String> args : this.registry_existenceOperators) {
          {
            Map<String, String> newArgs = args;
            String _get = args.get("input");
            String _plus = (_get + ",");
            String _plus_1 = (_plus + selectInput);
            newArgs.put("input", _plus_1);
          }
        }
        OperatorCache _operatorCache = this.cacheService.getOperatorCache();
        Optional<String> t = _operatorCache.getOperator(select);
      } else {
        for (final Map<String, String> args_1 : this.registry_existenceOperators) {
          {
            Map<String, String> newArgs = args_1;
            String _get = args_1.get("input");
            String _plus = (_get + ",");
            String _plus_1 = (_plus + selectInput);
            newArgs.put("input", _plus_1);
          }
        }
      }
    }
  }
  
  @Override
  public Map<String, String> getOperator(final int i) {
    return this.registry_existenceOperators.get(i);
  }
  
  @Override
  public Collection<Map<String, String>> getOperators() {
    return this.registry_existenceOperators;
  }
}
