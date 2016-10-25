package de.uniol.inf.is.odysseus.parser.novel.cql.tests;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Expression;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Select_Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.CqlInjectorProvider;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsTypeProvider;
import org.eclipse.emf.common.util.EList;
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
  
  private Statement s;
  
  private String stmt = "SELECT attr FROM R1 WHERE ";
  
  @Test
  public void intConstant() {
    this.assertIntType("10");
  }
  
  @Test
  public void stringConstant() {
    this.assertStringType("\'hippo\'");
  }
  
  @Test
  public void boolConstant() {
    this.assertBoolType("\'false\'");
  }
  
  @Test
  public void notExp() {
    this.assertBoolType("!true");
  }
  
  @Test
  public void multExp() {
    this.assertIntType("1 * 2");
  }
  
  @Test
  public void divExp() {
    this.assertIntType("1 / 2");
  }
  
  @Test
  public void numericPlus() {
    this.assertIntType("1 + 2");
  }
  
  @Test
  public void stringPlus() {
    this.assertStringType("\'1\' + \'2\'");
  }
  
  @Test
  public void numAndStringPlus() {
    this.assertStringType("\'1\' + 2");
  }
  
  @Test
  public void numAndStringPlus2() {
    this.assertStringType("1 + \'2\'");
  }
  
  @Test
  public void boolAndStringPlus1() {
    this.assertStringType("true + \'a\'");
  }
  
  @Test
  public void boolAndStringPlus2() {
    this.assertStringType("\'b\' + \'false\'");
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
      ExpressionsModel _predicates = ((Select_Statement) this.s).getPredicates();
      EList<Expression> _elements = _predicates.getElements();
      Expression _last = IterableExtensions.<Expression>last(_elements);
      ExpressionsType _typeFor = this._expressionsTypeProvider.typeFor(_last);
      Assert.assertSame(type, _typeFor);
    }
  }
  
  public boolean assertStmt(final CharSequence input) {
    try {
      Model _parse = this._parseHelper.parse((this.stmt + input));
      EList<Statement> _statements = _parse.getStatements();
      Statement _get = _statements.get(0);
      Statement _s = (this.s = _get);
      EObject _type = _s.getType();
      return Objects.equal(_type, Select_Statement.class);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
