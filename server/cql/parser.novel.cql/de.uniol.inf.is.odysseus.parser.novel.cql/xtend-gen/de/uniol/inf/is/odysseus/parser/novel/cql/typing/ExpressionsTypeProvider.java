package de.uniol.inf.is.odysseus.parser.novel.cql.typing;

import com.google.common.base.Objects;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.And;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Attribute;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.BoolConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Comparision;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Equality;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Expression;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.IntConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Minus;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.NOT;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Or;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Plus;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.StringConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.BoolType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.IntType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.StringType;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ExpressionsTypeProvider {
  public final static StringType stringType = new StringType();
  
  public final static IntType intType = new IntType();
  
  public final static BoolType boolType = new BoolType();
  
  protected ExpressionsType _typeFor(final Expression e) {
    ExpressionsType _switchResult = null;
    boolean _matched = false;
    if (e instanceof StringConstant) {
      _matched=true;
      _switchResult = ExpressionsTypeProvider.stringType;
    }
    if (!_matched) {
      if (e instanceof Minus) {
        _matched=true;
      }
      if (!_matched) {
        if (e instanceof MulOrDiv) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (e instanceof Plus) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (e instanceof IntConstant) {
          _matched=true;
        }
      }
      if (_matched) {
        _switchResult = ExpressionsTypeProvider.intType;
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
    if (!_matched) {
      if (e instanceof Attribute) {
        _matched=true;
        _switchResult = null;
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
      if ((Objects.equal(left, ExpressionsTypeProvider.stringType) || Objects.equal(right, ExpressionsTypeProvider.stringType))) {
        _xifexpression = ExpressionsTypeProvider.stringType;
      } else {
        _xifexpression = ExpressionsTypeProvider.intType;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected ExpressionsType _typeFor(final Attribute e) {
    BoolType _xblockexpression = null;
    {
      final List<Attribute> elements = ExpressionsTypeProvider.attributesDefinedBefore(e);
      for (final Attribute a : elements) {
        String _name = a.getName();
        String _name_1 = e.getName();
        boolean _equals = Objects.equal(_name, _name_1);
        if (_equals) {
        }
      }
      _xblockexpression = ExpressionsTypeProvider.boolType;
    }
    return _xblockexpression;
  }
  
  public static List<Attribute> attributesDefinedBefore(final Expression e) {
    List<Attribute> _xblockexpression = null;
    {
      Model _containerOfType = EcoreUtil2.<Model>getContainerOfType(e, Model.class);
      final EList<Statement> allElements = _containerOfType.getStatements();
      final Function1<Statement, Boolean> _function = (Statement it) -> {
        return Boolean.valueOf(EcoreUtil.isAncestor(it, e));
      };
      final Statement contained = IterableExtensions.<Statement>findFirst(allElements, _function);
      int _indexOf = allElements.indexOf(contained);
      List<Statement> _subList = allElements.subList(0, _indexOf);
      _xblockexpression = EcoreUtil2.<Attribute>typeSelect(_subList, Attribute.class);
    }
    return _xblockexpression;
  }
  
  public ExpressionsType typeFor(final Expression e) {
    if (e instanceof Attribute) {
      return _typeFor((Attribute)e);
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
