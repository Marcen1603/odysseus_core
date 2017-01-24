package de.uniol.inf.is.odysseus.parser.novel.cql.util;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source;

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
}
