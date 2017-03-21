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
            String _get = list.get(i);
            String[] _split = _get.split("\\.");
            String name = _split[1];
            AttributeStruct newStruct = new AttributeStruct();
            newStruct.attributename = name;
            newStruct.datatype = struct.datatype;
            newStruct.sourcename = struct.sourcename;
            ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
            newStruct.aliases = _newArrayList;
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
        AttributeStruct _next = iter.next();
        boolean _equals = _next.equals(attribute);
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
      String _get = split[0];
      boolean _equals = _get.equals(this.sourcename);
      if (_equals) {
        for (final AttributeStruct attribute : this.attributes) {
          Object _get_1 = split[1];
          boolean _equals_1 = attribute.attributename.equals(_get_1);
          if (_equals_1) {
            return attribute;
          } else {
            Object _get_2 = split[1];
            boolean _contains_1 = attribute.aliases.contains(_get_2);
            if (_contains_1) {
              return attribute;
            }
          }
        }
      } else {
        Object _get_3 = split[0];
        boolean _contains_2 = this.aliases.contains(_get_3);
        if (_contains_2) {
          for (final AttributeStruct attribute2 : this.attributes) {
            Object _get_4 = split[1];
            boolean _equals_2 = attribute2.attributename.equals(_get_4);
            if (_equals_2) {
              return attribute2;
            } else {
              Object _get_5 = split[1];
              boolean _contains_3 = attribute2.aliases.contains(_get_5);
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
      String _lowerCase = attribute.datatype.toLowerCase();
      String _lowerCase_1 = datatype.toLowerCase();
      boolean _equals = _lowerCase.equals(_lowerCase_1);
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
