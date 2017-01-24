package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.novel.cql.CQLDictionaryProvider
import de.uniol.inf.is.odysseus.parser.novel.cql.CQLParser
import de.uniol.inf.is.odysseus.parser.novel.cql.validation.AbstractCQLValidator
import org.eclipse.xtext.validation.Check
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeDefinition

class TypeListener extends AbstractCQLValidator
{
	
	@Inject extension CQLParser
	@Check def foo(AttributeDefinition m)
	{
		/*
		 * Only scan current file.
		 * Sources that are already placed, can be accessed via IExecutor!
		 */
//		println("create dictionary" + m.name)
//		createDictionary(m, CQLDictionaryProvider.currentUser)
	}
	
}