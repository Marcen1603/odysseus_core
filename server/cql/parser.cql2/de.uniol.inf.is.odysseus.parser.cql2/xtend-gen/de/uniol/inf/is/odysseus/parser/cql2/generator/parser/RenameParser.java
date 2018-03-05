package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemSource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Pair;

@SuppressWarnings("all")
public class RenameParser implements IRenameParser {
  private IUtilityService utilityService;
  
  private AbstractPQLOperatorBuilder builder;
  
  private IJoinParser joinParser;
  
  private ICacheService cacheService;
  
  private Collection<String> renameAliases;
  
  private Collection<String> processedSources;
  
  private Collection<QueryCache.QuerySource> sourcesDuringRename;
  
  @Inject
  public RenameParser(final AbstractPQLOperatorBuilder builder, final IUtilityService utilityService, final IJoinParser joinParser, final ICacheService cacheService) {
    this.utilityService = utilityService;
    this.builder = builder;
    this.joinParser = joinParser;
    this.cacheService = cacheService;
    ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
    this.renameAliases = _newArrayList;
    ArrayList<String> _newArrayList_1 = CollectionLiterals.<String>newArrayList();
    this.processedSources = _newArrayList_1;
    ArrayList<QueryCache.QuerySource> _newArrayList_2 = CollectionLiterals.<QueryCache.QuerySource>newArrayList();
    this.sourcesDuringRename = _newArrayList_2;
  }
  
  @Override
  public CharSequence buildRename(final CharSequence input, final SimpleSource simpleSource, final SimpleSelect select, final int selfJoin) {
    final ArrayList<List<String>> listOfLists = CollectionLiterals.<List<String>>newArrayList();
    final SystemSource source = this.utilityService.getSystemSource(simpleSource);
    String _xifexpression = null;
    Alias _alias = simpleSource.getAlias();
    boolean _tripleNotEquals = (_alias != null);
    if (_tripleNotEquals) {
      Alias _alias_1 = simpleSource.getAlias();
      _xifexpression = _alias_1.getName();
    } else {
      _xifexpression = null;
    }
    final String sourcealias = _xifexpression;
    for (int j = 0; (j < source.getAttributeList().size()); j++) {
      {
        Collection<SystemAttribute> _attributeList = source.getAttributeList();
        SystemAttribute attr = ((SystemAttribute[])Conversions.unwrapArray(_attributeList, SystemAttribute.class))[j];
        int k = 0;
        int l = 0;
        while ((l < attr.aliases.size())) {
          {
            String attrAlias = attr.aliases.get(l);
            String sourceAlias = source.getAssociatedSource(attrAlias);
            if (((sourceAlias != null) && (sourceAlias.equals(sourcealias) || sourceAlias.equals(simpleSource.getName())))) {
              List<String> col = null;
              int _size = listOfLists.size();
              boolean _lessEqualsThan = (_size <= k);
              if (_lessEqualsThan) {
                ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
                col = _newArrayList;
              } else {
                List<String> _get = listOfLists.get(k);
                col = _get;
              }
              col.add(attr.attributename);
              col.add(attrAlias);
              int _size_1 = listOfLists.size();
              boolean _lessEqualsThan_1 = (_size_1 <= k);
              if (_lessEqualsThan_1) {
                listOfLists.add(col);
              }
              k++;
            }
            l++;
          }
        }
      }
    }
    if ((((listOfLists.size() > 1) || (selfJoin > 0)) || (sourcealias != null))) {
      for (int j = 0; (j < listOfLists.size()); j++) {
        {
          List<String> list = listOfLists.get(j);
          for (int k = 0; (k < source.getAttributeList().size()); k++) {
            Collection<SystemAttribute> _attributeList = source.getAttributeList();
            SystemAttribute _get = ((SystemAttribute[])Conversions.unwrapArray(_attributeList, SystemAttribute.class))[k];
            boolean _contains = list.contains(_get.attributename);
            boolean _not = (!_contains);
            if (_not) {
              String alias = null;
              Collection<SystemAttribute> _attributeList_1 = source.getAttributeList();
              SystemAttribute _get_1 = ((SystemAttribute[])Conversions.unwrapArray(_attributeList_1, SystemAttribute.class))[k];
              String name = _get_1.attributename;
              if ((sourcealias != null)) {
                if (((j > 0) && (listOfLists.size() > 1))) {
                  String _name = source.getName();
                  String _generateAlias = this.generateAlias(name, _name, j);
                  alias = _generateAlias;
                } else {
                  alias = ((sourcealias + ".") + name);
                }
              }
              this.renameAliases.add(name);
              String _name_1 = source.getName();
              this.renameAliases.add(_name_1);
              this.renameAliases.add(alias);
              list.add(name);
              list.add(alias);
            }
          }
        }
      }
    }
    ArrayList<String> renames = CollectionLiterals.<String>newArrayList();
    String _name = source.getName();
    this.processedSources.add(_name);
    for (int j = 0; (j < listOfLists.size()); j++) {
      List<String> _get = listOfLists.get(j);
      String _string = input.toString();
      String _parse = this.parse(_get, _string, select);
      renames.add(_parse);
    }
    int _size = renames.size();
    boolean _greaterThan = (_size > 1);
    if (_greaterThan) {
      final ArrayList<String> _converted_renames = (ArrayList<String>)renames;
      return this.joinParser.buildJoin(((String[])Conversions.unwrapArray(_converted_renames, String.class)));
    }
    int _size_1 = renames.size();
    boolean _equals = (_size_1 == 1);
    if (_equals) {
      return renames.get(0);
    }
    return input;
  }
  
  @Override
  public String parse(final Collection<String> groupAttributes, final String input, final SimpleSelect select) {
    OperatorCache _operatorCache = this.cacheService.getOperatorCache();
    Pair<String, String> _mappedTo = Pair.<String, String>of("pairs", "true");
    String _generateListString = this.utilityService.generateListString(groupAttributes);
    Pair<String, String> _mappedTo_1 = Pair.<String, String>of("aliases", _generateListString);
    Pair<String, String> _mappedTo_2 = Pair.<String, String>of("input", input);
    HashMap<String, String> _newHashMap = CollectionLiterals.<String, String>newHashMap(_mappedTo, _mappedTo_1, _mappedTo_2);
    String _build = this.builder.build(RenameAO.class, _newHashMap);
    return _operatorCache.add(select, _build);
  }
  
  private String generateAlias(final String attributename, final String sourcename, final int number) {
    String alias = ((((sourcename + ".") + attributename) + "#") + Integer.valueOf(number));
    boolean _contains = this.renameAliases.contains(alias);
    if (_contains) {
      String _generateAlias = this.generateAlias(attributename, sourcename, (number + 1));
      return alias = _generateAlias;
    }
    return alias;
  }
  
  @Override
  public Collection<String> getAliases() {
    return this.renameAliases;
  }
  
  @Override
  public Collection<QueryCache.QuerySource> getSources() {
    return this.sourcesDuringRename;
  }
  
  @Override
  public void clear() {
    this.renameAliases.clear();
    this.processedSources.clear();
    this.sourcesDuringRename.clear();
  }
  
  @Override
  public void setSources(final Collection<QueryCache.QuerySource> sources) {
    if ((sources != null)) {
      ArrayList<QueryCache.QuerySource> _arrayList = new ArrayList<QueryCache.QuerySource>(sources);
      this.sourcesDuringRename = _arrayList;
    }
  }
}
