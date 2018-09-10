package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class AttributeNameParser implements IAttributeNameParser {
  private IUtilityService utilityService;
  
  private ICacheService cacheService;
  
  private IRenameParser renameParser;
  
  @Inject
  public AttributeNameParser(final IUtilityService utilityService, final ICacheService cacheService, final IRenameParser renameParser) {
    this.utilityService = utilityService;
    this.cacheService = cacheService;
    this.renameParser = renameParser;
  }
  
  @Override
  public String parse(final String attributename, final String sourcename) {
    String attribute = null;
    String source = null;
    if (((sourcename != null) && (!sourcename.equals("")))) {
      SystemSource tmp = null;
      attribute = attributename;
      String _xifexpression = null;
      SystemSource _systemSource = this.utilityService.getSystemSource(sourcename);
      SystemSource _tmp = (tmp = _systemSource);
      boolean _tripleNotEquals = (_tmp != null);
      if (_tripleNotEquals) {
        _xifexpression = tmp.getName();
      } else {
        _xifexpression = null;
      }
      source = _xifexpression;
    } else {
      boolean _contains = attributename.contains(".");
      if (_contains) {
        String[] split = attributename.split("\\.");
        String _get = split[1];
        attribute = _get;
        String _get_1 = split[0];
        source = _get_1;
      }
    }
    if ((source != null)) {
      boolean _isAggregationAttribute = this.utilityService.isAggregationAttribute(attribute);
      if (_isAggregationAttribute) {
        return attribute;
      }
      boolean isAlias = this.utilityService.isAttributeAlias(attribute);
      boolean _isSourceAlias = this.utilityService.isSourceAlias(source);
      if (_isSourceAlias) {
        if (isAlias) {
          return attribute;
        } else {
          String r = ((source + ".") + attribute);
          List<String> _attributeAliasesAsList = this.utilityService.getAttributeAliasesAsList();
          boolean _contains_1 = _attributeAliasesAsList.contains(r);
          if (_contains_1) {
            return r;
          } else {
            SystemSource _systemSource_1 = this.utilityService.getSystemSource(source);
            SystemAttribute _findByName = _systemSource_1.findByName(attributename);
            List<String> attributeAliases = _findByName.getAliases();
            boolean _isEmpty = attributeAliases.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              return attributeAliases.get(0);
            } else {
              return ((source + ".") + attribute);
            }
          }
        }
      } else {
        if (isAlias) {
          return attribute;
        } else {
          return ((source + ".") + attribute);
        }
      }
    } else {
      attribute = attributename;
      boolean _isAggregationAttribute_1 = this.utilityService.isAggregationAttribute(attribute);
      if (_isAggregationAttribute_1) {
        return attributename;
      }
      Optional<String> expressionString = this.utilityService.getQueryExpressionString(attribute);
      boolean _isPresent = expressionString.isPresent();
      if (_isPresent) {
        return expressionString.get();
      }
      boolean _isAttributeAlias = this.utilityService.isAttributeAlias(attribute);
      if (_isAttributeAlias) {
        return attribute;
      }
      ArrayList<SystemSource> containedBySources = CollectionLiterals.<SystemSource>newArrayList();
      Collection<String> _querySources = SystemSource.getQuerySources();
      for (final String name : _querySources) {
        {
          SystemSource source2 = this.utilityService.getSystemSource(name);
          boolean _hasAttribute = source2.hasAttribute(attribute);
          if (_hasAttribute) {
            containedBySources.add(source2);
          }
        }
      }
      int _size = containedBySources.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        SystemSource _get_2 = containedBySources.get(0);
        String _name = _get_2.getName();
        SystemSource _systemSource_2 = this.utilityService.getSystemSource(_name);
        SystemAttribute _findByName_1 = _systemSource_2.findByName(attribute);
        List<String> aliases = _findByName_1.getAliases();
        boolean _isEmpty_1 = aliases.isEmpty();
        boolean _not_1 = (!_isEmpty_1);
        if (_not_1) {
          boolean _isAggregationAttribute_2 = this.utilityService.isAggregationAttribute(attribute);
          if (_isAggregationAttribute_2) {
            return attribute;
          }
          return aliases.get(0);
        }
        SystemSource _get_3 = containedBySources.get(0);
        String _name_1 = _get_3.getName();
        SystemSource sourceStruct = this.utilityService.getSystemSource(_name_1);
        boolean _isEmpty_2 = sourceStruct.aliasList.isEmpty();
        boolean _not_2 = (!_isEmpty_2);
        if (_not_2) {
          String _get_4 = sourceStruct.aliasList.get(0);
          String _plus = (_get_4 + ".");
          return (_plus + attributename);
        }
        SystemSource _get_5 = containedBySources.get(0);
        String _name_2 = _get_5.getName();
        String _plus_1 = (_name_2 + ".");
        return (_plus_1 + attribute);
      }
      boolean _contains_2 = attributename.contains("()");
      if (_contains_2) {
        String _replace = attributename.replace("(", "");
        String _replace_1 = _replace.replace(")", "");
        String _plus_2 = ("${" + _replace_1);
        return (_plus_2 + "}");
      }
      boolean _contains_3 = attributename.contains("$(");
      if (_contains_3) {
        return attributename;
      }
    }
    throw new IllegalArgumentException((("attribute " + attribute) + " could not be resolved"));
  }
  
  @Override
  public String parse(final String attributename) {
    return this.parse(attributename, null);
  }
  
  @Override
  public String parse(final Attribute attribute) {
    String _xifexpression = null;
    if ((attribute != null)) {
      String _name = attribute.getName();
      _xifexpression = this.parse(_name);
    }
    return _xifexpression;
  }
}
