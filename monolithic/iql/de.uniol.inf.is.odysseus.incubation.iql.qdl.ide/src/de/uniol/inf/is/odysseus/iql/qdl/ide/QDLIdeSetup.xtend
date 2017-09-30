/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.iql.qdl.ide

import com.google.inject.Guice
import de.uniol.inf.is.odysseus.iql.qdl.QDLRuntimeModule
import de.uniol.inf.is.odysseus.iql.qdl.QDLStandaloneSetup
import org.eclipse.xtext.util.Modules2

/**
 * Initialization support for running Xtext languages as language servers.
 */
class QDLIdeSetup extends QDLStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new QDLRuntimeModule, new QDLIdeModule))
	}
	
}
