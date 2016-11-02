/**
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.validation;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.And;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Or;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType;
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsTypeProvider;
import de.uniol.inf.is.odysseus.parser.novel.cql.validation.AbstractCQLValidator;
import java.util.List;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;

/**
 * This class contains custom validation rules.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
@SuppressWarnings("all")
public class CQLValidator extends AbstractCQLValidator {
  public final static String WRONG_TYPE = "de.uniol.inf.is.odysseus.parser.novel.CQL.WrongType";
  
  @Inject
  @Extension
  private ExpressionsTypeProvider _expressionsTypeProvider;
  
  private void checkExpectedBoolean(final Expression e, final EReference ref) {
    this.checkExpectedType(e, ref, ExpressionsTypeProvider.boolType);
  }
  
  private void checkExpectedNumber(final Expression e, final EReference ref) {
    this.checkExpectedType(e, ref, ExpressionsTypeProvider.intType, ExpressionsTypeProvider.floatType);
  }
  
  public void checkExpectedType(final Expression e, final EReference ref, final ExpressionsType... type) {
    final ExpressionsType actualType = this.getTypeAndNotNull(e, ref);
    boolean _contains = ((List<ExpressionsType>)Conversions.doWrapArray(type)).contains(actualType);
    boolean _not = (!_contains);
    if (_not) {
      this.error(((("expected " + e) + " type, but was actually ") + actualType), ref, CQLValidator.WRONG_TYPE);
    }
  }
  
  public ExpressionsType getTypeAndNotNull(final Expression e, final EReference ref) {
    ExpressionsType _typeFor = null;
    if (e!=null) {
      _typeFor=this._expressionsTypeProvider.typeFor(e);
    }
    ExpressionsType type = _typeFor;
    boolean _equals = Objects.equal(type, null);
    if (_equals) {
      this.error("null type", ref, CQLValidator.WRONG_TYPE);
    }
    return type;
  }
  
  @Check
  public void checkType(final Plus type) {
    Expression _left = type.getLeft();
    this.checkExpectedNumber(_left, CQLPackage.Literals.PLUS__LEFT);
    Expression _right = type.getRight();
    this.checkExpectedNumber(_right, CQLPackage.Literals.PLUS__RIGHT);
  }
  
  @Check
  public void checkType(final MulOrDiv type) {
    Expression _left = type.getLeft();
    this.checkExpectedNumber(_left, CQLPackage.Literals.PLUS__LEFT);
    Expression _right = type.getRight();
    this.checkExpectedNumber(_right, CQLPackage.Literals.PLUS__RIGHT);
  }
  
  @Check
  public void checkType(final Minus type) {
    Expression _left = type.getLeft();
    this.checkExpectedNumber(_left, CQLPackage.Literals.PLUS__LEFT);
    Expression _right = type.getRight();
    this.checkExpectedNumber(_right, CQLPackage.Literals.PLUS__RIGHT);
  }
  
  @Check
  public void checkType(final NOT type) {
    Expression _expression = type.getExpression();
    this.checkExpectedBoolean(_expression, CQLPackage.Literals.NOT__EXPRESSION);
  }
  
  @Check
  public void checkType(final And type) {
    Expression _left = type.getLeft();
    this.checkExpectedBoolean(_left, CQLPackage.Literals.AND__LEFT);
    Expression _right = type.getRight();
    this.checkExpectedBoolean(_right, CQLPackage.Literals.AND__RIGHT);
  }
  
  @Check
  public void checkType(final Or type) {
    Expression _left = type.getLeft();
    this.checkExpectedBoolean(_left, CQLPackage.Literals.OR__LEFT);
    Expression _right = type.getRight();
    this.checkExpectedBoolean(_right, CQLPackage.Literals.OR__RIGHT);
  }
  
  @Check
  public void checkType(final Equality type) {
    Expression _left = type.getLeft();
    final ExpressionsType left = this.getTypeAndNotNull(_left, CQLPackage.Literals.EQUALITY__LEFT);
    Expression _right = type.getRight();
    final ExpressionsType right = this.getTypeAndNotNull(_right, CQLPackage.Literals.EQUALITY__RIGHT);
    this.checkExpectedSame(left, right);
  }
  
  @Check
  public void checkType(final Comparision type) {
    Expression _left = type.getLeft();
    final ExpressionsType left = this.getTypeAndNotNull(_left, CQLPackage.Literals.COMPARISION__LEFT);
    Expression _right = type.getRight();
    final ExpressionsType right = this.getTypeAndNotNull(_right, CQLPackage.Literals.COMPARISION__RIGHT);
    this.checkExpectedSame(left, right);
    this.checkNotBoolean(left, CQLPackage.Literals.COMPARISION__LEFT);
    this.checkNotBoolean(left, CQLPackage.Literals.COMPARISION__RIGHT);
  }
  
  @Check
  public Object checkType(final Attribute type) {
    return null;
  }
  
  public void checkExpectedSame(final ExpressionsType left, final ExpressionsType right) {
    if ((((!Objects.equal(left, null)) && (!Objects.equal(right, null))) && (!Objects.equal(right, left)))) {
      EAttribute _eIDAttribute = CQLPackage.Literals.EQUALITY.getEIDAttribute();
      this.error(((("expected the same type, but was " + left) + ", ") + right), _eIDAttribute, CQLValidator.WRONG_TYPE);
    }
  }
  
  public void checkNotBoolean(final ExpressionsType type, final EReference ref) {
    boolean _equals = Objects.equal(type, ExpressionsTypeProvider.boolType);
    if (_equals) {
      this.error("cannot be boolean", ref, CQLValidator.WRONG_TYPE);
    }
  }
}
