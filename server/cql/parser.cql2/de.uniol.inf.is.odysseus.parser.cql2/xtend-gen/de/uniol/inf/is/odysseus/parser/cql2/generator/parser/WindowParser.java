package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSource;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Time;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.TimebasedWindow;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.TuplebasedWindow;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.WindowOperator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.AbstractPQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IWindowParser;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class WindowParser implements IWindowParser {
  private AbstractPQLOperatorBuilder builder;
  
  @Inject
  public WindowParser(final AbstractPQLOperatorBuilder builder) {
    this.builder = builder;
  }
  
  @Override
  public String parse(final SimpleSource source) throws PQLOperatorBuilderException {
    WindowOperator _window = source.getWindow();
    boolean _tripleEquals = (_window == null);
    if (_tripleEquals) {
      return source.getName();
    }
    Map<String, String> args = CollectionLiterals.<String, String>newHashMap();
    WindowOperator window = source.getWindow();
    if ((window instanceof TimebasedWindow)) {
      String _xifexpression = null;
      int _advance_size = ((TimebasedWindow)window).getAdvance_size();
      boolean _tripleNotEquals = (_advance_size != 0);
      if (_tripleNotEquals) {
        int _advance_size_1 = ((TimebasedWindow)window).getAdvance_size();
        _xifexpression = Integer.valueOf(_advance_size_1).toString();
      } else {
        _xifexpression = "1";
      }
      String var1 = _xifexpression;
      String _xifexpression_1 = null;
      Time _unit = ((TimebasedWindow)window).getUnit();
      boolean _tripleNotEquals_1 = (_unit != Time.NULL);
      if (_tripleNotEquals_1) {
        Time _unit_1 = ((TimebasedWindow)window).getUnit();
        _xifexpression_1 = _unit_1.getName();
      } else {
        _xifexpression_1 = Time.NANOSECONDS.getName();
      }
      String var3 = _xifexpression_1;
      String _xifexpression_2 = null;
      Time _advance_unit = ((TimebasedWindow)window).getAdvance_unit();
      boolean _tripleNotEquals_2 = (_advance_unit != Time.NULL);
      if (_tripleNotEquals_2) {
        Time _advance_unit_1 = ((TimebasedWindow)window).getAdvance_unit();
        _xifexpression_2 = _advance_unit_1.getName();
      } else {
        _xifexpression_2 = var3;
      }
      String var2 = _xifexpression_2;
      String _var2 = var2;
      String _xifexpression_3 = null;
      int _length = var2.length();
      int _minus = (_length - 1);
      char _charAt = var2.charAt(_minus);
      String _string = Character.valueOf(_charAt).toString();
      boolean _equalsIgnoreCase = _string.equalsIgnoreCase("S");
      boolean _not = (!_equalsIgnoreCase);
      if (_not) {
        _xifexpression_3 = "S";
      } else {
        _xifexpression_3 = "";
      }
      var2 = (_var2 + _xifexpression_3);
      int _size = ((TimebasedWindow)window).getSize();
      String _string_1 = Integer.valueOf(_size).toString();
      String _plus = (_string_1 + ",\'");
      String _plus_1 = (_plus + var3);
      String _plus_2 = (_plus_1 + "\'");
      args.put("size", _plus_2);
      args.put("advance", (((var1 + ",\'") + var2) + "\'"));
      String _name = source.getName();
      args.put("input", _name);
      return this.builder.build(TimeWindowAO.class, args);
    } else {
      if ((window instanceof TuplebasedWindow)) {
        int _size_1 = ((TuplebasedWindow)window).getSize();
        String _string_2 = Integer.valueOf(_size_1).toString();
        args.put("size", _string_2);
        int _xifexpression_4 = (int) 0;
        int _advance_size_2 = ((TuplebasedWindow)window).getAdvance_size();
        boolean _notEquals = (_advance_size_2 != 0);
        if (_notEquals) {
          _xifexpression_4 = ((TuplebasedWindow)window).getAdvance_size();
        } else {
          _xifexpression_4 = 1;
        }
        String _string_3 = Integer.valueOf(_xifexpression_4).toString();
        args.put("advance", _string_3);
        String _xifexpression_5 = null;
        Attribute _partition_attribute = ((TuplebasedWindow)window).getPartition_attribute();
        boolean _tripleNotEquals_3 = (_partition_attribute != null);
        if (_tripleNotEquals_3) {
          Attribute _partition_attribute_1 = ((TuplebasedWindow)window).getPartition_attribute();
          _xifexpression_5 = _partition_attribute_1.getName();
        } else {
          _xifexpression_5 = null;
        }
        args.put("partition", _xifexpression_5);
        String _name_1 = source.getName();
        args.put("input", _name_1);
        return this.builder.build(ElementWindowAO.class, args);
      } else {
        return source.getName();
      }
    }
  }
}
