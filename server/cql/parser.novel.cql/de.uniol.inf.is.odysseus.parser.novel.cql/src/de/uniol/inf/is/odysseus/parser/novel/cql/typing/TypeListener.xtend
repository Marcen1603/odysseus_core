package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.novel.cql.CQLParser
import de.uniol.inf.is.odysseus.parser.novel.cql.validation.AbstractCQLValidator

class TypeListener extends AbstractCQLValidator
{
	
	@Inject CQLParser parser
	
	@Check def foo(Create_Statement m)
	{
		var ISession u = UserManagementProvider.getSessionmanagement().loginSuperUser(null,
			UserManagementProvider.getDefaultTenant().getName())
		parser.createDictionary(EcoreUtil2.eAllOfType(m.eContainer, Create_Statement), u)
	}
	
}