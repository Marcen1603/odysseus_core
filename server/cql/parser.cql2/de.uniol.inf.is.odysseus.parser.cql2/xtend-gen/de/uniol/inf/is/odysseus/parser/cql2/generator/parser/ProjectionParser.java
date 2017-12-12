package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IProjectionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class ProjectionParser implements IProjectionParser {
  private AbstractPQLOperatorBuilder builder;
  
  private ISelectParser selectParser;
  
  private IRenameParser renameParser;
  
  private IUtilityService utilityService;
  
  private ICacheService cacheService;
  
  @Inject
  public ProjectionParser(final AbstractPQLOperatorBuilder builder, final ISelectParser selectParser, final IRenameParser renameParser, final IUtilityService utilityService, final ICacheService cacheService) {
    this.builder = builder;
    this.selectParser = selectParser;
    this.utilityService = utilityService;
    this.cacheService = cacheService;
    this.renameParser = renameParser;
  }
  
  @Override
  public Object[] parse(final Collection<SelectExpression> expressions, final String input) {
    return this.buildMapOperator(expressions);
  }
  
  @Override
  public String parse(final SimpleSelect select, final String operator) {
    return this.buildProjection(select, operator);
  }
  
  private Object[] buildMapOperator(final Collection<SelectExpression> expressions) {
    return this.buildMapOperator(expressions, null);
  }
  
  private Object[] buildMapOperator(final Collection<SelectExpression> expressions, final String input) {
    String expressionArgument = "";
    List<String> expressionStrings = CollectionLiterals.<String>newArrayList();
    List<String> attributeNames = CollectionLiterals.<String>newArrayList();
    for (int i = 0; (i < expressions.size()); i++) {
      {
        String expressionName = "";
        SelectExpression _get = ((SelectExpression[])Conversions.unwrapArray(expressions, SelectExpression.class))[i];
        String _parseExpression = this.selectParser.parseExpression(_get);
        String expressionString = _parseExpression.toString();
        SelectExpression _get_1 = ((SelectExpression[])Conversions.unwrapArray(expressions, SelectExpression.class))[i];
        Alias _alias = _get_1.getAlias();
        boolean _tripleEquals = (_alias == null);
        if (_tripleEquals) {
          String _expressionName = this.utilityService.getExpressionName();
          expressionName = _expressionName;
        } else {
          SelectExpression _get_2 = ((SelectExpression[])Conversions.unwrapArray(expressions, SelectExpression.class))[i];
          Alias _alias_1 = _get_2.getAlias();
          String _name = _alias_1.getName();
          expressionName = _name;
        }
        expressionStrings.add(expressionString);
        expressionStrings.add(expressionName);
        expressionStrings.add(",");
        final List<String> _converted_expressionStrings = (List<String>)expressionStrings;
        String t = this.utilityService.generateKeyValueString(((String[])Conversions.unwrapArray(_converted_expressionStrings, String.class)));
        String _expressionArgument = expressionArgument;
        expressionArgument = (_expressionArgument + t);
        Map<String, String> _expressionCache = this.cacheService.getExpressionCache();
        _expressionCache.put(expressionName, t);
        int _size = expressions.size();
        int _minus = (_size - 1);
        boolean _notEquals = (i != _minus);
        if (_notEquals) {
          String _expressionArgument_1 = expressionArgument;
          expressionArgument = (_expressionArgument_1 + ",");
        }
        expressionStrings.clear();
        attributeNames.add(expressionName);
      }
    }
    Pair<String, String> _mappedTo = Pair.<String, String>of("expressions", expressionArgument);
    Pair<String, String> _mappedTo_1 = Pair.<String, String>of("input", input);
    LinkedHashMap<String, String> _newLinkedHashMap = CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1);
    String _build = this.builder.build(MapAO.class, _newLinkedHashMap);
    return new Object[] { attributeNames, _build };
  }
  
  private String buildProjection(final SimpleSelect select, final CharSequence operator) {
    QueryCache _queryCache = this.cacheService.getQueryCache();
    Collection<String> attributes = _queryCache.getProjectionAttributes(select);
    for (int i = 0; (i < (this.renameParser.getAliases().size() - 2)); i = (i + 3)) {
      {
        Collection<String> _aliases = this.renameParser.getAliases();
        String attributename = ((String[])Conversions.unwrapArray(_aliases, String.class))[i];
        Collection<String> _aliases_1 = this.renameParser.getAliases();
        String sourcename = ((String[])Conversions.unwrapArray(_aliases_1, String.class))[(i + 1)];
        Collection<String> _aliases_2 = this.renameParser.getAliases();
        String alias = ((String[])Conversions.unwrapArray(_aliases_2, String.class))[(i + 2)];
        SourceStruct _source = this.utilityService.getSource(sourcename);
        _source.addAliasTo(attributename, alias);
      }
    }
    ArrayList<String> list = CollectionLiterals.<String>newArrayList();
    for (int i = 0; (i < attributes.size()); i++) {
      {
        final Collection<String> _converted_attributes = (Collection<String>)attributes;
        String _get = ((String[])Conversions.unwrapArray(_converted_attributes, String.class))[i];
        String attribute1 = this.utilityService.getProjectAttribute(_get);
        list.add(attribute1);
      }
    }
    for (int i = 0; (i < (this.renameParser.getAliases().size() - 2)); i = (i + 3)) {
      {
        Collection<String> _aliases = this.renameParser.getAliases();
        String attributename = ((String[])Conversions.unwrapArray(_aliases, String.class))[i];
        Collection<String> _aliases_1 = this.renameParser.getAliases();
        String sourcename = ((String[])Conversions.unwrapArray(_aliases_1, String.class))[(i + 1)];
        Collection<String> _aliases_2 = this.renameParser.getAliases();
        String alias = ((String[])Conversions.unwrapArray(_aliases_2, String.class))[(i + 2)];
        SourceStruct _source = this.utilityService.getSource(sourcename);
        _source.removeAliasFrom(attributename, alias);
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
  }
}
