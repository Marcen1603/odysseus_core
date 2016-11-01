package de.uniol.inf.is.odysseus.parser.novel.cql.typing;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.And;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Attribute;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.BoolConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Comparision;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Equality;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Expression;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.FloatConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.IntConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Minus;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.NOT;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Or;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Plus;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.StringConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.BoolType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.FloatType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.IntType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.StringType;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ExpressionsTypeProvider {
  public final static StringType stringType = new StringType();
  
  public final static IntType intType = new IntType();
  
  public final static BoolType boolType = new BoolType();
  
  public final static FloatType floatType = new FloatType();
  
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
        if (e instanceof And) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (e instanceof Or) {
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
  
  protected ExpressionsType _typeFor(final Attribute e) {
    return ExpressionsTypeProvider.boolType;
  }
  
  public static String attributesDefinedBefore(final Expression e) {
    String _xblockexpression = null;
    {
      ExpressionsModel _containerOfType = EcoreUtil2.<ExpressionsModel>getContainerOfType(e, ExpressionsModel.class);
      final EList<Expression> allElements = _containerOfType.getElements();
      int _size = allElements.size();
      String _plus = ("size: " + Integer.valueOf(_size));
      String _plus_1 = (_plus + " , list:");
      String _plus_2 = (_plus_1 + allElements);
      InputOutput.<String>println(_plus_2);
      final Function1<Expression, Boolean> _function = (Expression it) -> {
        return Boolean.valueOf(EcoreUtil.isAncestor(it, e));
      };
      final Expression contained = IterableExtensions.<Expression>findFirst(allElements, _function);
      InputOutput.<String>println(("contained: " + contained));
      int _indexOf = allElements.indexOf(contained);
      final List<Expression> sublist = allElements.subList(0, _indexOf);
      _xblockexpression = InputOutput.<String>println(("sub list: " + sublist));
    }
    return _xblockexpression;
  }
  
  public ExpressionsType typeFor(final Expression e) {
    if (e instanceof Attribute) {
      return _typeFor((Attribute)e);
    } else if (e instanceof Minus) {
      return _typeFor((Minus)e);
    } else if (e instanceof MulOrDiv) {
      return _typeFor((MulOrDiv)e);
    } else if (e instanceof Plus) {
      return _typeFor((Plus)e);
    } else if (e != null) {
      return _typeFor(e);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(e).toString());
    }
  }
}
