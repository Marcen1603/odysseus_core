package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.novel.cql.CQLDictionaryProvider
import de.uniol.inf.is.odysseus.parser.novel.cql.CQLParser
import de.uniol.inf.is.odysseus.parser.novel.cql.validation.AbstractCQLValidator
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.validation.Check
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Create

class TypeListener extends AbstractCQLValidator
{
	
	@Inject extension CQLParser
	@Check def foo(Create m)
	{
		createDictionary(
			EcoreUtil2.eAllOfType(m.eContainer, Create),
			CQLDictionaryProvider.currentUser
		)
	}
	
}