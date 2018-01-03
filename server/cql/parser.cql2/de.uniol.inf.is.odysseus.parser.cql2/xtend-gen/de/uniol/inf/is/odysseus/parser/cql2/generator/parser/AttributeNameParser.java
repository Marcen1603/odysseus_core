package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class AttributeNameParser implements IAttributeNameParser {
  private final Logger log = LoggerFactory.getLogger(AttributeNameParser.class);
  
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
      SystemSource _source = this.utilityService.getSource(sourcename);
      SystemSource _tmp = (tmp = _source);
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
          List<String> _attributeAliasesAsList = this.utilityService.getAttributeAliasesAsList();
          boolean _contains_1 = _attributeAliasesAsList.contains(r);
          if (_contains_1) {
            this.log.info("YO-1");
            return r;
          } else {
            SystemSource _source_1 = this.utilityService.getSource(source);
            SystemAttribute _findByName = _source_1.findByName(attributename);
            List<String> attributeAliases = _findByName.getAliases();
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
      Collection<Pair<SelectExpression, String>> _aggregationAttributeCache = this.cacheService.getAggregationAttributeCache();
      String _plus = ("aggregationAttributes=" + _aggregationAttributeCache);
      this.log.info(_plus);
      Collection<Pair<SelectExpression, String>> _aggregationAttributeCache_1 = this.cacheService.getAggregationAttributeCache();
      Stream<Pair<SelectExpression, String>> _stream = _aggregationAttributeCache_1.stream();
      final Function<Pair<SelectExpression, String>, String> _function = (Pair<SelectExpression, String> e) -> {
        return e.getE2();
      };
      Stream<String> _map = _stream.<String>map(_function);
      final Predicate<String> _function_1 = (String p) -> {
        return p.equals(attributename);
      };
      Stream<String> _filter = _map.filter(_function_1);
      Optional<String> _findFirst = _filter.findFirst();
      boolean _isPresent = _findFirst.isPresent();
      if (_isPresent) {
        return attributename;
      }
      Map<String, String> _expressionCache = this.cacheService.getExpressionCache();
      boolean _containsValue = _expressionCache.containsValue(attribute);
      if (_containsValue) {
        Map<String, String> _expressionCache_1 = this.cacheService.getExpressionCache();
        String _get_2 = _expressionCache_1.get(attribute);
        return ((String) _get_2);
      }
      this.log.info("HERE0");
      boolean _isAttributeAlias = this.utilityService.isAttributeAlias(attribute);
      if (_isAttributeAlias) {
        return attribute;
      }
      ArrayList<SystemSource> containedBySources = CollectionLiterals.<SystemSource>newArrayList();
      Collection<String> _querySources = SystemSource.getQuerySources();
      for (final String name : _querySources) {
        {
          this.log.info(("querySource::" + name));
          SystemSource source2 = this.utilityService.getSource(name);
          boolean _hasAttribute = source2.hasAttribute(attribute);
          if (_hasAttribute) {
            containedBySources.add(source2);
          }
        }
      }
      String _string = containedBySources.toString();
      String _plus_1 = ("HERE1::" + _string);
      this.log.info(_plus_1);
      int _size = containedBySources.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        SystemSource _get_3 = containedBySources.get(0);
        String _name = _get_3.getName();
        SystemSource _source_2 = this.utilityService.getSource(_name);
        SystemAttribute _findByName_1 = _source_2.findByName(attribute);
        List<String> aliases = _findByName_1.getAliases();
        boolean _isEmpty_1 = aliases.isEmpty();
        boolean _not_1 = (!_isEmpty_1);
        if (_not_1) {
          boolean _isAggregationAttribute_1 = this.utilityService.isAggregationAttribute(attribute);
          if (_isAggregationAttribute_1) {
            return attribute;
          }
          this.log.info("HERE1 ALIASES NOT EMPTY");
          return aliases.get(0);
        }
        this.log.info("HERE2");
        SystemSource _get_4 = containedBySources.get(0);
        String _name_1 = _get_4.getName();
        SystemSource sourceStruct = this.utilityService.getSource(_name_1);
        boolean _isEmpty_2 = sourceStruct.aliasList.isEmpty();
        boolean _not_2 = (!_isEmpty_2);
        if (_not_2) {
          String _get_5 = sourceStruct.aliasList.get(0);
          String _plus_2 = (_get_5 + ".");
          return (_plus_2 + attributename);
        }
        this.log.info("HERE3");
        SystemSource _get_6 = containedBySources.get(0);
        String _name_2 = _get_6.getName();
        String _plus_3 = (_name_2 + ".");
        return (_plus_3 + attribute);
      }
      boolean _contains_2 = attributename.contains("()");
      if (_contains_2) {
        String _replace = attributename.replace("(", "");
        String _replace_1 = _replace.replace(")", "");
        String _plus_4 = ("${" + _replace_1);
        return (_plus_4 + "}");
      }
      this.log.info("HERE4");
      boolean _contains_3 = attributename.contains("$(");
      if (_contains_3) {
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
      String _name = attribute.getName();
      _xifexpression = this.parse(_name);
    }
    return _xifexpression;
  }
}
