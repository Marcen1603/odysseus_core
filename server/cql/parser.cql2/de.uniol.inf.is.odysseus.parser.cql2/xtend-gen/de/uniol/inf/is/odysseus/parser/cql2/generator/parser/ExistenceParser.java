package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IExistenceParser;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class ExistenceParser implements IExistenceParser {
  private List<Map<String, String>> registry_existenceOperators = CollectionLiterals.<Map<String, String>>newArrayList();
  
  private AbstractPQLOperatorBuilder builder;
  
  private ICacheService cacheService;
  
  @Inject
  public ExistenceParser(final AbstractPQLOperatorBuilder builder, final ICacheService cacheService) {
    this.builder = builder;
    this.cacheService = cacheService;
  }
  
  @Override
  public String parse() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
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
            this.cacheService.getOperatorCache().registerOperator(this.builder.build(ExistenceAO.class, newArgs));
          }
        }
        String t = this.cacheService.getOperatorCache().getOperator(select);
        OperatorCache _operatorCache = this.cacheService.getOperatorCache();
        String _substring = t.substring(0, t.lastIndexOf("}"));
        String _plus = (_substring + "},");
        String _plus_1 = (_plus + "JOIN(");
        String _lastOperatorId = this.cacheService.getOperatorCache().lastOperatorId();
        String _plus_2 = (_plus_1 + _lastOperatorId);
        String _plus_3 = (_plus_2 + ",");
        String _plus_4 = (_plus_3 + selectInput);
        String _plus_5 = (_plus_4 + "))");
        _operatorCache.addOperator(select, _plus_5);
        this.cacheService.getOperatorCache().swapLastOperators();
      } else {
        for (final Map<String, String> args_1 : this.registry_existenceOperators) {
          {
            Map<String, String> newArgs = args_1;
            String _get = args_1.get("input");
            String _plus_6 = (_get + ",");
            String _plus_7 = (_plus_6 + selectInput);
            newArgs.put("input", _plus_7);
            this.cacheService.getOperatorCache().registerOperator(this.builder.build(ExistenceAO.class, newArgs));
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
