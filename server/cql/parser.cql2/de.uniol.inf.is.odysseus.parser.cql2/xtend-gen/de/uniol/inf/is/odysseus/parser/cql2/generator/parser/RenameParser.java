package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Source;
import de.uniol.inf.is.odysseus.parser.cql2.generator.AttributeStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SourceStruct;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;
import java.util.ArrayList;
import java.util.Collection;
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
  
  private Collection<Source> sourcesDuringRename;
  
  @Inject
  public RenameParser(final AbstractPQLOperatorBuilder builder, final IUtilityService utilityService, final IJoinParser joinParser, final ICacheService cacheService) {
    this.utilityService = utilityService;
    this.builder = builder;
    this.joinParser = joinParser;
    this.cacheService = cacheService;
    this.renameAliases = CollectionLiterals.<String>newArrayList();
    this.processedSources = CollectionLiterals.<String>newArrayList();
    this.sourcesDuringRename = CollectionLiterals.<Source>newArrayList();
  }
  
  @Override
  public CharSequence buildRename(final CharSequence input, final SimpleSource simpleSource, final int selfJoin) {
    ArrayList<List<String>> listOfLists = CollectionLiterals.<List<String>>newArrayList();
    SourceStruct source = this.utilityService.getSource(simpleSource);
    String _xifexpression = null;
    Alias _alias = simpleSource.getAlias();
    boolean _tripleNotEquals = (_alias != null);
    if (_tripleNotEquals) {
      _xifexpression = simpleSource.getAlias().getName();
    } else {
      _xifexpression = null;
    }
    String sourcealias = _xifexpression;
    Collection<AttributeStruct> attributeList = source.getAttributeList();
    for (int j = 0; (j < attributeList.size()); j++) {
      {
        int k = 0;
        final Collection<AttributeStruct> _converted_attributeList = (Collection<AttributeStruct>)attributeList;
        for (final String attributealias : ((AttributeStruct[])Conversions.unwrapArray(_converted_attributeList, AttributeStruct.class))[j].aliases) {
          {
            String sourceFromAlias = source.getAssociatedSource(attributealias);
            if (((sourceFromAlias != null) && (sourceFromAlias.equals(sourcealias) || sourceFromAlias.equals(simpleSource.getName())))) {
              int _size = listOfLists.size();
              boolean b = (_size <= k);
              List<String> list = null;
              if (b) {
                list = CollectionLiterals.<String>newArrayList();
              } else {
                list = listOfLists.get(k);
              }
              final Collection<AttributeStruct> _converted_attributeList_1 = (Collection<AttributeStruct>)attributeList;
              list.add(((AttributeStruct[])Conversions.unwrapArray(_converted_attributeList_1, AttributeStruct.class))[j].attributename);
              list.add(attributealias);
              if (b) {
                listOfLists.add(list);
              }
              k++;
            }
          }
        }
      }
    }
    if ((((listOfLists.size() > 1) || (selfJoin > 0)) || (sourcealias != null))) {
      for (int j = 0; (j < listOfLists.size()); j++) {
        {
          List<String> list = listOfLists.get(j);
          for (int k = 0; (k < attributeList.size()); k++) {
            final Collection<AttributeStruct> _converted_attributeList = (Collection<AttributeStruct>)attributeList;
            boolean _contains = list.contains(((AttributeStruct[])Conversions.unwrapArray(_converted_attributeList, AttributeStruct.class))[k].attributename);
            boolean _not = (!_contains);
            if (_not) {
              String alias = null;
              final Collection<AttributeStruct> _converted_attributeList_1 = (Collection<AttributeStruct>)attributeList;
              String name = ((AttributeStruct[])Conversions.unwrapArray(_converted_attributeList_1, AttributeStruct.class))[k].attributename;
              if ((sourcealias != null)) {
                if (((j > 0) && (listOfLists.size() > 1))) {
                  alias = this.generateAlias(name, source.getName(), j);
                } else {
                  alias = ((sourcealias + ".") + name);
                }
              }
              this.renameAliases.add(name);
              this.renameAliases.add(source.getName());
              this.renameAliases.add(alias);
              list.add(name);
              list.add(alias);
            }
          }
        }
      }
    }
    ArrayList<String> renames = CollectionLiterals.<String>newArrayList();
    this.processedSources.add(source.getName());
    for (int j = 0; (j < listOfLists.size()); j++) {
      String _generateListString = this.utilityService.generateListString(listOfLists.get(j));
      Pair<String, String> _mappedTo = Pair.<String, String>of("aliases", _generateListString);
      Pair<String, String> _mappedTo_1 = Pair.<String, String>of("pairs", "true");
      String _string = input.toString();
      Pair<String, String> _mappedTo_2 = Pair.<String, String>of("input", _string);
      renames.add(
        this.cacheService.getOperatorCache().registerOperator(
          this.builder.build(RenameAO.class, 
            CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1, _mappedTo_2))));
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
  
  private String generateAlias(final String attributename, final String sourcename, final int number) {
    String alias = ((((sourcename + ".") + attributename) + "#") + Integer.valueOf(number));
    boolean _contains = this.renameAliases.contains(alias);
    if (_contains) {
      return alias = this.generateAlias(attributename, sourcename, (number + 1));
    }
    return alias;
  }
  
  @Override
  public Collection<String> getAliases() {
    return this.renameAliases;
  }
  
  @Override
  public Collection<Source> getSources() {
    return this.sourcesDuringRename;
  }
  
  @Override
  public void clear() {
    this.renameAliases.clear();
    this.processedSources.clear();
    this.sourcesDuringRename.clear();
  }
  
  @Override
  public void setSources(final Collection<Source> sources) {
    if ((sources != null)) {
      ArrayList<Source> _arrayList = new ArrayList<Source>(sources);
      this.sourcesDuringRename = _arrayList;
    }
  }
}
