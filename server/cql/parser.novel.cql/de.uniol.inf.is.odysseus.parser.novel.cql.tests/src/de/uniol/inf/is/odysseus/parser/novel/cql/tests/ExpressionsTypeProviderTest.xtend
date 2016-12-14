package de.uniol.inf.is.odysseus.parser.novel.cql.tests

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Expression
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select_Statement
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.util.ExpressionsTypeProviderHelper
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsTypeProvider
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

import static extension org.junit.Assert.*

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(CQLInjectorProvider))
class ExpressionsTypeProviderTest 
{
	@Inject extension ParseHelper<Model>
	@Inject extension ExpressionsTypeProviderHelper
	
	@Test def void stringConstant1() 	{ "'hippo'".assertStringType }
//	@Test def void stringConstant2() 	{ (""+"hippo"+"").assertStringType }
	@Test def void boolConstant() 		{ "FALSE".assertBoolType }
	
	@Test def void notExp()				{ "NOT TRUE".assertBoolType}
	@Test def void notExp2()			{ "NOT NOT (3 < 1)".assertBoolType}
	@Test def void multExp()			{ "1 * 2".assertIntType}
	@Test def void divExp()				{ "1 / 2".assertIntType}

	@Test def void intConstant() 		{ "10".assertIntType }
	@Test def void floatConstant()		{ "1.009".assertFloatType}
	
	@Test def void intPlus()			{ "1 + 2".assertIntType}
	@Test def void floatPlus()			{ "1.9999 + 5.2".assertFloatType}
	
	@Test def void floatIntPlus()		{ "2.203 + 7".assertFloatType}
	@Test def void intFloatPlus()		{ "12 + 1.8".assertFloatType}
	@Test def void floatIntDivOrMul()	{ "2.203 / 7 * 1".assertFloatType}
	@Test def void intFloatDivOrMul()	{ "12 * 1.8 / 2".assertFloatType}	
	@Test def void floatIntMinus()		{ "2.203 - 7".assertFloatType}
	@Test def void intFloatMinus()		{ "12 - 1.8".assertFloatType}
	@Test def void intMinus()			{ "3 - 7".assertIntType}
	@Test def void floatMinus()			{ "3.1 - 7.9".assertFloatType}
	
	@Test def void comparison1()		{ "2 > 10".assertBoolType}
	@Test def void comparison2()		{ "2 >= 1".assertBoolType}
	@Test def void comparison3()		{ "72 > 21".assertBoolType}
	@Test def void comparison4()		{ "42 >= 11".assertBoolType}
	
	@Test def void equality1()			{ "12 == 1".assertBoolType}
	@Test def void equality2()			{ "TRUE == FALSE".assertBoolType}
	@Test def void equality3()			{ "'caat' != 'Cat'".assertBoolType}

//	@Test def void attribute1()			{ "attr2 < attr".assertIntType }

	def assertFloatType(CharSequence input)  { input.assertType(ExpressionsTypeProvider::floatType) }
	def assertIntType(CharSequence input) 	 { input.assertType(ExpressionsTypeProvider::intType) }
	def assertStringType(CharSequence input) { input.assertType(ExpressionsTypeProvider::stringType) }
	def assertBoolType(CharSequence input) 	 { input.assertType(ExpressionsTypeProvider::boolType) }
	
	def void assertType(CharSequence input, ExpressionsType type)	
	{
		var model = ("SELECT * FROM stream1 WHERE " + input).parse
		type.assertSame(EcoreUtil2.eAllOfType(model, Expression).get(0).typeFor)
	}
	
}
