/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql

import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator
import org.eclipse.xtext.generator.IGenerator2
import org.eclipse.xtext.scoping.IGlobalScopeProvider
import de.uniol.inf.is.odysseus.parser.novel.cql.scoping.CQLScopeProvider

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class CQLRuntimeModule extends AbstractCQLRuntimeModule {

		
	def Class<? extends IGenerator2> bindGenerator() 
	{
		return CQLGenerator;
	}
		
}
