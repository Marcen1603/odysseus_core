/**
 * Ø * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.validation;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AndPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.AttributeRef;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Comparision;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Equality;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Expression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Minus;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.NOT;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.OrPredicate;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Plus;
import de.uniol.inf.is.odysseus.parser.cql2.typing.ExpressionsType;
import de.uniol.inf.is.odysseus.parser.cql2.typing.ExpressionsTypeProvider;
import de.uniol.inf.is.odysseus.parser.cql2.validation.AbstractCQLValidator;
import java.util.List;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.EValidatorRegistrar;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;

/**
 * This class contains custom validation rules.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
@Deprecated
@SuppressWarnings("all")
public class CQLExpressionsValidator extends AbstractCQLValidator {
  @Inject
  @Override
  public void register(final EValidatorRegistrar register) {
  }
  
  public final static String WRONG_TYPE = "de.uniol.inf.is.odysseus.parser.cql2.WrongType";
  
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
      this.error(((("expected " + e) + " type, but was actually ") + actualType), ref, CQLExpressionsValidator.WRONG_TYPE);
    }
  }
  
  public ExpressionsType getTypeAndNotNull(final Expression e, final EReference ref) {
    ExpressionsType _typeFor = null;
    if (e!=null) {
      _typeFor=this._expressionsTypeProvider.typeFor(e);
    }
    ExpressionsType type = _typeFor;
    if ((type == null)) {
      this.error("null type", ref, CQLExpressionsValidator.WRONG_TYPE);
    }
    return type;
  }
  
  @Check
  public void checkType(final Plus type) {
    this.checkExpectedNumber(type.getLeft(), CQLPackage.Literals.PLUS__LEFT);
    this.checkExpectedNumber(type.getRight(), CQLPackage.Literals.PLUS__RIGHT);
  }
  
  @Check
  public void checkType(final MulOrDiv type) {
    this.checkExpectedNumber(type.getLeft(), CQLPackage.Literals.PLUS__LEFT);
    this.checkExpectedNumber(type.getRight(), CQLPackage.Literals.PLUS__RIGHT);
  }
  
  @Check
  public void checkType(final Minus type) {
    this.checkExpectedNumber(type.getLeft(), CQLPackage.Literals.PLUS__LEFT);
    this.checkExpectedNumber(type.getRight(), CQLPackage.Literals.PLUS__RIGHT);
  }
  
  @Check
  public void checkType(final NOT type) {
    this.checkExpectedBoolean(type.getExpression(), CQLPackage.Literals.NOT__EXPRESSION);
  }
  
  @Check
  public void checkType(final AndPredicate type) {
    this.checkExpectedBoolean(type.getLeft(), CQLPackage.Literals.AND_PREDICATE__LEFT);
    this.checkExpectedBoolean(type.getRight(), CQLPackage.Literals.AND_PREDICATE__RIGHT);
  }
  
  @Check
  public void checkType(final OrPredicate type) {
    this.checkExpectedBoolean(type.getLeft(), CQLPackage.Literals.OR_PREDICATE__LEFT);
    this.checkExpectedBoolean(type.getRight(), CQLPackage.Literals.OR_PREDICATE__RIGHT);
  }
  
  @Check
  public void checkType(final Equality type) {
    final ExpressionsType left = this.getTypeAndNotNull(type.getLeft(), CQLPackage.Literals.EQUALITY__LEFT);
    final ExpressionsType right = this.getTypeAndNotNull(type.getRight(), CQLPackage.Literals.EQUALITY__RIGHT);
    this.checkExpectedSame(left, right);
  }
  
  @Check
  public void checkType(final Comparision type) {
    final ExpressionsType left = this.getTypeAndNotNull(type.getLeft(), CQLPackage.Literals.COMPARISION__LEFT);
    final ExpressionsType right = this.getTypeAndNotNull(type.getRight(), CQLPackage.Literals.COMPARISION__RIGHT);
    this.checkExpectedSame(left, right);
    this.checkNotBoolean(left, CQLPackage.Literals.COMPARISION__LEFT);
    this.checkNotBoolean(left, CQLPackage.Literals.COMPARISION__RIGHT);
  }
  
  @Check
  public ExpressionsType checkType(final AttributeRef type) {
    ExpressionsType _typeFor = null;
    if (type!=null) {
      _typeFor=this._expressionsTypeProvider.typeFor(type);
    }
    return _typeFor;
  }
  
  public void checkExpectedSame(final ExpressionsType left, final ExpressionsType right) {
    if ((((left != null) && (right != null)) && (right != left))) {
      this.error(
        ((("expected the same type, but was " + left) + ", ") + right), 
        CQLPackage.Literals.EQUALITY.getEIDAttribute(), 
        CQLExpressionsValidator.WRONG_TYPE);
    }
  }
  
  public void checkNotBoolean(final ExpressionsType type, final EReference ref) {
    boolean _equals = Objects.equal(type, ExpressionsTypeProvider.boolType);
    if (_equals) {
      this.error("cannot be boolean", ref, CQLExpressionsValidator.WRONG_TYPE);
    }
  }
}
