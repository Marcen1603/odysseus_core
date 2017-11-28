package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IProjectionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        String expressionString = this.selectParser.parseExpression(((SelectExpression[])Conversions.unwrapArray(expressions, SelectExpression.class))[i]).toString();
        Alias _alias = ((SelectExpression[])Conversions.unwrapArray(expressions, SelectExpression.class))[i].getAlias();
        boolean _tripleEquals = (_alias == null);
        if (_tripleEquals) {
          expressionName = this.utilityService.getExpressionName();
        } else {
          expressionName = ((SelectExpression[])Conversions.unwrapArray(expressions, SelectExpression.class))[i].getAlias().getName();
        }
        expressionStrings.add(expressionString);
        expressionStrings.add(expressionName);
        expressionStrings.add(",");
        final List<String> _converted_expressionStrings = (List<String>)expressionStrings;
        String t = this.utilityService.generateKeyValueString(((String[])Conversions.unwrapArray(_converted_expressionStrings, String.class)));
        String _expressionArgument = expressionArgument;
        expressionArgument = (_expressionArgument + t);
        this.cacheService.getExpressionCache().put(expressionName, t);
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
    String _build = this.builder.build(MapAO.class, CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1));
    return new Object[] { attributeNames, _build };
  }
  
  private String buildProjection(final SimpleSelect select, final CharSequence operator) {
    Collection<String> attributes = this.cacheService.getQueryCache().getProjectionAttributes(select);
    for (int i = 0; (i < (this.renameParser.getAliases().size() - 2)); i = (i + 3)) {
      {
        String attributename = ((String[])Conversions.unwrapArray(this.renameParser.getAliases(), String.class))[i];
        String sourcename = ((String[])Conversions.unwrapArray(this.renameParser.getAliases(), String.class))[(i + 1)];
        String alias = ((String[])Conversions.unwrapArray(this.renameParser.getAliases(), String.class))[(i + 2)];
        this.utilityService.getSource(sourcename).addAliasTo(attributename, alias);
      }
    }
    ArrayList<String> list = CollectionLiterals.<String>newArrayList();
    for (int i = 0; (i < attributes.size()); i++) {
      {
        final Collection<String> _converted_attributes = (Collection<String>)attributes;
        String attribute1 = this.utilityService.getProjectAttribute(((String[])Conversions.unwrapArray(_converted_attributes, String.class))[i]);
        list.add(attribute1);
      }
    }
    for (int i = 0; (i < (this.renameParser.getAliases().size() - 2)); i = (i + 3)) {
      {
        String attributename = ((String[])Conversions.unwrapArray(this.renameParser.getAliases(), String.class))[i];
        String sourcename = ((String[])Conversions.unwrapArray(this.renameParser.getAliases(), String.class))[(i + 1)];
        String alias = ((String[])Conversions.unwrapArray(this.renameParser.getAliases(), String.class))[(i + 2)];
        this.utilityService.getSource(sourcename).removeAliasFrom(attributename, alias);
      }
    }
    String argument = this.utilityService.generateListString(list).replace("\'[\'", "[\'").replace("\']\'", "\']");
    Pair<String, String> _mappedTo = Pair.<String, String>of("expressions", argument);
    String _string = operator.toString();
    Pair<String, String> _mappedTo_1 = Pair.<String, String>of("input", _string);
    return this.builder.build(MapAO.class, CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1));
  }
}
