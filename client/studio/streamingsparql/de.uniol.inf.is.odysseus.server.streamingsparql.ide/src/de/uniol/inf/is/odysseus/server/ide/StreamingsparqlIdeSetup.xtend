/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.server.ide

import com.google.inject.Guice
import de.uniol.inf.is.odysseus.server.StreamingsparqlRuntimeModule
import de.uniol.inf.is.odysseus.server.StreamingsparqlStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class StreamingsparqlIdeSetup extends StreamingsparqlStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new StreamingsparqlRuntimeModule, new StreamingsparqlIdeModule))
	}
	
}
