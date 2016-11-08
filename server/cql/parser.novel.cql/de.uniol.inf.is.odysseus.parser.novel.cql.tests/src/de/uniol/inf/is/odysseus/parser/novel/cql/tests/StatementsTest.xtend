package de.uniol.inf.is.odysseus.parser.novel.cql.tests

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(CQLInjectorProvider))
class StatementsTest 
{

	@Inject extension ParseHelper<Model>
	@Inject extension ValidationTestHelper

	public static val SELECT = "SELECT attr1 FROM R1 WHERE attr1 > 2";

	@Test
	def void selectStatement()
	{
		'''
		SELECT attr1
		FROM R1
		WHERE 1 > 2;
		'''.parse.assertNoErrors	
	}	
	
	
	
	
}