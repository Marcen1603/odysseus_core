/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.generator;

import com.google.common.collect.Iterables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.uniol.inf.is.odysseus.parser.cql2.CQLRuntimeModule;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.ComplexSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Create;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Query;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StreamTo;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLBuilderModule;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.CacheModule;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.SelectCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ParserModule;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAggregationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ICreateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExistenceParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IPredicateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IQuantificationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.UtilityModule;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGenerator2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates PQL text from a CQL text.
 */
@SuppressWarnings("all")
public class CQLGenerator implements IGenerator2 {
  private final Logger log = LoggerFactory.getLogger(CQLGenerator.class);
  
  public static Injector injector;
  
  public static Map<String, String> databaseConnections = CollectionLiterals.<String, String>newHashMap();
  
  private IPredicateParser predicateParser;
  
  private IUtilityService utilityService;
  
  private ICacheService cacheService;
  
  private IAttributeNameParser nameParser;
  
  private IAttributeParser attributeParser;
  
  private IRenameParser renameParser;
  
  private IJoinParser joinParser;
  
  private ISelectParser selectParser;
  
  private IExistenceParser existenceParser;
  
  private IAggregationParser aggregationParser;
  
  private IQuantificationParser quantificationParser;
  
  private AbstractPQLOperatorBuilder builder;
  
  private ICreateParser createParser;
  
  public CQLGenerator() {
    CQLRuntimeModule _cQLRuntimeModule = new CQLRuntimeModule();
    UtilityModule _utilityModule = new UtilityModule();
    CacheModule _cacheModule = new CacheModule();
    PQLBuilderModule _pQLBuilderModule = new PQLBuilderModule();
    ParserModule _parserModule = new ParserModule();
    Injector _createInjector = Guice.createInjector(_cQLRuntimeModule, _utilityModule, _cacheModule, _pQLBuilderModule, _parserModule);
    CQLGenerator.injector = _createInjector;
    IUtilityService _instance = CQLGenerator.injector.<IUtilityService>getInstance(IUtilityService.class);
    this.utilityService = _instance;
    ICacheService _instance_1 = CQLGenerator.injector.<ICacheService>getInstance(ICacheService.class);
    this.cacheService = _instance_1;
    IPredicateParser _instance_2 = CQLGenerator.injector.<IPredicateParser>getInstance(IPredicateParser.class);
    this.predicateParser = _instance_2;
    IAttributeNameParser _instance_3 = CQLGenerator.injector.<IAttributeNameParser>getInstance(IAttributeNameParser.class);
    this.nameParser = _instance_3;
    IAttributeParser _instance_4 = CQLGenerator.injector.<IAttributeParser>getInstance(IAttributeParser.class);
    this.attributeParser = _instance_4;
    IRenameParser _instance_5 = CQLGenerator.injector.<IRenameParser>getInstance(IRenameParser.class);
    this.renameParser = _instance_5;
    IJoinParser _instance_6 = CQLGenerator.injector.<IJoinParser>getInstance(IJoinParser.class);
    this.joinParser = _instance_6;
    ISelectParser _instance_7 = CQLGenerator.injector.<ISelectParser>getInstance(ISelectParser.class);
    this.selectParser = _instance_7;
    IExistenceParser _instance_8 = CQLGenerator.injector.<IExistenceParser>getInstance(IExistenceParser.class);
    this.existenceParser = _instance_8;
    IAggregationParser _instance_9 = CQLGenerator.injector.<IAggregationParser>getInstance(IAggregationParser.class);
    this.aggregationParser = _instance_9;
    IQuantificationParser _instance_10 = CQLGenerator.injector.<IQuantificationParser>getInstance(IQuantificationParser.class);
    this.quantificationParser = _instance_10;
    AbstractPQLOperatorBuilder _instance_11 = CQLGenerator.injector.<AbstractPQLOperatorBuilder>getInstance(AbstractPQLOperatorBuilder.class);
    this.builder = _instance_11;
    ICreateParser _instance_12 = CQLGenerator.injector.<ICreateParser>getInstance(ICreateParser.class);
    this.createParser = _instance_12;
  }
  
  public void clear() {
    this.predicateParser.clear();
    this.utilityService.clear();
    this.joinParser.clear();
    this.renameParser.clear();
    this.selectParser.clear();
    this.attributeParser.clear();
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    _operatorCache.flush();
    Collection<SystemSource> _systemSources = this.cacheService.getSystemSources();
    _systemSources.clear();
    SelectCache _selectCache = this.cacheService.getSelectCache();
    _selectCache.flush();
    QueryCache _queryCache = this.cacheService.getQueryCache();
    _queryCache.clear();
    SystemSource.clearQuerySources();
    SystemSource.clearAttributeAliases();
  }
  
  @Override
  public void afterGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    this.clear();
  }
  
  @Override
  public void beforeGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    this.clear();
  }
  
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    TreeIterator<EObject> _allContents = resource.getAllContents();
    Iterable<EObject> _iterable = IteratorExtensions.<EObject>toIterable(_allContents);
    Iterable<Query> _filter = Iterables.<Query>filter(_iterable, Query.class);
    Query _get = ((Query[])Conversions.unwrapArray(_filter, Query.class))[0];
    CharSequence _parseStatement = this.parseStatement(_get);
    fsa.generateFile(("" + Integer.valueOf(1)), _parseStatement);
  }
  
  /**
   * Parses a {@link Query} object that either represents a {@link ComplexSelect}, {@link Create} or {@link StreamTo}.
   * It returns an operator plan that consists of PQL-operators.
   */
  public CharSequence parseStatement(final Query query) {
    this.log.debug("parsing CQL query: selecting query type");
    EObject _type = query.getType();
    if ((_type instanceof ComplexSelect)) {
      EObject _type_1 = query.getType();
      ComplexSelect select = ((ComplexSelect) _type_1);
      String _operation = select.getOperation();
      boolean _tripleNotEquals = (_operation != null);
      if (_tripleNotEquals) {
        SimpleSelect _left = select.getLeft();
        SimpleSelect _right = select.getRight();
        String _operation_1 = select.getOperation();
        this.selectParser.parseComplex(_left, _right, _operation_1);
      } else {
        SimpleSelect _left_1 = select.getLeft();
        this.selectParser.parse(_left_1);
      }
    } else {
      EObject _type_2 = query.getType();
      if ((_type_2 instanceof Create)) {
        EObject _type_3 = query.getType();
        this.createParser.parseCreate(((Create) _type_3));
      } else {
        EObject _type_4 = query.getType();
        if ((_type_4 instanceof StreamTo)) {
          EObject _type_5 = query.getType();
          this.createParser.parseStreamTo(((StreamTo) _type_5));
        }
      }
    }
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    return _operatorCache.getPQL();
  }
  
  public void setSchema(final Collection<SystemSource> schemas) {
    Stream<SystemSource> _stream = schemas.stream();
    final Consumer<SystemSource> _function = (SystemSource e) -> {
      Collection<SystemSource> _systemSources = this.cacheService.getSystemSources();
      _systemSources.add(e);
    };
    _stream.forEach(_function);
  }
  
  public void setMetaAttributes(final Collection<SystemSource> schemas) {
    Stream<SystemSource> _stream = schemas.stream();
    final Consumer<SystemSource> _function = (SystemSource e) -> {
      Collection<SystemSource> _systemSources = this.cacheService.getSystemSources();
      SystemSource _systemSource = new SystemSource(e, true);
      _systemSources.add(_systemSource);
    };
    _stream.forEach(_function);
  }
  
  public Map<String, String> setDatabaseConnections(final Map<String, String> connections) {
    return CQLGenerator.databaseConnections = connections;
  }
}
