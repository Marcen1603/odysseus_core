package de.uniol.inf.is.odysseus.parser.novel.cql.tests;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Expression;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Select_Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.impl.Select_StatementImpl;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.CqlInjectorProvider;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsTypeProvider;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(CqlInjectorProvider.class)
@SuppressWarnings("all")
public class ExpressionsTypeProviderTest {
  @Inject
  @Extension
  private ParseHelper<Model> _parseHelper;
  
  @Inject
  @Extension
  private ExpressionsTypeProvider _expressionsTypeProvider;
  
  private Select_Statement s;
  
  @Test
  public void attribute1() {
    this.assertIntType("attr2 < attr");
  }
  
  public void assertFloatType(final CharSequence input) {
    this.assertType(input, ExpressionsTypeProvider.floatType);
  }
  
  public void assertIntType(final CharSequence input) {
    this.assertType(input, ExpressionsTypeProvider.intType);
  }
  
  public void assertStringType(final CharSequence input) {
    this.assertType(input, ExpressionsTypeProvider.stringType);
  }
  
  public void assertBoolType(final CharSequence input) {
    this.assertType(input, ExpressionsTypeProvider.boolType);
  }
  
  public void assertType(final CharSequence input, final ExpressionsType type) {
    boolean _assertStmt = this.assertStmt(input);
    if (_assertStmt) {
      ExpressionsModel _predicates = this.s.getPredicates();
      EList<Expression> _elements = _predicates.getElements();
      Expression _last = IterableExtensions.<Expression>last(_elements);
      ExpressionsType _typeFor = this._expressionsTypeProvider.typeFor(_last);
      Assert.assertSame(type, _typeFor);
    }
  }
  
  public boolean assertStmt(final CharSequence input) {
    try {
      boolean _xblockexpression = false;
      {
        String stmt = "SELECT attr, attr2 FROM R1 WHERE ";
        boolean _xifexpression = false;
        Model _parse = this._parseHelper.parse((stmt + input));
        EList<Statement> _statements = _parse.getStatements();
        Statement _get = _statements.get(0);
        EObject _type = _get.getType();
        EClass _eClass = _type.eClass();
        String _name = _eClass.getName();
        String _simpleName = Select_Statement.class.getSimpleName();
        boolean _equals = _name.equals(_simpleName);
        if (_equals) {
          boolean _xblockexpression_1 = false;
          {
            Model _parse_1 = this._parseHelper.parse((stmt + input));
            EList<Statement> _statements_1 = _parse_1.getStatements();
            Statement _get_1 = _statements_1.get(0);
            EObject _type_1 = _get_1.getType();
            this.s = ((Select_StatementImpl) _type_1);
            _xblockexpression_1 = true;
          }
          _xifexpression = _xblockexpression_1;
        } else {
          _xifexpression = false;
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
