/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class CqlStandaloneSetup extends CqlStandaloneSetupGenerated {

	def static void doSetup() {
		new CqlStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
