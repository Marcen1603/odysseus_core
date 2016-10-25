package de.uniol.inf.is.odysseus.parser.novel.cql.tests

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Model
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Select_Statement
import de.uniol.inf.is.odysseus.parser.novel.cql.cql.Statement
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsType
import de.uniol.inf.is.odysseus.parser.novel.cql.typing.ExpressionsTypeProvider
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

import static extension org.junit.Assert.*

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(CqlInjectorProvider))
class ExpressionsTypeProviderTest 
{
	@Inject extension ParseHelper<Model>
	@Inject extension ExpressionsTypeProvider
	
	private Statement s
	private var stmt = "SELECT attr FROM R1 WHERE "
	
	@Test def void intConstant() 		{ "10".assertIntType }
	@Test def void stringConstant() 	{ "'hippo'".assertStringType }
	@Test def void boolConstant() 		{ "'false'".assertBoolType }
	@Test def void notExp()				{ "!true".assertBoolType}
	@Test def void multExp()			{ "1 * 2".assertIntType}
	@Test def void divExp()				{ "1 / 2".assertIntType}
	@Test def void numericPlus()		{ "1 + 2".assertIntType}
	@Test def void stringPlus()			{ "'1' + '2'".assertStringType}
	@Test def void numAndStringPlus()	{ "'1' + 2".assertStringType}
	@Test def void numAndStringPlus2()	{ "1 + '2'".assertStringType}
	@Test def void boolAndStringPlus1()	{ "true + 'a'".assertStringType}
	@Test def void boolAndStringPlus2()	{ "'b' + 'false'".assertStringType}
	
	
	
	def assertIntType(CharSequence input) 	 { input.assertType(ExpressionsTypeProvider::intType) }
	def assertStringType(CharSequence input) { input.assertType(ExpressionsTypeProvider::stringType) }
	def assertBoolType(CharSequence input) 	 { input.assertType(ExpressionsTypeProvider::boolType)}
	
	def void assertType(CharSequence input, ExpressionsType type)	
	{
		if (assertStmt(input)) type.assertSame((s as Select_Statement).predicates.elements.last.typeFor)
	}
	
	def assertStmt(CharSequence input)
	{
		(s = (stmt + input).parse.statements.get(0)).type == Select_Statement
	}
	
	
}
