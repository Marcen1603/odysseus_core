package de.uniol.inf.is.odysseus.parser.novel.cql.generator;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.AttributeStruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class SourceStruct {
  public String sourcename;
  
  public List<AttributeStruct> attributes;
  
  public List<String> aliases;
  
  public boolean internal;
  
  public boolean addRenamedAttributes(final List<String> list) {
    boolean _xblockexpression = false;
    {
      ArrayList<AttributeStruct> newStructs = CollectionLiterals.<AttributeStruct>newArrayList();
      for (final AttributeStruct struct : this.attributes) {
        for (int i = 1; (i < list.size()); i++) {
          if (((i % 2) == 1)) {
            String name = list.get(i).split("\\.")[1];
            AttributeStruct newStruct = new AttributeStruct();
            newStruct.attributename = name;
            newStruct.datatype = struct.datatype;
            newStruct.sourcename = struct.sourcename;
            newStruct.aliases = CollectionLiterals.<String>newArrayList();
            newStructs.add(newStruct);
          }
        }
      }
      _xblockexpression = this.attributes.addAll(newStructs);
    }
    return _xblockexpression;
  }
  
  public boolean update(final AttributeStruct attribute) {
    boolean _xblockexpression = false;
    {
      boolean b = true;
      Iterator<AttributeStruct> iter = this.attributes.iterator();
      while ((b && iter.hasNext())) {
        boolean _equals = iter.next().equals(attribute);
        if (_equals) {
          iter.remove();
          b = false;
        }
      }
      _xblockexpression = this.attributes.add(attribute);
    }
    return _xblockexpression;
  }
  
  public AttributeStruct findbyName(final String name) {
    boolean _contains = name.contains(".");
    if (_contains) {
      String[] split = name.split("\\.");
      boolean _equals = split[0].equals(this.sourcename);
      if (_equals) {
        for (final AttributeStruct attribute : this.attributes) {
          boolean _equals_1 = attribute.attributename.equals(split[1]);
          if (_equals_1) {
            return attribute;
          } else {
            boolean _contains_1 = attribute.aliases.contains(split[1]);
            if (_contains_1) {
              return attribute;
            }
          }
        }
      } else {
        boolean _contains_2 = this.aliases.contains(split[0]);
        if (_contains_2) {
          for (final AttributeStruct attribute2 : this.attributes) {
            boolean _equals_2 = attribute2.attributename.equals(split[1]);
            if (_equals_2) {
              return attribute2;
            } else {
              boolean _contains_3 = attribute2.aliases.contains(split[1]);
              if (_contains_3) {
                return attribute2;
              }
            }
          }
        }
      }
    }
    for (final AttributeStruct attribute_1 : this.attributes) {
      boolean _equals_3 = attribute_1.attributename.equals(name);
      if (_equals_3) {
        return attribute_1;
      } else {
        boolean _contains_4 = attribute_1.aliases.contains(name);
        if (_contains_4) {
          return attribute_1;
        }
      }
    }
    return null;
  }
  
  public List<AttributeStruct> findByType(final String datatype) {
    ArrayList<AttributeStruct> list = CollectionLiterals.<AttributeStruct>newArrayList();
    for (final AttributeStruct attribute : this.attributes) {
      boolean _equals = attribute.datatype.toLowerCase().equals(datatype.toLowerCase());
      if (_equals) {
        list.add(attribute);
      }
    }
    return list;
  }
  
  public AttributeStruct getStarttimeStamp() {
    List<AttributeStruct> a = this.findByType("StartTimestamp");
    AttributeStruct _xifexpression = null;
    boolean _isEmpty = a.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      _xifexpression = a.get(0);
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public AttributeStruct getEndtimeStamp() {
    List<AttributeStruct> a = this.findByType("EndTimestamp");
    AttributeStruct _xifexpression = null;
    boolean _isEmpty = a.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      _xifexpression = a.get(0);
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public boolean containsAttribute(final String attributename) {
    boolean _xifexpression = false;
    AttributeStruct _findbyName = this.findbyName(attributename);
    boolean _notEquals = (!Objects.equal(_findbyName, null));
    if (_notEquals) {
      _xifexpression = true;
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof SourceStruct)) {
      if ((((this.sourcename.equals(((SourceStruct) obj).sourcename) && this.attributes.equals(((SourceStruct) obj).attributes)) && this.aliases.equals(((SourceStruct) obj).aliases)) && Boolean.valueOf(this.internal).equals(Boolean.valueOf(((SourceStruct) obj).internal)))) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return 0;
  }
  
  @Override
  public String toString() {
    return this.sourcename;
  }
}
