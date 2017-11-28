package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class AttributeNameParser implements IAttributeNameParser {
  private final Logger log = LoggerFactory.getLogger(AttributeNameParser.class);
  
  private IUtilityService utilityService;
  
  private ICacheService cacheService;
  
  @Inject
  public AttributeNameParser(final IUtilityService utilityService, final ICacheService cacheService) {
    this.utilityService = utilityService;
    this.cacheService = cacheService;
  }
  
  @Override
  public String parse(final String attributename, final String sourcename) {
    this.log.info((((("[parse:: attributename=" + attributename) + ", sourcename=") + sourcename) + "]"));
    String attribute = null;
    String source = null;
    if (((sourcename != null) && (!sourcename.equals("")))) {
      SourceStruct tmp = null;
      attribute = attributename;
      String _xifexpression = null;
      if (((tmp = this.utilityService.getSource(sourcename)) != null)) {
        _xifexpression = tmp.getName();
      } else {
        _xifexpression = null;
      }
      source = _xifexpression;
    } else {
      boolean _contains = attributename.contains(".");
      if (_contains) {
        String[] split = attributename.split("\\.");
        attribute = split[1];
        source = split[0];
      }
    }
    if ((source != null)) {
      boolean _isAggregationAttribute = this.utilityService.isAggregationAttribute(attribute);
      if (_isAggregationAttribute) {
        this.log.info("YO-2");
        return attribute;
      }
      boolean isAlias = this.utilityService.isAttributeAlias(attribute);
      boolean _isSourceAlias = this.utilityService.isSourceAlias(source);
      if (_isSourceAlias) {
        if (isAlias) {
          this.log.info("YO-2");
          return attribute;
        } else {
          String r = ((source + ".") + attribute);
          boolean _contains_1 = this.utilityService.getAttributeAliasesAsList().contains(r);
          if (_contains_1) {
            this.log.info("YO-1");
            return r;
          } else {
            List<String> attributeAliases = this.utilityService.getSource(source).findByName(attributename).getAliases();
            boolean _isEmpty = attributeAliases.isEmpty();
            boolean _not = (!_isEmpty);
            if (_not) {
              this.log.info("YO0");
              return attributeAliases.get(0);
            } else {
              this.log.info("YO1");
              return ((source + ".") + attribute);
            }
          }
        }
      } else {
        if (isAlias) {
          this.log.info("YO2");
          return attribute;
        } else {
          this.log.info("Y3");
          return ((source + ".") + attribute);
        }
      }
    } else {
      attribute = attributename;
      this.log.info(("HERE-1::" + attribute));
      boolean _contains_2 = this.cacheService.getAggregationAttributeCache().contains(attribute);
      if (_contains_2) {
        return attribute;
      }
      boolean _containsValue = this.cacheService.getExpressionCache().containsValue(attribute);
      if (_containsValue) {
        String _get = this.cacheService.getExpressionCache().get(attribute);
        return ((String) _get);
      }
      this.log.info("HERE0");
      boolean _isAttributeAlias = this.utilityService.isAttributeAlias(attribute);
      if (_isAttributeAlias) {
        return attribute;
      }
      ArrayList<SourceStruct> containedBySources = CollectionLiterals.<SourceStruct>newArrayList();
      Collection<String> _querySources = SourceStruct.getQuerySources();
      for (final String name : _querySources) {
        {
          this.log.info(("querySource::" + name));
          SourceStruct source2 = this.utilityService.getSource(name);
          boolean _hasAttribute = source2.hasAttribute(attribute);
          if (_hasAttribute) {
            containedBySources.add(source2);
          }
        }
      }
      String _string = containedBySources.toString();
      String _plus = ("HERE1::" + _string);
      this.log.info(_plus);
      int _size = containedBySources.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        List<String> aliases = this.utilityService.getSource(containedBySources.get(0).getName()).findByName(attribute).getAliases();
        boolean _isEmpty_1 = aliases.isEmpty();
        boolean _not_1 = (!_isEmpty_1);
        if (_not_1) {
          this.log.info("HERE1 ALIASES NOT EMPTY");
          return aliases.get(0);
        }
        this.log.info("HERE2");
        SourceStruct sourceStruct = this.utilityService.getSource(containedBySources.get(0).getName());
        boolean _isEmpty_2 = sourceStruct.aliasList.isEmpty();
        boolean _not_2 = (!_isEmpty_2);
        if (_not_2) {
          String _get_1 = sourceStruct.aliasList.get(0);
          String _plus_1 = (_get_1 + ".");
          return (_plus_1 + attributename);
        }
        this.log.info("HERE3");
        String _name = containedBySources.get(0).getName();
        String _plus_2 = (_name + ".");
        return (_plus_2 + attribute);
      }
      boolean _contains_3 = attributename.contains("()");
      if (_contains_3) {
        String _replace = attributename.replace("(", "").replace(")", "");
        String _plus_3 = ("${" + _replace);
        return (_plus_3 + "}");
      }
      this.log.info("HERE4");
      boolean _contains_4 = attributename.contains("$(");
      if (_contains_4) {
        return attributename;
      }
      this.log.info("HERE5");
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
      _xifexpression = this.parse(attribute.getName());
    }
    return _xifexpression;
  }
}
