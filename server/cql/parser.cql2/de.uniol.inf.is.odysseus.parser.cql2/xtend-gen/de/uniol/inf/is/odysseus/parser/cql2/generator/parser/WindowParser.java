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
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.IWindowParser;
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
  public String parse(final SimpleSource source) {
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
        _xifexpression = Integer.valueOf(((TimebasedWindow)window).getAdvance_size()).toString();
      } else {
        _xifexpression = "1";
      }
      String var1 = _xifexpression;
      String _xifexpression_1 = null;
      Time _unit = ((TimebasedWindow)window).getUnit();
      boolean _tripleNotEquals_1 = (_unit != Time.NULL);
      if (_tripleNotEquals_1) {
        _xifexpression_1 = ((TimebasedWindow)window).getUnit().getName();
      } else {
        _xifexpression_1 = Time.NANOSECONDS.getName();
      }
      String var3 = _xifexpression_1;
      String _xifexpression_2 = null;
      Time _advance_unit = ((TimebasedWindow)window).getAdvance_unit();
      boolean _tripleNotEquals_2 = (_advance_unit != Time.NULL);
      if (_tripleNotEquals_2) {
        _xifexpression_2 = ((TimebasedWindow)window).getAdvance_unit().getName();
      } else {
        _xifexpression_2 = var3;
      }
      String var2 = _xifexpression_2;
      String _var2 = var2;
      String _xifexpression_3 = null;
      int _length = var2.length();
      int _minus = (_length - 1);
      boolean _equalsIgnoreCase = Character.valueOf(var2.charAt(_minus)).toString().equalsIgnoreCase("S");
      boolean _not = (!_equalsIgnoreCase);
      if (_not) {
        _xifexpression_3 = "S";
      } else {
        _xifexpression_3 = "";
      }
      var2 = (_var2 + _xifexpression_3);
      String _string = Integer.valueOf(((TimebasedWindow)window).getSize()).toString();
      String _plus = (_string + ",\'");
      String _plus_1 = (_plus + var3);
      String _plus_2 = (_plus_1 + "\'");
      args.put("size", _plus_2);
      args.put("advance", (((var1 + ",\'") + var2) + "\'"));
      args.put("input", source.getName());
      return this.builder.build(TimeWindowAO.class, args);
    } else {
      if ((window instanceof TuplebasedWindow)) {
        args.put("size", Integer.valueOf(((TuplebasedWindow)window).getSize()).toString());
        int _xifexpression_4 = (int) 0;
        int _advance_size_1 = ((TuplebasedWindow)window).getAdvance_size();
        boolean _notEquals = (_advance_size_1 != 0);
        if (_notEquals) {
          _xifexpression_4 = ((TuplebasedWindow)window).getAdvance_size();
        } else {
          _xifexpression_4 = 1;
        }
        args.put("advance", Integer.valueOf(_xifexpression_4).toString());
        String _xifexpression_5 = null;
        Attribute _partition_attribute = ((TuplebasedWindow)window).getPartition_attribute();
        boolean _tripleNotEquals_3 = (_partition_attribute != null);
        if (_tripleNotEquals_3) {
          _xifexpression_5 = ((TuplebasedWindow)window).getPartition_attribute().getName();
        } else {
          _xifexpression_5 = null;
        }
        args.put("partition", _xifexpression_5);
        args.put("input", source.getName());
        return this.builder.build(ElementWindowAO.class, args);
      } else {
        return source.getName();
      }
    }
  }
}
