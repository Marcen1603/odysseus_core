package de.uniol.inf.is.odysseus.server.generator

import de.uniol.inf.is.odysseus.core.collection.Context
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor
import de.uniol.inf.is.odysseus.core.usermanagement.ISession
import org.eclipse.emf.ecore.resource.Resource

interface IStreamingSparqlParser {
	def String parse(Resource resource)

	def String parse(String query, ISession user, IDataDictionary dd, Context context,
		IMetaAttribute metaAttribute, IServerExecutor executor)
}