/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.validation

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.And
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Create_Statement
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Or
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsTypeProvider
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.EValidatorRegistrar
import de.uniol.inf.is.odysseus.parser.novel.cql.CQLParser
import org.eclipse.xtext.EcoreUtil2


/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class CQLExpressionsValidator extends AbstractCQLValidator 
{
	
    @Inject
	@Override
    override register(EValidatorRegistrar register) 
    {
        /* 
         *  Have to be empty, otherwise the main validator cannot call
         *  the check methods of this class.
         */
    }
	
	public static val WRONG_TYPE = "de.uniol.inf.is.odysseus.parser.novel.cql.WrongType";
	
	@Inject extension ExpressionsTypeProvider
	
	def private checkExpectedBoolean(Expression e, EReference ref)
	{
		checkExpectedType(e, ref, ExpressionsTypeProvider::boolType)
	}
	
	def private checkExpectedNumber(Expression e, EReference ref)
	{
		checkExpectedType(e, ref, ExpressionsTypeProvider::intType, ExpressionsTypeProvider::floatType)
	}
	
	def checkExpectedType(Expression e, EReference ref, ExpressionsType ... type) 
	{
		val actualType = getTypeAndNotNull(e, ref)
		if(!type.contains(actualType))
		{
			error("expected " +  e +" type, but was actually " + actualType, ref, WRONG_TYPE)
		}
	}
	def ExpressionsType getTypeAndNotNull(Expression e, EReference ref) 
	{
		var type = e?.typeFor
		if(type == null)
			error("null type", ref, WRONG_TYPE)
		return type	
	}

	@Check def checkType(Plus type)
	{ 
		checkExpectedNumber(type.left, CQLPackage.Literals::PLUS__LEFT)
		checkExpectedNumber(type.right, CQLPackage.Literals::PLUS__RIGHT)
	}
	
	@Check def checkType(MulOrDiv type)
	{ 
		checkExpectedNumber(type.left, CQLPackage.Literals::PLUS__LEFT)
		checkExpectedNumber(type.right, CQLPackage.Literals::PLUS__RIGHT)
	}
	
	@Check def checkType(Minus type)
	{ 
		checkExpectedNumber(type.left, CQLPackage.Literals::PLUS__LEFT)
		checkExpectedNumber(type.right, CQLPackage.Literals::PLUS__RIGHT)
	}
	
	@Check def checkType(NOT type)
	{
		checkExpectedBoolean(type.expression, CQLPackage.Literals::NOT__EXPRESSION)
	}
	
	@Check def checkType(And type)
	{
		checkExpectedBoolean(type.left, CQLPackage.Literals::AND__LEFT)
		checkExpectedBoolean(type.right, CQLPackage.Literals::AND__RIGHT)
	}
	
	@Check def checkType(Or type)
	{
		checkExpectedBoolean(type.left, CQLPackage.Literals::OR__LEFT)
		checkExpectedBoolean(type.right, CQLPackage.Literals::OR__RIGHT)
	}
	
	@Check def checkType(Equality type)
	{
		val left = getTypeAndNotNull(type.left, CQLPackage.Literals::EQUALITY__LEFT)
		val right = getTypeAndNotNull(type.right, CQLPackage.Literals::EQUALITY__RIGHT)
		checkExpectedSame(left, right)			
	}
	
	@Check def checkType(Comparision type)
	{
		val left = getTypeAndNotNull(type.left, CQLPackage.Literals::COMPARISION__LEFT)
		val right = getTypeAndNotNull(type.right, CQLPackage.Literals::COMPARISION__RIGHT)
		checkExpectedSame(left, right)			
		checkNotBoolean(left, CQLPackage.Literals::COMPARISION__LEFT)
		checkNotBoolean(left, CQLPackage.Literals::COMPARISION__RIGHT)
	}	
	
	def checkExpectedSame(ExpressionsType left, ExpressionsType right) 
	{
		if(left != null && right != null && right != left)
			error("expected the same type, but was " + left + ", " + right,
				CQLPackage.Literals::EQUALITY.EIDAttribute, WRONG_TYPE
			)
	}
	
	def checkNotBoolean(ExpressionsType type, EReference ref)
	{
		if(type == ExpressionsTypeProvider::boolType)
			error("cannot be boolean", ref, WRONG_TYPE)
	}
}

