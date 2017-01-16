package de.uniol.inf.is.odysseus.parser.novel.cql.util;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class CQLUtil {
  public static boolean equality(final Object obj1, final Object obj2) {
    boolean _matched = false;
    if (obj1 instanceof Source) {
      _matched=true;
      if ((!(obj2 instanceof Source))) {
        return false;
      }
      String _name = ((Source)obj1).getName();
      String _name_1 = ((Source) obj2).getName();
      boolean _notEquals = (!Objects.equal(_name, _name_1));
      if (_notEquals) {
        return false;
      }
      return true;
    }
    if (!_matched) {
      if (obj1 instanceof Attribute) {
        _matched=true;
        if ((!(obj2 instanceof Attribute))) {
          return false;
        }
        String _name = ((Attribute)obj1).getName();
        String _name_1 = ((Attribute) obj2).getName();
        boolean _notEquals = (!Objects.equal(_name, _name_1));
        if (_notEquals) {
          return false;
        }
        return true;
      }
    }
    return false;
  }
  
  public static List<?> merge(final Collection<?> l1, final Collection<?> l2) {
    ArrayList<Object> l = CollectionLiterals.<Object>newArrayList();
    int _size = l1.size();
    int _size_1 = l2.size();
    boolean _greaterThan = (_size > _size_1);
    if (_greaterThan) {
      for (final Object o : l2) {
        for (final Object p : l1) {
          boolean _equality = CQLUtil.equality(o, p);
          if (_equality) {
            l.add(o);
          }
        }
      }
    } else {
      for (final Object o_1 : l1) {
        for (final Object p_1 : l2) {
          boolean _equality_1 = CQLUtil.equality(o_1, p_1);
          if (_equality_1) {
            l.add(o_1);
          }
        }
      }
    }
    return l;
  }
}
