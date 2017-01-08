package de.uniol.inf.is.odysseus.parser.novel.cql.tests;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.CQLInjectorProvider;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.util.ExpressionsTypeProviderHelper;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsTypeProvider;
import java.util.List;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(CQLInjectorProvider.class)
@SuppressWarnings("all")
public class ExpressionsTypeProviderTest {
  @Inject
  @Extension
  private ParseHelper<Model> _parseHelper;
  
  @Inject
  @Extension
  private ExpressionsTypeProviderHelper _expressionsTypeProviderHelper;
  
  @Test
  public void stringConstant1() {
    this.assertStringType("\'hippo\'");
  }
  
  @Test
  public void boolConstant() {
    this.assertBoolType("FALSE");
  }
  
  @Test
  public void notExp() {
    this.assertBoolType("NOT TRUE");
  }
  
  @Test
  public void notExp2() {
    this.assertBoolType("NOT NOT (3 < 1)");
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
  public void intConstant() {
    this.assertIntType("10");
  }
  
  @Test
  public void floatConstant() {
    this.assertFloatType("1.009");
  }
  
  @Test
  public void intPlus() {
    this.assertIntType("1 + 2");
  }
  
  @Test
  public void floatPlus() {
    this.assertFloatType("1.9999 + 5.2");
  }
  
  @Test
  public void floatIntPlus() {
    this.assertFloatType("2.203 + 7");
  }
  
  @Test
  public void intFloatPlus() {
    this.assertFloatType("12 + 1.8");
  }
  
  @Test
  public void floatIntDivOrMul() {
    this.assertFloatType("2.203 / 7 * 1");
  }
  
  @Test
  public void intFloatDivOrMul() {
    this.assertFloatType("12 * 1.8 / 2");
  }
  
  @Test
  public void floatIntMinus() {
    this.assertFloatType("2.203 - 7");
  }
  
  @Test
  public void intFloatMinus() {
    this.assertFloatType("12 - 1.8");
  }
  
  @Test
  public void intMinus() {
    this.assertIntType("3 - 7");
  }
  
  @Test
  public void floatMinus() {
    this.assertFloatType("3.1 - 7.9");
  }
  
  @Test
  public void comparison1() {
    this.assertBoolType("2 > 10");
  }
  
  @Test
  public void comparison2() {
    this.assertBoolType("2 >= 1");
  }
  
  @Test
  public void comparison3() {
    this.assertBoolType("72 > 21");
  }
  
  @Test
  public void comparison4() {
    this.assertBoolType("42 >= 11");
  }
  
  @Test
  public void equality1() {
    this.assertBoolType("12 == 1");
  }
  
  @Test
  public void equality2() {
    this.assertBoolType("TRUE == FALSE");
  }
  
  @Test
  public void equality3() {
    this.assertBoolType("\'caat\' != \'Cat\'");
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
    try {
      Model model = this._parseHelper.parse(("SELECT * FROM stream1 WHERE " + input));
      List<Expression> _eAllOfType = EcoreUtil2.<Expression>eAllOfType(model, Expression.class);
      Expression _get = _eAllOfType.get(0);
      ExpressionsType _typeFor = this._expressionsTypeProviderHelper.typeFor(_get);
      Assert.assertSame(type, _typeFor);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
