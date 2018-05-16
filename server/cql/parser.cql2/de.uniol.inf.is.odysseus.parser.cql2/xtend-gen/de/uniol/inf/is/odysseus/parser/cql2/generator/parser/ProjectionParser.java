package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExpressionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IParsedObject;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IProjectionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class ProjectionParser implements IProjectionParser {
  private AbstractPQLOperatorBuilder builder;
  
  private ISelectParser selectParser;
  
  private IRenameParser renameParser;
  
  private IUtilityService utilityService;
  
  private ICacheService cacheService;
  
  private IAttributeParser attributeParser;
  
  private IExpressionParser expressionParser;
  
  private String curStrRep = "";
  
  @Inject
  public ProjectionParser(final AbstractPQLOperatorBuilder builder, final ISelectParser selectParser, final IRenameParser renameParser, final IUtilityService utilityService, final ICacheService cacheService, final IAttributeParser attributeParser, final IExpressionParser expressionParser) {
    this.builder = builder;
    this.selectParser = selectParser;
    this.utilityService = utilityService;
    this.cacheService = cacheService;
    this.renameParser = renameParser;
    this.attributeParser = attributeParser;
    this.expressionParser = expressionParser;
  }
  
  @Override
  public Object[] parse(final Collection<QueryCache.QueryExpression> expressions, final String input) {
    return this.buildMapOperator(expressions);
  }
  
  @Override
  public String parse(final SimpleSelect select, final String operator) {
    return this.buildProjection(select, operator);
  }
  
  private Object[] buildMapOperator(final Collection<QueryCache.QueryExpression> expressions) {
    try {
      return this.buildMapOperator(expressions, null);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private Object[] buildMapOperator(final Collection<QueryCache.QueryExpression> queryExpressions, final String input) throws PQLOperatorBuilderException {
    this.curStrRep = "";
    final Collection<String> stringList = CollectionLiterals.<String>newArrayList();
    final Collection<String> attributes = CollectionLiterals.<String>newArrayList();
    Stream<QueryCache.QueryExpression> _stream = queryExpressions.stream();
    final Consumer<QueryCache.QueryExpression> _function = (QueryCache.QueryExpression e) -> {
      String expressionName = "";
      String expressionString = e.parsedExpression.toString();
      String _name = e.parsedExpression.getName();
      boolean _tripleEquals = (_name == null);
      if (_tripleEquals) {
        String _expressionName = this.attributeParser.getExpressionName();
        expressionName = _expressionName;
      } else {
        String _name_1 = e.parsedExpression.getName();
        expressionName = _name_1;
      }
      stringList.add(expressionString);
      stringList.add(expressionName);
      stringList.add(",");
      String t = this.utilityService.generateKeyValueString(((String[])Conversions.unwrapArray(stringList, String.class)));
      String _curStrRep = this.curStrRep;
      this.curStrRep = (_curStrRep + (t + ","));
      stringList.clear();
      attributes.add(expressionName);
    };
    _stream.forEach(_function);
    String _replaceAll = this.curStrRep.replaceAll(",$", "");
    this.curStrRep = _replaceAll;
    Pair<String, String> _mappedTo = Pair.<String, String>of("expressions", this.curStrRep);
    Pair<String, String> _mappedTo_1 = Pair.<String, String>of("input", input);
    LinkedHashMap<String, String> _newLinkedHashMap = CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1);
    String build = this.builder.build(MapAO.class, _newLinkedHashMap);
    return new Object[] { attributes, build };
  }
  
  private String buildProjection(final SimpleSelect select, final CharSequence operator) {
    try {
      QueryCache _queryCache = this.cacheService.getQueryCache();
      Collection<QueryCache.QueryAttribute> attributes = _queryCache.getProjectionAttributes(select);
      for (int i = 0; (i < (this.renameParser.getAliases().size() - 2)); i = (i + 3)) {
        {
          Collection<String> _aliases = this.renameParser.getAliases();
          String attributename = ((String[])Conversions.unwrapArray(_aliases, String.class))[i];
          Collection<String> _aliases_1 = this.renameParser.getAliases();
          String sourcename = ((String[])Conversions.unwrapArray(_aliases_1, String.class))[(i + 1)];
          Collection<String> _aliases_2 = this.renameParser.getAliases();
          String alias = ((String[])Conversions.unwrapArray(_aliases_2, String.class))[(i + 2)];
          SystemSource _systemSource = this.utilityService.getSystemSource(sourcename);
          _systemSource.addAliasTo(attributename, alias);
        }
      }
      final ArrayList<String> list = CollectionLiterals.<String>newArrayList();
      Stream<QueryCache.QueryAttribute> _stream = attributes.stream();
      final Consumer<QueryCache.QueryAttribute> _function = (QueryCache.QueryAttribute e) -> {
        boolean _equals = e.type.equals(IParsedObject.Type.EXPRESSION);
        if (_equals) {
          String _string = ((QueryCache.QueryExpression) e).parsedExpression.toString();
          list.add(_string);
        } else {
          String name = "";
          boolean _equals_1 = e.type.equals(IParsedObject.Type.AGGREGATION);
          if (_equals_1) {
            String _name = ((QueryCache.QueryAggregate) e).parsedAggregation.getName();
            name = _name;
          } else {
            String _string_1 = e.parsedAttribute.toString();
            name = _string_1;
          }
          list.add(name);
        }
      };
      _stream.forEach(_function);
      for (int i = 0; (i < (this.renameParser.getAliases().size() - 2)); i = (i + 3)) {
        {
          Collection<String> _aliases = this.renameParser.getAliases();
          String attributename = ((String[])Conversions.unwrapArray(_aliases, String.class))[i];
          Collection<String> _aliases_1 = this.renameParser.getAliases();
          String sourcename = ((String[])Conversions.unwrapArray(_aliases_1, String.class))[(i + 1)];
          Collection<String> _aliases_2 = this.renameParser.getAliases();
          String alias = ((String[])Conversions.unwrapArray(_aliases_2, String.class))[(i + 2)];
          SystemSource _systemSource = this.utilityService.getSystemSource(sourcename);
          _systemSource.removeAliasFrom(attributename, alias);
        }
      }
      String _generateListString = this.utilityService.generateListString(list);
      String _replace = _generateListString.replace("\'[\'", "[\'");
      String argument = _replace.replace("\']\'", "\']");
      Pair<String, String> _mappedTo = Pair.<String, String>of("expressions", argument);
      String _string = operator.toString();
      Pair<String, String> _mappedTo_1 = Pair.<String, String>of("input", _string);
      LinkedHashMap<String, String> _newLinkedHashMap = CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1);
      return this.builder.build(MapAO.class, _newLinkedHashMap);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
