package de.uniol.inf.is.odysseus.parser.cql2.typing;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AndPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AttributeRef;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.BoolConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Comparision;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Equality;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.FloatConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.IntConstant;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Minus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NOT;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.OrPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Plus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.StringConstant;
import de.uniol.inf.is.odysseus.parser.cql2.typing.BoolType;
import de.uniol.inf.is.odysseus.parser.cql2.typing.ExpressionsType;
import de.uniol.inf.is.odysseus.parser.cql2.typing.FloatType;
import de.uniol.inf.is.odysseus.parser.cql2.typing.IntType;
import de.uniol.inf.is.odysseus.parser.cql2.typing.StringType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class ExpressionsTypeProvider {
  public final static StringType stringType = new StringType();
  
  public final static IntType intType = new IntType();
  
  public final static BoolType boolType = new BoolType();
  
  public final static FloatType floatType = new FloatType();
  
  public static List<String> aliases = CollectionLiterals.<String>newArrayList();
  
  public static Collection<SDFSchema> outputschemas;
  
  protected ExpressionsType _typeFor(final Expression e) {
    ExpressionsType _switchResult = null;
    boolean _matched = false;
    if (e instanceof StringConstant) {
      _matched=true;
      _switchResult = ExpressionsTypeProvider.stringType;
    }
    if (!_matched) {
      if (e instanceof IntConstant) {
        _matched=true;
        _switchResult = ExpressionsTypeProvider.intType;
      }
    }
    if (!_matched) {
      if (e instanceof FloatConstant) {
        _matched=true;
        _switchResult = ExpressionsTypeProvider.floatType;
      }
    }
    if (!_matched) {
      if (e instanceof NOT) {
        _matched=true;
      }
      if (!_matched) {
        if (e instanceof Comparision) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (e instanceof Equality) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (e instanceof BoolConstant) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (e instanceof AndPredicate) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (e instanceof OrPredicate) {
          _matched=true;
        }
      }
      if (_matched) {
        _switchResult = ExpressionsTypeProvider.boolType;
      }
    }
    return _switchResult;
  }
  
  protected ExpressionsType _typeFor(final Plus e) {
    ExpressionsType _xblockexpression = null;
    {
      Expression _left = e.getLeft();
      ExpressionsType _typeFor = null;
      if (_left!=null) {
        _typeFor=this.typeFor(_left);
      }
      final ExpressionsType left = _typeFor;
      Expression _right = e.getRight();
      ExpressionsType _typeFor_1 = null;
      if (_right!=null) {
        _typeFor_1=this.typeFor(_right);
      }
      final ExpressionsType right = _typeFor_1;
      ExpressionsType _xifexpression = null;
      if ((((Objects.equal(left, ExpressionsTypeProvider.floatType) && (!Objects.equal(right, ExpressionsTypeProvider.boolType))) && (!Objects.equal(right, ExpressionsTypeProvider.stringType))) || ((Objects.equal(right, ExpressionsTypeProvider.floatType) && (!Objects.equal(left, ExpressionsTypeProvider.boolType))) && (!Objects.equal(left, ExpressionsTypeProvider.stringType))))) {
        _xifexpression = ExpressionsTypeProvider.floatType;
      } else {
        IntType _xifexpression_1 = null;
        if ((Objects.equal(left, ExpressionsTypeProvider.intType) && Objects.equal(right, ExpressionsTypeProvider.intType))) {
          _xifexpression_1 = ExpressionsTypeProvider.intType;
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected ExpressionsType _typeFor(final MulOrDiv e) {
    ExpressionsType _xblockexpression = null;
    {
      Expression _left = e.getLeft();
      ExpressionsType _typeFor = null;
      if (_left!=null) {
        _typeFor=this.typeFor(_left);
      }
      final ExpressionsType left = _typeFor;
      Expression _right = e.getRight();
      ExpressionsType _typeFor_1 = null;
      if (_right!=null) {
        _typeFor_1=this.typeFor(_right);
      }
      final ExpressionsType right = _typeFor_1;
      ExpressionsType _xifexpression = null;
      if ((((Objects.equal(left, ExpressionsTypeProvider.floatType) && (!Objects.equal(right, ExpressionsTypeProvider.boolType))) && (!Objects.equal(right, ExpressionsTypeProvider.stringType))) || ((Objects.equal(right, ExpressionsTypeProvider.floatType) && (!Objects.equal(left, ExpressionsTypeProvider.boolType))) && (!Objects.equal(left, ExpressionsTypeProvider.stringType))))) {
        _xifexpression = ExpressionsTypeProvider.floatType;
      } else {
        IntType _xifexpression_1 = null;
        if ((Objects.equal(left, ExpressionsTypeProvider.intType) && Objects.equal(right, ExpressionsTypeProvider.intType))) {
          _xifexpression_1 = ExpressionsTypeProvider.intType;
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected ExpressionsType _typeFor(final Minus e) {
    ExpressionsType _xblockexpression = null;
    {
      Expression _left = e.getLeft();
      ExpressionsType _typeFor = null;
      if (_left!=null) {
        _typeFor=this.typeFor(_left);
      }
      final ExpressionsType left = _typeFor;
      Expression _right = e.getRight();
      ExpressionsType _typeFor_1 = null;
      if (_right!=null) {
        _typeFor_1=this.typeFor(_right);
      }
      final ExpressionsType right = _typeFor_1;
      ExpressionsType _xifexpression = null;
      if ((((Objects.equal(left, ExpressionsTypeProvider.floatType) && (!Objects.equal(right, ExpressionsTypeProvider.boolType))) && (!Objects.equal(right, ExpressionsTypeProvider.stringType))) || ((Objects.equal(right, ExpressionsTypeProvider.floatType) && (!Objects.equal(left, ExpressionsTypeProvider.boolType))) && (!Objects.equal(left, ExpressionsTypeProvider.stringType))))) {
        _xifexpression = ExpressionsTypeProvider.floatType;
      } else {
        IntType _xifexpression_1 = null;
        if ((Objects.equal(left, ExpressionsTypeProvider.intType) && Objects.equal(right, ExpressionsTypeProvider.intType))) {
          _xifexpression_1 = ExpressionsTypeProvider.intType;
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected ExpressionsType _typeFor(final AttributeRef e) {
    if ((ExpressionsTypeProvider.outputschemas == null)) {
      return null;
    }
    for (final SDFSchema s : ExpressionsTypeProvider.outputschemas) {
      List<SDFAttribute> _attributes = s.getAttributes();
      for (final SDFAttribute a : _attributes) {
        {
          String name = a.getAttributeName();
          Attribute _value = e.getValue();
          String _name = ((Attribute) _value).getName();
          boolean _equals = name.equals(_name);
          if (_equals) {
            SDFDatatype _datatype = a.getDatatype();
            return this.typeFor(_datatype);
          }
        }
      }
    }
    return null;
  }
  
  protected ExpressionsType _typeFor(final SDFDatatype e) {
    ExpressionsType _switchResult = null;
    String _uRI = e.getURI();
    String _upperCase = _uRI.toUpperCase();
    switch (_upperCase) {
      case "INTEGER":
        _switchResult = ExpressionsTypeProvider.intType;
        break;
      case "STRING":
        _switchResult = ExpressionsTypeProvider.stringType;
        break;
      case "BOOLEAN":
        _switchResult = ExpressionsTypeProvider.boolType;
        break;
      case "DOUBLE":
        _switchResult = ExpressionsTypeProvider.floatType;
        break;
      default:
        _switchResult = ExpressionsTypeProvider.stringType;
        break;
    }
    return _switchResult;
  }
  
  public static Collection<SDFSchema> setOutputSchema(final Collection<SDFSchema> schema) {
    return ExpressionsTypeProvider.outputschemas = schema;
  }
  
  public ExpressionsType typeFor(final Object e) {
    if (e instanceof SDFDatatype) {
      return _typeFor((SDFDatatype)e);
    } else if (e instanceof AttributeRef) {
      return _typeFor((AttributeRef)e);
    } else if (e instanceof Minus) {
      return _typeFor((Minus)e);
    } else if (e instanceof MulOrDiv) {
      return _typeFor((MulOrDiv)e);
    } else if (e instanceof Plus) {
      return _typeFor((Plus)e);
    } else if (e instanceof Expression) {
      return _typeFor((Expression)e);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(e).toString());
    }
  }
}
