package de.uniol.inf.is.odysseus.parser.novel.cql.typing

import de.uniol.inf.is.odysseus.parser.novel.cql.validation.AbstractCQLValidator
import de.uniol.inf.is.odysseus.parser.novel.cql.CQLParser
import com.google.inject.Inject
import org.eclipse.xtext.validation.Check
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Create_Statement
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider
import de.uniol.inf.is.odysseus.core.usermanagement.ISession
import org.eclipse.xtext.EcoreUtil2

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